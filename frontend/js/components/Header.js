/**
 * Componente responsável pelo cabeçalho
 * da aplicação.
 */

export function renderHeader() {

    const header = document.querySelector("#header");

    header.className = "header";

    header.innerHTML = `

        <div class="header-title">

            <span>Sistema</span>

            <h1 id="page-title">
                Dashboard
            </h1>

        </div>


        <div class="header-user">

            <div class="user-avatar">
                A
            </div>

            <div class="user-details">

                <strong>
                    Administrador
                </strong>

                <span>
                    Gestão acadêmica
                </span>

            </div>

        </div>

    `;
}

