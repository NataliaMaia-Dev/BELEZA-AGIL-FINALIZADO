package service;

import dto.AgendamentoDTO;
import dto.ComandaDTO;
import org.junit.Before;
import org.junit.Test;
import repository.AgendamentoRepository;
import repository.ComandaRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AgendamentoServiceTest {

    private AgendamentoRepository agendamentoRepositoryMock;
    private ComandaRepository comandaRepositoryMock;
    private AgendamentoService service;

    @Before
    public void setUp() {
        agendamentoRepositoryMock = new AgendamentoRepositoryMock();
        comandaRepositoryMock = new ComandaRepositoryMock();
        service = new AgendamentoService(agendamentoRepositoryMock, comandaRepositoryMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void salvarAgendamentosComComanda_ComandaNula_LancaExcecao() throws SQLException {
        service.salvarAgendamentosComComanda(null, List.of(new AgendamentoDTO()));
    }

    @Test
    public void salvarAgendamentosComComanda_ComandaNula_MensagemCorreta() {
        try {
            service.salvarAgendamentosComComanda(null, List.of(new AgendamentoDTO()));
            fail("Deveria ter lançado IllegalArgumentException");
        } catch (SQLException e) {
            fail("Não deveria lançar SQLException");
        } catch (IllegalArgumentException e) {
            assertEquals("Comanda não pode ser nula.", e.getMessage());
        }
    }

    @Test
    public void salvarAgendamentosComComanda_ListaNula_NaoLancaExcecaoENaoChamaRepositorio() throws SQLException {
        ComandaDTO comanda = new ComandaDTO();
        AgendamentoRepositoryMock mock = (AgendamentoRepositoryMock) agendamentoRepositoryMock;
        mock.resetChamadas();

        service.salvarAgendamentosComComanda(comanda, null);

        assertEquals(0, mock.getQuantidadeChamadasSalvar());
    }

    @Test
    public void salvarAgendamentosComComanda_ListaVazia_NaoChamaRepositorio() throws SQLException {
        ComandaDTO comanda = new ComandaDTO();
        AgendamentoRepositoryMock mock = (AgendamentoRepositoryMock) agendamentoRepositoryMock;
        mock.resetChamadas();

        service.salvarAgendamentosComComanda(comanda, new ArrayList<>());

        assertEquals(0, mock.getQuantidadeChamadasSalvar());
    }

    @Test
    public void salvarAgendamentosComComanda_ListaComUmAgendamento_ChamaSalvarUmaVez() throws SQLException {
        ComandaDTO comanda = new ComandaDTO();
        comanda.setIdComanda(1);
        List<AgendamentoDTO> agendamentos = List.of(new AgendamentoDTO());
        AgendamentoRepositoryMock mock = (AgendamentoRepositoryMock) agendamentoRepositoryMock;
        mock.resetChamadas();

        service.salvarAgendamentosComComanda(comanda, agendamentos);

        assertEquals(1, mock.getQuantidadeChamadasSalvar());
        assertSame(comanda, agendamentos.get(0).getComanda());
    }

    /**
     * Mock simples do AgendamentoRepository (sem acesso ao banco).
     */
    private static class AgendamentoRepositoryMock implements AgendamentoRepository {
        private int chamadasSalvar = 0;

        void resetChamadas() {
            chamadasSalvar = 0;
        }

        int getQuantidadeChamadasSalvar() {
            return chamadasSalvar;
        }

        @Override
        public void salvar(AgendamentoDTO agendamento) throws SQLException {
            chamadasSalvar++;
        }

        @Override
        public List<AgendamentoDTO> listarTodos() throws SQLException {
            return List.of();
        }

        @Override
        public List<AgendamentoDTO> listarPorData(java.time.LocalDate data) throws SQLException {
            return List.of();
        }

        @Override
        public void atualizarFinalizado(int idAgendamento, boolean finalizado) throws SQLException {
        }

        @Override
        public void excluir(int idAgendamento) throws SQLException {
        }

        @Override
        public void finalizarComanda(int comandaId) throws SQLException {
        }

        @Override
        public void excluirPorComanda(int comandaId) throws SQLException {
        }
    }

    /**
     * Mock simples do ComandaRepository (sem acesso ao banco).
     */
    private static class ComandaRepositoryMock implements ComandaRepository {
        @Override
        public void salvar(ComandaDTO comanda) throws SQLException {
        }

        @Override
        public List<ComandaDTO> listarTodos() throws SQLException {
            return List.of();
        }

        @Override
        public void excluir(int idComanda) throws SQLException {
        }

        @Override
        public List<AgendamentoDTO> listarItensPorComanda(int idComanda) throws SQLException {
            return List.of();
        }

        @Override
        public int getProximoNumeroComanda() throws SQLException {
            return 1;
        }
    }
}
