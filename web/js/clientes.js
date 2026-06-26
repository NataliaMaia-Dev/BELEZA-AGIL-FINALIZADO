const ClientesPage = {
    selectedId: null,
    clientes: [],

    async init() {
        this.form = document.getElementById('form-cliente');
        this.tableBody = document.getElementById('tabela-clientes');
        this.searchInput = document.getElementById('busca-cliente');
        this.bindEvents();
        await this.loadClientes();
    },

    bindEvents() {
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
        document.getElementById('btn-cancelar').addEventListener('click', () => this.resetForm());
        document.getElementById('btn-excluir').addEventListener('click', () => this.handleDelete());
        document.getElementById('cpf').addEventListener('input', (e) => {
            e.target.value = Utils.formatCPF(e.target.value);
        });
        document.getElementById('telefone').addEventListener('input', (e) => {
            e.target.value = Utils.formatPhone(e.target.value);
        });
        this.searchInput.addEventListener('input', Utils.debounce(() => this.renderTable(), 300));
    },

    async loadClientes() {
        try {
            this.clientes = await Storage.getClientes();
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
        const telefone = document.getElementById('telefone').value;

        const validations = [
            { input: 'nome', error: Validators.validateNome(nome) },
            { input: 'data-nascimento', error: Validators.validateDateNotFuture(dataNascimento) },
            { input: 'cpf', error: Validators.validateCPF(cpf) },
            { input: 'telefone', error: Validators.validatePhone(telefone) }
        ];

        validations.forEach(({ input, error }) => {
            if (error) {
                Validators.showFieldError(document.getElementById(input), error);
                isValid = false;
            }
        });

        if (isValid && await Storage.cpfExists(cpf, 'Clientes', this.selectedId)) {
            Validators.showFieldError(document.getElementById('cpf'), 'CPF já cadastrado.');
            isValid = false;
        }

        return isValid;
    },

    async handleSubmit(e) {
        e.preventDefault();
        if (!(await this.validateForm())) return;

        const cliente = {
            id: this.selectedId,
            nome: document.getElementById('nome').value.trim(),
            dataNascimento: document.getElementById('data-nascimento').value,
            cpf: Utils.cleanCPF(document.getElementById('cpf').value),
            telefone: Utils.cleanPhone(document.getElementById('telefone').value)
        };

        try {
            await Storage.saveCliente(cliente);
            Utils.showAlert('alert-container', this.selectedId ? 'Cliente atualizado com sucesso!' : 'Cliente cadastrado com sucesso!');
            this.resetForm();
            await this.loadClientes();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    async handleDelete() {
        if (!this.selectedId) {
            Utils.showAlert('alert-container', 'Selecione um cliente para excluir.', 'warning');
            return;
        }

        if (!confirm('Deseja realmente excluir este cliente?')) return;

        try {
            await Storage.deleteCliente(this.selectedId);
            Utils.showAlert('alert-container', 'Cliente excluído com sucesso!');
            this.resetForm();
            await this.loadClientes();
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    selectCliente(id) {
        const cliente = this.clientes.find(c => c.id === id);
        if (!cliente) return;

        this.selectedId = id;
        document.getElementById('nome').value = cliente.nome;
        document.getElementById('data-nascimento').value = cliente.dataNascimento;
        document.getElementById('cpf').value = Utils.formatCPF(cliente.cpf);
        document.getElementById('telefone').value = Utils.formatPhone(cliente.telefone);
        document.getElementById('btn-excluir').disabled = false;
        document.getElementById('form-title').textContent = 'Editar cliente';
    },

    resetForm() {
        this.selectedId = null;
        this.form.reset();
        Validators.clearAllErrors(this.form);
        document.getElementById('btn-excluir').disabled = true;
        document.getElementById('form-title').textContent = 'Cadastrar cliente';
    },

    renderTable() {
        const search = this.searchInput.value.toLowerCase();
        const clientes = this.clientes.filter(c =>
            c.nome.toLowerCase().includes(search) ||
            c.cpf.includes(search.replace(/\D/g, ''))
        );

        if (clientes.length === 0) {
            this.tableBody.innerHTML = `
                <tr><td colspan="5" class="empty-state">Nenhum cliente encontrado.</td></tr>
            `;
            return;
        }

        this.tableBody.innerHTML = clientes.map(c => `
            <tr class="${this.selectedId === c.id ? 'selected' : ''}" data-id="${c.id}">
                <td>${c.id}</td>
                <td>${c.nome}</td>
                <td>${Utils.formatDate(c.dataNascimento)}</td>
                <td>${Utils.formatCPF(c.cpf)}</td>
                <td>${Utils.formatPhone(c.telefone)}</td>
            </tr>
        `).join('');

        this.tableBody.querySelectorAll('tr[data-id]').forEach(row => {
            row.addEventListener('click', () => {
                this.selectCliente(parseInt(row.dataset.id, 10));
                this.renderTable();
            });
        });
    }
};

document.addEventListener('DOMContentLoaded', () => ClientesPage.init());
