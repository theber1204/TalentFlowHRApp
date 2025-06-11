// src/main/webapp/resources/js/main.js
document.addEventListener('DOMContentLoaded', function() {
    console.log('TalentFlow HR - Aplicación inicializada');

    // Activar elementos de menú según la página actual
    highlightCurrentMenu();

    // Inicializar tooltips personalizados
    initializeCustomTooltips();

    // Añadir efectos visuales a las tarjetas
    initializeCardAnimations();
});

function highlightCurrentMenu() {
    // Obtiene la ruta de la URL actual
    const currentPath = window.location.pathname;

    // Obtiene todos los elementos del menú
    const menuItems = document.querySelectorAll('.sidebar-nav .nav-link');

    // Recorre los elementos del menú y activa el correspondiente
    menuItems.forEach(item => {
        const href = item.getAttribute('href');
        if (href && currentPath.includes(href.split('?')[0])) {
            item.classList.add('active');
            const parentLi = item.closest('li');
            if (parentLi) {
                parentLi.classList.add('active');
            }
        }
    });
}

function initializeCustomTooltips() {
    // Si tienes tooltips personalizados
}

function initializeCardAnimations() {
    // Para dar efectos a las tarjetas del dashboard
    const dashboardCards = document.querySelectorAll('.dashboard-card');

    dashboardCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.classList.add('card-hovered');
        });

        card.addEventListener('mouseleave', function() {
            this.classList.remove('card-hovered');
        });
    });
}