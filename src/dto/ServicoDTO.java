package dto;

import java.math.BigDecimal;
import java.time.Duration;

public class ServicoDTO {

    private int idServico;
    private String nome;
    private Duration tempoExecucao;
    private BigDecimal valor;

    public ServicoDTO() {
    }

    public ServicoDTO(int idServico, String nome, Duration tempoExecucao, BigDecimal valor) {
        this.idServico = idServico;
        this.nome = nome;
        this.tempoExecucao = tempoExecucao;
        this.valor = valor;
    }

    public ServicoDTO(int idServico, String nome, BigDecimal valor) {
        this.idServico = idServico;
        this.nome = nome;
        this.valor = valor;
    }
    
    

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Duration getTempoExecucao() {
        return tempoExecucao;
    }

    public void setTempoExecucao(Duration tempoExecucao) {
        this.tempoExecucao = tempoExecucao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return nome; 
    }
}
