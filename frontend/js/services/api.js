/**
 * Configuração central da API.
 */

const API_BASE_URL =
    "http://localhost:8080/api";

const TOKEN_KEY =
    "gerenciamento_cursos_token";


/**
 * Realiza uma requisição HTTP para a API.
 *
 * @param {string} endpoint
 * @param {Object} options
 * @returns {Promise<any>}
 */
export async function apiRequest(
    endpoint,
    options = {}
) {

    const token =
        localStorage.getItem(TOKEN_KEY);


    const headers = {

        "Content-Type":
            "application/json",

        ...options.headers

    };


    if (token) {

        headers.Authorization =
            `Bearer ${token}`;

    }


    const response =
        await fetch(
            `${API_BASE_URL}${endpoint}`,
            {
                ...options,
                headers
            }
        );


    if (!response.ok) {

        let message =
            "Ocorreu um erro na comunicação com a API.";

        try {

            const errorData =
                await response.json();

            message =
                errorData.message ||
                errorData.error ||
                message;

        } catch {
            // Resposta sem JSON.
        }


        if (response.status === 401) {

            message =
                "Sessão expirada ou não autenticada.";

        }


        if (response.status === 403) {

            message =
                "Você não possui permissão para realizar esta ação.";

        }


        throw new Error(message);

    }


    if (response.status === 204) {

        return null;

    }


    return response.json();

}