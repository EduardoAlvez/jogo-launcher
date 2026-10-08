package com.portfolio.launcher.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ComandoDeLancamentoTest {

    @Rule
    public TemporaryFolder pasta = new TemporaryFolder();

    @Test
    public void montarCompoeJavaJarECaminhoDoJar() {
        Path raiz = pasta.getRoot().toPath();
        JogoInfo jogo = CatalogoDeJogos.porId("snake").get();
        String java = "C:/java/bin/java.exe";
        List<String> comando = ComandoDeLancamento.montar(java, raiz, jogo);
        assertEquals(List.of(java, "-jar",
                DisponibilidadeDeJogos.caminhoDoJar(raiz, jogo.getId()).toString()),
                comando);
    }

    @Test
    public void montarUsaAMesmaConvencaoDaDisponibilidade() {
        Path raiz = pasta.getRoot().toPath();
        JogoInfo jogo = CatalogoDeJogos.porId("forca").get();
        List<String> comando = ComandoDeLancamento.montar("java", raiz, jogo);
        assertEquals(DisponibilidadeDeJogos.caminhoDoJar(raiz, jogo.getId()).toString(),
                comando.get(2));
    }

    @Test
    public void javaDaJvmAtualApontaProBinDaJvmCorrente() {
        String java = ComandoDeLancamento.javaDaJvmAtual();
        Path base = Path.of(System.getProperty("java.home"), "bin");
        assertTrue("java deve morar em " + base, Files.isRegularFile(Path.of(java)));
    }
}