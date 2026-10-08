package com.portfolio.launcher.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public final class ComandoDeLancamento {

    private ComandoDeLancamento() {
    }

    public static String javaDaJvmAtual() {
        String base = System.getProperty("java.home");
        boolean windows = System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win");
        return Path.of(base, "bin", windows ? "java.exe" : "java").toString();
    }

    public static List<String> montar(String javaBin, Path raiz, JogoInfo jogo) {
        Path jar = DisponibilidadeDeJogos.caminhoDoJar(raiz, jogo.getId());
        return List.of(javaBin, "-jar", jar.toString());
    }
}