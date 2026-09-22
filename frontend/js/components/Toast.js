/**
 * Exibe mensagens temporárias para o usuário.
 *
 * @param {string} message - Mensagem a ser exibida.
 * @param {"success"|"error"|"info"} type - Tipo da mensagem.
 */

export function showToast(message, type = "info") {

    const container =
        document.querySelector("#toast-container");

    const toast =
        document.createElement("div");

    toast.className = `toast toast-${type}`;

    toast.textContent = message;

    container.appendChild(toast);


    setTimeout(() => {

        toast.remove();

    }, 3000);
}

