const ServicosPage = {
    selectedId: null,
    servicos: [],

    async init() {
        this.form = document.getElementById('form-servico');
        this.tableBody = document.getElementById('tabela-servicos');
        this.searchInput = document.getElementById('busca-servico');
        this.bindEvents();
        await this.loadServicos();
    },

    bindEvents() {
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
        document.getElementById('btn-cancelar').addEventListener('click', () => this.resetForm());
        document.getElementById('btn-excluir').addEventListener('click', () => this.handleDelete());
        document.getElementById('valor').addEventListener('input', (e) => {
            let value = e.target.value.replace(/[^\d,]/g, '');
            e.target.value = value;
        });
        this.searchInput.addEventListener('input', Utils.debounce(() => this.renderTable(), 300));
    },

    async loadServicos() {
        try {
            this.servicos = await Storage.getServicos();
            this.renderTable();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    validateForm() {
        Validators.clearAllErrors(this.form);
        let isValid = true;

        const nome = document.getElementById('nome').value;
        const tempo = document.getElementById('tempo').value;
        const valor = document.getElementById('valor').value;

        const validations = [
            { input: 'nome', error: Validators.validateNome(nome) },
            { input: 'tempo', error: Validators.validatePositiveInteger(tempo, 'Tempo de execução') },
            { input: 'valor', error: Validators.validatePositiveNumber(valor, 'Valor') }
        ];

        validations.forEach(({ input, error }) => {
            if (error) {
                Validators.showFieldError(document.getElementById(input), error);
                isValid = false;
            }
        });

        return isValid;
    },

    async handleSubmit(e) {
        e.preventDefault();
        if (!this.validateForm()) return;

        const servico = {
            id: this.selectedId,
            nome: document.getElementById('nome').value.trim(),
            tempoExecucao: parseInt(document.getElementById('tempo').value, 10),
            valor: parseFloat(document.getElementById('valor').value.replace(',', '.'))
        };

        try {
            await Storage.saveServico(servico);
            Utils.showAlert('alert-container', this.selectedId ? 'Serviço atualizado com sucesso!' : 'Serviço cadastrado com sucesso!');
            this.resetForm();
            await this.loadServicos();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    async handleDelete() {
        if (!this.selectedId) {
            Utils.showAlert('alert-container', 'Selecione um serviço para excluir.', 'warning');
            return;
        }

        if (!confirm('Deseja realmente excluir este serviço?')) return;

        try {
            await Storage.deleteServico(this.selectedId);
            Utils.showAlert('alert-container', 'Serviço excluído com sucesso!');
            this.resetForm();
            await this.loadServicos();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    selectServico(id) {
        const servico = this.servicos.find(s => s.id === id);
        if (!servico) return;

        this.selectedId = id;
        document.getElementById('nome').value = servico.nome;
        document.getElementById('tempo').value = servico.tempoExecucao;
        document.getElementById('valor').value = Number(servico.valor).toFixed(2).replace('.', ',');
        document.getElementById('btn-excluir').disabled = false;
        document.getElementById('form-title').textContent = 'Editar serviço';
    },

    resetForm() {
        this.selectedId = null;
        this.form.reset();
        Validators.clearAllErrors(this.form);
        document.getElementById('btn-excluir').disabled = true;
        document.getElementById('form-title').textContent = 'Cadastrar serviço';
    },

    renderTable() {
        const search = this.searchInput.value.toLowerCase();
        const servicos = this.servicos.filter(s =>
            s.nome.toLowerCase().includes(search)
        );

        if (servicos.length === 0) {
            this.tableBody.innerHTML = `
                <tr><td colspan="4" class="empty-state">Nenhum serviço encontrado.</td></tr>
            `;
            return;
        }

        this.tableBody.innerHTML = servicos.map(s => `
            <tr class="${this.selectedId === s.id ? 'selected' : ''}" data-id="${s.id}">
                <td>${s.id}</td>
                <td>${s.nome}</td>
                <td>${s.tempoExecucao} min</td>
                <td>${Utils.formatCurrency(s.valor)}</td>
            </tr>
        `).join('');

        this.tableBody.querySelectorAll('tr[data-id]').forEach(row => {
            row.addEventListener('click', () => {
                this.selectServico(parseInt(row.dataset.id, 10));
                this.renderTable();
            });
        });
    }
};

document.addEventListener('DOMContentLoaded', () => ServicosPage.init());
