const Navigation = {
    pages: [
        { href: 'index.html', icon: '🏠', label: 'Início', id: 'home' },
        { href: 'agenda.html', icon: '📅', label: 'Agenda', id: 'agenda' },
        { href: 'agendamento.html', icon: '📝', label: 'Agendar', id: 'agendamento' },
        { href: 'clientes.html', icon: '👤', label: 'Clientes', id: 'clientes' },
        { href: 'profissionais.html', icon: '💼', label: 'Profissionais', id: 'profissionais' },
        { href: 'servicos.html', icon: '✂️', label: 'Serviços', id: 'servicos' }
    ],

    renderSidebar(activePage) {
        const nav = document.getElementById('sidebar-nav');
        if (!nav) return;

        nav.innerHTML = this.pages.map(page => `
            <a href="${page.href}" class="${page.id === activePage ? 'active' : ''}">
                <span class="nav-icon">${page.icon}</span>
                <span>${page.label}</span>
            </a>
        `).join('');
    },

    async init(activePage) {
        await Storage.init();
        this.renderSidebar(activePage);
    }
};

document.addEventListener('DOMContentLoaded', () => {
    const activePage = document.body.dataset.page;
    if (activePage) {
        Navigation.init(activePage);
    }
});
