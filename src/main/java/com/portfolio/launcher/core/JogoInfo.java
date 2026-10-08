package com.portfolio.launcher.core;

import java.util.Objects;

public final class JogoInfo {

    private final String id;
    private final String nome;
    private final String tag;
    private final String descricao;
    private final String controles;
    private final boolean temArte;

    public JogoInfo(String id, String nome, String tag, String descricao,
                    String controles, boolean temArte) {
        this.id = exigirNaoVazio(id, "id");
        this.nome = exigirNaoVazio(nome, "nome");
        this.tag = Objects.toString(tag, "");
        this.descricao = Objects.toString(descricao, "");
        this.controles = Objects.toString(controles, "");
        this.temArte = temArte;
    }

    private static String exigirNaoVazio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("campo '" + campo + "' nao pode ser vazio");
        }
        return valor;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTag() {
        return tag;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getControles() {
        return controles;
    }

    public boolean temArte() {
        return temArte;
    }
}