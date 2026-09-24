import { openModal } from "../components/Modal.js";
import { renderEmptyState } from "../components/EmptyState.js";
import { showToast } from "../components/Toast.js";

import {
    listarCursos,
    criarCurso,
    atualizarCurso,
    excluirCurso
} from "../services/cursoService.js";

let cursos = [];

export async function renderCursos() {
    const content = document.querySelector("#content");

    content.innerHTML = `
        <div class="page-header">
            <div>
                <span class="section-label">GERENCIAMENTO</span>
                <h2>Cursos</h2>
                <p>Cadastre e gerencie os cursos disponíveis.</p>
            </div>

            <button
                class="btn btn-primary"
                id="btn-novo-curso"
                type="button"
            >
                + Novo curso
            </button>
        </div>

        <div class="card">

            <div class="toolbar">
                <input
                    type="search"
                    class="search-input"
                    id="search-curso"
                    placeholder="Buscar por nome..."
                >
            </div>

            <div id="cursos-table-container">
                <div class="empty-state">
                    <div class="empty-state-icon">...</div>
                    <h3>Carregando cursos</h3>
                    <p>Buscando dados da API.</p>
                </div>
            </div>

        </div>
    `;

    setupCursosEvents();

    await carregarCursos();
}

async function carregarCursos() {
    try {
        cursos = await listarCursos();

        renderCursosTable(cursos);

    } catch (error) {

        console.error(error);

        const container =
            document.querySelector("#cursos-table-container");

        container.innerHTML = renderEmptyState(
            "Não foi possível carregar os cursos",
            error.message
        );

        showToast(
            "Erro ao carregar os cursos.",
            "error"
        );
    }
}

function renderCursosTable(lista) {
    const container =
        document.querySelector("#cursos-table-container");

    if (!lista.length) {

        container.innerHTML = renderEmptyState(
            "Nenhum curso cadastrado",
            "Cadastre um curso para começar."
        );

        return;
    }

    container.innerHTML = `
        <div class="table-wrapper">

            <table class="data-table">

                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Descrição</th>
                        <th>Carga horária</th>
                        <th>Ações</th>
                    </tr>
                </thead>

                <tbody>

                    ${lista.map(curso => `
                        <tr>

                            <td>
                                <strong>${curso.nome}</strong>
                            </td>

                            <td>
                                ${curso.descricao || "-"}
                            </td>

                            <td>
                                ${curso.cargaHoraria}h
                            </td>

                            <td>

                                <div class="table-actions">

                                    <button
                                        class="btn btn-secondary btn-editar-curso"
                                        data-id="${curso.id}"
                                        type="button"
                                    >
                                        Editar
                                    </button>

                                    <button
                                        class="btn btn-danger btn-excluir-curso"
                                        data-id="${curso.id}"
                                        type="button"
                                    >
                                        Excluir
                                    </button>

                                </div>

                            </td>

                        </tr>
                    `).join("")}

                </tbody>

            </table>

        </div>
    `;

    setupTableEvents();
}

function setupCursosEvents() {

    const button =
        document.querySelector("#btn-novo-curso");

    button.addEventListener(
        "click",
        () => openCursoModal()
    );

    const search =
        document.querySelector("#search-curso");

    search.addEventListener(
        "input",
        handleSearch
    );
}

function setupTableEvents() {

    document
        .querySelectorAll(".btn-editar-curso")
        .forEach(button => {

            button.addEventListener(
                "click",
                () => {

                    const curso =
                        cursos.find(
                            item => item.id === button.dataset.id
                        );

                    if (curso) {
                        openCursoModal(curso);
                    }

                }
            );

        });

    document
        .querySelectorAll(".btn-excluir-curso")
        .forEach(button => {

            button.addEventListener(
                "click",
                () => {

                    const curso =
                        cursos.find(
                            item => item.id === button.dataset.id
                        );

                    if (curso) {
                        openExcluirCursoModal(curso);
                    }

                }
            );

        });
}

function openCursoModal(curso = null) {

    const editando = Boolean(curso);

    const modal = openModal({

        title: editando
            ? "Editar curso"
            : "Novo curso",

        content: `

            <div class="form-group">

                <label for="curso-nome">
                    Nome
                </label>

                <input
                    type="text"
                    id="curso-nome"
                    class="form-input"
                    maxlength="100"
                    value="${curso?.nome || ""}"
                    placeholder="Nome do curso"
                    required
                >

            </div>

            <div class="form-group">

                <label for="curso-descricao">
                    Descrição
                </label>

                <textarea
                    id="curso-descricao"
                    class="form-input"
                    maxlength="500"
                    rows="4"
                    placeholder="Descrição do curso"
                >${curso?.descricao || ""}</textarea>

            </div>

            <div class="form-group">

                <label for="curso-carga">
                    Carga horária
                </label>

                <input
                    type="number"
                    id="curso-carga"
                    class="form-input"
                    min="1"
                    value="${curso?.cargaHoraria || ""}"
                    placeholder="Ex.: 40"
                    required
                >

            </div>

            <div class="modal-footer">

                <button
                    type="button"
                    class="btn btn-secondary"
                    id="btn-fechar-curso"
                >
                    Cancelar
                </button>

                <button
                    type="button"
                    class="btn btn-primary"
                    id="btn-salvar-curso"
                >
                    ${editando ? "Salvar alterações" : "Cadastrar curso"}
                </button>

            </div>
        `
    });

    const fecharButton =
        document.querySelector("#btn-fechar-curso");

    const salvarButton =
        document.querySelector("#btn-salvar-curso");

    fecharButton.addEventListener(
        "click",
        modal.close
    );

    salvarButton.addEventListener(
        "click",
        async () => {

            const nome =
                document.querySelector("#curso-nome").value.trim();

            const descricao =
                document.querySelector("#curso-descricao").value.trim();

            const cargaHoraria =
                document.querySelector("#curso-carga").value;

            if (!nome || !cargaHoraria) {

                showToast(
                    "Preencha os campos obrigatórios.",
                    "error"
                );

                return;
            }

            salvarButton.disabled = true;

            salvarButton.textContent =
                editando
                    ? "Salvando..."
                    : "Cadastrando...";

            try {

                const dados = {
                    nome,
                    descricao,
                    cargaHoraria: Number(cargaHoraria)
                };

                if (editando) {

                    await atualizarCurso(
                        curso.id,
                        dados
                    );

                    showToast(
                        "Curso atualizado com sucesso!",
                        "success"
                    );

                } else {

                    await criarCurso(dados);

                    showToast(
                        "Curso cadastrado com sucesso!",
                        "success"
                    );
                }

                modal.close();

                await carregarCursos();

            } catch (error) {

                console.error(error);

                showToast(
                    error.message,
                    "error"
                );

                salvarButton.disabled = false;

                salvarButton.textContent =
                    editando
                        ? "Salvar alterações"
                        : "Cadastrar curso";
            }

        }
    );
}

function openExcluirCursoModal(curso) {

    const modal = openModal({

        title: "Excluir curso",

        content: `

            <p style="
                margin-bottom: 20px;
                color: var(--text-secondary);
                font-size: 13px;
                line-height: 1.5;
            ">

                Tem certeza que deseja excluir o curso
                <strong>${curso.nome}</strong>?

                Esta ação não poderá ser desfeita.

            </p>

            <div class="modal-footer">

                <button
                    type="button"
                    class="btn btn-secondary"
                    id="btn-fechar-exclusao"
                >
                    Voltar
                </button>

                <button
                    type="button"
                    class="btn btn-danger"
                    id="btn-confirmar-exclusao"
                >
                    Excluir curso
                </button>

            </div>
        `
    });

    document
        .querySelector("#btn-fechar-exclusao")
        .addEventListener(
            "click",
            modal.close
        );

    document
        .querySelector("#btn-confirmar-exclusao")
        .addEventListener(
            "click",
            async () => {

                const button =
                    document.querySelector(
                        "#btn-confirmar-exclusao"
                    );

                button.disabled = true;
                button.textContent = "Excluindo...";

                try {

                    await excluirCurso(curso.id);

                    modal.close();

                    showToast(
                        "Curso excluído com sucesso!",
                        "success"
                    );

                    await carregarCursos();

                } catch (error) {

                    console.error(error);

                    showToast(
                        error.message,
                        "error"
                    );

                    button.disabled = false;
                    button.textContent = "Excluir curso";
                }

            }
        );
}

function handleSearch(event) {

    const valor =
        event.target.value
            .trim()
            .toLowerCase();

    const filtrados =
        cursos.filter(curso =>
            curso.nome
                .toLowerCase()
                .includes(valor)
        );

    renderCursosTable(filtrados);
}