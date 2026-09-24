import { apiRequest } from "./api.js";

export async function listarCursos() {
    return apiRequest("/cursos");
}

export async function criarCurso(curso) {
    return apiRequest("/cursos", {
        method: "POST",
        body: JSON.stringify(curso)
    });
}

export async function atualizarCurso(id, curso) {
    return apiRequest(`/cursos/${id}`, {
        method: "PUT",
        body: JSON.stringify(curso)
    });
}

export async function excluirCurso(id) {
    return apiRequest(`/cursos/${id}`, {
        method: "DELETE"
    });
}