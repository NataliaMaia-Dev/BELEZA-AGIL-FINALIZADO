package service;

import dao.ClienteDAO;
import dto.ClienteDTO;
import repository.ClienteRepository;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService() {
        this(new ClienteDAO());
    }

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void salvar(ClienteDTO cliente) throws SQLException {
        clienteRepository.salvar(cliente);
    }

    public List<ClienteDTO> listarTodos() throws SQLException {
        return clienteRepository.listarTodos();
    }

    public void atualizar(ClienteDTO cliente) throws SQLException {
        clienteRepository.atualizar(cliente);
    }

    public void excluir(int idCliente) throws SQLException {
        clienteRepository.excluir(idCliente);
    }
}

