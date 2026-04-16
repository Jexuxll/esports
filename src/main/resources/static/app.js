document.addEventListener('DOMContentLoaded', function () {

    // ---- Filas y cards clicables (data-href) ----
    document.querySelectorAll('[data-href]').forEach(function (el) {
        el.addEventListener('click', function () {
            window.location = this.dataset.href;
        });
    });

    // ---- Confirmación en enlaces de borrado (data-confirm) ----
    // También detiene la propagación para que no active el data-href del padre
    document.querySelectorAll('[data-confirm]').forEach(function (el) {
        el.addEventListener('click', function (e) {
            e.stopPropagation();
            if (!confirm(this.dataset.confirm)) {
                e.preventDefault();
            }
        });
    });

    // ---- Botones que solo paran propagación (stop-propagate) ----
    document.querySelectorAll('.stop-propagate').forEach(function (el) {
        el.addEventListener('click', function (e) {
            e.stopPropagation();
        });
    });

    // ---- Pestañas archivador ----
    const tabBtns   = document.querySelectorAll('.tab-btn');
    const tabPanels = document.querySelectorAll('.tab-panel');
    tabBtns.forEach(function (btn) {
        btn.addEventListener('click', function () {
            const idx = this.dataset.tab;
            tabBtns.forEach(function (b)   { b.classList.remove('active'); });
            tabPanels.forEach(function (p) { p.classList.remove('active'); });
            this.classList.add('active');
            document.querySelector('.tab-panel[data-panel="' + idx + '"]').classList.add('active');
        });
    });

    // ---- Cuadro bracket: líneas conectoras SVG ----
    drawBracketLines();

});

function drawBracketLines() {
    var wrapper = document.querySelector('.bracket-wrapper');
    if (!wrapper) return;

    var existing = wrapper.querySelector('.bracket-svg');
    if (existing) existing.remove();

    var rounds = Array.from(wrapper.querySelectorAll(':scope > .bracket-round'));
    if (rounds.length < 2) return;

    var ns  = 'http://www.w3.org/2000/svg';
    var svg = document.createElementNS(ns, 'svg');
    svg.classList.add('bracket-svg');
    svg.style.cssText = 'position:absolute;top:0;left:0;pointer-events:none;overflow:visible;';
    svg.setAttribute('width',  wrapper.scrollWidth);
    svg.setAttribute('height', wrapper.scrollHeight);
    wrapper.appendChild(svg);

    var wRect = wrapper.getBoundingClientRect();

    for (var r = 0; r < rounds.length - 1; r++) {
        var curMatches = Array.from(rounds[r].querySelectorAll('.bracket-match'));
        var nxtMatches = Array.from(rounds[r + 1].querySelectorAll('.bracket-match'));

        nxtMatches.forEach(function (nxt, i) {
            var m1 = curMatches[i * 2];
            var m2 = curMatches[i * 2 + 1];
            var nRect = nxt.getBoundingClientRect();
            var nx = nRect.left  - wRect.left + wrapper.scrollLeft;
            var ny = nRect.top   + nRect.height / 2 - wRect.top + wrapper.scrollTop;

            [m1, m2].forEach(function (m) {
                if (!m) return;
                var mRect = m.getBoundingClientRect();
                var mx = mRect.right - wRect.left + wrapper.scrollLeft;
                var my = mRect.top   + mRect.height / 2 - wRect.top + wrapper.scrollTop;
                var xm = mx + (nx - mx) / 2;

                var path = document.createElementNS(ns, 'path');
                path.setAttribute('d', 'M' + mx + ',' + my + ' H' + xm + ' V' + ny + ' H' + nx);
                path.setAttribute('stroke', 'rgba(124,58,237,0.45)');
                path.setAttribute('stroke-width', '1.5');
                path.setAttribute('fill', 'none');
                path.setAttribute('stroke-linecap', 'square');
                svg.appendChild(path);
            });
        });
    }
}

window.addEventListener('resize', drawBracketLines);

// ---- Calendario de partidos ----
(function () {
    var wrapper = document.getElementById('cal-wrapper');
    if (!wrapper) return;

    var PARTIDOS = JSON.parse(wrapper.dataset.partidos || '[]');
    var MONTHS = ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
                  'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
    var today = new Date();
    var viewYear  = today.getFullYear();
    var viewMonth = today.getMonth();
    var selectedCell = null;

    function buildIndex(data) {
        var idx = {};
        data.forEach(function (p) {
            var key = p.year + '-' + p.month + '-' + p.day;
            if (!idx[key]) idx[key] = [];
            idx[key].push(p);
        });
        return idx;
    }

    function logoSmall(foto, tag) {
        if (foto) return '<img src="/Imagenes/' + foto + '" class="cal-ev-logo" alt="' + tag + '" onerror="this.style.display=\'none\'">';
        return '<span class="cal-ev-logo-ph">🛡</span>';
    }

    function logoBig(foto, tag) {
        if (foto) return '<img src="/Imagenes/' + foto + '" class="cal-panel-logo" alt="' + tag + '" onerror="this.style.display=\'none\'">';
        return '<span class="cal-panel-logo-ph">🛡️</span>';
    }

    function renderCalendar() {
        var idx = buildIndex(PARTIDOS);
        document.getElementById('cal-title').textContent = MONTHS[viewMonth] + ' ' + viewYear;

        var firstDay    = new Date(viewYear, viewMonth, 1);
        var daysInMonth = new Date(viewYear, viewMonth + 1, 0).getDate();
        var startOffset = (firstDay.getDay() + 6) % 7;
        var totalCells  = Math.ceil((startOffset + daysInMonth) / 7) * 7;

        var grid = document.getElementById('cal-grid-days');
        grid.innerHTML = '';
        selectedCell = null;
        hidePanel();

        for (var i = 0; i < totalCells; i++) {
            var dayNum = i - startOffset + 1;
            var cell = document.createElement('div');

            if (dayNum < 1 || dayNum > daysInMonth) {
                cell.className = 'cal-cell cal-cell--empty';
                grid.appendChild(cell);
                continue;
            }

            var key = viewYear + '-' + viewMonth + '-' + dayNum;
            var partidos = idx[key] || [];
            var isToday = viewYear === today.getFullYear()
                       && viewMonth === today.getMonth()
                       && dayNum   === today.getDate();

            cell.className = 'cal-cell'
                + (isToday          ? ' cal-cell--today'       : '')
                + (partidos.length  ? ' cal-cell--has-matches' : '');

            var html = '<div class="cal-cell-num">' + dayNum + '</div>';

            partidos.slice(0, 2).forEach(function (p) {
                var hasScore = p.marcadorLocal !== null;
                var mid = hasScore
                    ? '<span class="cal-ev-score">' + p.marcadorLocal + '-' + p.marcadorVisitante + '</span>'
                    : '<span class="cal-ev-vs">vs</span>';
                html += '<div class="cal-ev">' + logoSmall(p.localFoto, p.localTag) + mid + logoSmall(p.visitanteFoto, p.visitanteTag) + '</div>';
            });

            if (partidos.length > 2) {
                html += '<div class="cal-ev-more">+' + (partidos.length - 2) + ' más</div>';
            }

            cell.innerHTML = html;

            if (partidos.length) {
                (function (c, day, ps) {
                    c.addEventListener('click', function () {
                        if (selectedCell) selectedCell.classList.remove('cal-cell--selected');
                        if (selectedCell === c) {
                            selectedCell = null;
                            hidePanel();
                            return;
                        }
                        selectedCell = c;
                        c.classList.add('cal-cell--selected');
                        showPanel(day, viewMonth, viewYear, ps);
                    });
                })(cell, dayNum, partidos);
            }

            grid.appendChild(cell);
        }
    }

    function showPanel(day, month, year, partidos) {
        var panel = document.getElementById('cal-panel');
        document.getElementById('cal-panel-title').textContent =
            day + ' de ' + MONTHS[month] + ' de ' + year;

        var body = document.getElementById('cal-panel-body');
        body.innerHTML = partidos.map(function (p) {
            var hasScore = p.marcadorLocal !== null;
            var center = hasScore
                ? '<div class="cal-panel-score"><span class="cal-panel-score-num">' + p.marcadorLocal + '</span><span class="cal-panel-score-sep">-</span><span class="cal-panel-score-num">' + p.marcadorVisitante + '</span></div>'
                : '<div class="cal-panel-vs">VS</div><div class="cal-panel-hora">' + p.hora + '</div>';
            var winner = p.ganador ? '<div class="cal-panel-winner">🏆 ' + p.ganador + '</div>' : '';

            return '<div class="cal-panel-card' + (hasScore ? ' cal-panel-card--done' : '') + '">'
                + '<div class="cal-panel-meta">'
                +   '<span class="cal-panel-torneo">' + p.torneo + '</span>'
                +   (p.ronda ? '<span class="cal-panel-ronda">' + p.ronda + '</span>' : '')
                + '</div>'
                + '<div class="cal-panel-match">'
                +   '<div class="cal-panel-team">'
                +     '<div class="cal-panel-logo-wrap">' + logoBig(p.localFoto, p.localTag) + '</div>'
                +     '<span class="cal-panel-nombre">' + p.localNombre + '</span>'
                +     '<span class="cal-panel-tag">' + p.localTag + '</span>'
                +   '</div>'
                +   '<div class="cal-panel-center">' + center + winner + '</div>'
                +   '<div class="cal-panel-team">'
                +     '<div class="cal-panel-logo-wrap">' + logoBig(p.visitanteFoto, p.visitanteTag) + '</div>'
                +     '<span class="cal-panel-nombre">' + p.visitanteNombre + '</span>'
                +     '<span class="cal-panel-tag">' + p.visitanteTag + '</span>'
                +   '</div>'
                + '</div>'
                + '</div>';
        }).join('');

        panel.hidden = false;
        panel.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }

    function hidePanel() {
        var panel = document.getElementById('cal-panel');
        if (panel) panel.hidden = true;
    }

    document.getElementById('cal-prev').addEventListener('click', function () {
        viewMonth--;
        if (viewMonth < 0) { viewMonth = 11; viewYear--; }
        renderCalendar();
    });
    document.getElementById('cal-next').addEventListener('click', function () {
        viewMonth++;
        if (viewMonth > 11) { viewMonth = 0; viewYear++; }
        renderCalendar();
    });

    renderCalendar();
})();
