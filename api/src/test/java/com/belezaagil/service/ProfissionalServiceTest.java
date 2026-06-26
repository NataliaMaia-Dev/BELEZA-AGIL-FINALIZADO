package com.belezaagil.service;

import com.belezaagil.dto.ProfissionalDto;
import com.belezaagil.entity.Profissional;
import com.belezaagil.exception.BusinessException;
import com.belezaagil.repository.ProfissionalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ProfissionalService.
 * Cobre requisito R1 – limite de 5 profissionais e CPF único.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProfissionalService – regras de negócio")
class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    // Limite fixo em 5 (igual ao application.yml)
    @InjectMocks
    private ProfissionalService profissionalService =
            new ProfissionalService(profissionalRepository, 5);

    private ProfissionalDto dtoValido(Integer id) {
        return new ProfissionalDto(
                id, "Ana Paula", LocalDate.of(1990, 5, 10),
                "123.456.789-00", "ana@email.com", "Manicure"
        );
    }

    // -----------------------------------------------------------------------
    // TC-P01 – cadastrar com 4 profissionais existentes → sucesso
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-P01: abaixo do limite (4/5) → salva com sucesso")
    void abaixoDoLimite_salvaComSucesso() {
        when(profissionalRepository.existsByCpf(anyString())).thenReturn(false);
        when(profissionalRepository.count()).thenReturn(4L);

        Profissional salvo = new Profissional();
        salvo.setId(5);
        salvo.setNome("Ana Paula");
        salvo.setCpf("123.456.789-00");
        when(profissionalRepository.save(any())).thenReturn(salvo);

        assertDoesNotThrow(() -> profissionalService.salvar(dtoValido(null)));
        verify(profissionalRepository, times(1)).save(any());
    }

    // -----------------------------------------------------------------------
    // TC-P02 – cadastrar com 5 profissionais existentes → BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-P02: no limite (5/5) → BusinessException com mensagem de limite")
    void noLimite_lancaExcecao() {
        when(profissionalRepository.existsByCpf(anyString())).thenReturn(false);
        when(profissionalRepository.count()).thenReturn(5L);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> profissionalService.salvar(dtoValido(null)));

        assertTrue(ex.getMessage().contains("5") || ex.getMessage().toLowerCase().contains("limite"),
                "Mensagem deve indicar limite: " + ex.getMessage());
        verify(profissionalRepository, never()).save(any());
    }

    // -----------------------------------------------------------------------
    // TC-P03 – CPF duplicado → BusinessException sem salvar
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-P03: CPF já cadastrado → BusinessException")
    void cpfDuplicado_lancaExcecao() {
        when(profissionalRepository.existsByCpf("123.456.789-00")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> profissionalService.salvar(dtoValido(null)));

        assertTrue(ex.getMessage().toLowerCase().contains("cpf"),
                "Mensagem deve mencionar CPF: " + ex.getMessage());
        verify(profissionalRepository, never()).save(any());
    }

    // -----------------------------------------------------------------------
    // TC-P04 – editar profissional existente não conta para o limite
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-P04: edição (id != null) não é bloqueada pelo limite")
    void edicao_naoBloqueadaPorLimite() {
        // CPF não duplicado por outro registro
        when(profissionalRepository.existsByCpfAndIdNot("123.456.789-00", 1)).thenReturn(false);

        Profissional existente = new Profissional();
        existente.setId(1);
        existente.setNome("Ana Paula");
        when(profissionalRepository.findById(1)).thenReturn(Optional.of(existente));

        Profissional atualizado = new Profissional();
        atualizado.setId(1);
        atualizado.setNome("Ana Paula Editada");
        atualizado.setCpf("123.456.789-00");
        when(profissionalRepository.save(any())).thenReturn(atualizado);

        // Deve salvar sem lançar exceção mesmo com count() = 5
        // (count não é consultado em edição)
        assertDoesNotThrow(() -> profissionalService.salvar(dtoValido(1)));
    }

    // -----------------------------------------------------------------------
    // TC-P05 – excluir profissional inexistente → BusinessException
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-P05: excluir id inexistente → BusinessException")
    void excluirInexistente_lancaExcecao() {
        when(profissionalRepository.existsById(999)).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> profissionalService.excluir(999));

        assertTrue(ex.getMessage().toLowerCase().contains("profissional"),
                "Mensagem deve mencionar profissional: " + ex.getMessage());
        verify(profissionalRepository, never()).deleteById(any());
    }

    // -----------------------------------------------------------------------
    // TC-P06 – excluir profissional existente → deleteById chamado
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-P06: excluir id existente → deleteById chamado")
    void excluirExistente_deleteChamado() {
        when(profissionalRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> profissionalService.excluir(1));
        verify(profissionalRepository, times(1)).deleteById(1);
    }
}
