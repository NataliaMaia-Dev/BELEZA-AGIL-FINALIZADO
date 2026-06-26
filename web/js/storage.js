const API_BASE_URL = 'http://localhost:8080/api';

const ApiClient = {
    async request(path, options = {}) {
        const response = await fetch(`${API_BASE_URL}${path}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });

        if (response.status === 204) {
            return null;
        }

        const text = await response.text();
        const data = text ? JSON.parse(text) : null;

        if (!response.ok) {
            const message = data?.message || 'Erro na requisição à API.';
            throw new Error(message);
        }

        return data;
    },

    get(path) {
        return this.request(path);
    },

    post(path, body) {
        return this.request(path, { method: 'POST', body: JSON.stringify(body) });
    },

    put(path, body) {
        return this.request(path, { method: 'PUT', body: JSON.stringify(body) });
    },

    patch(path) {
        return this.request(path, { method: 'PATCH' });
    },

    delete(path) {
        return this.request(path, { method: 'DELETE' });
    }
};

const Storage = {
    async init() {
        try {
            await ApiClient.get('/clientes');
        } catch (error) {
            console.warn('API indisponível. Verifique se o backend Spring Boot está em execução.', error.message);
        }
    },

    async getClientes() {
        return ApiClient.get('/clientes');
    },

    async saveCliente(cliente) {
        if (cliente.id) {
            return ApiClient.put(`/clientes/${cliente.id}`, cliente);
        }
        return ApiClient.post('/clientes', cliente);
    },

    async deleteCliente(id) {
        await ApiClient.delete(`/clientes/${id}`);
    },

    async getProfissionais() {
        return ApiClient.get('/profissionais');
    },

    async saveProfissional(profissional) {
        if (profissional.id) {
            return ApiClient.put(`/profissionais/${profissional.id}`, profissional);
        }
        return ApiClient.post('/profissionais', profissional);
    },

    async deleteProfissional(id) {
        await ApiClient.delete(`/profissionais/${id}`);
    },

    async countProfissionais() {
        const response = await ApiClient.get('/profissionais/count');
        return response.count;
    },

    async getServicos() {
        return ApiClient.get('/servicos');
    },

    async saveServico(servico) {
        if (servico.id) {
            return ApiClient.put(`/servicos/${servico.id}`, servico);
        }
        return ApiClient.post('/servicos', servico);
    },

    async deleteServico(id) {
        await ApiClient.delete(`/servicos/${id}`);
    },

    async getAgendamentos() {
        return ApiClient.get('/agendamentos');
    },

    async saveAgendamentos(agendamentos) {
        return ApiClient.post('/agendamentos/lote', { agendamentos });
    },

    async getAgendamentosPorData(data) {
        return ApiClient.get(`/agendamentos?data=${data}`);
    },

    async deleteAgendamentosPorComanda(comandaId) {
        await ApiClient.delete(`/agendamentos/comanda/${comandaId}`);
    },

    async finalizarComanda(comandaId) {
        await ApiClient.patch(`/agendamentos/comanda/${comandaId}/finalizar`);
    },

    async criarComanda() {
        return ApiClient.post('/comandas');
    },

    async getProximoNumeroComanda() {
        const response = await ApiClient.get('/comandas/proximo-numero');
        return response.id;
    },

    async cpfExists(cpf, entity, excludeId = null) {
        const cleaned = Utils.cleanCPF(cpf);
        const endpoint = entity === 'Clientes' ? 'clientes' : 'profissionais';
        const params = new URLSearchParams({ cpf: cleaned });
        if (excludeId) {
            params.append('excludeId', excludeId);
        }
        const response = await ApiClient.get(`/${endpoint}/cpf-exists?${params}`);
        return response.exists;
    }
};
