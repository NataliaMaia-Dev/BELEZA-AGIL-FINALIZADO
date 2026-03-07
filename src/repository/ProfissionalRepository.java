package repository;

import dto.ProfissionalDTO;

import java.sql.SQLException;
import java.util.List;

public interface ProfissionalRepository {

    void salvar(ProfissionalDTO profissional) throws SQLException;

    List<ProfissionalDTO> listarTodos() throws SQLException;

    void atualizar(ProfissionalDTO profissional) throws SQLException;

    void excluir(int idProfissional) throws SQLException;

    int contarProfissionais() throws SQLException;
}

