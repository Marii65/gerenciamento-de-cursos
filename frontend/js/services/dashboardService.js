import { apiRequest } from "./api.js";

export async function carregarDadosDashboard() {
    const [alunos, cursos, matriculas] = await Promise.all([
        apiRequest("/alunos"),
        apiRequest("/cursos"),
        apiRequest("/matriculas")
    ]);

    return {
        alunos,
        cursos,
        matriculas
    };
}