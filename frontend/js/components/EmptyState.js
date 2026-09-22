/**
 * Renderiza um estado vazio para tabelas
 * ou listas sem registros.
 *
 * @param {string} title - Título.
 * @param {string} message - Descrição.
 */

export function renderEmptyState(
    title,
    message
) {

    return `

        <div class="empty-state">

            <div class="empty-state-icon">
                ⓘ
            </div>

            <h3>
                ${title}
            </h3>

            <p>
                ${message}
            </p>

        </div>

    `;
}

