package com.belezaagil.dto;

import java.math.BigDecimal;

public record ServicoDto(Integer id, String nome, Integer tempoExecucao, BigDecimal valor) {}
