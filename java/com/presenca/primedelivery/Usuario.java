package com.presenca.primedelivery;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String tipo;
    private String veiculo;

    // Construtor vazio que o Firebase precisa
    public Usuario() {
    }

    // Construtor para criar usuário rapidamente
    public Usuario(String nome, String email, String tipo, String veiculo) {
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.veiculo = veiculo;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }
}