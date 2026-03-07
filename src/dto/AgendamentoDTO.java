package dto;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoDTO {

    private int idAgendamento;
    private LocalDateTime dataAgendamento;
    private ClienteDTO cliente;
    private ServicoDTO servico;
    private ProfissionalDTO profissional;
    private ComandaDTO comanda;
    private boolean finalizado;
    private List<String> horariosOcupados;


    public AgendamentoDTO() {
    }

    public AgendamentoDTO(LocalDateTime dataAgendamento, ClienteDTO cliente, ServicoDTO servico, ProfissionalDTO profissional, ComandaDTO comanda, boolean finalizado) {
        this.dataAgendamento = dataAgendamento;
        this.cliente = cliente;
        this.servico = servico;
        this.profissional = profissional;
        this.comanda = comanda;
        this.finalizado = false;
    }

    public int getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(int idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDateTime dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public ServicoDTO getServico() {
        return servico;
    }

    public void setServico(ServicoDTO servico) {
        this.servico = servico;
    }

    public ProfissionalDTO getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalDTO profissional) {
        this.profissional = profissional;
    }

    public ComandaDTO getComanda() {
        return comanda;
    }

    public void setComanda(ComandaDTO comanda) {
        this.comanda = comanda;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public List<String> getHorariosOcupados() {
        return horariosOcupados;
    }

    public void setHorariosOcupados(List<String> horariosOcupados) {
        this.horariosOcupados = horariosOcupados;
    }
    
}
