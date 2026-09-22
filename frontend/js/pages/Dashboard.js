/**
 * Renderiza a página inicial da aplicação.
 */

export function renderDashboard() {

    const content = document.querySelector("#content");

    content.innerHTML = `

        <div class="page-header">

            <div>
                <span class="section-label">
                    VISÃO GERAL
                </span>

                <h2>
                    Dashboard
                </h2>

                <p>
                    Acompanhe os principais dados do sistema.
                </p>
            </div>

        </div>


        <div class="welcome-card">

            <span>
                SISTEMA DE GERENCIAMENTO
            </span>

            <h2>
                Bem-vindo ao sistema!
            </h2>

            <p>
                Gerencie alunos, cursos e matrículas
                de forma simples e organizada.
            </p>

        </div>


        <div class="stats-grid">

            <div class="card stat-card">

                <div class="stat-icon blue">
                    👤
                </div>

                <div class="stat-info">

                    <span>
                        Total de alunos
                    </span>

                    <strong>
                        0
                    </strong>

                </div>

            </div>


            <div class="card stat-card">

                <div class="stat-icon green">
                    ▣
                </div>

                <div class="stat-info">

                    <span>
                        Total de cursos
                    </span>

                    <strong>
                        0
                    </strong>

                </div>

            </div>


            <div class="card stat-card">

                <div class="stat-icon orange">
                    ✓
                </div>

                <div class="stat-info">

                    <span>
                        Matrículas ativas
                    </span>

                    <strong>
                        0
                    </strong>

                </div>

            </div>

        </div>


        <div class="card">

            <div class="card-header">

                <h3>
                    Resumo das matrículas
                </h3>

                <p>
                    Distribuição das matrículas por status.
                </p>

            </div>


            <div class="empty-state">

                <div class="empty-state-icon">
                    ⓘ
                </div>

                <h3>
                    Nenhuma matrícula registrada
                </h3>

                <p>
                    Os dados aparecerão aqui quando
                    houver matrículas cadastradas.
                </p>

            </div>

        </div>

    `;
}