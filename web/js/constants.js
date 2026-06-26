const HORARIOS_BASE = [
    '08:00', '08:10', '08:20', '08:30', '08:40', '08:50',
    '09:00', '09:10', '09:20', '09:30', '09:40', '09:50',
    '10:00', '10:10', '10:20', '10:30', '10:40', '10:50',
    '11:00', '11:10', '11:20', '11:30', '11:40', '11:50',
    '12:00', '12:10', '12:20', '12:30', '12:40', '12:50',
    '13:00', '13:10', '13:20', '13:30', '13:40', '13:50',
    '14:00', '14:10', '14:20', '14:30', '14:40', '14:50',
    '15:00', '15:10', '15:20', '15:30', '15:40', '15:50',
    '16:00', '16:10', '16:20', '16:30', '16:40', '16:50',
    '17:00', '17:10', '17:20', '17:30', '17:40', '17:50',
    '18:00', '18:10', '18:20', '18:30', '18:40', '18:50',
    '19:00'
];

const FUNCOES_PROFISSIONAL = ['Manicure', 'Depiladora', 'Massagista'];

const INTERVALO_MINUTOS = 10;

const LIMITE_PROFISSIONAIS = 5;

const STORAGE_KEYS = {
    clientes: 'beleza_agil_clientes',
    profissionais: 'beleza_agil_profissionais',
    servicos: 'beleza_agil_servicos',
    agendamentos: 'beleza_agil_agendamentos',
    comandas: 'beleza_agil_comandas',
    initialized: 'beleza_agil_initialized'
};

const DADOS_INICIAIS = {
    clientes: [
        { id: 1, nome: 'Amanda Rodrigues', dataNascimento: '1996-11-11', cpf: '55522211120', telefone: '51989192748' },
        { id: 2, nome: 'Larissa Manuela', dataNascimento: '2000-07-04', cpf: '11122244480', telefone: '51988475441' },
        { id: 3, nome: 'Fátima Bernardes', dataNascimento: '1985-05-07', cpf: '11144477798', telefone: '51991122032' }
    ],
    profissionais: [
        { id: 1, nome: 'Renata Nogueira', dataNascimento: '1978-12-12', cpf: '22244455566', email: 'renatanog@gmail.com', funcao: 'Manicure' },
        { id: 2, nome: 'Paola Leandra', dataNascimento: '2000-11-23', cpf: '22266655599', email: 'paolaleandra@hotmail.com', funcao: 'Manicure' },
        { id: 3, nome: 'Ana Paula', dataNascimento: '2005-02-19', cpf: '11122255580', email: 'anapaulinha@gmail.com', funcao: 'Depiladora' },
        { id: 4, nome: 'Beatriz da Rosa', dataNascimento: '1985-11-12', cpf: '22255599950', email: 'beadarosa@gmail.com', funcao: 'Massagista' },
        { id: 5, nome: 'Maria da Silva', dataNascimento: '1998-04-12', cpf: '22266655580', email: 'mariamaria@gmail.com', funcao: 'Manicure' }
    ],
    servicos: [
        { id: 1, nome: 'Mão simples', tempoExecucao: 50, valor: 45.00 },
        { id: 2, nome: 'Pé simples', tempoExecucao: 40, valor: 40.00 },
        { id: 3, nome: 'Massagem relaxante', tempoExecucao: 60, valor: 100.00 },
        { id: 4, nome: 'Depilação de perna inteira', tempoExecucao: 30, valor: 50.00 },
        { id: 5, nome: 'Depilação de axilas', tempoExecucao: 10, valor: 20.00 },
        { id: 6, nome: 'Massagem terapêutica', tempoExecucao: 60, valor: 110.00 }
    ],
    agendamentos: [],
    comandas: [{ id: 1 }]
};
