package repository;

import dto.ClienteDTO;

import java.sql.SQLException;
import java.util.List;

public interface ClienteRepository {

    void salvar(ClienteDTO cliente) throws SQLException;

    List<ClienteDTO> listarTodos() throws SQLException;

    void atualizar(ClienteDTO cliente) throws SQLException;

    void excluir(int idCliente) throws SQLException;
}

