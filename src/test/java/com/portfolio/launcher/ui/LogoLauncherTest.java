package com.portfolio.launcher.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.Test;

/**
 * O icone do launcher, conferido no <b>produto</b>: o {@code logo.ico} que o
 * launch4j embute no executavel e os PNGs que a janela carrega. As ferramentas
 * que os produzem moram em {@code tools/}, fora do {@code src/}, entao nao
 * entram no build do Maven — o que importa e se o resultado esta certo.
 *
 * <p>Como no Campo Minado, a mascara AND do {@code .ico} e conferida no arquivo
 * final: o bit 1 significa "transparente" e uma mascara zerada declara o icone
 * inteiro opaco, um defeito que so aparece na barra de tarefas.
 */
public class LogoLauncherTest {

    private static final int[] TAMANHOS_ESPERADOS = {16, 24, 32, 48, 64, 128, 256};
    private static final int TAMANHO_ENTRADA_ICO = 16;
    private static final int LIMIAR_ALFA = 128;

    private static final int COR_CARD_DESTAQUE = 0x2E3842;
    private static final int COR_ACENTO = 0x4FA34F;
    private static final int COR_ACENTO_CLARO = 0x62C46A;

    @Test
    public void oIcoDeclaraAsSeteResolucoes() throws IOException {
        byte[] ico = lerRecurso("/logo.ico");

        int tipo = (ico[2] & 0xFF) | ((ico[3] & 0xFF) << 8);
        assertEquals("o tipo do arquivo tem que ser 1 (icone)", 1, tipo);

        int entradas = (ico[4] & 0xFF) | ((ico[5] & 0xFF) << 8);
        assertEquals("entradas no diretorio do .ico", TAMANHOS_ESPERADOS.length, entradas);

        for (int i = 0; i < TAMANHOS_ESPERADOS.length; i++) {
            int pos = 6 + i * TAMANHO_ENTRADA_ICO;
            int lado = ico[pos] & 0xFF;
            assertEquals("largura da entrada " + i, TAMANHOS_ESPERADOS[i] == 256 ? 0 : TAMANHOS_ESPERADOS[i], lado);
        }
    }

    @Test
    public void oIcoDeclaraTransparenciaNasBordas() throws IOException {
        byte[] ico = lerRecurso("/logo.ico");
        byte[] entrada = entradaDoIco(ico, 0);
        int lado = 16;
        int bytesDaLinhaDaMascara = ((lado + 31) / 32) * 4;
        int inicioMascara = 40 + lado * lado * 4;

        boolean algumTransparente = false;
        for (int i = inicioMascara; i < entrada.length; i++) {
            if ((entrada[i] & 0xFF) != 0) {
                algumTransparente = true;
                break;
            }
        }
        assertTrue("a mascara AND do .ico esta zerada: o icone sai quadrado e opaco "
                + "na barra de tarefas", algumTransparente);
    }

    @Test
    public void oMestreEQuadradoETemCantoTransparente() throws IOException {
        BufferedImage imagem = ImageIO.read(abrirRecurso("/logo-256.png"));
        assertNotNull("o logo-256.png nao esta no classpath", imagem);
        assertEquals(256, imagem.getWidth());
        assertEquals(256, imagem.getHeight());
        assertEquals("o canto tem que ser transparente para o icone nao sair chapado",
                0, imagem.getRGB(0, 0) >>> 24);
        assertEquals("o centro tem que ser opaco", 255, imagem.getRGB(128, 128) >>> 24);
    }

    @Test
    public void oMestreTemAsCoresDoLauncher() throws IOException {
        BufferedImage imagem = ImageIO.read(abrirRecurso("/logo-256.png"));
        Set<Integer> cores = new HashSet<>();
        for (int y = 0; y < imagem.getHeight(); y++) {
            for (int x = 0; x < imagem.getWidth(); x++) {
                if ((imagem.getRGB(x, y) >>> 24) >= LIMIAR_ALFA) {
                    cores.add(imagem.getRGB(x, y) & 0x00FFFFFF);
                }
            }
        }
        assertTrue("falta o card em destaque", cores.contains(COR_CARD_DESTAQUE));
        assertTrue("falta o verde de acento", cores.contains(COR_ACENTO));
        assertTrue("falta o verde claro do botao de play", cores.contains(COR_ACENTO_CLARO));
    }

    @Test
    public void oMestreTemAsSeteResolucoesEmDisco() throws IOException {
        for (int lado : TAMANHOS_ESPERADOS) {
            BufferedImage imagem = ImageIO.read(abrirRecurso("/logo-" + lado + ".png"));
            assertNotNull("falta o logo-" + lado + ".png", imagem);
            assertEquals(lado, imagem.getWidth());
        }
    }

    private static ByteArrayInputStream abrirRecurso(String caminho) throws IOException {
        return new ByteArrayInputStream(lerRecurso(caminho));
    }

    private static byte[] entradaDoIco(byte[] ico, int indice) throws IOException {
        int pos = 6 + indice * TAMANHO_ENTRADA_ICO;
        int tamanho = lerInteiroLe(ico, pos + 8);
        int deslocamento = lerInteiroLe(ico, pos + 12);
        if (deslocamento < 0 || tamanho < 0 || deslocamento + tamanho > ico.length) {
            throw new IOException("entrada " + indice + " do .ico aponta fora do arquivo");
        }
        byte[] entrada = new byte[tamanho];
        System.arraycopy(ico, deslocamento, entrada, 0, tamanho);
        return entrada;
    }

    private static int lerInteiroLe(byte[] dados, int pos) {
        return (dados[pos] & 0xFF)
                | ((dados[pos + 1] & 0xFF) << 8)
                | ((dados[pos + 2] & 0xFF) << 16)
                | ((dados[pos + 3] & 0xFF) << 24);
    }

    private static byte[] lerRecurso(String caminho) throws IOException {
        InputStream entrada = LogoLauncherTest.class.getResourceAsStream(caminho);
        assertNotNull("o recurso " + caminho + " nao esta no classpath; ele e gerado por "
                + "tools/DesenharLogoLauncher.java e tools/GerarLogo.java", entrada);
        try (InputStream in = entrada) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] pedaco = new byte[8192];
            int lidos;
            while ((lidos = in.read(pedaco)) > 0) {
                buffer.write(pedaco, 0, lidos);
            }
            return buffer.toByteArray();
        }
    }
}