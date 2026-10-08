package com.portfolio.launcher.ui;

import com.portfolio.launcher.core.CatalogoDeJogos;
import com.portfolio.launcher.core.JogoInfo;

import java.util.Set;

public final class EstadoLauncher {

    private final Set<String> disponiveis;
    private int indiceSelecionado;
    private Alvo hover = Alvo.NENHUM;
    private String mensagem = "";

    public EstadoLauncher(Set<String> disponiveis) {
        this.disponiveis = Set.copyOf(disponiveis);
    }

    public JogoInfo jogoSelecionado() {
        return CatalogoDeJogos.lista().get(indiceSelecionado);
    }

    public int getIndiceSelecionado() {
        return indiceSelecionado;
    }

    public void setIndiceSelecionado(int indice) {
        if (indice < 0 || indice >= CatalogoDeJogos.quantidade()) {
            throw new IllegalArgumentException("indice de jogo invalido: " + indice);
        }
        this.indiceSelecionado = indice;
    }

    public Alvo getHover() {
        return hover;
    }

    public void setHover(Alvo hover) {
        this.hover = hover == null ? Alvo.NENHUM : hover;
    }

    public boolean disponivel(JogoInfo jogo) {
        return disponiveis.contains(jogo.getId());
    }

    public boolean disponivel(int indice) {
        return disponivel(CatalogoDeJogos.lista().get(indice));
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem == null ? "" : mensagem;
    }
}