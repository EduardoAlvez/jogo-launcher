package com.portfolio.launcher.recursos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import javax.imageio.ImageIO;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CarregadorDeArtesTest {

    @Rule
    public TemporaryFolder pasta = new TemporaryFolder();

    @Test
    public void carregaOLogoDeDentroDeUmJarReal() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        byte[] png = pngDeQuatroPorQuatro();
        gravarJar(raiz.resolve("jogos").resolve("pong.jar"),
                new String[]{"logo-256.png"}, new byte[][]{png});

        Map<String, BufferedImage> artes = CarregadorDeArtes.carregar(raiz);
        BufferedImage logo = artes.get("pong");
        assertNotNull(logo);
        assertEquals(4, logo.getWidth());
        assertEquals(4, logo.getHeight());
    }

    @Test
    public void jogoSemArteNuncaEntraNoMapa() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        assertTrue(CarregadorDeArtes.carregar(raiz).isEmpty());
    }

    @Test
    public void jarSemAEntradaDeLogoFicaDeFora() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        gravarJar(raiz.resolve("jogos").resolve("snake.jar"),
                new String[]{"ouro.outracoisa.txt"}, new byte[][]{new byte[]{1}});

        Map<String, BufferedImage> artes = CarregadorDeArtes.carregar(raiz);
        assertFalse(artes.containsKey("snake"));
    }

    @Test
    public void jarCorrompidoOuAusenteEIgnorado() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        Files.write(raiz.resolve("jogos").resolve("pong.jar"),
                new byte[]{9, 9, 9, 9, 9, 9});
        Map<String, BufferedImage> artes = CarregadorDeArtes.carregar(raiz);
        assertFalse(artes.containsKey("pong"));
        assertTrue(artes.isEmpty());
    }

    @Test
    public void logosDeVariosJogosSaoCarregadosJuntos() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        byte[] png = pngDeQuatroPorQuatro();
        gravarJar(raiz.resolve("jogos").resolve("pong.jar"),
                new String[]{"logo-256.png"}, new byte[][]{png});
        gravarJar(raiz.resolve("jogos").resolve("snake.jar"),
                new String[]{"logo-256.png"}, new byte[][]{png});

        Map<String, BufferedImage> artes = CarregadorDeArtes.carregar(raiz);
        assertEquals(2, artes.size());
        assertTrue(artes.containsKey("pong"));
        assertTrue(artes.containsKey("snake"));
    }

    private byte[] pngDeQuatroPorQuatro() throws IOException {
        BufferedImage imagem = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        ImageIO.write(imagem, "png", saida);
        return saida.toByteArray();
    }

    private void gravarJar(Path destino, String[] nomes, byte[][] conteudos)
            throws IOException {
        try (JarOutputStream saída = new JarOutputStream(
                Files.newOutputStream(destino))) {
            for (int i = 0; i < nomes.length; i++) {
                saída.putNextEntry(new JarEntry(nomes[i]));
                saída.write(conteudos[i]);
                saída.closeEntry();
            }
        }
    }
}