package com.belezaagil.service;

import com.belezaagil.dto.AgendamentoLoteRequest;
import com.belezaagil.dto.AgendamentoRequest;
import com.belezaagil.dto.ComandaDto;
import com.belezaagil.entity.*;
import com.belezaagil.exception.BusinessException;
import com.belezaagil.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AgendamentoService.
 * Repositórios são mockados – nenhum acesso real a banco de dados.
 * Cobre requisito R4 – regras de salvar agendamentos com comanda.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AgendamentoService – regras de negócio")
class AgendamentoServiceTest {

    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private ClienteRepository     clienteRepository;
    @Mock private ServicoRepository     servicoRepository;
    @Mock private ProfissionalRepository profissionalRepository;
    @Mock private ComandaRepository     comandaRepository;
    @Mock private ComandaService        comandaService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    // Entidades mínimas para os mocks retornarem
    private Cliente     clienteMock;
    private Servico     servicoMock;
    private Profissional profissionalMock;
    private Comanda     comandaMock;

    @BeforeEach
    void setUp() {
        clienteMock = new Cliente();
        clienteMock.setId(1);
        clienteMock.setNome("Maria Teste");

        servicoMock = new Servico();
        servicoMock.setId(1);
        servicoMock.setNome("Manicure");

        profissionalMock = new Profissional();
        profissionalMock.setId(1);
        profissionalMock.setNome("Ana Paula");

        comandaMock = new Comanda();
        comandaMock.setId(99);
    }

    // -----------------------------------------------------------------------
    // TC-S01 – lista null de agendamentos → BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-S01: lista null de agendamentos → BusinessException")
    void listaNula_lancaExcecao() {
        AgendamentoLoteRequest request = new AgendamentoLoteRequest(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.salvarLote(request));

        assertNotNull(ex.getMessage(), "Mensagem de erro não deve ser nula.");
        // Repositório jamais deve ser chamado
        verify(agendamentoRepository, never()).save(any());
    }

    // -----------------------------------------------------------------------
    // TC-S02 – lista vazia de agendamentos → BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-S02: lista vazia de agendamentos → BusinessException")
    void listaVazia_lancaExcecao() {
        AgendamentoLoteRequest request = new AgendamentoLoteRequest(Collections.emptyList());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.salvarLote(request));

        assertNotNull(ex.getMessage());
        verify(agendamentoRepository, never()).save(any());
    }

    // -----------------------------------------------------------------------
    // TC-S03 – agendamento em data no passado → BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-S03: data no passado → BusinessException")
    void dataNoPassado_lancaExcecao() {
        AgendamentoRequest req = new AgendamentoRequest(
                1, 1, 1, "2000-01-01", "09:00", List.of("09:00")
        );
        AgendamentoLoteRequest request = new AgendamentoLoteRequest(List.of(req));

        // Mocks necessários para chegar até a validação de data
        when(comandaService.criar()).thenReturn(new ComandaDto(99));
        when(comandaRepository.findById(99)).thenReturn(Optional.of(comandaMock));
        when(agendamentoRepository.findByDataAndProfissional(any(), anyInt()))
                .thenReturn(Collections.emptyList());
        when(clienteRepository.findById(1)).thenReturn(Optional.of(clienteMock));
        when(servicoRepository.findById(1)).thenReturn(Optional.of(servicoMock));
        when(profissionalRepository.findById(1)).thenReturn(Optional.of(profissionalMock));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.salvarLote(request));

        assertTrue(ex.getMessage().toLowerCase().contains("passado") ||
                   ex.getMessage().toLowerCase().contains("data"),
                "Mensagem deve indicar problema com data: " + ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // TC-S04 – agendamento válido → repositório chamado e dados associados
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-S04: agendamento válido → save chamado com comanda associada")
    void agendamentoValido_salvaComComanda() {
        AgendamentoRequest req = new AgendamentoRequest(
                1, 1, 1, "2099-12-31", "09:00", List.of("09:00", "09:15")
        );
        AgendamentoLoteRequest request = new AgendamentoLoteRequest(List.of(req));

        // Stub de criação de comanda
        when(comandaService.criar()).thenReturn(new ComandaDto(99));
        when(comandaRepository.findById(99)).thenReturn(Optional.of(comandaMock));

        // Stub de verificação de conflito
        when(agendamentoRepository.findByDataAndProfissional(any(), anyInt()))
                .thenReturn(Collections.emptyList());

        // Stubs de entidades
        when(clienteRepository.findById(1)).thenReturn(Optional.of(clienteMock));
        when(servicoRepository.findById(1)).thenReturn(Optional.of(servicoMock));
        when(profissionalRepository.findById(1)).thenReturn(Optional.of(profissionalMock));

        // Stub do save
        Agendamento agendamentoSalvo = new Agendamento();
        agendamentoSalvo.setId(1);
        agendamentoSalvo.setCliente(clienteMock);
        agendamentoSalvo.setServico(servicoMock);
        agendamentoSalvo.setProfissional(profissionalMock);
        agendamentoSalvo.setComanda(comandaMock);
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoSalvo);

        assertDoesNotThrow(() -> agendamentoService.salvarLote(request));

        // Verifica que save foi chamado exatamente 1 vez
        verify(agendamentoRepository, times(1)).save(argThat(a ->
                a.getComanda() != null && a.getComanda().getId().equals(99)
        ));
    }

    // -----------------------------------------------------------------------
    // TC-S05 – conflito de horário → BusinessException sem salvar
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-S05: conflito de horário → BusinessException, save não chamado")
    void conflitoDeHorario_lancaExcecaoSemSalvar() {
        // Agendamento existente no banco com horário 09:00
        AgendamentoHorario horarioExistente = new AgendamentoHorario();
        horarioExistente.setHorario("09:00");

        Agendamento existente = new Agendamento();
        existente.setId(10);
        existente.setHorarios(List.of(horarioExistente));

        AgendamentoRequest req = new AgendamentoRequest(
                1, 1, 1, "2099-12-31", "09:00", List.of("09:00") // colide com existente
        );
        AgendamentoLoteRequest request = new AgendamentoLoteRequest(List.of(req));

        when(comandaService.criar()).thenReturn(new ComandaDto(99));
        when(comandaRepository.findById(99)).thenReturn(Optional.of(comandaMock));
        when(agendamentoRepository.findByDataAndProfissional(any(), anyInt()))
                .thenReturn(List.of(existente));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.salvarLote(request));

        assertTrue(ex.getMessage().toLowerCase().contains("conflito") ||
                   ex.getMessage().toLowerCase().contains("hor"),
                "Mensagem deve indicar conflito de horário: " + ex.getMessage());

        verify(agendamentoRepository, never()).save(any());
    }

    // -----------------------------------------------------------------------
    // TC-S06 – cliente não encontrado → BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-S06: cliente inexistente → BusinessException")
    void clienteNaoEncontrado_lancaExcecao() {
        AgendamentoRequest req = new AgendamentoRequest(
                999, 1, 1, "2099-12-31", "09:00", List.of("09:00")
        );
        AgendamentoLoteRequest request = new AgendamentoLoteRequest(List.of(req));

        when(comandaService.criar()).thenReturn(new ComandaDto(99));
        when(comandaRepository.findById(99)).thenReturn(Optional.of(comandaMock));
        when(agendamentoRepository.findByDataAndProfissional(any(), anyInt()))
                .thenReturn(Collections.emptyList());
        when(clienteRepository.findById(999)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.salvarLote(request));

        assertTrue(ex.getMessage().toLowerCase().contains("cliente"),
                "Mensagem deve mencionar cliente: " + ex.getMessage());
    }
}
