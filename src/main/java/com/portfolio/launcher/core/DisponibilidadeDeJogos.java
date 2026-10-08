package com.portfolio.launcher.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class DisponibilidadeDeJogos {

    private static final String PASTA_JOGOS = "jogos";

    private DisponibilidadeDeJogos() {
    }

    public static Path caminhoDoJar(Path raiz, String id) {
        return raiz.resolve(PASTA_JOGOS).resolve(id + ".jar");
    }

    public static boolean disponivel(Path raiz, JogoInfo jogo) {
        return Files.isRegularFile(caminhoDoJar(raiz, jogo.getId()));
    }
}