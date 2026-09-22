const API_BASE_URL = "http://localhost:8080/api";
const TOKEN_KEY = "gerenciamento_cursos_token";

export async function login(email, senha) {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email,
            senha
        })
    });

    if (!response.ok) {
        let message = "Não foi possível realizar o login.";

        try {
            const errorData = await response.json();
            message = errorData.message || errorData.error || message;
        } catch {
            // Mantém a mensagem padrão
        }

        throw new Error(message);
    }

    const data = await response.json();

    localStorage.setItem(TOKEN_KEY, data.token);

    return data;
}

export async function cadastrar(email, senha) {
    const response = await fetch(`${API_BASE_URL}/auth/cadastro`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email,
            senha
        })
    });

    if (!response.ok) {
        let message = "Não foi possível realizar o cadastro.";

        try {
            const errorData = await response.json();
            message = errorData.message || errorData.error || message;
        } catch {
            // Mantém a mensagem padrão
        }

        throw new Error(message);
    }
}

export function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

export function isAuthenticated() {
    return !!getToken();
}

export function logout() {
    localStorage.removeItem(TOKEN_KEY);
}