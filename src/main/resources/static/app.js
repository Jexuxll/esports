document.addEventListener('DOMContentLoaded', function () {

    // ---- Fondo dinámico del banner en detalle_torneo ----
    document.querySelectorAll('.torneo-banner[data-banner-photo]').forEach(function (banner) {
        var photo = (banner.dataset.bannerPhoto || '').trim();
        if (!photo || photo === 'null' || photo === 'undefined') return;
        var safePhoto = encodeURIComponent(photo).replace(/%2F/g, '/');
        banner.style.setProperty('--torneo-banner-bg', "url('/Imagenes/" + safePhoto + "')");
    });

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
            title.textContent = torneo;
            var torneoId = slides[index].dataset.carouselTorneoId || '';
            if (title.tagName === 'A') {
                title.href = torneoId ? '/torneos/' + torneoId : '#';
            }
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

    // ---- editar_partido: ganador dinámico (solo los dos equipos del partido) ----
    (function () {
        var localSel     = document.getElementById('equipoLocalId');
        var visitanteSel = document.getElementById('equipoVisitanteId');
        var ganadorSel   = document.getElementById('ganadorId');
        if (!localSel || !visitanteSel || !ganadorSel) return;

        function selectedText(sel) {
            return sel.options[sel.selectedIndex] ? sel.options[sel.selectedIndex].text : '';
        }

        function rebuildGanador() {
            var prev       = ganadorSel.value;
            var localId    = localSel.value;
            var localTxt   = selectedText(localSel);
            var visitId    = visitanteSel.value;
            var visitTxt   = selectedText(visitanteSel);

            ganadorSel.innerHTML = '<option value="">-- Sin ganador --</option>';

            function addOpt(id, txt) {
                if (!id) return;
                var o = document.createElement('option');
                o.value = id;
                o.textContent = txt;
                if (id === prev) o.selected = true;
                ganadorSel.appendChild(o);
            }

            addOpt(localId, localTxt);
            addOpt(visitId, visitTxt);
        }

        localSel.addEventListener('change', rebuildGanador);
        visitanteSel.addEventListener('change', rebuildGanador);
        rebuildGanador();
    })();

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

            // Si el partido previo del local tiene ganador y el slot está vacío, propagar clasificado
            if (m1 && m1.dataset.winnerId) {
                fillTeamFromWinner(m1, m1.dataset.winnerId, teamLocal);
            }

            // Si el partido previo del local no tiene ganador → Por definir (solo si no hay equipo asignado)
            if (m1 && !m1.dataset.winnerId) {
                if (!teamLocal || !teamLocal.dataset.teamId) {
                    blankTeam(teamLocal);
                }
            }

            // Si el partido previo del visitante tiene ganador y el slot está vacío, propagar clasificado
            if (m2 && m2.dataset.winnerId) {
                fillTeamFromWinner(m2, m2.dataset.winnerId, teamVisitante);
            }

            // Si el partido previo del visitante no tiene ganador → Por definir (solo si no hay equipo asignado)
            if (m2 && !m2.dataset.winnerId) {
                if (!teamVisitante || !teamVisitante.dataset.teamId) {
                    blankTeam(teamVisitante);
                }
            }

        });

    }
}

function fillTeamFromWinner(prevMatchEl, winnerId, targetTeamEl) {
    if (!prevMatchEl || !winnerId || !targetTeamEl) return;
    if (targetTeamEl.dataset.teamId) return;

    var winnerTeamEl = prevMatchEl.querySelector('.bm-team[data-team-id="' + winnerId + '"]');
    if (!winnerTeamEl) return;

    targetTeamEl.dataset.teamId = winnerId;
    targetTeamEl.classList.remove('bm-tbd');

    var sourceName = winnerTeamEl.querySelector('.bm-name');
    var targetName = targetTeamEl.querySelector('.bm-name');
    var targetScore = targetTeamEl.querySelector('.bm-score');
    if (sourceName && targetName) {
        targetName.textContent = sourceName.textContent;
        targetName.style.opacity = '';
    }

    var sourceImg = winnerTeamEl.querySelector('.bm-logo');
    var sourcePh = winnerTeamEl.querySelector('.bm-logo-ph');
    var targetImg = targetTeamEl.querySelector('.bm-logo');
    var targetPh = targetTeamEl.querySelector('.bm-logo-ph');

    if (sourceImg && sourceImg.getAttribute('src')) {
        if (!targetImg) {
            targetImg = document.createElement('img');
            targetImg.className = 'bm-logo';
            targetImg.alt = '';
            if (targetPh) {
                targetTeamEl.insertBefore(targetImg, targetPh);
            } else if (targetName) {
                targetTeamEl.insertBefore(targetImg, targetName);
            } else {
                targetTeamEl.insertBefore(targetImg, targetTeamEl.firstChild);
            }
        }
        if (targetImg) {
            targetImg.setAttribute('src', sourceImg.getAttribute('src'));
            targetImg.style.display = '';
        }
        if (targetPh) targetPh.style.display = 'none';
    } else {
        if (targetImg) targetImg.style.display = 'none';
        if (!targetPh) {
            targetPh = document.createElement('span');
            targetPh.className = 'bm-logo-ph';
            targetPh.textContent = sourcePh && sourcePh.textContent ? sourcePh.textContent : '🛡️';
            if (targetName) {
                targetTeamEl.insertBefore(targetPh, targetName);
            } else if (targetScore) {
                targetTeamEl.insertBefore(targetPh, targetScore);
            } else {
                targetTeamEl.appendChild(targetPh);
            }
        }
        targetPh.style.display = '';
    }

    if (targetScore && !targetScore.textContent.trim()) {
        targetScore.textContent = '-';
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
    if (name) { name.textContent = 'Por definir'; name.style.opacity = '0.4'; }

    var score = teamEl.querySelector('.bm-score');
    if (score) score.textContent = '';
}

// ---- Banner carrusel automático de torneos destacados (index) ----
(function () {
    var carousel = document.getElementById('featuredTorneosCarousel');
    var track = document.getElementById('featuredTorneosTrack');
    if (!carousel || !track) return;

    var slides = Array.from(track.querySelectorAll('.featured-slide'));
    if (!slides.length) return;

    slides = slides.filter(function (slide) {
        var estado = (slide.dataset.estado || '').trim().toUpperCase();
        return estado === 'ACTIVO' || estado === 'PENDIENTE';
    });

    if (!slides.length) {
        var wrapper = carousel.parentElement;
        if (wrapper) wrapper.style.display = 'none';
        return;
    }

    // Mostrar solo 3 torneos: últimos por id; si no hay ids válidos, aleatorios.
    if (slides.length > 3) {
        var withIds = slides
            .map(function (slide) {
                var id = parseInt(slide.dataset.id, 10);
                return { slide: slide, id: Number.isNaN(id) ? null : id };
            })
            .filter(function (it) { return it.id !== null; });

        var selected;
        if (withIds.length >= 3) {
            selected = withIds
                .sort(function (a, b) { return b.id - a.id; })
                .slice(0, 3)
                .map(function (it) { return it.slide; });
        } else {
            selected = slides.slice();
            for (var i = selected.length - 1; i > 0; i--) {
                var j = Math.floor(Math.random() * (i + 1));
                var tmp = selected[i];
                selected[i] = selected[j];
                selected[j] = tmp;
            }
            selected = selected.slice(0, 3);
        }

        slides = selected;
    }

    track.innerHTML = '';
    slides.forEach(function (slide) { track.appendChild(slide); });

    var useInfinite = slides.length > 1;
    var realCount = slides.length;

    if (useInfinite) {
        var firstClone = slides[0].cloneNode(true);
        var lastClone = slides[realCount - 1].cloneNode(true);
        firstClone.classList.add('featured-slide--clone');
        lastClone.classList.add('featured-slide--clone');
        track.appendChild(firstClone);
        track.insertBefore(lastClone, track.firstChild);
    }

    var index = useInfinite ? 1 : 0;
    var timer = null;
    var intervalMs = 3500;

    function currentRealIndex() {
        if (!useInfinite) return index;
        if (index === 0) return realCount - 1;
        if (index === realCount + 1) return 0;
        return index - 1;
    }

    function render() {
        track.style.transform = 'translateX(' + (-index * 100) + '%)';
    }

    function goTo(nextIndex) {
        index = nextIndex;
        render();
    }

    function jumpWithoutTransition(targetIndex) {
        track.style.transition = 'none';
        index = targetIndex;
        render();
        track.offsetHeight;
        track.style.transition = '';
    }

    function stopAutoplay() {
        if (!timer) return;
        clearInterval(timer);
        timer = null;
    }

    function startAutoplay() {
        if (slides.length <= 1 || timer) return;
        timer = setInterval(function () {
            goTo(index + 1);
        }, intervalMs);
    }

    slides.forEach(function (_, i) {
        var dot = document.createElement('button');
        dot.type = 'button';
        dot.className = 'featured-carousel__dot';
        dot.setAttribute('aria-label', 'Ir al torneo ' + (i + 1));
        dot.addEventListener('click', function (e) {
            e.stopPropagation();
            goTo(useInfinite ? i + 1 : i);
            stopAutoplay();
            startAutoplay();
        });
    });

    if (useInfinite) {
        track.addEventListener('transitionend', function () {
            if (index === 0) {
                jumpWithoutTransition(realCount);
            } else if (index === realCount + 1) {
                jumpWithoutTransition(1);
            }
        });
    }

    carousel.addEventListener('mouseenter', stopAutoplay);
    carousel.addEventListener('mouseleave', startAutoplay);
    carousel.addEventListener('focusin', stopAutoplay);
    carousel.addEventListener('focusout', startAutoplay);

    document.addEventListener('visibilitychange', function () {
        if (document.hidden) stopAutoplay();
        else startAutoplay();
    });

    if (useInfinite) {
        jumpWithoutTransition(1);
    } else {
        render();
    }
    startAutoplay();
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
    function teamTag(tag) {
            var t = String(tag || '').trim();
            // Si viene "?" o "-" o "null" o "undefined" → no hay equipo
            if (t === '' || t === '?' || t === '-' || t.toLowerCase() === 'null' || t.toLowerCase() === 'undefined') {
                return 'Por definir';
            }
            return t;
    }
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

const ROLES_POR_JUEGO = {
    "League of Legends": ["Top", "Jungle", "Mid", "ADC", "Support"],
    "Valorant":          ["Controlador", "Duelista", "Iniciador", "Centinela","IGL", "Entry Fragger", "Lurker"],
    "CS2":               ["IGL", "Entry Fragger", "Lurker", "AWPer", "Support"],
    "Dota 2":            ["Carry", "Mid", "Offlaner", "Soft Support", "Hard Support"],
    "Rocket League":     ["Striker", "Midfield", "Goalkeeper"],
    "Fortnite":          ["Builder", "Fragger", "Support"],
    "Apex Legends":      ["Tanque", "Sanador", "Reconocimiento", "Control"],
    "Overwatch 2":       ["Tank", "DPS", "Support"],
    "Rainbow Six Siege": ["IGL", "Hard Breach", "Soft Breach", "Atacante", "Defensor"],
    "FIFA":              ["Portero", "Defensa", "Centrocampista", "Delantero"]
};

function actualizarRoles() {
    const juegoSelect = document.getElementById("juego");
    const rolSelect   = document.getElementById("rol");
    const juegoNombre = juegoSelect.value;
    const rolActual   = rolSelect.dataset.valorActual || "";

    rolSelect.innerHTML = "<option value=\"\">-- Selecciona un rol --</option>";

    const roles = ROLES_POR_JUEGO[juegoNombre] || [];
    roles.forEach(function(r) {
        const opt = document.createElement("option");
        opt.value = r;
        opt.textContent = r;
        if (r === rolActual) opt.selected = true;
        rolSelect.appendChild(opt);
    });

    // Si no hay roles definidos para este juego deja el select vacÃ­o con el placeholder
}

document.addEventListener("DOMContentLoaded", function () {
    const juegoSelect = document.getElementById("juego");
    if (juegoSelect) {
        actualizarRoles();
        juegoSelect.addEventListener("change", actualizarRoles);
    }
});

