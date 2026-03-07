package service;

import dao.ServicoDAO;
import dto.ServicoDTO;
import repository.ServicoRepository;

import java.sql.SQLException;
import java.util.List;

public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService() {
        this(new ServicoDAO());
    }

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public void salvar(ServicoDTO servico) throws SQLException {
        servicoRepository.salvar(servico);
    }

    public List<ServicoDTO> listarTodos() throws SQLException {
        return servicoRepository.listarTodos();
    }

    public void atualizar(ServicoDTO servico) throws SQLException {
        servicoRepository.atualizar(servico);
    }

    public void excluir(int idServico) throws SQLException {
        servicoRepository.excluir(idServico);
    }
}

