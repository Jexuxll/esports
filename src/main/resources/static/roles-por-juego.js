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

    // Si no hay roles definidos para este juego deja el select vacío con el placeholder
}

document.addEventListener("DOMContentLoaded", function () {
    const juegoSelect = document.getElementById("juego");
    if (juegoSelect) {
        actualizarRoles();
        juegoSelect.addEventListener("change", actualizarRoles);
    }
});
