package com.portfolio.launcher.core;

import java.util.List;
import java.util.Optional;

public final class CatalogoDeJogos {

    private static final List<JogoInfo> JOGOS = List.of(
            new JogoInfo(
                    "pong",
                    "Pong",
                    "Espaco · 1v1",
                    "Pong personalizavel: modo Tempo estilo futebol e modo Classico, "
                            + "bola que pega fogo, premios estilo Mario e 6 skins.",
                    "P1: W/S · P2: setas · Espaco pausa · Z/M: especial",
                    true),
            new JogoInfo(
                    "forca",
                    "Jogo da Forca",
                    "Palavras",
                    "Adivinhe a palavra secreta antes de o boneco cair: 58 palavras, "
                            + "8 categorias, 3 niveis de dificuldade e 4 personagens.",
                    "Digite as letras · Enter confirma · clique no alfabeto",
                    false),
            new JogoInfo(
                    "snake",
                    "Snake",
                    "Nostalgia",
                    "A cobra numa grade 16x16: duas bordas, tres dificuldades, "
                            + "dois poderes e quatro skins.",
                    "Setas / WASD · Espaco ou P pausa · N troca de skin",
                    true),
            new JogoInfo(
                    "campominado",
                    "Campo Minado",
                    "Estrategia",
                    "9x9, 16x16 ou 16x30: o primeiro clique nunca e mina, abertura "
                            + "em cascata, bandeiras por mouse ou teclado e recorde de tempo.",
                    "Mouse esquerdo/direito · Setas / WASD · Espaco abre · F marca",
                    true));

    private CatalogoDeJogos() {
    }

    public static List<JogoInfo> lista() {
        return JOGOS;
    }

    public static int quantidade() {
        return JOGOS.size();
    }

    public static Optional<JogoInfo> porId(String id) {
        for (JogoInfo jogo : JOGOS) {
            if (jogo.getId().equals(id)) {
                return Optional.of(jogo);
            }
        }
        return Optional.empty();
    }
}