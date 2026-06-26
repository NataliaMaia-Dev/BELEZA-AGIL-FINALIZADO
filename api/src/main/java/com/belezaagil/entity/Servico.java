package com.belezaagil.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico")
    private Integer id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(name = "tempo_execucao_minutes", nullable = false)
    private Integer tempoExecucaoMinutes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getTempoExecucaoMinutes() {
        return tempoExecucaoMinutes;
    }

    public void setTempoExecucaoMinutes(Integer tempoExecucaoMinutes) {
        this.tempoExecucaoMinutes = tempoExecucaoMinutes;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
