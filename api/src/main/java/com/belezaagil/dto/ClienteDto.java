package com.belezaagil.dto;

import java.time.LocalDate;

public record ClienteDto(Integer id, String nome, LocalDate dataNascimento, String cpf, String telefone) {}
