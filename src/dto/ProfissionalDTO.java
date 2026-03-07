package dto;

import java.time.LocalDate;

public class ProfissionalDTO {
    private int idProfissional;
    private String nome;
    private LocalDate dataNascimento;
    private String cpf;
    private String email;
    private String funcao;

    public ProfissionalDTO() {}

    public ProfissionalDTO(int idProfissional, String nome, LocalDate dataNascimento, String cpf, String email, String funcao) {
        this.idProfissional = idProfissional;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.email = email;
        this.funcao = funcao;
    }

    public int getIdProfissional() {
        return idProfissional;
    }

    public void setIdProfissional(int idProfissional) {
        this.idProfissional = idProfissional;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
        @Override
    public String toString() {
        return nome;
    }
    
}