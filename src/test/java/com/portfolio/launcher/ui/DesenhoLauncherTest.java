package com.portfolio.launcher.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class DesenhoLauncherTest {

    private static final int MAGENTA = 0xFF00FF;

    @Test
    public void fundoEscureceDeCimaParaBaixoNaFaixaVisivel() {
        BufferedImage imagem = renderizar(estadoComTodosDisponiveis(), artes());
        int topo = corEm(imagem, 5, 506);
        int base = corEm(imagem, 5, 638);
        assertTrue("o gradiente do fundo tem que escurecer de cima para baixo",
                somaDeCanais(topo) < somaDeCanais(base));
    }

    @Test
    public void cardSelecionadoEhMarcadoPelaBordaDeAcento() {
        EstadoLauncher estado = estadoComTodosDisponiveis();
        estado.setIndiceSelecionado(0);
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle card = LayoutLauncher.cardDoIndice(LayoutLauncher.areaBase(), 0);
        Rectangle visual = DesenhoLauncher.cardVisual(card, true, false);
        assertEquals(DesenhoLauncher.COR_ACENTO,
                corEm(imagem, visual.x + visual.width / 2, visual.y + 2));
    }

    @Test
    public void cardSelecionadoFicaMaiorQueOsOutros() {
        EstadoLauncher estado = estadoComTodosDisponiveis();
        estado.setIndiceSelecionado(0);
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle card = LayoutLauncher.cardDoIndice(LayoutLauncher.areaBase(), 0);
        Rectangle visual = DesenhoLauncher.cardVisual(card, true, false);
        assertTrue("o card selecionado tem que crescer", visual.width > card.width);
        assertEquals(DesenhoLauncher.COR_ACENTO,
                corEm(imagem, visual.x + visual.width / 2, visual.y + 2));
    }

    @Test
    public void cardNaoSelecionadoNaoTemBordaDeAcento() {
        EstadoLauncher estado = estadoComTodosDisponiveis();
        estado.setIndiceSelecionado(1);
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle card = LayoutLauncher.cardDoIndice(LayoutLauncher.areaBase(), 0);
        assertEquals(DesenhoLauncher.COR_CARD, corEm(imagem, card.x + 130, card.y + 2));
    }

    @Test
    public void cardEmHoverClareiaOFundo() {
        EstadoLauncher estado = estadoComTodosDisponiveis();
        estado.setHover(Alvo.CARD_2);
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle card = LayoutLauncher.cardDoIndice(LayoutLauncher.areaBase(), 2);
        assertEquals(DesenhoLauncher.COR_CARD_HOVER, corEm(imagem, card.x + 8, card.y + 20));
    }

    @Test
    public void cardSemOJarFicaEscurecido() {
        EstadoLauncher estado = novoEstadoComDisponivel("pong", "campominado");
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle card = LayoutLauncher.cardDoIndice(LayoutLauncher.areaBase(), 1);
        assertEquals(DesenhoLauncher.COR_CARD_DESABILITADO, corEm(imagem, card.x + 8, card.y + 20));
    }

    @Test
    public void jogarSemHoverUsaOVerdeDoBotao() {
        BufferedImage imagem = renderizar(estadoComTodosDisponiveis(), artes());
        Rectangle jogar = LayoutLauncher.jogar(LayoutLauncher.areaBase());
        assertEquals(DesenhoLauncher.COR_JOGAR, corEm(imagem, jogar.x + 8, jogar.y + 30));
    }

    @Test
    public void jogarComHoverClareiaOBotao() {
        EstadoLauncher estado = estadoComTodosDisponiveis();
        estado.setHover(Alvo.JOGAR);
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle jogar = LayoutLauncher.jogar(LayoutLauncher.areaBase());
        assertEquals(DesenhoLauncher.COR_JOGAR_HOVER, corEm(imagem, jogar.x + 8, jogar.y + 30));
    }

    @Test
    public void jogarFicaDesabilitadoQuandoOSelecionadoNaoTemJar() {
        EstadoLauncher estado = novoEstadoComDisponivel("forca");
        estado.setIndiceSelecionado(2);
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle jogar = LayoutLauncher.jogar(LayoutLauncher.areaBase());
        assertEquals(DesenhoLauncher.COR_JOGAR_DESABILITADO, corEm(imagem, jogar.x + 8, jogar.y + 30));
    }

    @Test
    public void painelDeDescricaoTemOFundoDoPainel() {
        BufferedImage imagem = renderizar(estadoComTodosDisponiveis(), artes());
        Rectangle painel = LayoutLauncher.descricao(LayoutLauncher.areaBase());
        assertEquals(DesenhoLauncher.COR_PAINEL, corEm(imagem, painel.x + 8, painel.y + 8));
    }

    @Test
    public void arteDoJogoEhDesenhadaCobrindoOBanner() {
        BufferedImage imagem = renderizar(estadoComTodosDisponiveis(), artes());
        assertEquals(MAGENTA, corEm(imagem, 800, 300));
    }

    @Test
    public void jogoSemArteMostraOPainelDeGuarda() {
        EstadoLauncher estado = novoEstadoComDisponivel("pong");
        estado.setIndiceSelecionado(1);
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle guarda = LayoutLauncher.areaDeGuarda(LayoutLauncher.areaBase());
        assertEquals(DesenhoLauncher.COR_PAINEL, corEm(imagem, guarda.x + 8, guarda.y + 8));
    }

    @Test
    public void bonecoDaForcaTemOPosteECabecaVerdeNoBanner() {
        EstadoLauncher estado = novoEstadoComDisponivel("pong");
        estado.setIndiceSelecionado(1);
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle palco = LayoutLauncher.areaDeGuarda(LayoutLauncher.areaBase());
        int xPoste = palco.x + palco.width * 26 / 100;
        int topoY = palco.y + palco.height * 8 / 100;
        int baseY = palco.y + palco.height * 90 / 100;
        assertEquals(DesenhoLauncher.COR_TEXTO_MEDIO, corEm(imagem, xPoste + 3, (topoY + baseY) / 2));
        int cx = xPoste + palco.width * 28 / 100 - 2;
        int cyCabeca = topoY + 7 + palco.height * 6 / 100;
        assertEquals(DesenhoLauncher.COR_ACENTO, corEm(imagem, cx, cyCabeca + palco.width * 5 / 100));
    }

    @Test
    public void cardDaForcaDesenhaOBonecoNoLugarDoIcone() {
        EstadoLauncher estado = novoEstadoComDisponivel("pong");
        estado.setIndiceSelecionado(1);
        BufferedImage imagem = renderizar(estado, artes());
        Rectangle card = LayoutLauncher.cardDoIndice(LayoutLauncher.areaBase(), 1);
        Rectangle visual = DesenhoLauncher.cardVisual(card, true, false);
        Rectangle icone = LayoutLauncher.iconeDoCard(visual);
        assertEquals(DesenhoLauncher.COR_ACENTO, corEm(imagem, icone.x + 26, icone.y + 22));
    }

    @Test
    public void mensagemApareceNoPainelDeToast() {
        EstadoLauncher estado = estadoComTodosDisponiveis();
        estado.setMensagem("Abrindo Snake...");
        BufferedImage imagem = renderizar(estado, artes());
        assertEquals(DesenhoLauncher.COR_TOAST, corEm(imagem, 700, 103));
    }

    @Test
    public void semMensagemNaoHaToast() {
        BufferedImage imagem = renderizar(estadoComTodosDisponiveis(), artes());
        assertEquals(MAGENTA, corEm(imagem, 700, 103));
    }

    private EstadoLauncher estadoComTodosDisponiveis() {
        return novoEstadoComDisponivel("pong", "forca", "snake", "campominado");
    }

    private EstadoLauncher novoEstadoComDisponivel(String... ids) {
        return new EstadoLauncher(Set.copyOf(java.util.List.of(ids)));
    }

    private Map<String, BufferedImage> artes() {
        BufferedImage arte = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = arte.createGraphics();
        g.setColor(new Color(MAGENTA));
        g.fillRect(0, 0, 10, 10);
        g.dispose();
        Map<String, BufferedImage> artes = new HashMap<>();
        for (String id : new String[]{"pong", "snake", "campominado"}) {
            artes.put(id, arte);
        }
        return artes;
    }

    private BufferedImage renderizar(EstadoLauncher estado, Map<String, BufferedImage> artes) {
        Rectangle area = LayoutLauncher.areaBase();
        BufferedImage imagem = new BufferedImage(area.width, area.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagem.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        DesenhoLauncher.desenhar(g, area, estado, artes);
        g.dispose();
        return imagem;
    }

    private int corEm(BufferedImage imagem, int x, int y) {
        return imagem.getRGB(x, y) & 0xFFFFFF;
    }

    private int somaDeCanais(int rgb) {
        return ((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + (rgb & 0xFF);
    }
}