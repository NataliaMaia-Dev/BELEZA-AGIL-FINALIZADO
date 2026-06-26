package com.belezaagil.dto;

import java.util.List;

public record AgendamentoDto(
        Integer id,
        Integer clienteId,
        Integer servicoId,
        Integer profissionalId,
        String data,
        String horario,
        List<String> horariosOcupados,
        Integer comandaId,
        boolean finalizado
) {}
