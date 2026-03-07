package repository;

import dto.AgendamentoDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository {

    void salvar(AgendamentoDTO agendamento) throws SQLException;

    List<AgendamentoDTO> listarTodos() throws SQLException;

    List<AgendamentoDTO> listarPorData(LocalDate data) throws SQLException;

    void atualizarFinalizado(int idAgendamento, boolean finalizado) throws SQLException;

    void excluir(int idAgendamento) throws SQLException;

    void finalizarComanda(int comandaId) throws SQLException;

    void excluirPorComanda(int comandaId) throws SQLException;
}

