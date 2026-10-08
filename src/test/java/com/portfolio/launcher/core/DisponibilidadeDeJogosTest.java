package com.portfolio.launcher.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DisponibilidadeDeJogosTest {

    @Rule
    public TemporaryFolder pasta = new TemporaryFolder();

    @Test
    public void caminhoDoJarMontaRaizJogosIdJar() {
        Path raiz = pasta.getRoot().toPath();
        assertEquals(raiz.resolve("jogos").resolve("snake.jar"),
                DisponibilidadeDeJogos.caminhoDoJar(raiz, "snake"));
    }

    @Test
    public void jogoEstaDisponivelQuandoOJarExiste() throws IOException {
        Path raiz = criarJar("pong");
        assertTrue(DisponibilidadeDeJogos.disponivel(raiz,
                CatalogoDeJogos.porId("pong").get()));
    }

    @Test
    public void jogoIndisponivelQuandoOJarNaoExiste() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        assertFalse(DisponibilidadeDeJogos.disponivel(raiz,
                CatalogoDeJogos.porId("snake").get()));
    }

    @Test
    public void indisponivelQuandoNemAPastaDeJogosExiste() {
        Path raiz = pasta.getRoot().toPath();
        assertFalse(DisponibilidadeDeJogos.disponivel(raiz,
                CatalogoDeJogos.porId("forca").get()));
    }

    @Test
    public void diretorioComONomeDeJarNaoContaComoDisponivel() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos").resolve("snake.jar"));
        assertFalse(DisponibilidadeDeJogos.disponivel(raiz,
                CatalogoDeJogos.porId("snake").get()));
    }

    @Test
    public void extensaoDiferenteNaoConta() throws IOException {
        Path raiz = criarJar("forca");
        Path errado = DisponibilidadeDeJogos.caminhoDoJar(raiz, "forca");
        Files.delete(errado);
        Files.write(raiz.resolve("jogos").resolve("forca.exe"), new byte[]{1});
        assertFalse(DisponibilidadeDeJogos.disponivel(raiz,
                CatalogoDeJogos.porId("forca").get()));
    }

    private Path criarJar(String id) throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        Files.write(DisponibilidadeDeJogos.caminhoDoJar(raiz, id), new byte[]{1});
        return raiz;
    }
}