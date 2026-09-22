/**
 * Cria e exibe um modal reutilizável.
 *
 * @param {Object} options - Configurações do modal.
 * @param {string} options.title - Título.
 * @param {string} options.content - Conteúdo HTML.
 * @param {Function} options.onClose - Callback ao fechar.
 */

export function openModal({
    title,
    content,
    onClose
}) {

    const overlay =
        document.createElement("div");

    overlay.className = "modal-overlay";

    overlay.innerHTML = `

        <div
            class="modal"
            role="dialog"
            aria-modal="true"
            aria-labelledby="modal-title"
        >

            <div class="modal-header">

                <div class="modal-title-area">

                    <span class="modal-label">
                        FORMULÁRIO
                    </span>

                    <h2 id="modal-title">
                        ${title}
                    </h2>

                </div>

                <button
                    class="modal-close"
                    type="button"
                    aria-label="Fechar"
                >
                    ×
                </button>

            </div>


            <div class="modal-body">
                ${content}
            </div>

        </div>

    `;

    const closeButton =
        overlay.querySelector(".modal-close");


    function close() {

        overlay.remove();

        if (onClose) {
            onClose();
        }

    }


    closeButton.addEventListener(
        "click",
        close
    );


    overlay.addEventListener(
        "click",
        (event) => {

            if (event.target === overlay) {
                close();
            }

        }
    );


    document.addEventListener(
        "keydown",
        handleEscape
    );


    function handleEscape(event) {

        if (event.key === "Escape") {

            document.removeEventListener(
                "keydown",
                handleEscape
            );

            close();

        }

    }


    document.body.appendChild(overlay);

    return {
        close
    };

}