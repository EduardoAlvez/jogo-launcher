package com.portfolio.launcher.ui;

import com.portfolio.launcher.core.CatalogoDeJogos;
import com.portfolio.launcher.core.JogoInfo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DesenhoLauncher {

    public static final int COR_FUNDO_INICIO = 0x101318;
    public static final int COR_FUNDO_FIM = 0x1a212a;
    public static final int COR_PAINEL = 0x1d242d;
    public static final int COR_CARD = 0x232a33;
    public static final int COR_CARD_HOVER = 0x2e3842;
    public static final int COR_CARD_DESABILITADO = 0x15181d;
    public static final int COR_BORDA_CARD = 0x333c46;
    public static final int COR_ACENTO = 0x4fa34f;
    public static final int COR_ACENTO_CLARO = 0x62c46a;
    public static final int COR_JOGAR = 0x4fa34f;
    public static final int COR_JOGAR_HOVER = 0x63c76b;
    public static final int COR_JOGAR_DESABILITADO = 0x2a313a;
    public static final int COR_TEXTO = 0xe8eaed;
    public static final int COR_TEXTO_MEDIO = 0x98a2ac;
    public static final int COR_TOAST = 0x29313b;

    private static final int ARREDONDAMENTO = 14;
    private static final int LARGURA_ESCURECIMENTO = 520;

    private DesenhoLauncher() {
    }

    public static void desenhar(Graphics2D g, Rectangle area, EstadoLauncher estado,
                                Map<String, BufferedImage> artes) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        desenharFundo(g, area);
        desenharArte(g, area, estado.jogoSelecionado(), arteDe(artes, estado.jogoSelecionado()));
        desenharTitulo(g, area);
        desenharDescricao(g, area, estado.jogoSelecionado());
        for (int i = 0; i < CatalogoDeJogos.quantidade(); i++) {
            desenharCard(g, area, i, arteDe(artes, CatalogoDeJogos.lista().get(i)), estado);
        }
        desenharJogar(g, area, estado);
        desenharMensagem(g, area, estado);
        desenharDica(g, area);
    }

    private static BufferedImage arteDe(Map<String, BufferedImage> artes, JogoInfo jogo) {
        return artes.get(jogo.getId());
    }

    private static void desenharFundo(Graphics2D g, Rectangle area) {
        g.setPaint(new java.awt.GradientPaint(
                area.x, area.y, new Color(COR_FUNDO_INICIO),
                area.x, area.y + area.height, new Color(COR_FUNDO_FIM)));
        g.fillRect(area.x, area.y, area.width, area.height);
    }

    private static void desenharArte(Graphics2D g, Rectangle area, JogoInfo jogo,
                                     BufferedImage arte) {
        Rectangle banner = LayoutLauncher.areaDaArte(area);
        if (jogo.temArte() && arte != null) {
            desenharCobrindo(g, banner, arte);
            desenharEscurecimento(g, banner);
            return;
        }
        desenharArteDeGuarda(g, LayoutLauncher.areaDeGuarda(area), jogo);
    }

    private static void desenharCobrindo(Graphics2D g, Rectangle banner, BufferedImage arte) {
        double escala = Math.max(
                (double) banner.width / arte.getWidth(),
                (double) banner.height / arte.getHeight());
        int largura = (int) Math.ceil(arte.getWidth() * escala);
        int altura = (int) Math.ceil(arte.getHeight() * escala);
        int x = banner.x + (banner.width - largura) / 2;
        int y = banner.y + (banner.height - altura) / 2;
        Shape antigo = g.getClip();
        g.setClip(banner);
        g.drawImage(arte, x, y, largura, altura, null);
        g.setClip(antigo);
    }

    private static void desenharEscurecimento(Graphics2D g, Rectangle banner) {
        int r = (COR_FUNDO_INICIO >> 16) & 0xFF;
        int gr = (COR_FUNDO_INICIO >> 8) & 0xFF;
        int b = COR_FUNDO_INICIO & 0xFF;
        g.setPaint(new java.awt.GradientPaint(
                banner.x, banner.y, new Color(r, gr, b, 0xEA),
                banner.x + LARGURA_ESCURECIMENTO, banner.y, new Color(r, gr, b, 0)));
        g.fillRect(banner.x, banner.y, LARGURA_ESCURECIMENTO, banner.height);
    }

    private static void desenharArteDeGuarda(Graphics2D g, Rectangle guarda, JogoInfo jogo) {
        if (!jogo.temArte()) {
            desenharBonecoDaForca(g, guarda, jogo);
            return;
        }
        g.setColor(new Color(COR_PAINEL));
        g.fillRoundRect(guarda.x, guarda.y, guarda.width, guarda.height,
                ARREDONDAMENTO, ARREDONDAMENTO);
        g.setColor(new Color(COR_BORDA_CARD));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(guarda.x, guarda.y, guarda.width, guarda.height,
                ARREDONDAMENTO, ARREDONDAMENTO);
        g.setColor(new Color(COR_TEXTO));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 54));
        centralizar(g, jogo.getNome(), guarda, guarda.y + guarda.height / 2);
        g.setColor(new Color(COR_TEXTO_MEDIO));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        centralizar(g, "JOGO DO PORTFOLIO", guarda, guarda.y + guarda.height / 2 + 54);
    }

    private static void desenharBonecoDaForca(Graphics2D g, Rectangle palco, JogoInfo jogo) {
        g.setColor(new Color(COR_PAINEL));
        g.fillRoundRect(palco.x, palco.y, palco.width, palco.height,
                ARREDONDAMENTO, ARREDONDAMENTO);
        g.setColor(new Color(COR_BORDA_CARD));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(palco.x, palco.y, palco.width, palco.height,
                ARREDONDAMENTO, ARREDONDAMENTO);

        int xPoste = palco.x + palco.width * 26 / 100;
        int topoY = palco.y + palco.height * 8 / 100;
        int baseY = palco.y + palco.height * 90 / 100;
        int grossura = Math.max(6, palco.width / 80);
        int viga = palco.width * 28 / 100;
        int corda = Math.max(4, grossura - 2);
        int queda = palco.height * 6 / 100;
        int cx = xPoste + viga - corda / 2;
        int cyCabeca = topoY + grossura + queda;
        int raio = palco.width * 5 / 100;

        g.setColor(new Color(COR_TEXTO_MEDIO));
        g.fillRect(xPoste, topoY, grossura, baseY - topoY);
        g.fillRect(xPoste, topoY, viga, grossura);
        g.fillRect(xPoste + viga - corda, topoY + grossura, corda, queda);

        g.setColor(new Color(COR_ACENTO));
        g.fillOval(cx - raio, cyCabeca, raio * 2, raio * 2);
        int ombroY = cyCabeca + raio * 2;
        int troncoFim = ombroY + palco.height * 24 / 100;
        g.fillRect(cx - grossura / 2, ombroY, grossura, troncoFim - ombroY);
        int braco = palco.width * 7 / 100;
        g.fillRect(cx - braco, ombroY + 16, braco, 6);
        g.fillRect(cx, ombroY + 16, braco, 6);
        g.fillRect(cx - braco, troncoFim, braco, 6);
        g.fillRect(cx, troncoFim, braco, 6);

        g.setColor(new Color(COR_TEXTO));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 34));
        centralizar(g, jogo.getNome(), palco, palco.y + palco.height * 88 / 100);
    }

    private static void desenharTitulo(Graphics2D g, Rectangle area) {
        g.setColor(new Color(COR_TEXTO));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        g.drawString("PORTFOLIO DE JOGOS", area.x + 36, area.y + 64);
    }

    private static void desenharDescricao(Graphics2D g, Rectangle area, JogoInfo jogo) {
        Rectangle painel = LayoutLauncher.descricao(area);
        g.setColor(new Color(COR_PAINEL));
        g.fillRoundRect(painel.x, painel.y, painel.width, painel.height,
                ARREDONDAMENTO, ARREDONDAMENTO);
        g.setColor(new Color(0x66, 0x73, 0x80, 60));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(painel.x, painel.y, painel.width, painel.height,
                ARREDONDAMENTO, ARREDONDAMENTO);

        int x = painel.x + 24;
        int yNome = painel.y + 36;
        g.setColor(new Color(COR_TEXTO));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        g.drawString(jogo.getNome(), x, yNome);
        int tgX = x + largura(g, jogo.getNome()) + 14;

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        String tag = jogo.getTag();
        g.setColor(new Color(COR_ACENTO));
        g.fillRoundRect(tgX, yNome - 16, largura(g, tag) + 18, 22, 11, 11);
        g.setColor(new Color(COR_TEXTO));
        g.drawString(tag, tgX + 9, yNome);

        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        g.setColor(new Color(COR_TEXTO_MEDIO));
        int y = painel.y + 74;
        for (String linha : quebrar(g, jogo.getDescricao(), painel.width - 48)) {
            g.drawString(linha, x, y);
            y += 20;
        }

        y += 12;
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        g.drawString("Controles", x, y);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        y += 22;
        for (String linha : quebrar(g, jogo.getControles(), painel.width - 48)) {
            g.drawString(linha, x, y);
            y += 18;
        }
    }

    private static void desenharCard(Graphics2D g, Rectangle area, int indice,
                                     BufferedImage arte, EstadoLauncher estado) {
        Rectangle card = LayoutLauncher.cardDoIndice(area, indice);
        JogoInfo jogo = CatalogoDeJogos.lista().get(indice);
        boolean selecionado = indice == estado.getIndiceSelecionado();
        boolean hover = ehHoverDesteCard(indice, estado.getHover());
        boolean disponivel = estado.disponivel(jogo);

        int corFundo = !disponivel ? COR_CARD_DESABILITADO
                : hover ? COR_CARD_HOVER : COR_CARD;
        g.setColor(new Color(corFundo));
        g.fillRoundRect(card.x, card.y, card.width, card.height,
                ARREDONDAMENTO, ARREDONDAMENTO);

        if (selecionado && disponivel) {
            g.setColor(new Color(COR_ACENTO));
            g.setStroke(new BasicStroke(3f));
            g.drawRoundRect(card.x + 1, card.y + 1, card.width - 2, card.height - 2,
                    ARREDONDAMENTO, ARREDONDAMENTO);
        }

        Rectangle icone = LayoutLauncher.iconeDoCard(card);
        desenharIcone(g, icone, jogo, arte);

        int tx = icone.x + icone.width + 14;
        g.setColor(new Color(disponivel ? COR_TEXTO : COR_TEXTO_MEDIO));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        g.drawString(jogo.getNome(), tx, card.y + 34);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        g.drawString(jogo.getTag(), tx, card.y + 54);
    }

    private static boolean ehHoverDesteCard(int indice, Alvo hover) {
        switch (indice) {
            case 0:
                return hover == Alvo.CARD_0;
            case 1:
                return hover == Alvo.CARD_1;
            case 2:
                return hover == Alvo.CARD_2;
            default:
                return hover == Alvo.CARD_3;
        }
    }

    private static void desenharIcone(Graphics2D g, Rectangle icone, JogoInfo jogo,
                                      BufferedImage arte) {
        if (jogo.temArte() && arte != null) {
            g.drawImage(arte, icone.x, icone.y, icone.width, icone.height, null);
            return;
        }
        g.setColor(new Color(COR_CARD_HOVER));
        g.fillRoundRect(icone.x, icone.y, icone.width, icone.height, 10, 10);
        g.setColor(new Color(COR_ACENTO));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(icone.x, icone.y, icone.width, icone.height, 10, 10);
        desenharBonequinho(g, icone);
    }

    private static void desenharBonequinho(Graphics2D g, Rectangle icone) {
        g.setColor(new Color(COR_TEXTO_MEDIO));
        g.fillRect(icone.x + 13, icone.y + 6, 3, 30);
        g.fillRect(icone.x + 13, icone.y + 6, 18, 3);
        g.fillRect(icone.x + 27, icone.y + 9, 3, 8);
        g.setColor(new Color(COR_ACENTO));
        g.fillOval(icone.x + 21, icone.y + 17, 10, 10);
        g.fillRect(icone.x + 24, icone.y + 27, 4, 14);
        g.fillRect(icone.x + 20, icone.y + 39, 6, 3);
        g.fillRect(icone.x + 28, icone.y + 39, 6, 3);
    }

    private static void desenharJogar(Graphics2D g, Rectangle area, EstadoLauncher estado) {
        Rectangle botao = LayoutLauncher.jogar(area);
        boolean disponivel = estado.disponivel(estado.jogoSelecionado());
        boolean hover = estado.getHover() == Alvo.JOGAR && disponivel;
        int cor = !disponivel ? COR_JOGAR_DESABILITADO
                : hover ? COR_JOGAR_HOVER : COR_JOGAR;
        g.setColor(new Color(cor));
        g.fillRoundRect(botao.x, botao.y, botao.width, botao.height, 12, 12);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        g.setColor(new Color(COR_TEXTO));
        centralizar(g, "JOGAR", botao, botao.y + botao.height / 2 + 8);
    }

    private static void desenharMensagem(Graphics2D g, Rectangle area, EstadoLauncher estado) {
        String mensagem = estado.getMensagem();
        if (mensagem.isBlank()) {
            return;
        }
        int largura = 560;
        int altura = 38;
        int x = area.x + (area.width - largura) / 2;
        int y = area.y + 84;
        g.setColor(new Color(COR_TOAST));
        g.fillRoundRect(x, y, largura, altura, 19, 19);
        g.setColor(new Color(COR_TEXTO));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        centralizar(g, mensagem, new Rectangle(x, y, largura, altura), y + 26);
    }

    private static void desenharDica(Graphics2D g, Rectangle area) {
        g.setColor(new Color(COR_TEXTO_MEDIO));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        String dica = "← → escolhe  ·  Enter joga  ·  Esc sai";
        g.drawString(dica, area.x + area.width - 20 - largura(g, dica), area.y + 64);
    }

    private static void centralizar(Graphics2D g, String texto, Rectangle box, int y) {
        g.drawString(texto, box.x + (box.width - largura(g, texto)) / 2, y);
    }

    private static int largura(Graphics2D g, String texto) {
        FontMetrics metricas = g.getFontMetrics();
        return metricas.stringWidth(texto);
    }

    private static List<String> quebrar(Graphics2D g, String texto, int larguraMaxima) {
        List<String> linhas = new ArrayList<>();
        if (texto.isEmpty()) {
            return linhas;
        }
        String[] palavras = texto.trim().split("\\s+");
        StringBuilder atual = new StringBuilder();
        for (String palavra : palavras) {
            String candidata = atual.length() == 0 ? palavra : atual + " " + palavra;
            if (largura(g, candidata) <= larguraMaxima || atual.length() == 0) {
                atual = new StringBuilder(candidata);
            } else {
                linhas.add(atual.toString());
                atual = new StringBuilder(palavra);
            }
        }
        if (atual.length() > 0) {
            linhas.add(atual.toString());
        }
        return linhas;
    }
}