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
