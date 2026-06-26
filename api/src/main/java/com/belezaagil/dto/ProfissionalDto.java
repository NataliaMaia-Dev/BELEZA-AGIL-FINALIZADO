package com.belezaagil.dto;

import java.time.LocalDate;

public record ProfissionalDto(Integer id, String nome, LocalDate dataNascimento, String cpf, String email, String funcao) {}
