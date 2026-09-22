/**
 * Componente responsável pela navegação principal
 * da aplicação.
 */

import { logout } from "../services/authService.js";

export function renderSidebar() {

    const sidebar = document.querySelector("#sidebar");

    sidebar.className = "sidebar";

    sidebar.innerHTML = `
        <div class="sidebar-logo">

            <div class="logo-icon">
                GC
            </div>

            <div class="logo-text">
                <strong>Gerenciamento</strong>
                <span>de Cursos</span>
            </div>

        </div>


        <nav class="sidebar-navigation">

            <button
                class="nav-item active"
                data-page="dashboard"
            >
                <span class="nav-icon">⌂</span>
                <span>Dashboard</span>
            </button>


            <button
                class="nav-item"
                data-page="alunos"
            >
                <span class="nav-icon">👤</span>
                <span>Alunos</span>
            </button>


            <button
                class="nav-item"
                data-page="cursos"
            >
                <span class="nav-icon">▣</span>
                <span>Cursos</span>
            </button>


            <button
                class="nav-item"
                data-page="matriculas"
            >
                <span class="nav-icon">✓</span>
                <span>Matrículas</span>
            </button>

        </nav>


        <div class="sidebar-footer">

            <button
                class="logout-button"
                id="logout-button"
                type="button"
            >
                <span class="nav-icon">↪</span>
                <span>Sair</span>
            </button>

            <div class="sidebar-info">
                Sistema de Gerenciamento
                <br>
                Versão 1.0.0
            </div>

        </div>
    `;

    setupLogout();
}


function setupLogout() {

    const logoutButton = document.querySelector("#logout-button");

    logoutButton.addEventListener("click", () => {

        logout();

        window.location.reload();

    });
}