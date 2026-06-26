package com.belezaagil.mapper;

import com.belezaagil.dto.*;
import com.belezaagil.entity.*;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public final class EntityMapper {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private EntityMapper() {
    }

    public static ClienteDto toDto(Cliente entity) {
        return new ClienteDto(
                entity.getId(),
                entity.getNome(),
                entity.getDataNascimento(),
                entity.getCpf(),
                entity.getTelefone()
        );
    }

    public static Cliente toEntity(ClienteDto dto) {
        Cliente entity = new Cliente();
        entity.setId(dto.id());
        entity.setNome(dto.nome());
        entity.setDataNascimento(dto.dataNascimento());
        entity.setCpf(dto.cpf());
        entity.setTelefone(dto.telefone());
        return entity;
    }

    public static ProfissionalDto toDto(Profissional entity) {
        return new ProfissionalDto(
                entity.getId(),
                entity.getNome(),
                entity.getDataNascimento(),
                entity.getCpf(),
                entity.getEmail(),
                entity.getFuncao()
        );
    }

    public static Profissional toEntity(ProfissionalDto dto) {
        Profissional entity = new Profissional();
        entity.setId(dto.id());
        entity.setNome(dto.nome());
        entity.setDataNascimento(dto.dataNascimento());
        entity.setCpf(dto.cpf());
        entity.setEmail(dto.email());
        entity.setFuncao(dto.funcao());
        return entity;
    }

    public static ServicoDto toDto(Servico entity) {
        return new ServicoDto(
                entity.getId(),
                entity.getNome(),
                entity.getTempoExecucaoMinutes(),
                entity.getValor()
        );
    }

    public static Servico toEntity(ServicoDto dto) {
        Servico entity = new Servico();
        entity.setId(dto.id());
        entity.setNome(dto.nome());
        entity.setTempoExecucaoMinutes(dto.tempoExecucao());
        entity.setValor(dto.valor());
        return entity;
    }

    public static ComandaDto toDto(Comanda entity) {
        return new ComandaDto(entity.getId());
    }

    public static AgendamentoDto toDto(Agendamento entity) {
        List<String> horarios = entity.getHorarios().stream()
                .map(AgendamentoHorario::getHorario)
                .sorted(Comparator.naturalOrder())
                .toList();

        String horario = horarios.isEmpty()
                ? entity.getDataAgendamento().format(TIME_FORMAT)
                : horarios.get(0);

        return new AgendamentoDto(
                entity.getId(),
                entity.getCliente().getId(),
                entity.getServico().getId(),
                entity.getProfissional().getId(),
                entity.getDataAgendamento().toLocalDate().toString(),
                horario,
                horarios,
                entity.getComanda() != null ? entity.getComanda().getId() : null,
                entity.isFinalizado()
        );
    }
}
