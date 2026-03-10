package utils;

import org.junit.Test;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.Assert.*;

public class AgendamentoUtilsTest {

    @Test
    public void gerarHorariosOcupados_UmSlot_RetornaUmHorario() {
        List<String> horarios = AgendamentoUtils.gerarHorariosOcupados(
                "09:00", Duration.ofMinutes(30), 30);
        assertNotNull(horarios);
        assertEquals(1, horarios.size());
        assertEquals("09:00", horarios.get(0));
    }

    @Test
    public void gerarHorariosOcupados_DoisSlots_RetornaDoisHorariosConsecutivos() {
        List<String> horarios = AgendamentoUtils.gerarHorariosOcupados(
                "09:00", Duration.ofMinutes(60), 30);
        assertNotNull(horarios);
        assertEquals(2, horarios.size());
        assertEquals("09:00", horarios.get(0));
        assertEquals("09:30", horarios.get(1));
    }

    @Test
    public void gerarHorariosOcupados_DuracaoNaoExata_ArredondaParaCima() {
        List<String> horarios = AgendamentoUtils.gerarHorariosOcupados(
                "10:00", Duration.ofMinutes(45), 30);
        assertNotNull(horarios);
        assertEquals(2, horarios.size());
        assertEquals("10:00", horarios.get(0));
        assertEquals("10:30", horarios.get(1));
    }

    @Test
    public void gerarHorariosOcupados_Intervalo15Min_GeraQuatroSlotsPorHora() {
        List<String> horarios = AgendamentoUtils.gerarHorariosOcupados(
                "14:00", Duration.ofMinutes(60), 15);
        assertNotNull(horarios);
        assertEquals(4, horarios.size());
        assertEquals("14:00", horarios.get(0));
        assertEquals("14:15", horarios.get(1));
        assertEquals("14:30", horarios.get(2));
        assertEquals("14:45", horarios.get(3));
    }

    @Test
    public void gerarHorariosOcupados_UltrapassaMeiaNoite_NaoAplicado_FormatoHHmm() {
        List<String> horarios = AgendamentoUtils.gerarHorariosOcupados(
                "23:30", Duration.ofMinutes(60), 30);
        assertNotNull(horarios);
        assertEquals(2, horarios.size());
        assertEquals("23:30", horarios.get(0));
        assertEquals("00:00", horarios.get(1));
    }

    @Test(expected = DateTimeParseException.class)
    public void gerarHorariosOcupados_FormatoInvalido_LancaExcecao() {
        AgendamentoUtils.gerarHorariosOcupados("25:00", Duration.ofMinutes(30), 30);
    }
}
