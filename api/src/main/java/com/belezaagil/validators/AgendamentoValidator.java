package com.belezaagil.validators;

import com.belezaagil.dto.AgendamentoRequest;
import com.belezaagil.exception.BusinessException;

public class AgendamentoValidator {
    public static void validar(AgendamentoRequest req) {
        if (req.clienteId() == null)
            throw new BusinessException("Cliente é obrigatório.");
        if (req.servicoId() == null)
            throw new BusinessException("Serviço é obrigatório.");
        if (req.profissionalId() == null)
            throw new BusinessException("Profissional é obrigatório.");
        if (req.data() == null || req.data().isBlank())
            throw new BusinessException("Data é obrigatória.");
        if (req.horario() == null || req.horario().isBlank())
            throw new BusinessException("Horário é obrigatório.");
        if (req.horariosOcupados() == null)
            throw new BusinessException("Lista de horários ocupados é obrigatória.");
    }
}