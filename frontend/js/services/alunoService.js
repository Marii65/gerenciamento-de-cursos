import { apiRequest } from "./api.js";


export async function listarAlunos() {
    return apiRequest("/alunos");
}


export async function buscarAlunoPorId(id) {
    return apiRequest(`/alunos/${id}`);
}


export async function criarAluno(aluno) {
    return apiRequest("/alunos", {
        method: "POST",
        body: JSON.stringify(aluno)
    });
}


export async function atualizarAluno(id, aluno) {
    return apiRequest(`/alunos/${id}`, {
        method: "PUT",
        body: JSON.stringify(aluno)
    });
}


export async function excluirAluno(id) {
    return apiRequest(`/alunos/${id}`, {
        method: "DELETE"
    });
}