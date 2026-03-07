package service;

import dao.ProfissionalDAO;
import dto.ProfissionalDTO;
import repository.ProfissionalRepository;

import java.sql.SQLException;
import java.util.List;

public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalService() {
        this(new ProfissionalDAO());
    }

    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public void salvar(ProfissionalDTO profissional) throws SQLException {
        profissionalRepository.salvar(profissional);
    }

    public List<ProfissionalDTO> listarTodos() throws SQLException {
        return profissionalRepository.listarTodos();
    }

    public void atualizar(ProfissionalDTO profissional) throws SQLException {
        profissionalRepository.atualizar(profissional);
    }

    public void excluir(int idProfissional) throws SQLException {
        profissionalRepository.excluir(idProfissional);
    }

    public int contarProfissionais() throws SQLException {
        return profissionalRepository.contarProfissionais();
    }
}

