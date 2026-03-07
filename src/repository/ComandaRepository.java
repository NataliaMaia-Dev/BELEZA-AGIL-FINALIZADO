package repository;

import dto.AgendamentoDTO;
import dto.ComandaDTO;

import java.sql.SQLException;
import java.util.List;

public interface ComandaRepository {

    void salvar(ComandaDTO comanda) throws SQLException;

    List<ComandaDTO> listarTodos() throws SQLException;

    void excluir(int idComanda) throws SQLException;

    List<AgendamentoDTO> listarItensPorComanda(int idComanda) throws SQLException;

    int getProximoNumeroComanda() throws SQLException;
}

