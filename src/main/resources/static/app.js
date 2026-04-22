document.addEventListener('DOMContentLoaded', function () {

    // ---- Botones volver a página previa (con fallback al href) ----
    document.querySelectorAll('.btn-back').forEach(function (el) {
        el.addEventListener('click', function (e) {
            var ref = document.referrer || '';
            var sameOrigin = ref && ref.indexOf(window.location.origin) === 0;
            var differentPage = ref && ref !== window.location.href;

            if (sameOrigin && differentPage && window.history.length > 1) {
                e.preventDefault();
                window.history.back();
            }
        });
    });

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

    // ---- Carrusel manual por torneo (detalle equipo) ----
    document.querySelectorAll('[data-carousel]').forEach(function (carousel) {
        var slides = Array.from(carousel.querySelectorAll('.match-carousel-slide'));
        var btnPrev = carousel.querySelector('[data-carousel-prev]');
        var btnNext = carousel.querySelector('[data-carousel-next]');
        var title = carousel.querySelector('[data-carousel-title]');
        if (!slides.length || !btnPrev || !btnNext || !title) return;

        var index = slides.findIndex(function (s) { return s.classList.contains('active'); });
        if (index < 0) index = 0;

        function renderCarousel() {
            slides.forEach(function (slide, i) {
                slide.classList.toggle('active', i === index);
            });

            var torneo = slides[index].dataset.carouselTorneo || 'Torneo';
            title.textContent = torneo + ' (' + (index + 1) + '/' + slides.length + ')';
            btnPrev.disabled = slides.length <= 1;
            btnNext.disabled = slides.length <= 1;
        }

        btnPrev.addEventListener('click', function () {
            index = (index - 1 + slides.length) % slides.length;
            renderCarousel();
        });

        btnNext.addEventListener('click', function () {
            index = (index + 1) % slides.length;
            renderCarousel();
        });

        renderCarousel();
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

// ---- Carrusel de torneos (index) ----
(function () {
    var track   = document.getElementById('torneosCarousel');
    var btnPrev = document.getElementById('torneosCarouselPrev');
    var btnNext = document.getElementById('torneosCarouselNext');
    if (!track || !btnPrev || !btnNext) return;

    var CARD_GAP  = 12; // matches CSS gap
    var visibleCards = function () {
        var w = track.parentElement.offsetWidth - 80; // subtract buttons width
        var cardW = track.querySelector('.torneos-carousel-card');
        if (!cardW) return 3;
        return Math.max(1, Math.round(w / (cardW.offsetWidth + CARD_GAP)));
    };

    function scrollByCards(dir) {
        var cardW = track.querySelector('.torneos-carousel-card');
        if (!cardW) return;
        var step = (cardW.offsetWidth + CARD_GAP) * visibleCards();
        track.scrollBy({ left: dir * step, behavior: 'smooth' });
    }

    btnPrev.addEventListener('click', function (e) { e.stopPropagation(); scrollByCards(-1); });
    btnNext.addEventListener('click', function (e) { e.stopPropagation(); scrollByCards(1); });
})();

// ---- Schedule de partidos (solo días con partido) ----
(function () {
    var wrapper = document.getElementById('cal-wrapper');
    if (!wrapper) return;

    var PARTIDOS = JSON.parse(wrapper.dataset.partidos || '[]');
    var MONTHS = ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
                  'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
    var SHORT_DAYS = ['Dom','Lun','Mar','Mié','Jue','Vie','Sáb'];
    var today = new Date();
    var selectedKey = null;

    function escapeHtml(v) {
        return String(v || '').replace(/&/g,'&amp;').replace(/</g,'&lt;')
            .replace(/>/g,'&gt;').replace(/"/g,'&quot;').replace(/'/g,'&#39;');
    }
    function teamTag(tag) { return String(tag || '').trim() || 'TEAM'; }
    function teamLogo(foto, tag, cls, alt) {
        alt = escapeHtml(alt || teamTag(tag));
        if (!foto) return '<span class="' + cls + ' cal-logo-ph" title="' + alt + '">🛡️</span>';
        return '<img src="/Imagenes/' + foto + '" class="' + cls + '" alt="' + alt + '" onerror="this.onerror=null;this.outerHTML=\'<span class=\\\'' + cls + ' cal-logo-ph\\\'>\uD83D\uDEE1\uFE0F</span>\';">';
    }

    // Build date index
    var dateIndex = {};
    var sortedDates = [];
    PARTIDOS.forEach(function (p) {
        var key = p.year + '-' + p.month + '-' + p.day;
        if (!dateIndex[key]) {
            dateIndex[key] = [];
            sortedDates.push({ year: p.year, month: p.month, day: p.day, key: key });
        }
        dateIndex[key].push(p);
    });
    sortedDates.sort(function (a, b) {
        if (a.year !== b.year) return a.year - b.year;
        if (a.month !== b.month) return a.month - b.month;
        return a.day - b.day;
    });

    function render() {
        var grid = document.getElementById('cal-grid-days');
        if (!grid) return;
        grid.innerHTML = '';

        if (sortedDates.length === 0) {
            grid.innerHTML = '<div class="cal-empty">No hay partidos programados.</div>';
            return;
        }

        sortedDates.forEach(function (d) {
            var date = new Date(d.year, d.month, d.day);
            var isToday = date.getFullYear() === today.getFullYear()
                       && date.getMonth()    === today.getMonth()
                       && date.getDate()     === today.getDate();
            var isPast = date < new Date(today.getFullYear(), today.getMonth(), today.getDate());
            var partidos = dateIndex[d.key];

            var chip = document.createElement('div');
            chip.className = 'cal-date-chip'
                + (isToday         ? ' cal-date-chip--today'    : '')
                + (isPast          ? ' cal-date-chip--past'     : '')
                + (d.key === selectedKey ? ' cal-date-chip--selected' : '');

            chip.innerHTML =
                '<div class="cal-chip-day">'   + SHORT_DAYS[date.getDay()] + '</div>'
              + '<div class="cal-chip-num">'   + d.day + '</div>'
              + '<div class="cal-chip-month">' + MONTHS[d.month].slice(0, 3) + '</div>'
              + '<div class="cal-chip-count">' + partidos.length + ' '
              + (partidos.length === 1 ? 'partido' : 'partidos') + '</div>';

            chip.addEventListener('click', function () {
                if (selectedKey === d.key) {
                    selectedKey = null;
                    hidePanel();
                } else {
                    selectedKey = d.key;
                    showPanel(d.day, d.month, d.year, partidos);
                }
                render();
            });

            grid.appendChild(chip);
        });
    }

    function showPanel(day, month, year, partidos) {
        var panel = document.getElementById('cal-panel');
        if (!panel) return;
        document.getElementById('cal-panel-title').textContent =
            day + ' de ' + MONTHS[month] + ' de ' + year;

        var body = document.getElementById('cal-panel-body');
        body.innerHTML = partidos.map(function (p) {
            var hasScore = p.marcadorLocal !== null;
            var center = hasScore
                ? '<div class="cal-panel-score"><span class="cal-panel-score-num">' + p.marcadorLocal + '</span><span class="cal-panel-score-sep">-</span><span class="cal-panel-score-num">' + p.marcadorVisitante + '</span></div>'
                : '<div class="cal-panel-vs">VS</div><div class="cal-panel-hora">' + escapeHtml(p.hora) + '</div>';
            var winner = p.ganador ? '<div class="cal-panel-winner">🏆 ' + escapeHtml(p.ganador) + '</div>' : '';

            return '<div class="cal-panel-card' + (hasScore ? ' cal-panel-card--done' : '') + '">'
                + '<div class="cal-panel-meta">'
                +   '<span class="cal-panel-torneo">' + escapeHtml(p.torneo) + '</span>'
                +   (p.ronda ? '<span class="cal-panel-ronda">' + escapeHtml(p.ronda) + '</span>' : '')
                + '</div>'
                + '<div class="cal-panel-match">'
                +   '<div class="cal-panel-team">'
                +     '<div class="cal-panel-logo-wrap">' + teamLogo(p.localFoto, p.localTag, 'cal-panel-logo', p.localNombre) + '</div>'
                +     '<span class="cal-panel-nombre">' + escapeHtml(teamTag(p.localTag)) + '</span>'
                +   '</div>'
                +   '<div class="cal-panel-center">' + center + winner + '</div>'
                +   '<div class="cal-panel-team">'
                +     '<div class="cal-panel-logo-wrap">' + teamLogo(p.visitanteFoto, p.visitanteTag, 'cal-panel-logo', p.visitanteNombre) + '</div>'
                +     '<span class="cal-panel-nombre">' + escapeHtml(teamTag(p.visitanteTag)) + '</span>'
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

    render();
})();
