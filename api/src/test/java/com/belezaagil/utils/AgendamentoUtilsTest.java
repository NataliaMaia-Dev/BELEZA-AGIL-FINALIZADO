package com.belezaagil.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para geração de horários ocupados.
 * Cobre requisito R4 – Agendamento com data e hora (cálculo de slots).
 *
 * Sem acesso a banco de dados.
 */
@DisplayName("AgendamentoUtils – gerarHorariosOcupados")
class AgendamentoUtilsTest {

    // -----------------------------------------------------------------------
    // TC-U01 – serviço de 15 min a partir de 09:00 → apenas 1 slot
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-U01: duração 15 min, início 09:00 → [09:00]")
    void umSlotDe15Min() {
        List<String> slots = AgendamentoUtils.gerarHorariosOcupados("09:00", 15, 15);
        assertEquals(List.of("09:00"), slots,
                "Serviço de exatamente 1 intervalo deve gerar 1 slot.");
    }

    // -----------------------------------------------------------------------
    // TC-U02 – serviço de 30 min, intervalo 15 min → 2 slots
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-U02: duração 30 min, intervalo 15 min → [09:00, 09:15]")
    void doisSlotsDE30Min() {
        List<String> slots = AgendamentoUtils.gerarHorariosOcupados("09:00", 30, 15);
        assertEquals(List.of("09:00", "09:15"), slots);
    }

    // -----------------------------------------------------------------------
    // TC-U03 – serviço de 60 min, intervalo 15 min → 4 slots
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-U03: duração 60 min, intervalo 15 min → 4 slots")
    void quatroSlotsDE60Min() {
        List<String> slots = AgendamentoUtils.gerarHorariosOcupados("10:00", 60, 15);
        assertEquals(4, slots.size());
        assertEquals("10:00", slots.get(0));
        assertEquals("10:45", slots.get(3));
    }

    // -----------------------------------------------------------------------
    // TC-U04 – duração não múltipla de 15 deve ser arredondada para cima
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-U04: duração 20 min arredonda para 2 slots de 15 min")
    void duracaoNaoMultiplaArredondaParaCima() {
        List<String> slots = AgendamentoUtils.gerarHorariosOcupados("09:00", 20, 15);
        // 20 min / 15 → ceil(1,33) = 2 slots
        assertEquals(2, slots.size(),
                "Duração não-múltipla deve arredondar para cima.");
    }

    // -----------------------------------------------------------------------
    // TC-U05 – formato dos slots deve ser HH:mm (dois dígitos cada)
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-U05: slots gerados têm formato HH:mm")
    void formatoHHmm() {
        List<String> slots = AgendamentoUtils.gerarHorariosOcupados("08:00", 30, 15);
        for (String slot : slots) {
            assertTrue(slot.matches("\\d{2}:\\d{2}"),
                    "Slot inválido: " + slot + " – esperado formato HH:mm");
        }
    }

    // -----------------------------------------------------------------------
    // TC-U06 – horário em formato inválido deve lançar exceção
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-U06: formato inválido (9:0) → exceção")
    void formatoInvalido_lancaExcecao() {
        assertThrows(Exception.class,
                () -> AgendamentoUtils.gerarHorariosOcupados("9:0", 30, 15),
                "Formato de hora inválido deve lançar exceção.");
    }

    // -----------------------------------------------------------------------
    // TC-U07 – slots cruzam a virada de hora (09:45 + 30 min → 09:45, 10:00)
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("TC-U07: slots cruzam virada de hora corretamente")
    void slotsViradaDeHora() {
        List<String> slots = AgendamentoUtils.gerarHorariosOcupados("09:45", 30, 15);
        assertEquals(List.of("09:45", "10:00"), slots,
                "Slots devem cruzar a virada de hora sem erro.");
    }
}
