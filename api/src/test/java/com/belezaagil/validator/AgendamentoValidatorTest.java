package com.belezaagil.validator;

import com.belezaagil.dto.AgendamentoRequest;
import com.belezaagil.exception.BusinessException;
import com.belezaagil.validators.AgendamentoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para validação de dados de agendamento.
 * Cobre requisito R4 – Agendamento com data e hora.
 *
 * Sem acesso a banco de dados (apenas lógica pura).
 */
@DisplayName("AgendamentoValidator – validação de campos obrigatórios")
class AgendamentoValidatorTest {

    // -----------------------------------------------------------------------
    // Utilitário: cria um request válido como base para os testes
    // -----------------------------------------------------------------------
    private AgendamentoRequest requestValido() {
        return new AgendamentoRequest(
                1,                          // clienteId
                1,                          // servicoId
                1,                          // profissionalId
                "2099-12-31",               // data (futuro garantido)
                "09:00",                    // horario
                List.of("09:00", "09:15"),   // horariosOcupados
                1
        );
    }

    // -----------------------------------------------------------------------
    // TC-V01 – request completamente válido não deve lançar exceção
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-V01: request válido não lança exceção")
    void requestValido_naoLancaExcecao() {
        AgendamentoRequest req = requestValido();
        assertDoesNotThrow(() -> AgendamentoValidator.validar(req));
    }

    // -----------------------------------------------------------------------
    // TC-V02 – clienteId nulo deve lançar BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-V02: clienteId nulo → BusinessException com mensagem correta")
    void clienteIdNulo_lancaExcecao() {
        AgendamentoRequest req = new AgendamentoRequest(
                null, 1, 1, "2099-12-31", "09:00", List.of("09:00")
        , 1);
        BusinessException ex = assertThrows(BusinessException.class,
                () -> AgendamentoValidator.validar(req));
        assertTrue(ex.getMessage().toLowerCase().contains("cliente"),
                "Mensagem deveria mencionar 'cliente', mas foi: " + ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // TC-V03 – servicoId nulo deve lançar BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-V03: servicoId nulo → BusinessException com mensagem correta")
    void servicoIdNulo_lancaExcecao() {
        AgendamentoRequest req = new AgendamentoRequest(
                1, null, 1, "2099-12-31", "09:00", List.of("09:00")
        ,1);
        BusinessException ex = assertThrows(BusinessException.class,
                () -> AgendamentoValidator.validar(req));
        assertTrue(ex.getMessage().toLowerCase().contains("servi"),
                "Mensagem deveria mencionar 'serviço', mas foi: " + ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // TC-V04 – profissionalId nulo deve lançar BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-V04: profissionalId nulo → BusinessException com mensagem correta")
    void profissionalIdNulo_lancaExcecao() {
        AgendamentoRequest req = new AgendamentoRequest(
                1, 1, null, "2099-12-31", "09:00", List.of("09:00")
        ,1);
        BusinessException ex = assertThrows(BusinessException.class,
                () -> AgendamentoValidator.validar(req));
        assertTrue(ex.getMessage().toLowerCase().contains("profissional"),
                "Mensagem deveria mencionar 'profissional', mas foi: " + ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // TC-V05 – data nula deve lançar BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-V05: data nula → BusinessException com mensagem correta")
    void dataNula_lancaExcecao() {
        AgendamentoRequest req = new AgendamentoRequest(
                1, 1, 1, null, "09:00", List.of("09:00")
        ,1);
        BusinessException ex = assertThrows(BusinessException.class,
                () -> AgendamentoValidator.validar(req));
        assertTrue(ex.getMessage().toLowerCase().contains("data"),
                "Mensagem deveria mencionar 'data', mas foi: " + ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // TC-V06 – horario nulo deve lançar BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-V06: horario nulo → BusinessException com mensagem correta")
    void horarioNulo_lancaExcecao() {
        AgendamentoRequest req = new AgendamentoRequest(
                1, 1, 1, "2099-12-31", null, List.of("09:00")
        ,1);
        BusinessException ex = assertThrows(BusinessException.class,
                () -> AgendamentoValidator.validar(req));
        assertTrue(ex.getMessage().toLowerCase().contains("hor"),
                "Mensagem deveria mencionar 'horário', mas foi: " + ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // TC-V07 – lista de horariosOcupados nula deve lançar BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-V07: horariosOcupados nulo → BusinessException")
    void horariosOcupadosNulos_lancaExcecao() {
        AgendamentoRequest req = new AgendamentoRequest(
                1, 1, 1, "2099-12-31", "09:00", null
        ,1);
        BusinessException ex = assertThrows(BusinessException.class,
                () -> AgendamentoValidator.validar(req));
        assertNotNull(ex.getMessage());
    }
}
