/**
 * Componente responsável por criar
 * tabelas reutilizáveis.
 *
 * @param {Array<string>} columns - Cabeçalhos.
 * @param {Array<Array>} rows - Dados das linhas.
 * @returns {string} HTML da tabela.
 */

export function createTable(
    columns,
    rows
) {

    const headers = columns
        .map(column => `<th>${column}</th>`)
        .join("");


    const body = rows
        .map(row => `

            <tr>
                ${row
                    .map(value => `<td>${value}</td>`)
                    .join("")
                }
            </tr>

        `)
        .join("");


    return `

        <div class="table-wrapper">

            <table class="data-table">

                <thead>
                    <tr>
                        ${headers}
                    </tr>
                </thead>

                <tbody>
                    ${body}
                </tbody>

            </table>

        </div>

    `;
}

