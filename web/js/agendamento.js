const AgendamentoPage = {
    extraServicesCount: 0,
    clientes: [],
    servicos: [],
    profissionais: [],
    agendamentosData: [],

    async init() {
        this.form = document.getElementById('form-agendamento');
        this.extraContainer = document.getElementById('servicos-extras');

        await this.loadData();
        this.populateSelects();
        await this.setDefaultDate();
        this.bindEvents();
    },

    async loadData() {
        try {
            [this.clientes, this.servicos, this.profissionais] = await Promise.all([
                Storage.getClientes(),
                Storage.getServicos(),
                Storage.getProfissionais()
            ]);
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    },

    async setDefaultDate() {
        const params = new URLSearchParams(window.location.search);
        const data = params.get('data') || Utils.getTodayISO();
        document.getElementById('data').value = data;
        document.getElementById('data').min = Utils.getTodayISO();
        try {
            document.getElementById('comanda').value = await Storage.getProximoNumeroComanda();
        } catch (error) {
            document.getElementById('comanda').value = '—';
        }
    },

    populateSelects() {
        this.fillSelect('cliente', this.clientes, 'nome');
        this.fillSelect('servico', this.servicos, 'nome');
        this.fillSelect('profissional', this.profissionais, 'nome');
        this.fillSelect('horario', HORARIOS_BASE.map(h => ({ id: h, nome: h })), 'nome');
    },

    fillSelect(id, items, labelKey) {
        const select = document.getElementById(id);
        select.innerHTML = '<option value="">Selecione...</option>' +
            items.map(item => `<option value="${item.id}">${item[labelKey]}</option>`).join('');
    },

    bindEvents() {
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
        document.getElementById('btn-cancelar').addEventListener('click', () => {
            window.location.href = 'agenda.html';
        });
        document.getElementById('btn-add-servico').addEventListener('click', () => this.addExtraService());
    },

    addExtraService() {
        this.extraServicesCount++;
        const blockId = `extra-${this.extraServicesCount}`;

        const block = document.createElement('div');
        block.className = 'extra-service-block';
        block.id = blockId;
        block.innerHTML = `
            <div class="form-group">
                <label>Horário</label>
                <select class="form-control extra-horario" required>
                    <option value="">Selecione...</option>
                    ${HORARIOS_BASE.map(h => `<option value="${h}">${h}</option>`).join('')}
                </select>
            </div>
            <div class="form-group">
                <label>Serviço</label>
                <select class="form-control extra-servico" required>
                    <option value="">Selecione...</option>
                    ${this.servicos.map(s => `<option value="${s.id}">${s.nome}</option>`).join('')}
                </select>
            </div>
            <div class="form-group">
                <label>Profissional</label>
                <select class="form-control extra-profissional" required>
                    <option value="">Selecione...</option>
                    ${this.profissionais.map(p => `<option value="${p.id}">${p.nome}</option>`).join('')}
                </select>
            </div>
            <button type="button" class="btn btn-danger btn-sm btn-remove-extra">✕</button>
        `;

        block.querySelector('.btn-remove-extra').addEventListener('click', () => block.remove());
        this.extraContainer.appendChild(block);
    },

    validateForm() {
        Validators.clearAllErrors(this.form);
        let isValid = true;

        const fields = ['cliente', 'data', 'servico', 'profissional', 'horario'];
        const fieldNames = {
            cliente: 'Cliente',
            data: 'Data',
            servico: 'Serviço',
            profissional: 'Profissional',
            horario: 'Horário'
        };

        fields.forEach(field => {
            const value = document.getElementById(field).value;
            const error = Validators.validateRequired(value, fieldNames[field]);
            if (error) {
                Validators.showFieldError(document.getElementById(field), error);
                isValid = false;
            }
        });

        const dataError = Validators.validateDateNotPast(document.getElementById('data').value);
        if (dataError && isValid) {
            Validators.showFieldError(document.getElementById('data'), dataError);
            isValid = false;
        }

        return isValid;
    },

    temConflito(profissionalId, horarios, agendamentosExistentes, agendamentosParaSalvar) {
        for (const horario of horarios) {
            const conflitoExistente = agendamentosExistentes.some(ag =>
                ag.profissionalId === profissionalId &&
                ag.horariosOcupados.includes(horario)
            );
            if (conflitoExistente) return true;

            const conflitoInterno = agendamentosParaSalvar.some(ag =>
                ag.profissionalId === profissionalId &&
                ag.horariosOcupados.some(h => horarios.includes(h))
            );
            if (conflitoInterno) return true;
        }

        return false;
    },

    criarAgendamento(clienteId, servicoId, profissionalId, data, horario) {
        const servico = this.servicos.find(s => s.id === servicoId);
        const horariosOcupados = Utils.gerarHorariosOcupados(horario, servico.tempoExecucao, INTERVALO_MINUTOS);

        return {
            clienteId,
            servicoId,
            profissionalId,
            data,
            horario,
            horariosOcupados
        };
    },

    async handleSubmit(e) {
        e.preventDefault();
        if (!this.validateForm()) return;

        const clienteId = parseInt(document.getElementById('cliente').value, 10);
        const servicoId = parseInt(document.getElementById('servico').value, 10);
        const profissionalId = parseInt(document.getElementById('profissional').value, 10);
        const data = document.getElementById('data').value;
        const horario = document.getElementById('horario').value;

        const servico = this.servicos.find(s => s.id === servicoId);
        const profissional = this.profissionais.find(p => p.id === profissionalId);
        const horariosPrincipal = Utils.gerarHorariosOcupados(horario, servico.tempoExecucao, INTERVALO_MINUTOS);

        const agendamentosParaSalvar = [];

        try {
            const agendamentosExistentes = await Storage.getAgendamentosPorData(data);

            if (this.temConflito(profissionalId, horariosPrincipal, agendamentosExistentes, agendamentosParaSalvar)) {
                Utils.showAlert('alert-container', `Conflito detectado para o profissional ${profissional.nome}.`, 'error');
                return;
            }

            agendamentosParaSalvar.push(
                this.criarAgendamento(clienteId, servicoId, profissionalId, data, horario)
            );

            const extraBlocks = this.extraContainer.querySelectorAll('.extra-service-block');
            for (const block of extraBlocks) {
                const horaExtra = block.querySelector('.extra-horario').value;
                const servicoExtraId = parseInt(block.querySelector('.extra-servico').value, 10);
                const profExtraId = parseInt(block.querySelector('.extra-profissional').value, 10);

                if (!horaExtra || !servicoExtraId || !profExtraId) {
                    Utils.showAlert('alert-container', 'Preencha todos os campos dos serviços extras.', 'warning');
                    return;
                }

                const servicoExtra = this.servicos.find(s => s.id === servicoExtraId);
                const profExtra = this.profissionais.find(p => p.id === profExtraId);
                const horariosExtra = Utils.gerarHorariosOcupados(horaExtra, servicoExtra.tempoExecucao, INTERVALO_MINUTOS);

                if (this.temConflito(profExtraId, horariosExtra, agendamentosExistentes, agendamentosParaSalvar)) {
                    Utils.showAlert('alert-container', `Conflito detectado para ${profExtra.nome} no horário ${horaExtra}.`, 'error');
                    return;
                }

                agendamentosParaSalvar.push(
                    this.criarAgendamento(clienteId, servicoExtraId, profExtraId, data, horaExtra)
                );
            }

            await Storage.saveAgendamentos(agendamentosParaSalvar);
            Utils.showAlert('alert-container', 'Agendamento(s) salvo(s) com sucesso!');

            setTimeout(() => {
                window.location.href = `agenda.html?data=${data}`;
            }, 1500);
        } catch (error) {
            Utils.showAlert('alert-container', error.message, 'error');
        }
    }
};

document.addEventListener('DOMContentLoaded', () => AgendamentoPage.init());
