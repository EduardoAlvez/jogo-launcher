package com.portfolio.launcher.core;

import java.nio.file.Path;

public interface Lancador {

    boolean lancar(JogoInfo jogo, Path raiz);
}