package utils;

import dto.AgendamentoDTO;
import dto.AgendamentoSlot;
import javax.swing.JTable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoUtils {

    public static List<String> gerarHorariosOcupados(String horaInicialStr, Duration tempoServico, int intervaloMinutos) {
        LocalTime horaInicial = LocalTime.parse(horaInicialStr, DateTimeFormatter.ofPattern("HH:mm"));
        long duracaoMinutos = tempoServico.toMinutes();

        int slots = (int) Math.ceil((double) duracaoMinutos / intervaloMinutos);

        List<String> horarios = new ArrayList<>();

        for (int i = 0; i < slots; i++) {
            LocalTime slotTime = horaInicial.plusMinutes(i * intervaloMinutos);
            horarios.add(slotTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        return horarios;
    }

    public static boolean temConflito(
            JTable tabela,
            String nomeProfissional,
            List<String> horariosDoServico,
            List<String> horariosBase) {

        int colunaProfissional = -1;
        for (int col = 1; col < tabela.getColumnCount(); col++) {
            if (tabela.getColumnName(col).equalsIgnoreCase(nomeProfissional)) {
                colunaProfissional = col;
                break;
            }
        }

        if (colunaProfissional == -1) {
            return false;
        }

        for (String horario : horariosDoServico) {
            int linha = horariosBase.indexOf(horario);

            if (linha != -1) {
                Object valorCelula = tabela.getValueAt(linha, colunaProfissional);

                if (valorCelula instanceof AgendamentoDTO) {
                    return true;
                } else if (valorCelula instanceof AgendamentoSlot slot) {
                    if (slot.getAgendamento() != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
