import { openModal } from "../components/Modal.js";
import { renderEmptyState } from "../components/EmptyState.js";
import { showToast } from "../components/Toast.js";

import {
    listarMatriculas,
    criarMatricula,
    atualizarStatusMatricula
} from "../services/matriculaService.js";

import { listarAlunos } from "../services/alunoService.js";
import { listarCursos } from "../services/cursoService.js";

let matriculas = [];


/**
 * Renderiza a página de matrículas.
 */
export async function renderMatriculas() {

    const content =
        document.querySelector("#content");

    content.innerHTML = `

        <div class="page-header">

            <div>

                <span class="section-label">
                    GERENCIAMENTO
                </span>

                <h2>
                    Matrículas
                </h2>

                <p>
                    Consulte e gerencie as matrículas dos alunos.
                </p>

            </div>

            <button
                class="btn btn-primary"
                id="btn-nova-matricula"
                type="button"
            >
                + Nova matrícula
            </button>

        </div>


        <div class="card">

            <div class="toolbar">

                <input
                    type="search"
                    class="search-input"
                    id="search-matricula"
                    placeholder="Buscar por aluno ou curso..."
                >

            </div>


            <div id="matriculas-table-container">

                <div class="empty-state">

                    <div class="empty-state-icon">
                        ...
                    </div>

                    <h3>
                        Carregando matrículas
                    </h3>

                    <p>
                        Buscando dados da API.
                    </p>

                </div>

            </div>

        </div>

    `;

    setupMatriculasEvents();

    await carregarMatriculas();

}


/**
 * Busca as matrículas na API.
 */
async function carregarMatriculas() {

    try {

        matriculas = await listarMatriculas();

        renderMatriculasTable(matriculas);

    } catch (error) {

        console.error(error);

        const container =
            document.querySelector(
                "#matriculas-table-container"
            );

        container.innerHTML =
            renderEmptyState(
                "Não foi possível carregar as matrículas",
                error.message
            );

        showToast(
            "Erro ao carregar as matrículas.",
            "error"
        );

    }

}


/**
 * Renderiza a tabela de matrículas.
 *
 * @param {Array} lista - Lista de matrículas.
 */
function renderMatriculasTable(lista) {

    const container =
        document.querySelector(
            "#matriculas-table-container"
        );


    if (!lista.length) {

        container.innerHTML =
            renderEmptyState(
                "Nenhuma matrícula cadastrada",
                "Cadastre uma matrícula para começar."
            );

        return;
    }


    container.innerHTML = `

        <div class="table-wrapper">

            <table class="data-table">

                <thead>

                    <tr>

                        <th>Aluno</th>

                        <th>Curso</th>

                        <th>Data da matrícula</th>

                        <th>Status</th>

                        <th>Ações</th>

                    </tr>

                </thead>

                <tbody>

                    ${lista.map(matricula => `

                        <tr>

                            <td>
                                <strong>
                                    ${matricula.alunoNome}
                                </strong>
                            </td>

                            <td>
                                ${matricula.cursoNome}
                            </td>

                            <td>
                                ${formatarData(
                                    matricula.dataMatricula
                                )}
                            </td>

                            <td>

                                <span class="status status-${matricula.status.toLowerCase()}">
                                    ${formatarStatus(
                                        matricula.status
                                    )}
                                </span>

                            </td>

                            <td>

                                ${
                                    matricula.status === "ATIVA"
                                        ? `
                                            <button
                                                class="btn btn-danger btn-cancelar-matricula"
                                                data-id="${matricula.id}"
                                                type="button"
                                            >
                                                Cancelar
                                            </button>
                                          `
                                        : `
                                            <span>
                                                -
                                            </span>
                                          `
                                }

                            </td>

                        </tr>

                    `).join("")}

                </tbody>

            </table>

        </div>

    `;

    setupTableEvents();

}


/**
 * Configura os eventos da página.
 */
function setupMatriculasEvents() {

    const button =
        document.querySelector(
            "#btn-nova-matricula"
        );


    button.addEventListener(
        "click",
        openNovaMatriculaModal
    );


    const search =
        document.querySelector(
            "#search-matricula"
        );


    search.addEventListener(
        "input",
        handleSearch
    );

}


/**
 * Configura os eventos dos botões da tabela.
 */
function setupTableEvents() {

    const cancelButtons =
        document.querySelectorAll(
            ".btn-cancelar-matricula"
        );


    cancelButtons.forEach(button => {

        button.addEventListener(
            "click",
            () => {

                const id =
                    button.dataset.id;

                const matricula =
                    matriculas.find(
                        item => item.id === id
                    );

                if (matricula) {
                    openCancelarMatriculaModal(
                        matricula
                    );
                }

            }
        );

    });

}


/**
 * Abre o modal de nova matrícula.
 *
 */
async function openNovaMatriculaModal() {

    const modal = openModal({
        title: "Nova matrícula",
        content: `
            <div class="form-group">
                <label for="matricula-aluno">
                    Aluno
                </label>

                <select
                    id="matricula-aluno"
                    class="form-input"
                    required
                >
                    <option value="">
                        Carregando alunos...
                    </option>
                </select>
            </div>

            <div class="form-group">
                <label for="matricula-curso">
                    Curso
                </label>

                <select
                    id="matricula-curso"
                    class="form-input"
                    required
                >
                    <option value="">
                        Carregando cursos...
                    </option>
                </select>
            </div>

            <div class="modal-footer">

                <button
                    type="button"
                    class="btn btn-secondary"
                    id="btn-fechar-matricula"
                >
                    Cancelar
                </button>

                <button
                    type="button"
                    class="btn btn-primary"
                    id="btn-confirmar-matricula"
                >
                    Matricular aluno
                </button>

            </div>
        `
    });

    const alunoSelect =
        document.querySelector("#matricula-aluno");

    const cursoSelect =
        document.querySelector("#matricula-curso");

    const fecharButton =
        document.querySelector("#btn-fechar-matricula");

    const confirmarButton =
        document.querySelector("#btn-confirmar-matricula");

    fecharButton.addEventListener(
        "click",
        modal.close
    );

    try {

        const [alunos, cursos] = await Promise.all([
            listarAlunos(),
            listarCursos()
        ]);

        if (!alunos.length) {

            alunoSelect.innerHTML = `
                <option value="">
                    Nenhum aluno cadastrado
                </option>
            `;

        } else {

            alunoSelect.innerHTML = `
                <option value="">
                    Selecione um aluno
                </option>

                ${alunos.map(aluno => `
                    <option value="${aluno.id}">
                        ${aluno.nome}
                    </option>
                `).join("")}
            `;

        }

        if (!cursos.length) {

            cursoSelect.innerHTML = `
                <option value="">
                    Nenhum curso cadastrado
                </option>
            `;

        } else {

            cursoSelect.innerHTML = `
                <option value="">
                    Selecione um curso
                </option>

                ${cursos.map(curso => `
                    <option value="${curso.id}">
                        ${curso.nome}
                    </option>
                `).join("")}
            `;

        }

    } catch (error) {

        console.error(error);

        modal.close();

        showToast(
            error.message,
            "error"
        );

        return;
    }

    confirmarButton.addEventListener(
        "click",
        async () => {

            const alunoId =
                alunoSelect.value;

            const cursoId =
                cursoSelect.value;

            if (!alunoId || !cursoId) {

                showToast(
                    "Selecione o aluno e o curso.",
                    "error"
                );

                return;
            }

            confirmarButton.disabled = true;

            confirmarButton.textContent =
                "Matriculando...";

            try {

                await criarMatricula({
                    alunoId,
                    cursoId
                });

                modal.close();

                showToast(
                    "Matrícula criada com sucesso!",
                    "success"
                );

                await carregarMatriculas();

            } catch (error) {

                console.error(error);

                showToast(
                    error.message,
                    "error"
                );

                confirmarButton.disabled = false;

                confirmarButton.textContent =
                    "Matricular aluno";
            }

        }
    );
}


/**
 * Abre o modal de confirmação de cancelamento.
 *
 * @param {Object} matricula
 */
function openCancelarMatriculaModal(matricula) {

    const modal = openModal({

        title: "Cancelar matrícula",

        content: `

            <div>

                <p style="
                    margin-bottom: 20px;
                    color: var(--text-secondary);
                    font-size: 13px;
                    line-height: 1.5;
                ">

                    Tem certeza que deseja cancelar a matrícula de

                    <strong>
                        ${matricula.alunoNome}
                    </strong>

                    no curso

                    <strong>
                        ${matricula.cursoNome}
                    </strong>?

                    Esta ação não poderá ser desfeita.

                </p>


                <div class="modal-footer">

                    <button
                        type="button"
                        class="btn btn-secondary"
                        id="btn-cancelar-modal"
                    >
                        Voltar
                    </button>

                    <button
                        type="button"
                        class="btn btn-danger"
                        id="btn-confirmar-cancelamento"
                    >
                        Cancelar matrícula
                    </button>

                </div>

            </div>

        `

    });


    const cancelButton =
        document.querySelector(
            "#btn-cancelar-modal"
        );


    const confirmButton =
        document.querySelector(
            "#btn-confirmar-cancelamento"
        );


    cancelButton.addEventListener(
        "click",
        modal.close
    );


    confirmButton.addEventListener(
        "click",
        async () => {

            await handleCancelarMatricula(
                matricula.id,
                modal
            );

        }
    );

}


/**
 * Cancela uma matrícula através da API.
 *
 * @param {string} id
 * @param {Object} modal
 */
async function handleCancelarMatricula(
    id,
    modal
) {

    const button =
        document.querySelector(
            "#btn-confirmar-cancelamento"
        );


    button.disabled = true;

    button.textContent =
        "Cancelando...";


    try {

        await atualizarStatusMatricula(
            id,
            "CANCELADA"
        );

        modal.close();

        showToast(
            "Matrícula cancelada com sucesso!",
            "success"
        );

        await carregarMatriculas();

    } catch (error) {

        console.error(error);

        showToast(
            error.message,
            "error"
        );

        button.disabled = false;

        button.textContent =
            "Cancelar matrícula";

    }

}


/**
 * Filtra matrículas pelo nome do aluno ou curso.
 *
 * @param {Event} event
 */
function handleSearch(event) {

    const searchValue =
        event.target.value
            .trim()
            .toLowerCase();


    const filteredMatriculas =
        matriculas.filter(matricula =>
            matricula.alunoNome
                .toLowerCase()
                .includes(searchValue)
            ||
            matricula.cursoNome
                .toLowerCase()
                .includes(searchValue)
        );


    renderMatriculasTable(
        filteredMatriculas
    );

}


/**
 * Formata uma data ISO para o padrão brasileiro.
 *
 * @param {string} date
 * @returns {string}
 */
function formatarData(date) {

    if (!date) {
        return "-";
    }


    const [year, month, day] =
        date.split("-");


    return `${day}/${month}/${year}`;

}


/**
 * Formata o status da matrícula.
 *
 * @param {string} status
 * @returns {string}
 */
function formatarStatus(status) {

    const statusMap = {

        ATIVA: "Ativa",

        CANCELADA: "Cancelada",

        CONCLUIDA: "Concluída"

    };


    return statusMap[status] || status;

}