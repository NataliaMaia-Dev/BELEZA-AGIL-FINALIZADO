package service;

import dao.AgendamentoDAO;
import dao.ComandaDAO;
import dto.AgendamentoDTO;
import dto.ComandaDTO;
import repository.AgendamentoRepository;
import repository.ComandaRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ComandaRepository comandaRepository;

    public AgendamentoService() {
        this(new AgendamentoDAO(), new ComandaDAO());
    }

    public AgendamentoService(AgendamentoRepository agendamentoRepository, ComandaRepository comandaRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.comandaRepository = comandaRepository;
    }

    public List<AgendamentoDTO> listarPorData(LocalDate data) throws SQLException {
        return agendamentoRepository.listarPorData(data);
    }

    public List<AgendamentoDTO> listarTodos() throws SQLException {
        return agendamentoRepository.listarTodos();
    }

    public void salvarAgendamento(AgendamentoDTO agendamento) throws SQLException {
        agendamentoRepository.salvar(agendamento);
    }

    public void salvarAgendamentosComComanda(ComandaDTO comanda, List<AgendamentoDTO> agendamentos) throws SQLException {
        if (comanda == null) {
            throw new IllegalArgumentException("Comanda não pode ser nula.");
        }

        if (agendamentos == null || agendamentos.isEmpty()) {
            return;
        }

        for (AgendamentoDTO agendamento : agendamentos) {
            agendamento.setComanda(comanda);
            agendamentoRepository.salvar(agendamento);
        }
    }

    public ComandaDTO criarComandaVazia() throws SQLException {
        ComandaDTO comanda = new ComandaDTO();
        comandaRepository.salvar(comanda);
        return comanda;
    }

    public int getProximoNumeroComanda() throws SQLException {
        return comandaRepository.getProximoNumeroComanda();
    }

    public void finalizarComanda(int comandaId) throws SQLException {
        agendamentoRepository.finalizarComanda(comandaId);
    }

    public void excluirAgendamentosDaComanda(int comandaId) throws SQLException {
        agendamentoRepository.excluirPorComanda(comandaId);
    }
}

