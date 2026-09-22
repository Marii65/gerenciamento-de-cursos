import { renderSidebar } from "./components/Sidebar.js";
import { renderHeader } from "./components/Header.js";
import { renderDashboard } from "./pages/Dashboard.js";
import { renderAlunos } from "./pages/Alunos.js";
import { renderLogin } from "./pages/Login.js";
import { isAuthenticated, logout } from "./services/authService.js";
import { renderMatriculas } from "./pages/Matriculas.js";

function updateActiveNavigation(page) {
    const navItems = document.querySelectorAll(".nav-item");

    navItems.forEach(item => {
        item.classList.toggle(
            "active",
            item.dataset.page === page
        );
    });
}

function updateHeaderTitle(title) {
    const pageTitle = document.querySelector("#page-title");

    if (pageTitle) {
        pageTitle.textContent = title;
    }
}

function navigateTo(page) {
    updateActiveNavigation(page);

    switch (page) {
        case "dashboard":
            updateHeaderTitle("Dashboard");
            renderDashboard();
            break;

        case "alunos":
            updateHeaderTitle("Alunos");
            renderAlunos();
            break;

        case "matriculas":
            updateHeaderTitle("Matrículas");
            renderMatriculas();
            break;

        default:
            updateHeaderTitle("Dashboard");
            renderDashboard();

    }
}

function setupNavigation() {
    const navItems = document.querySelectorAll(".nav-item");

    navItems.forEach(item => {
        item.addEventListener("click", () => {
            const page = item.dataset.page;
            navigateTo(page);
        });
    });
}

function initializeSystem() {
    const app = document.querySelector("#app");

    app.innerHTML = `
        <aside id="sidebar"></aside>

        <main class="main-content">
            <header id="header"></header>

            <section id="content" class="content"></section>
        </main>
    `;

    renderSidebar();
    renderHeader();

    setupNavigation();

    navigateTo("dashboard");
}

function initializeLogin() {
    renderLogin();
}

window.addEventListener("login-success", () => {
    initializeSystem();
});

function initializeApp() {
    if (isAuthenticated()) {
        initializeSystem();
    } else {
        initializeLogin();
    }
}

initializeApp();