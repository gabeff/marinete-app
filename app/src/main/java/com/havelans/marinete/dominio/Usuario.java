package com.havelans.marinete.dominio;

/**
 * Created by gabriel.fernandes on 06/05/2016.
 * Bean usuario, cliente do app
 */
public class Usuario {
    private String nome;
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
