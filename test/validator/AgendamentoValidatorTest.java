package validator;

import dto.ClienteDTO;
import dto.ProfissionalDTO;
import dto.ServicoDTO;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class AgendamentoValidatorTest {

    private static final Date DATA_VALIDA = new Date();
    private static final ClienteDTO CLIENTE = new ClienteDTO();
    private static final ServicoDTO SERVICO = new ServicoDTO();
    private static final ProfissionalDTO PROFISSIONAL = new ProfissionalDTO();

    @Test
    public void validarDadosObrigatorios_ComTodosPreenchidos_NaoLancaExcecao() {
        AgendamentoValidator.validarDadosObrigatorios(CLIENTE, DATA_VALIDA, SERVICO, PROFISSIONAL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validarDadosObrigatorios_ClienteNulo_LancaExcecaoComMensagemCliente() {
        try {
            AgendamentoValidator.validarDadosObrigatorios(null, DATA_VALIDA, SERVICO, PROFISSIONAL);
        } catch (IllegalArgumentException e) {
            assertEquals("Selecione um cliente.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void validarDadosObrigatorios_DataNula_LancaExcecaoComMensagemData() {
        try {
            AgendamentoValidator.validarDadosObrigatorios(CLIENTE, null, SERVICO, PROFISSIONAL);
        } catch (IllegalArgumentException e) {
            assertEquals("Selecione uma data válida.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void validarDadosObrigatorios_ServicoNulo_LancaExcecaoComMensagemServico() {
        try {
            AgendamentoValidator.validarDadosObrigatorios(CLIENTE, DATA_VALIDA, null, PROFISSIONAL);
        } catch (IllegalArgumentException e) {
            assertEquals("Selecione um serviço.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void validarDadosObrigatorios_ProfissionalNulo_LancaExcecaoComMensagemProfissional() {
        try {
            AgendamentoValidator.validarDadosObrigatorios(CLIENTE, DATA_VALIDA, SERVICO, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Selecione um profissional.", e.getMessage());
            throw e;
        }
    }
}
