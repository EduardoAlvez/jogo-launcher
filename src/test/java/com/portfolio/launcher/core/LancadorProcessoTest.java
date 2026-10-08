package com.portfolio.launcher.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LancadorProcessoTest {

    @Rule
    public TemporaryFolder pasta = new TemporaryFolder();

    @Test
    public void iniciarRodaOJavaDaJvmESaiComSucesso() throws Exception {
        LancadorProcesso lancador = new LancadorProcesso();
        Process processo = lancador.iniciar(
                List.of(ComandoDeLancamento.javaDaJvmAtual(), "-version"));
        assertTrue(processo.waitFor(10, TimeUnit.SECONDS));
        assertEquals(0, processo.exitValue());
    }

    @Test
    public void iniciarFalhaQuandoOComandoNaoExiste() {
        LancadorProcesso lancador = new LancadorProcesso();
        try {
            lancador.iniciar(List.of("diretorio-que-nao-existe/x-programa", "-version"));
        } catch (IOException esperado) {
            return;
        }
        org.junit.Assert.fail("deveria lancar IOException com comando inexistente");
    }

    @Test
    public void lancarAbreOJogoQuandoOJarExisteNaPasta() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        Files.write(raiz.resolve("jogos").resolve("pong.jar"), new byte[]{1});
        LancadorProcesso lancador = new LancadorProcesso();
        assertTrue(lancador.lancar(CatalogoDeJogos.porId("pong").get(), raiz));
    }

    @Test
    public void lancarFalsoQuandoOJarNaoExiste() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        LancadorProcesso lancador = new LancadorProcesso("diretorio-que-nao-existe/java.exe");
        assertFalse(lancador.lancar(CatalogoDeJogos.porId("snake").get(), raiz));
    }
}