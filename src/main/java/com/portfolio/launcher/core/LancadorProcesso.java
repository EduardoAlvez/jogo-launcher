package com.portfolio.launcher.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class LancadorProcesso implements Lancador {

    private final String javaBin;

    public LancadorProcesso() {
        this(ComandoDeLancamento.javaDaJvmAtual());
    }

    public LancadorProcesso(String javaBin) {
        this.javaBin = javaBin;
    }

    @Override
    public boolean lancar(JogoInfo jogo, Path raiz) {
        try {
            iniciar(ComandoDeLancamento.montar(javaBin, raiz, jogo));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    Process iniciar(List<String> comando) throws IOException {
        return new ProcessBuilder(comando)
                .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                .redirectError(ProcessBuilder.Redirect.DISCARD)
                .start();
    }
}