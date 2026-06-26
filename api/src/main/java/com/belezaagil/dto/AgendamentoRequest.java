package com.belezaagil.dto;

import java.util.List;

public record AgendamentoRequest(
        Integer clienteId,
        Integer servicoId,
        Integer profissionalId,
        String data,
        String horario,
        List<String> horariosOcupados,
        Integer comandaId
) {}
