package dto;

public class AgendamentoSlot {
    private AgendamentoDTO agendamento;
    private int displayLevel; 

    public AgendamentoSlot(AgendamentoDTO agendamento, int displayLevel) {
        this.agendamento = agendamento;
        this.displayLevel = displayLevel;
    }

    public AgendamentoDTO getAgendamento() {
        return agendamento;
    }

    public int getDisplayLevel() {
        return displayLevel;
    }
}