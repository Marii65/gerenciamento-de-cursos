import { carregarDadosDashboard } from "../services/dashboardService.js";
import { renderEmptyState } from "../components/EmptyState.js";
import { showToast } from "../components/Toast.js";

export async function renderDashboard() {

    const content = document.querySelector("#content");

    content.innerHTML = `
        <div class="page-header">
            <div>
                <span class="section-label">VISÃO GERAL</span>
                <h2>Dashboard</h2>
                <p>Resumo do sistema de gerenciamento de cursos.</p>
            </div>
        </div>

        <div id="dashboard-container">

            <div class="empty-state">
                <div class="empty-state-icon">...</div>
                <h3>Carregando dashboard</h3>
                <p>Buscando informações do sistema.</p>
            </div>

        </div>
    `;

    await carregarDashboard();
}

async function carregarDashboard() {

    try {

        const dados = await carregarDadosDashboard();

        renderDashboardContent(dados);

    } catch (error) {

        console.error(error);

        const container =
            document.querySelector("#dashboard-container");

        container.innerHTML = renderEmptyState(
            "Não foi possível carregar o dashboard",
            error.message
        );

        showToast(
            "Erro ao carregar o dashboard.",
            "error"
        );
    }
}

function renderDashboardContent({
    alunos,
    cursos,
    matriculas
}) {

    const container =
        document.querySelector("#dashboard-container");

    const matriculasAtivas =
        matriculas.filter(
            matricula => matricula.status === "ATIVA"
        ).length;

    const matriculasConcluidas =
        matriculas.filter(
            matricula => matricula.status === "CONCLUIDA"
        ).length;

    const matriculasCanceladas =
        matriculas.filter(
            matricula => matricula.status === "CANCELADA"
        ).length;

    container.innerHTML = `

        <div class="dashboard-stats">

            <div class="card dashboard-stat">

                <div class="dashboard-stat-label">
                    ALUNOS
                </div>

                <div class="dashboard-stat-value">
                    ${alunos.length}
                </div>

                <p>Total de alunos cadastrados</p>

            </div>

            <div class="card dashboard-stat">

                <div class="dashboard-stat-label">
                    CURSOS
                </div>

                <div class="dashboard-stat-value">
                    ${cursos.length}
                </div>

                <p>Total de cursos cadastrados</p>

            </div>

            <div class="card dashboard-stat">

                <div class="dashboard-stat-label">
                    MATRÍCULAS ATIVAS
                </div>

                <div class="dashboard-stat-value">
                    ${matriculasAtivas}
                </div>

                <p>Alunos atualmente matriculados</p>

            </div>

            <div class="card dashboard-stat">

                <div class="dashboard-stat-label">
                    MATRÍCULAS CONCLUÍDAS
                </div>

                <div class="dashboard-stat-value">
                    ${matriculasConcluidas}
                </div>

                <p>Matriculas concluídas</p>

            </div>

        </div>

        <div class="dashboard-grid">

            <div class="card">

                <div class="card-header">
                    <div>
                        <span class="section-label">
                            MATRÍCULAS
                        </span>

                        <h3>
                            Situação das matrículas
                        </h3>
                    </div>
                </div>

                <div class="dashboard-status-list">

                    <div class="dashboard-status-item">

                        <span>
                            <span class="status status-ativa">
                                Ativas
                            </span>
                        </span>

                        <strong>
                            ${matriculasAtivas}
                        </strong>

                    </div>

                    <div class="dashboard-status-item">

                        <span>
                            <span class="status status-concluida">
                                Concluídas
                            </span>
                        </span>

                        <strong>
                            ${matriculasConcluidas}
                        </strong>

                    </div>

                    <div class="dashboard-status-item">

                        <span>
                            <span class="status status-cancelada">
                                Canceladas
                            </span>
                        </span>

                        <strong>
                            ${matriculasCanceladas}
                        </strong>

                    </div>

                </div>

            </div>

            <div class="card">

                <div class="card-header">
                    <div>
                        <span class="section-label">
                            SISTEMA
                        </span>

                        <h3>
                            Resumo
                        </h3>
                    </div>
                </div>

                <div class="dashboard-summary">

                    <div>
                        <span>Alunos cadastrados</span>
                        <strong>${alunos.length}</strong>
                    </div>

                    <div>
                        <span>Cursos disponíveis</span>
                        <strong>${cursos.length}</strong>
                    </div>

                    <div>
                        <span>Total de matrículas</span>
                        <strong>${matriculas.length}</strong>
                    </div>

                </div>

            </div>

        </div>
    `;
}