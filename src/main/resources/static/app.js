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

    // ---- Expandir/colapsar jugadores en tarjeta de partido ----
    document.querySelectorAll('[data-match-toggle]').forEach(function (item) {
        item.addEventListener('click', function (e) {
            if (e.target.closest('a, button')) return;
            var wrap = this.closest('.match-item-wrap');
            if (!wrap) return;
            var panel = wrap.querySelector('.match-item-players');
            if (!panel) return;
            var opening = panel.hidden;
            panel.hidden = !opening;
            this.classList.toggle('match-item--open', opening);
            var hint = this.querySelector('.match-item-expand-hint');
            if (hint) hint.textContent = opening ? '▴ Jugadores' : '▾ Jugadores';
        });
    });

    // ---- Cuadro bracket: líneas conectoras SVG ----
    drawBracketLines();
    propagateBracketWinners();

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

function propagateBracketWinners() {
    var wrapper = document.querySelector('.bracket-wrapper');
    if (!wrapper) return;

    var rounds = Array.from(wrapper.querySelectorAll(':scope > .bracket-round'));
    if (rounds.length < 2) return;

    for (var r = 1; r < rounds.length; r++) {

        var prevMatches = Array.from(rounds[r - 1].querySelectorAll('.bracket-match'));
        var curMatches  = Array.from(rounds[r].querySelectorAll('.bracket-match'));

        curMatches.forEach(function (cur, i) {

            var m1 = prevMatches[i * 2];
            var m2 = prevMatches[i * 2 + 1];

            var teams = cur.querySelectorAll('.bm-team');
            var teamLocal     = teams[0];
            var teamVisitante = teams[1];

            // Si el partido previo del local no tiene ganador → TBD
            if (m1 && !m1.dataset.winnerId) {
                blankTeam(teamLocal);
            } else if (m1 && m1.dataset.winnerId) {
                if (teamLocal && teamLocal.dataset.teamId === m1.dataset.winnerId) {
                    teamLocal.classList.add('bm-from-win');
                }
            }

            // Si el partido previo del visitante no tiene ganador → TBD
            if (m2 && !m2.dataset.winnerId) {
                blankTeam(teamVisitante);
            } else if (m2 && m2.dataset.winnerId) {
                if (teamVisitante && teamVisitante.dataset.teamId === m2.dataset.winnerId) {
                    teamVisitante.classList.add('bm-from-win');
                }
            }

        });

    }
}

function blankTeam(teamEl) {
    if (!teamEl) return;
    teamEl.classList.add('bm-tbd');

    var img = teamEl.querySelector('.bm-logo');
    if (img) img.style.display = 'none';

    var ph = teamEl.querySelector('.bm-logo-ph');
    if (ph) ph.style.display = 'none';

    var name = teamEl.querySelector('.bm-name');
    if (name) { name.textContent = 'TBD'; name.style.opacity = '0.4'; }

    var score = teamEl.querySelector('.bm-score');
    if (score) score.textContent = '';
}

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

// ---- Calendario de partidos (carrusel mensual) ----
(function () {
    var wrapper = document.getElementById('cal-wrapper');
    if (!wrapper) return;

    var PARTIDOS = JSON.parse(wrapper.dataset.partidos || '[]');
    var MONTHS = ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
                  'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
    var DAYS_SHORT = ['Dom','Lun','Mar','Mié','Jue','Vie','Sáb'];
    var today = new Date();
    var curMonth = today.getMonth();
    var curYear  = today.getFullYear();
    var selectedKey = null;

    function escapeHtml(v) {
        return String(v || '').replace(/&/g,'&amp;').replace(/</g,'&lt;')
            .replace(/>/g,'&gt;').replace(/"/g,'&quot;').replace(/'/g,'&#39;');
    }
    function teamTag(tag) { return String(tag || '').trim() || 'TEAM'; }
    function teamLogo(foto, tag, cls, alt) {
        alt = escapeHtml(alt || teamTag(tag));
        if (!foto) return '<span class="' + cls + ' cal-logo-ph" title="' + alt + '">\uD83D\uDEE1\uFE0F</span>';
        return '<img src="/Imagenes/' + escapeHtml(foto) + '" class="' + cls + '" alt="' + alt + '">';
    }

    // Group by day key
    var dateIndex = {};
    PARTIDOS.forEach(function (p) {
        var key = p.year + '-' + p.month + '-' + p.day;
        if (!dateIndex[key]) dateIndex[key] = [];
        dateIndex[key].push(p);
    });

    function render() {
        wrapper.innerHTML = '';

        // Nav bar
        var bar = document.createElement('div');
        bar.className = 'cal-bar';
        var btnPrev = document.createElement('button');
        btnPrev.className = 'cal-nav-btn'; btnPrev.type = 'button';
        btnPrev.innerHTML = '&#8249;'; btnPrev.setAttribute('aria-label', 'Mes anterior');
        var titleEl = document.createElement('span');
        titleEl.className = 'cal-bar-title';
        titleEl.textContent = MONTHS[curMonth] + ' ' + curYear;
        var btnNext = document.createElement('button');
        btnNext.className = 'cal-nav-btn'; btnNext.type = 'button';
        btnNext.innerHTML = '&#8250;'; btnNext.setAttribute('aria-label', 'Mes siguiente');
        bar.appendChild(btnPrev); bar.appendChild(titleEl); bar.appendChild(btnNext);
        wrapper.appendChild(bar);

        btnPrev.addEventListener('click', function () {
            curMonth--; if (curMonth < 0) { curMonth = 11; curYear--; }
            selectedKey = null; hidePanel(); render();
        });
        btnNext.addEventListener('click', function () {
            curMonth++; if (curMonth > 11) { curMonth = 0; curYear++; }
            selectedKey = null; hidePanel(); render();
        });

        // Date chips for days that have matches this month
        var days = document.createElement('div');
        days.className = 'cal-days';

        var daysInMonth = new Date(curYear, curMonth + 1, 0).getDate();
        var hasAny = false;

        for (var d = 1; d <= daysInMonth; d++) {
            var key = curYear + '-' + curMonth + '-' + d;
            var pts = dateIndex[key];
            if (!pts || pts.length === 0) continue;
            hasAny = true;

            var dt = new Date(curYear, curMonth, d);
            var isToday = dt.getFullYear() === today.getFullYear()
                       && dt.getMonth()    === today.getMonth()
                       && dt.getDate()     === today.getDate();
            var isPast  = dt < today && !isToday;

            var chip = document.createElement('div');
            var cls = 'cal-date-chip';
            if (isToday)             cls += ' cal-date-chip--today';
            if (isPast)              cls += ' cal-date-chip--past';
            if (key === selectedKey) cls += ' cal-date-chip--selected';
            chip.className = cls;
            chip.innerHTML =
                '<span class="cal-chip-day">' + DAYS_SHORT[dt.getDay()] + '</span>' +
                '<span class="cal-chip-num">' + d + '</span>' +
                '<span class="cal-chip-month">' + MONTHS[curMonth].substring(0,3) + '</span>' +
                '<span class="cal-chip-count">' + pts.length + (pts.length === 1 ? ' partido' : ' partidos') + '</span>';

            (function (ck, cd, cpts) {
                chip.addEventListener('click', function () {
                    if (selectedKey === ck) { selectedKey = null; hidePanel(); }
                    else { selectedKey = ck; showPanel(cd, cpts); }
                    render();
                });
            })(key, d, pts);

            days.appendChild(chip);
        }

        if (!hasAny) {
            var empty = document.createElement('p');
            empty.className = 'cal-empty';
            empty.textContent = 'Sin partidos este mes.';
            days.appendChild(empty);
        }

        wrapper.appendChild(days);
    }

    function showPanel(day, partidos) {
        var panel = document.getElementById('cal-panel');
        if (!panel) return;
        document.getElementById('cal-panel-title').textContent =
            day + ' de ' + MONTHS[curMonth] + ' de ' + curYear;
        var body = document.getElementById('cal-panel-body');
        body.innerHTML = partidos.map(function (p) {
            var hasScore = p.marcadorLocal !== null;
            var center = hasScore
                ? '<div class="cal-panel-score"><span class="cal-panel-score-num">' + p.marcadorLocal + '</span><span class="cal-panel-score-sep">-</span><span class="cal-panel-score-num">' + p.marcadorVisitante + '</span></div>'
                : '<div class="cal-panel-vs">VS</div><div class="cal-panel-hora">' + escapeHtml(p.hora) + '</div>';
            var winner = p.ganador ? '<div class="cal-panel-winner">\uD83C\uDFC6 ' + escapeHtml(p.ganador) + '</div>' : '';
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

// ---- Filtro indexJugador con paginación ----
(function () {
    var searchEl  = document.getElementById('filterSearch');
    var rolEl     = document.getElementById('filterRol');
    var juegoEl   = document.getElementById('filterJuego');
    var equipoEl  = document.getElementById('filterEquipo');
    var perPageEl = document.getElementById('filterPerPage');
    var countEl   = document.getElementById('filterCount');
    var pagerEl   = document.getElementById('idcard-pager');
    if (!searchEl || !rolEl || !equipoEl) return;

    var items = Array.from(document.querySelectorAll('.filter-item'));
    var currentPage = 1;

    // Poblar selects
    var roles = {}, juegos = {}, equipos = {};
    items.forEach(function (r) {
        if (r.dataset.rol)    roles[r.dataset.rol]      = true;
        if (r.dataset.juego)  juegos[r.dataset.juego]   = true;
        if (r.dataset.equipo) equipos[r.dataset.equipo] = true;
    });
    Object.keys(roles).sort().forEach(function (v) {
        var o = document.createElement('option'); o.value = v; o.textContent = v; rolEl.appendChild(o);
    });
    if (juegoEl) {
        Object.keys(juegos).sort().forEach(function (v) {
            var o = document.createElement('option'); o.value = v; o.textContent = v; juegoEl.appendChild(o);
        });
    }
    Object.keys(equipos).sort().forEach(function (v) {
        var o = document.createElement('option'); o.value = v; o.textContent = v; equipoEl.appendChild(o);
    });

    function getFiltered() {
        var term   = searchEl.value.toLowerCase().trim();
        var rol    = rolEl.value;
        var juego  = juegoEl ? juegoEl.value : '';
        var equipo = equipoEl.value;
        return items.filter(function (r) {
            return (!term   || (r.dataset.search || '').indexOf(term)  !== -1)
                && (!rol    || r.dataset.rol    === rol)
                && (!juego  || r.dataset.juego  === juego)
                && (!equipo || r.dataset.equipo === equipo);
        }).sort(function (a, b) {
            var na = (a.querySelector('.idcard-nick') ? a.querySelector('.idcard-nick').textContent : '').toLowerCase().trim();
            var nb = (b.querySelector('.idcard-nick') ? b.querySelector('.idcard-nick').textContent : '').toLowerCase().trim();
            return na.localeCompare(nb);
        });
    }

    function render() {
        var perPage  = perPageEl ? parseInt(perPageEl.value, 10) : 24;
        var filtered = getFiltered();
        var total    = filtered.length;
        var pages    = Math.max(1, Math.ceil(total / perPage));
        if (currentPage > pages) currentPage = pages;

        var start = (currentPage - 1) * perPage;
        var end   = start + perPage;

        // Reordenar nodos en el DOM según el orden alfabético del array filtrado
        var grid = items.length > 0 ? items[0].parentNode : null;
        if (grid) {
            // Primero todos los filtrados en su orden sorted, luego el resto
            var remaining = items.filter(function (r) { return filtered.indexOf(r) === -1; });
            filtered.concat(remaining).forEach(function (r) { grid.appendChild(r); });
        }

        items.forEach(function (r) { r.style.display = 'none'; });
        filtered.forEach(function (r, i) {
            r.style.display = (i >= start && i < end) ? '' : 'none';
        });

        if (countEl) countEl.textContent = total + ' / ' + items.length;

        if (!pagerEl) return;
        pagerEl.innerHTML = '';
        if (pages <= 1) return;

        function mkBtn(label, page, disabled, active) {
            var b = document.createElement('button');
            b.textContent = label;
            b.className   = 'pager-btn' + (active ? ' pager-btn--active' : '');
            b.disabled    = disabled;
            b.addEventListener('click', function () {
                currentPage = page;
                render();
                window.scrollTo({ top: 0, behavior: 'smooth' });
            });
            return b;
        }

        pagerEl.appendChild(mkBtn('‹', currentPage - 1, currentPage === 1, false));

        var range = [];
        for (var p = 1; p <= pages; p++) {
            if (p === 1 || p === pages || (p >= currentPage - 2 && p <= currentPage + 2)) {
                range.push(p);
            } else if (range[range.length - 1] !== '…') {
                range.push('…');
            }
        }
        range.forEach(function (p) {
            if (p === '…') {
                var s = document.createElement('span');
                s.className   = 'pager-ellipsis';
                s.textContent = '…';
                pagerEl.appendChild(s);
            } else {
                pagerEl.appendChild(mkBtn(p, p, false, p === currentPage));
            }
        });

        pagerEl.appendChild(mkBtn('›', currentPage + 1, currentPage === pages, false));
    }

    function resetAndRender() { currentPage = 1; render(); }

    searchEl.addEventListener('input',  resetAndRender);
    rolEl.addEventListener('change',    resetAndRender);
    if (juegoEl)   juegoEl.addEventListener('change',   resetAndRender);
    equipoEl.addEventListener('change', resetAndRender);
    if (perPageEl) perPageEl.addEventListener('change', resetAndRender);

    render();
})();

// ---- Filtro indexTorneo ----
(function () {
    var searchEl = document.getElementById('filterSearch');
    var estadoEl = document.getElementById('filterEstado');
    var juegoEl  = document.getElementById('filterJuego');
    var countEl  = document.getElementById('filterCount');
    if (!searchEl || !estadoEl) return;
    var items = Array.from(document.querySelectorAll('.filter-item'));

    var juegos = {};
    items.forEach(function (c) { if (c.dataset.juego) juegos[c.dataset.juego] = true; });
    if (juegoEl) {
        Object.keys(juegos).sort().forEach(function (v) {
            var o = document.createElement('option'); o.value = v; o.textContent = v; juegoEl.appendChild(o);
        });
    }

    function applyFilter() {
        var term   = searchEl.value.toLowerCase().trim();
        var estado = estadoEl.value;
        var juego  = juegoEl ? juegoEl.value : '';
        var visible = 0;
        items.forEach(function (c) {
            var show = (!term   || (c.dataset.search || '').indexOf(term)   !== -1)
                    && (!estado || c.dataset.estado === estado)
                    && (!juego  || c.dataset.juego  === juego);
            c.style.display = show ? '' : 'none';
            if (show) visible++;
        });
        if (countEl) countEl.textContent = visible + ' / ' + items.length;
    }
    searchEl.addEventListener('input',  applyFilter);
    estadoEl.addEventListener('change', applyFilter);
    if (juegoEl) juegoEl.addEventListener('change', applyFilter);
    applyFilter();
})();

// ---- Filtro indexJuego ----
(function () {
    var searchEl = document.getElementById('filterSearch');
    var countEl  = document.getElementById('filterCount');
    if (!searchEl || document.getElementById('filterEstado') || document.getElementById('filterRol')) return;
    var items = Array.from(document.querySelectorAll('.filter-item'));
    function applyFilter() {
        var term = searchEl.value.toLowerCase().trim();
        var visible = 0;
        items.forEach(function (r) {
            var show = !term || (r.dataset.search || '').indexOf(term) !== -1;
            r.style.display = show ? '' : 'none';
            if (show) visible++;
        });
        if (countEl) countEl.textContent = visible + ' / ' + items.length;
    }
    searchEl.addEventListener('input', applyFilter);
    applyFilter();
})();