import { apiRequest } from "./api.js";

export async function listarMatriculas() {
    return apiRequest("/matriculas");
}

export async function criarMatricula(matricula) {
    return apiRequest("/matriculas", {
        method: "POST",
        body: JSON.stringify(matricula)
    });
}

export async function atualizarStatusMatricula(id, status) {
    return apiRequest(`/matriculas/${id}`, {
        method: "PATCH",
        body: JSON.stringify({
            status
        })
    });
}