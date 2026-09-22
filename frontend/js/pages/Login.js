import { login, cadastrar } from "../services/authService.js";
import { showToast } from "../components/Toast.js";

export function renderLogin() {
    const app = document.querySelector("#app");

    app.innerHTML = `
        <div class="login-page">
            <div class="login-card">

                <div class="login-brand">
                    <div class="login-logo">GC</div>

                    <div>
                        <h1>Gerenciamento</h1>
                        <span>de Cursos</span>
                    </div>
                </div>

                <div class="login-heading">
                    <h2>Bem-vindo de volta</h2>
                    <p>Entre com suas credenciais para acessar o sistema.</p>
                </div>

                <form id="login-form">

                    <div class="form-group">
                        <label for="login-email">E-mail</label>
                        <input
                            type="email"
                            id="login-email"
                            name="email"
                            placeholder="seu@email.com"
                            required
                        >
                    </div>

                    <div class="form-group">
                        <label for="login-senha">Senha</label>
                        <input
                            type="password"
                            id="login-senha"
                            name="senha"
                            placeholder="Digite sua senha"
                            required
                        >
                    </div>

                    <button
                        type="submit"
                        class="btn btn-primary login-submit"
                        id="login-submit"
                    >
                        Entrar
                    </button>

                </form>

                <div class="login-register">
                    <span>Ainda não possui uma conta?</span>
                    <button type="button" id="show-register">
                        Criar conta
                    </button>
                </div>

            </div>
        </div>
    `;

    setupLoginEvents();
}

function setupLoginEvents() {
    const form = document.querySelector("#login-form");
    const submitButton = document.querySelector("#login-submit");
    const registerButton = document.querySelector("#show-register");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(form);

        const email = formData.get("email").trim();
        const senha = formData.get("senha");

        submitButton.disabled = true;
        submitButton.textContent = "Entrando...";

        try {
            await login(email, senha);

            showToast("Login realizado com sucesso.", "success");

            window.dispatchEvent(new CustomEvent("login-success"));

        } catch (error) {
            showToast(error.message, "error");

            submitButton.disabled = false;
            submitButton.textContent = "Entrar";
        }
    });

    registerButton.addEventListener("click", () => {
        renderCadastro();
    });
}

function renderCadastro() {
    const app = document.querySelector("#app");

    app.innerHTML = `
        <div class="login-page">
            <div class="login-card">

                <div class="login-brand">
                    <div class="login-logo">GC</div>

                    <div>
                        <h1>Gerenciamento</h1>
                        <span>de Cursos</span>
                    </div>
                </div>

                <div class="login-heading">
                    <h2>Criar sua conta</h2>
                    <p>Cadastre suas credenciais para acessar o sistema.</p>
                </div>

                <form id="register-form">

                    <div class="form-group">
                        <label for="register-email">E-mail</label>
                        <input
                            type="email"
                            id="register-email"
                            name="email"
                            placeholder="seu@email.com"
                            required
                        >
                    </div>

                    <div class="form-group">
                        <label for="register-senha">Senha</label>
                        <input
                            type="password"
                            id="register-senha"
                            name="senha"
                            placeholder="Mínimo de 6 caracteres"
                            minlength="6"
                            required
                        >
                    </div>

                    <button
                        type="submit"
                        class="btn btn-primary login-submit"
                        id="register-submit"
                    >
                        Criar conta
                    </button>

                </form>

                <div class="login-register">
                    <span>Já possui uma conta?</span>
                    <button type="button" id="back-login">
                        Voltar para login
                    </button>
                </div>

            </div>
        </div>
    `;

    setupCadastroEvents();
}

function setupCadastroEvents() {
    const form = document.querySelector("#register-form");
    const submitButton = document.querySelector("#register-submit");
    const backButton = document.querySelector("#back-login");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(form);

        const email = formData.get("email").trim();
        const senha = formData.get("senha");

        submitButton.disabled = true;
        submitButton.textContent = "Cadastrando...";

        try {
            await cadastrar(email, senha);

            showToast("Conta criada com sucesso. Faça login.", "success");

            renderLogin();

        } catch (error) {
            showToast(error.message, "error");

            submitButton.disabled = false;
            submitButton.textContent = "Criar conta";
        }
    });

    backButton.addEventListener("click", () => {
        renderLogin();
    });
}