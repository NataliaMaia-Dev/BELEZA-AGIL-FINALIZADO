package repository;

import dto.ServicoDTO;

import java.sql.SQLException;
import java.util.List;

public interface ServicoRepository {

    void salvar(ServicoDTO servico) throws SQLException;

    List<ServicoDTO> listarTodos() throws SQLException;

    void atualizar(ServicoDTO servico) throws SQLException;

    void excluir(int idServico) throws SQLException;
}

