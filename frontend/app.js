// Cambia esta dirección si tu API utiliza otro puerto o prefijo.
const API_URL = "http://localhost:8080";

const $ = (selector) => document.querySelector(selector);

function mostrarMensaje(selector, texto, tipo = "") {
    const elemento = $(selector);
    elemento.textContent = texto;
    elemento.className = `message ${tipo}`.trim();
}

async function pedirDatos(ruta) {
    const respuesta = await fetch(`${API_URL}${ruta}`, {
        headers: { "Accept": "application/json" }
    });
    if (!respuesta.ok) {
        throw new Error(`La API respondió con el estado ${respuesta.status}`);
    }
    return respuesta.status === 204 ? [] : respuesta.json();
}

function nombreAula(aula) {
    return aula.nombre ?? `Aula ${aula.idAula ?? ""}`;}
function nombreProfesor(profesor) {
    return [profesor.name ?? profesor.nombre, profesor.apellido].filter(Boolean).join(" ") || `Profesor ${profesor.idProfesor ?? ""}`;
}
function hora(valor) {
    return valor ? String(valor).slice(0, 5) : "—";
}
function pintarFilas(selector, filas, columnas, mensajeVacio) {
    const cuerpo = $(selector);
    cuerpo.replaceChildren();
    if (!filas.length) {
        const tr = document.createElement("tr");
        const td = document.createElement("td");
        td.colSpan = columnas;
        td.className = "empty";
        td.textContent = mensajeVacio;
        tr.append(td);
        cuerpo.append(tr);
        return;
    }
    filas.forEach((fila) => {
        const tr = document.createElement("tr");
        fila.forEach((contenido) => {
            const td = document.createElement("td");
            if (contenido instanceof HTMLElement) td.append(contenido);
            else td.textContent = String(contenido ?? "—");
            tr.append(td);
        });
        cuerpo.append(tr);
    });
}
function opcion(select, valor, texto) {
    const elemento = document.createElement("option");
    elemento.value = valor;
    elemento.textContent = texto;
    select.append(elemento);
}

let todasLasAulas = [];

async function cargarAulas() {
    try {
        todasLasAulas = await pedirDatos("/aulas");
        $("#total-aulas").textContent = todasLasAulas.length;
        pintarAulas(todasLasAulas);
        const select = $("#aula");
        const seleccionado = select.value;
        select.replaceChildren();
        opcion(select, "", "Selecciona un aula");
        todasLasAulas.forEach((aula) => opcion(select, aula.idAula, `${nombreAula(aula)} · ${aula.capacidad ?? "?"} plazas`));        select.value = seleccionado;
        mostrarMensaje("#aulas-message", `${todasLasAulas.length} aula(s) cargada(s).`, "success");
    } catch (error) {
        pintarFilas("#aulas-body", [], 3, "No se pudieron cargar las aulas.");
        mostrarMensaje("#aulas-message", `${error.message}. Comprueba que Spring Boot está iniciado y permite peticiones CORS desde este frontend.`, "error");
        $("#total-aulas").textContent = "—";
    }
}

function pintarAulas(aulas) {
    pintarFilas("#aulas-body", aulas.map((aula) => {
        const badge = document.createElement("span");
        badge.className = `pill ${aula.ordenadores ? "" : "no"}`.trim();
        badge.textContent = aula.ordenadores ? "Sí" : "No";
        return [nombreAula(aula), aula.capacidad ?? "—", badge];
    }), 3, "No hay aulas que coincidan con los filtros.");
}

async function cargarProfesores() {
    try {
        const profesores = await pedirDatos("/profesores");
        $("#total-profesores").textContent = profesores.length;
        pintarFilas("#profesores-body", profesores.map((p) => [p.name ?? p.nombre ?? "—", p.apellido ?? "—", p.email ?? "—"]), 3, "Todavía no hay profesores registrados.");
        const select = $("#profesor");
        const seleccionado = select.value;
        select.replaceChildren();
        opcion(select, "", "Selecciona un profesor");
        profesores.forEach((p) => opcion(select, p.idProfesor, nombreProfesor(p)));
        select.value = seleccionado;
    } catch (error) {
        pintarFilas("#profesores-body", [], 3, "No se pudieron cargar los profesores.");
    }
}

async function cargarFranjas() {
    try {
        const franjas = await pedirDatos("/franjasHorarias");
        $("#total-franjas").textContent = franjas.length;
        pintarFilas("#franjas-body", franjas.map((f) => [hora(f.horaInicio), hora(f.horaFin)]), 2, "Todavía no hay franjas horarias registradas.");
        const select = $("#franja");
        const seleccionado = select.value;
        select.replaceChildren();
        opcion(select, "", "Selecciona una franja");
        franjas.forEach((f) => opcion(select, f.idFranja, `${hora(f.horaInicio)} – ${hora(f.horaFin)}`));
        select.value = seleccionado;
    } catch (error) {
        pintarFilas("#franjas-body", [], 2, "No se pudieron cargar las franjas. Revisa la ruta de la API.");
        $("#total-franjas").textContent = "—";
    }
}

async function cargarReservas() {
    try {
        const reservas = await pedirDatos("/reservas");
        $("#total-reservas").textContent = reservas.length;
    } catch (error) {
        $("#total-reservas").textContent = "—";
    }
}

$("#filtros-aulas").addEventListener("submit", (evento) => {
    evento.preventDefault();
    const ordenador = $("#filtro-ordenadores").value;
    const capacidad = $("#filtro-capacidad").value;
    let filtradas = todasLasAulas.filter((aula) => {
        const coincideOrdenadores = ordenador === "" || String(Boolean(aula.ordenadores)) === ordenador;
        const coincideCapacidad = capacidad === "" || Number(aula.capacidad) > Number(capacidad);
        return coincideOrdenadores && coincideCapacidad;
    });
    pintarAulas(filtradas);
    mostrarMensaje("#aulas-message", `${filtradas.length} aula(s) encontradas.`, "success");
});

$("#limpiar-filtros").addEventListener("click", () => {
    $("#filtros-aulas").reset();
    pintarAulas(todasLasAulas);
    mostrarMensaje("#aulas-message", "Filtros eliminados.");
});
$("#refresh-aulas").addEventListener("click", cargarAulas);
$("#refresh-profesores").addEventListener("click", cargarProfesores);
$("#refresh-franjas").addEventListener("click", cargarFranjas);

$("#reserva-form").addEventListener("submit", async (evento) => {
    evento.preventDefault();


    const datos = {
        fecha: $("#fecha").value,
        profesorId: Number($("#profesor").value),
        aulaId: Number($("#aula").value),
        franjaId: Number($("#franja").value)
    };


    console.log("JSON enviado:", JSON.stringify(datos));
    try {
        const respuesta = await fetch(`${API_URL}/reservas`, {
            method: "POST",
            headers: { "Content-Type": "application/json", "Accept": "application/json" },
            body: JSON.stringify(datos)
        });
        if (!respuesta.ok) {
            const detalle = await respuesta.text();
            throw new Error(`No se pudo crear la reserva (${respuesta.status}). ${detalle.slice(0, 180)}`);
        }
        mostrarMensaje("#reserva-message", "¡Reserva creada correctamente!", "success");
        await cargarReservas();
        evento.target.reset();
    } catch (error) {
        mostrarMensaje("#reserva-message", `${error.message}. Comprueba que la API está activa y que CORS está configurado.`, "error");
    }
});

// Las fechas pasadas no se pueden elegir desde el formulario.
// La API también debe validar esta regla en el backend.
const fechaInput = $("#fecha");
fechaInput.min = new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10);

Promise.all([cargarAulas(), cargarProfesores(), cargarFranjas(), cargarReservas()]);
