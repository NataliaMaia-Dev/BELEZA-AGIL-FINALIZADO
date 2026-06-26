const HomePage = {
    async init() {
        await this.updateStats();
    },

    async updateStats() {
        try {
            const [clientes, profissionais, servicos] = await Promise.all([
                Storage.getClientes(),
                Storage.getProfissionais(),
                Storage.getServicos()
            ]);

            document.getElementById('stat-clientes').textContent = clientes.length;
            document.getElementById('stat-profissionais').textContent = profissionais.length;
            document.getElementById('stat-servicos').textContent = servicos.length;

            const hoje = Utils.getTodayISO();
            const agendamentosHoje = await Storage.getAgendamentosPorData(hoje);
            document.getElementById('stat-agendamentos').textContent = agendamentosHoje.length;
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    }
};

document.addEventListener('DOMContentLoaded', () => HomePage.init());
