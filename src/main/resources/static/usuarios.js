const API_URL = "http://localhost:8080/usuarios";


async function cargarUsuarios() {
    try {
        const respuesta = await fetch(API_URL);
        if (!respuesta.ok) throw new Error("Error al obtener usuarios");

        const usuarios = await respuesta.json();
        const tabla = document.querySelector("#tablaUsuarios tbody");
        tabla.innerHTML = ""; // limpiar

        usuarios.forEach(u => {
            const fila = `
                <tr>
                    <td>${u.login}</td>
                    <td>${u.rol}</td>
                    <td>${u.apiKey}</td>
                    <td>${u.persona ? u.persona.nombre + " " + u.persona.apellido : "Sin persona"}</td>
                </tr>
            `;
            tabla.innerHTML += fila;
        });
    } catch (error) {
        alert("No se pudieron cargar los usuarios");
        console.error(error);
    }
    async function crearUsuario(event) {
        event.preventDefault();

        const nuevoUsuario = {
            login: document.getElementById("login").value,
            password: document.getElementById("password").value,
            rol: document.getElementById("rol").value,
            persona: { id: document.getElementById("personaId").value }
        };

        try {
            const respuesta = await fetch(API_URL, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(nuevoUsuario)
            });

            if (!respuesta.ok) throw new Error("Error al crear el usuario");

            alert(" Usuario registrado correctamente");
            cargarUsuarios(); // refresca la tabla
        } catch (error) {
            alert(" No se pudo crear el usuario");
            console.error(error);
        }
    }

}
