const ProfissionaisPage = {
    selectedId: null,
    profissionais: [],

    async init() {
        this.form = document.getElementById('form-profissional');
        this.tableBody = document.getElementById('tabela-profissionais');
        this.searchInput = document.getElementById('busca-profissional');
        this.populateFuncoes();
        await this.updateCounter();
        this.bindEvents();
        await this.loadProfissionais();
    },

    populateFuncoes() {
        const select = document.getElementById('funcao');
        select.innerHTML = FUNCOES_PROFISSIONAL.map(f =>
            `<option value="${f}">${f}</option>`
        ).join('');
    },

    async updateCounter() {
        try {
            const count = await Storage.countProfissionais();
            document.getElementById('contador-profissionais').textContent =
                `${count} de ${LIMITE_PROFISSIONAIS} profissionais cadastrados`;
        } catch (error) {
            document.getElementById('contador-profissionais').textContent =
                `— de ${LIMITE_PROFISSIONAIS} profissionais cadastrados`;
        }
    },

    bindEvents() {
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
        document.getElementById('btn-cancelar').addEventListener('click', () => this.resetForm());
        document.getElementById('btn-excluir').addEventListener('click', () => this.handleDelete());
        document.getElementById('cpf').addEventListener('input', (e) => {
            e.target.value = Utils.formatCPF(e.target.value);
        });
        this.searchInput.addEventListener('input', Utils.debounce(() => this.renderTable(), 300));
    },

    async loadProfissionais() {
        try {
            this.profissionais = await Storage.getProfissionais();
            this.renderTable();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    async validateForm() {
        Validators.clearAllErrors(this.form);
        let isValid = true;

        const nome = document.getElementById('nome').value;
        const dataNascimento = document.getElementById('data-nascimento').value;
        const cpf = document.getElementById('cpf').value;
        const email = document.getElementById('email').value;
        const funcao = document.getElementById('funcao').value;

        const validations = [
            { input: 'nome', error: Validators.validateNome(nome) },
            { input: 'data-nascimento', error: Validators.validateDateNotFuture(dataNascimento) },
            { input: 'cpf', error: Validators.validateCPF(cpf) },
            { input: 'email', error: Validators.validateEmail(email) },
            { input: 'funcao', error: Validators.validateRequired(funcao, 'Função') }
        ];

        validations.forEach(({ input, error }) => {
            if (error) {
                Validators.showFieldError(document.getElementById(input), error);
                isValid = false;
            }
        });

        if (isValid && await Storage.cpfExists(cpf, 'Profissionais', this.selectedId)) {
            Validators.showFieldError(document.getElementById('cpf'), 'CPF já cadastrado.');
            isValid = false;
        }

        if (isValid && !this.selectedId && await Storage.countProfissionais() >= LIMITE_PROFISSIONAIS) {
            Utils.showAlert('alert-container', 'Limite máximo de 5 profissionais atingido!', 'warning');
            isValid = false;
        }

        return isValid;
    },

    async handleSubmit(e) {
        e.preventDefault();
        if (!(await this.validateForm())) return;

        const profissional = {
            id: this.selectedId,
            nome: document.getElementById('nome').value.trim(),
            dataNascimento: document.getElementById('data-nascimento').value,
            cpf: Utils.cleanCPF(document.getElementById('cpf').value),
            email: document.getElementById('email').value.trim(),
            funcao: document.getElementById('funcao').value
        };

        try {
            await Storage.saveProfissional(profissional);
            Utils.showAlert('alert-container', this.selectedId ? 'Profissional atualizado com sucesso!' : 'Profissional cadastrado com sucesso!');
            this.resetForm();
            await this.loadProfissionais();
            await this.updateCounter();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    async handleDelete() {
        if (!this.selectedId) {
            Utils.showAlert('alert-container', 'Selecione um profissional para excluir.', 'warning');
            return;
        }

        if (!confirm('Deseja realmente excluir este profissional?')) return;

        try {
            await Storage.deleteProfissional(this.selectedId);
            Utils.showAlert('alert-container', 'Profissional excluído com sucesso!');
            this.resetForm();
            await this.loadProfissionais();
            await this.updateCounter();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    selectProfissional(id) {
        const profissional = this.profissionais.find(p => p.id === id);
        if (!profissional) return;

        this.selectedId = id;
        document.getElementById('nome').value = profissional.nome;
        document.getElementById('data-nascimento').value = profissional.dataNascimento;
        document.getElementById('cpf').value = Utils.formatCPF(profissional.cpf);
        document.getElementById('email').value = profissional.email;
        document.getElementById('funcao').value = profissional.funcao;
        document.getElementById('btn-excluir').disabled = false;
        document.getElementById('form-title').textContent = 'Editar profissional';
    },

    resetForm() {
        this.selectedId = null;
        this.form.reset();
        Validators.clearAllErrors(this.form);
        document.getElementById('btn-excluir').disabled = true;
        document.getElementById('form-title').textContent = 'Cadastrar profissional';
    },

    renderTable() {
        const search = this.searchInput.value.toLowerCase();
        const profissionais = this.profissionais.filter(p =>
            p.nome.toLowerCase().includes(search) ||
            p.funcao.toLowerCase().includes(search)
        );

        if (profissionais.length === 0) {
            this.tableBody.innerHTML = `
                <tr><td colspan="6" class="empty-state">Nenhum profissional encontrado.</td></tr>
            `;
            return;
        }

        this.tableBody.innerHTML = profissionais.map(p => `
            <tr class="${this.selectedId === p.id ? 'selected' : ''}" data-id="${p.id}">
                <td>${p.id}</td>
                <td>${p.nome}</td>
                <td>${Utils.formatDate(p.dataNascimento)}</td>
                <td>${Utils.formatCPF(p.cpf)}</td>
                <td>${p.email}</td>
                <td><span class="badge badge-primary">${p.funcao}</span></td>
            </tr>
        `).join('');

        this.tableBody.querySelectorAll('tr[data-id]').forEach(row => {
            row.addEventListener('click', () => {
                this.selectProfissional(parseInt(row.dataset.id, 10));
                this.renderTable();
            });
        });
    }
};

document.addEventListener('DOMContentLoaded', () => ProfissionaisPage.init());
