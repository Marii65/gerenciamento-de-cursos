import { openModal } from "../components/Modal.js";
import { renderEmptyState } from "../components/EmptyState.js";
import { showToast } from "../components/Toast.js";

import {
    listarAlunos,
    criarAluno,
    atualizarAluno,
    excluirAluno
} from "../services/alunoService.js";


let alunos = [];


/**
 * Renderiza a página de alunos.
 */
export async function renderAlunos() {

    const content =
        document.querySelector("#content");

    content.innerHTML = `

        <div class="page-header">

            <div>

                <span class="section-label">
                    GERENCIAMENTO
                </span>

                <h2>
                    Alunos
                </h2>

                <p>
                    Cadastre e gerencie os alunos do sistema.
                </p>

            </div>

            <button
                class="btn btn-primary"
                id="btn-novo-aluno"
                type="button"
            >
                + Novo aluno
            </button>

        </div>


        <div class="card">

            <div class="toolbar">

                <input
                    type="search"
                    class="search-input"
                    id="search-aluno"
                    placeholder="Buscar por nome ou e-mail..."
                >

            </div>


            <div id="alunos-table-container">

                <div class="empty-state">

                    <div class="empty-state-icon">
                        ...
                    </div>

                    <h3>
                        Carregando alunos
                    </h3>

                    <p>
                        Buscando dados da API.
                    </p>

                </div>

            </div>

        </div>

    `;

    setupAlunosEvents();

    await carregarAlunos();

}


/**
 * Busca os alunos na API.
 */
async function carregarAlunos() {

    try {

        alunos = await listarAlunos();

        renderAlunosTable(alunos);

    } catch (error) {

        console.error(error);

        const container =
            document.querySelector(
                "#alunos-table-container"
            );

        container.innerHTML =
            renderEmptyState(
                "Não foi possível carregar os alunos",
                error.message
            );

        showToast(
            "Erro ao carregar os alunos.",
            "error"
        );

    }

}


/**
 * Renderiza a tabela de alunos.
 *
 * @param {Array} lista - Lista de alunos.
 */
function renderAlunosTable(lista) {

    const container =
        document.querySelector(
            "#alunos-table-container"
        );


    if (!lista.length) {

        container.innerHTML =
            renderEmptyState(
                "Nenhum aluno cadastrado",
                "Cadastre o primeiro aluno para começar."
            );

        return;
    }


    container.innerHTML = `

        <div class="table-wrapper">

            <table class="data-table">

                <thead>

                    <tr>

                        <th>Nome</th>

                        <th>E-mail</th>

                        <th>Data de nascimento</th>

                        <th>Ações</th>

                    </tr>

                </thead>

                <tbody>

                    ${lista.map(aluno => `

                        <tr>

                            <td>
                                <strong>
                                    ${aluno.nome}
                                </strong>
                            </td>

                            <td>
                                ${aluno.email}
                            </td>

                            <td>
                                ${formatarData(
                                    aluno.dataNascimento
                                )}
                            </td>

                            <td>

                                <button
                                    class="btn btn-secondary btn-editar-aluno"
                                    data-id="${aluno.id}"
                                    type="button"
                                >
                                    Editar
                                </button>

                                <button
                                    class="btn btn-danger btn-excluir-aluno"
                                    data-id="${aluno.id}"
                                    type="button"
                                >
                                    Excluir
                                </button>

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
function setupAlunosEvents() {

    const button =
        document.querySelector(
            "#btn-novo-aluno"
        );


    button.addEventListener(
        "click",
        openNovoAlunoModal
    );


    const search =
        document.querySelector(
            "#search-aluno"
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

    const editButtons =
        document.querySelectorAll(
            ".btn-editar-aluno"
        );


    editButtons.forEach(button => {

        button.addEventListener(
            "click",
            () => {

                const id =
                    button.dataset.id;

                const aluno =
                    alunos.find(
                        item => item.id === id
                    );

                if (aluno) {
                    openEditarAlunoModal(aluno);
                }

            }
        );

    });


    const deleteButtons =
        document.querySelectorAll(
            ".btn-excluir-aluno"
        );


    deleteButtons.forEach(button => {

        button.addEventListener(
            "click",
            () => {

                const id =
                    button.dataset.id;

                const aluno =
                    alunos.find(
                        item => item.id === id
                    );

                if (aluno) {
                    openExcluirAlunoModal(aluno);
                }

            }
        );

    });

}


/**
 * Abre o modal de cadastro.
 */
function openNovoAlunoModal() {

    const modal = openModal({

        title: "Novo aluno",

        content: `

            <form id="form-aluno">

                <div class="form-group">

                    <label for="nome">
                        Nome completo
                    </label>

                    <input
                        type="text"
                        id="nome"
                        name="nome"
                        placeholder="Ex.: Maria Luiza Silva"
                        maxlength="100"
                        autocomplete="name"
                        required
                    >

                </div>


                <div class="form-group">

                    <label for="email">
                        E-mail
                    </label>

                    <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="Ex.: maria@email.com"
                        maxlength="100"
                        autocomplete="email"
                        required
                    >

                </div>


                <div class="form-group">

                    <label for="dataNascimento">
                        Data de nascimento
                    </label>

                    <input
                        type="date"
                        id="dataNascimento"
                        name="dataNascimento"
                        required
                    >

                </div>


                <div class="modal-footer">

                    <button
                        type="button"
                        class="btn btn-secondary"
                        id="btn-cancelar-aluno"
                    >
                        Cancelar
                    </button>

                    <button
                        type="submit"
                        class="btn btn-primary"
                    >
                        Cadastrar aluno
                    </button>

                </div>

            </form>

        `

    });


    const form =
        document.querySelector(
            "#form-aluno"
        );


    const cancelButton =
        document.querySelector(
            "#btn-cancelar-aluno"
        );


    cancelButton.addEventListener(
        "click",
        modal.close
    );


    form.addEventListener(
        "submit",
        async (event) => {

            event.preventDefault();

            await handleCreateAluno(
                form,
                modal
            );

        }
    );

}


/**
 * Cadastra um aluno através da API.
 */
async function handleCreateAluno(
    form,
    modal
) {

    const button =
        form.querySelector(
            'button[type="submit"]'
        );


    const formData =
        new FormData(form);


    const aluno = {

        nome: formData.get("nome").trim(),

        email: formData.get("email").trim(),

        dataNascimento:
            formData.get("dataNascimento")

    };


    button.disabled = true;

    button.textContent = "Cadastrando...";


    try {

        await criarAluno(aluno);

        modal.close();

        showToast(
            "Aluno cadastrado com sucesso!",
            "success"
        );

        await carregarAlunos();

    } catch (error) {

        console.error(error);

        showToast(
            error.message,
            "error"
        );

        button.disabled = false;

        button.textContent =
            "Cadastrar aluno";

    }

}


/**
 * Abre o modal de edição.
 *
 * @param {Object} aluno
 */
function openEditarAlunoModal(aluno) {

    const modal = openModal({

        title: "Editar aluno",

        content: `

            <form id="form-editar-aluno">

                <div class="form-group">

                    <label for="editar-nome">
                        Nome completo
                    </label>

                    <input
                        type="text"
                        id="editar-nome"
                        name="nome"
                        value="${aluno.nome}"
                        maxlength="100"
                        required
                    >

                </div>


                <div class="form-group">

                    <label for="editar-email">
                        E-mail
                    </label>

                    <input
                        type="email"
                        id="editar-email"
                        name="email"
                        value="${aluno.email}"
                        maxlength="100"
                        required
                    >

                </div>


                <div class="form-group">

                    <label for="editar-dataNascimento">
                        Data de nascimento
                    </label>

                    <input
                        type="date"
                        id="editar-dataNascimento"
                        name="dataNascimento"
                        value="${aluno.dataNascimento}"
                        required
                    >

                </div>


                <div class="modal-footer">

                    <button
                        type="button"
                        class="btn btn-secondary"
                        id="btn-cancelar-edicao"
                    >
                        Cancelar
                    </button>

                    <button
                        type="submit"
                        class="btn btn-primary"
                    >
                        Salvar alterações
                    </button>

                </div>

            </form>

        `

    });


    const form =
        document.querySelector(
            "#form-editar-aluno"
        );


    const cancelButton =
        document.querySelector(
            "#btn-cancelar-edicao"
        );


    cancelButton.addEventListener(
        "click",
        modal.close
    );


    form.addEventListener(
        "submit",
        async (event) => {

            event.preventDefault();

            await handleUpdateAluno(
                aluno.id,
                form,
                modal
            );

        }
    );

}


/**
 * Atualiza um aluno através da API.
 *
 * @param {string} id
 * @param {HTMLFormElement} form
 * @param {Object} modal
 */
async function handleUpdateAluno(
    id,
    form,
    modal
) {

    const button =
        form.querySelector(
            'button[type="submit"]'
        );


    const formData =
        new FormData(form);


    const alunoAtualizado = {

        nome:
            formData.get("nome").trim(),

        email:
            formData.get("email").trim(),

        dataNascimento:
            formData.get("dataNascimento")

    };


    button.disabled = true;

    button.textContent = "Salvando...";


    try {

        await atualizarAluno(
            id,
            alunoAtualizado
        );

        modal.close();

        showToast(
            "Aluno atualizado com sucesso!",
            "success"
        );

        await carregarAlunos();

    } catch (error) {

        console.error(error);

        showToast(
            error.message,
            "error"
        );

        button.disabled = false;

        button.textContent =
            "Salvar alterações";

    }

}


/**
 * Abre o modal de confirmação de exclusão.
 *
 * @param {Object} aluno
 */
function openExcluirAlunoModal(aluno) {

    const modal = openModal({

        title: "Excluir aluno",

        content: `

            <div>

                <p style="
                    margin-bottom: 20px;
                    color: var(--text-secondary);
                    font-size: 13px;
                    line-height: 1.5;
                ">
                    Tem certeza que deseja excluir o aluno
                    <strong>${aluno.nome}</strong>?
                    Esta ação não poderá ser desfeita.
                </p>


                <div class="modal-footer">

                    <button
                        type="button"
                        class="btn btn-secondary"
                        id="btn-cancelar-exclusao"
                    >
                        Cancelar
                    </button>

                    <button
                        type="button"
                        class="btn btn-danger"
                        id="btn-confirmar-exclusao"
                    >
                        Excluir aluno
                    </button>

                </div>

            </div>

        `

    });


    const cancelButton =
        document.querySelector(
            "#btn-cancelar-exclusao"
        );


    const confirmButton =
        document.querySelector(
            "#btn-confirmar-exclusao"
        );


    cancelButton.addEventListener(
        "click",
        modal.close
    );


    confirmButton.addEventListener(
        "click",
        async () => {

            await handleDeleteAluno(
                aluno.id,
                modal
            );

        }
    );

}


/**
 * Exclui um aluno através da API.
 *
 * @param {string} id
 * @param {Object} modal
 */
async function handleDeleteAluno(
    id,
    modal
) {

    const button =
        document.querySelector(
            "#btn-confirmar-exclusao"
        );


    button.disabled = true;

    button.textContent = "Excluindo...";


    try {

        await excluirAluno(id);

        modal.close();

        showToast(
            "Aluno excluído com sucesso!",
            "success"
        );

        await carregarAlunos();

    } catch (error) {

        console.error(error);

        showToast(
            error.message,
            "error"
        );

        button.disabled = false;

        button.textContent =
            "Excluir aluno";

    }

}


/**
 * Filtra alunos pelo nome ou e-mail.
 *
 * @param {Event} event
 */
function handleSearch(event) {

    const searchValue =
        event.target.value
            .trim()
            .toLowerCase();


    const filteredAlunos =
        alunos.filter(aluno =>
            aluno.nome
                .toLowerCase()
                .includes(searchValue)
            ||
            aluno.email
                .toLowerCase()
                .includes(searchValue)
        );


    renderAlunosTable(filteredAlunos);

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