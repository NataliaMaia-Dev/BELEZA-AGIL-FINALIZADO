const AgendaPage = {
    selectedDate: Utils.getTodayISO(),
    selectedAgendamento: null,
    profissionais: [],
    clientes: [],
    servicos: [],
    agendamentos: [],

    async init() {
        this.dateInput = document.getElementById('data-agenda');
        this.tableContainer = document.getElementById('agenda-table-container');
        this.modal = document.getElementById('modal-detalhes');

        const params = new URLSearchParams(window.location.search);
        const dataParam = params.get('data');
        if (dataParam) {
            this.selectedDate = dataParam;
        }

        this.dateInput.value = this.selectedDate;
        this.dateInput.min = Utils.getTodayISO();

        this.bindEvents();
        await this.loadData();
    },

    bindEvents() {
        this.dateInput.addEventListener('change', async (e) => {
            this.selectedDate = e.target.value;
            await this.loadAgendamentos();
        });

        document.getElementById('btn-novo-agendamento').addEventListener('click', () => {
            window.location.href = `agendamento.html?data=${this.selectedDate}`;
        });

        document.getElementById('btn-finalizar').addEventListener('click', () => this.finalizarComanda());
        document.getElementById('btn-excluir-comanda').addEventListener('click', () => this.excluirComanda());
        document.getElementById('modal-close').addEventListener('click', () => this.closeModal());
        this.modal.addEventListener('click', (e) => {
            if (e.target === this.modal) this.closeModal();
        });
    },

    async loadData() {
        try {
            [this.profissionais, this.clientes, this.servicos] = await Promise.all([
                Storage.getProfissionais(),
                Storage.getClientes(),
                Storage.getServicos()
            ]);
            await this.loadAgendamentos();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    async loadAgendamentos() {
        try {
            this.agendamentos = await Storage.getAgendamentosPorData(this.selectedDate);
            this.renderAgenda();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    renderAgenda() {
        if (this.profissionais.length === 0) {
            this.tableContainer.innerHTML = `
                <div class="empty-state">
                    <div class="empty-state-icon">💼</div>
                    <p>Nenhum profissional cadastrado. Cadastre profissionais para visualizar a agenda.</p>
                </div>
            `;
            return;
        }

        let html = '<table class="agenda-table"><thead><tr>';
        html += '<th class="time-col">Horário</th>';
        this.profissionais.forEach(p => {
            html += `<th>${p.nome}</th>`;
        });
        html += '</tr></thead><tbody>';

        const ocupacao = this.buildOccupationMap(this.agendamentos, this.profissionais);

        HORARIOS_BASE.forEach(horario => {
            html += `<tr><td class="time-col">${horario}</td>`;
            this.profissionais.forEach(p => {
                const key = `${p.id}-${horario}`;
                const slot = ocupacao[key];
                if (slot) {
                    const levelClass = slot.level > 0 ? `occupied-level-${Math.min(slot.level, 3)}` : '';
                    const finalizadoClass = slot.finalizado ? 'finalizado' : '';
                    html += `<td class="occupied ${levelClass} ${finalizadoClass}" 
                        data-agendamento-id="${slot.agendamentoId}" 
                        title="${slot.cliente} - ${slot.servico}">${slot.level === 0 ? slot.cliente.split(' ')[0] : ''}</td>`;
                } else {
                    html += '<td></td>';
                }
            });
            html += '</tr>';
        });

        html += '</tbody></table>';
        this.tableContainer.innerHTML = html;

        this.tableContainer.querySelectorAll('td.occupied').forEach(cell => {
            cell.addEventListener('click', () => {
                const agendamentoId = parseInt(cell.dataset.agendamentoId, 10);
                this.showDetalhes(agendamentoId);
            });
        });
    },

    buildOccupationMap(agendamentos, profissionais) {
        const map = {};

        agendamentos.forEach(ag => {
            const profissional = profissionais.find(p => p.id === ag.profissionalId);
            const cliente = this.clientes.find(c => c.id === ag.clienteId);
            const servico = this.servicos.find(s => s.id === ag.servicoId);

            if (!profissional || !ag.horariosOcupados) return;

            ag.horariosOcupados.forEach((horario, index) => {
                const key = `${profissional.id}-${horario}`;
                map[key] = {
                    agendamentoId: ag.id,
                    comandaId: ag.comandaId,
                    cliente: cliente ? cliente.nome : 'Cliente',
                    servico: servico ? servico.nome : 'Serviço',
                    profissional: profissional.nome,
                    horario: ag.horario,
                    level: index,
                    finalizado: ag.finalizado
                };
            });
        });

        return map;
    },

    showDetalhes(agendamentoId) {
        const agendamento = this.agendamentos.find(a => a.id === agendamentoId);
        if (!agendamento) return;

        this.selectedAgendamento = agendamento;

        const cliente = this.clientes.find(c => c.id === agendamento.clienteId);
        const comandaAgendamentos = this.agendamentos.filter(a => a.comandaId === agendamento.comandaId);

        document.getElementById('detalhe-comanda').textContent = agendamento.comandaId;
        document.getElementById('detalhe-cliente').textContent = cliente ? cliente.nome : '-';
        document.getElementById('detalhe-data').textContent = Utils.formatDate(agendamento.data);
        document.getElementById('detalhe-status').textContent = agendamento.finalizado ? 'Finalizado' : 'Em andamento';

        const servicosList = document.getElementById('detalhe-servicos');
        servicosList.innerHTML = comandaAgendamentos.map(ag => {
            const s = this.servicos.find(sv => sv.id === ag.servicoId);
            const p = this.profissionais.find(pr => pr.id === ag.profissionalId);
            return `<li>${ag.horario} - ${s ? s.nome : ''} (${p ? p.nome : ''})</li>`;
        }).join('');

        document.getElementById('btn-finalizar').disabled = agendamento.finalizado;
        this.modal.classList.add('show');
    },

    closeModal() {
        this.modal.classList.remove('show');
        this.selectedAgendamento = null;
    },

    async finalizarComanda() {
        if (!this.selectedAgendamento) return;

        try {
            await Storage.finalizarComanda(this.selectedAgendamento.comandaId);
            Utils.showAlert('alert-container', 'Comanda finalizada com sucesso!');
            this.closeModal();
            await this.loadAgendamentos();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    async excluirComanda() {
        if (!this.selectedAgendamento) return;
        if (!confirm('Deseja realmente excluir todos os agendamentos desta comanda?')) return;

        try {
            await Storage.deleteAgendamentosPorComanda(this.selectedAgendamento.comandaId);
            Utils.showAlert('alert-container', 'Comanda excluída com sucesso!');
            this.closeModal();
            await this.loadAgendamentos();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    }
};

document.addEventListener('DOMContentLoaded', () => AgendaPage.init());
