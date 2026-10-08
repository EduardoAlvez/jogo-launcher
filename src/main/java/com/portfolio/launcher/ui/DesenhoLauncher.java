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

    private static final int ARREDONDAMENTO = 14;

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
        Rectangle espaco = LayoutLauncher.areaDaArte(area);
        if (jogo.temArte() && arte != null) {
            int lado = Math.min(espaco.width, espaco.height);
            int x = espaco.x + (espaco.width - lado) / 2;
            g.drawImage(arte, x, espaco.y, lado, espaco.height, null);
            desenharEscurecimento(g, area, espaco, lado);
            return;
        }
        desenharArteDeGuarda(g, espaco, jogo);
    }

    private static void desenharEscurecimento(Graphics2D g, Rectangle area,
                                              Rectangle espaco, int ladoArte) {
        Color transparente = new Color(COR_FUNDO_INICIO << 8 | 0x00, true);
        Color escuroDir = new Color(COR_FUNDO_INICIO << 8 | 0xEB, true);
        g.setPaint(new java.awt.GradientPaint(
                espaco.x + espaco.width - 280, espaco.y, transparente,
                espaco.x + espaco.width, espaco.y, escuroDir));
        Rectangle direita = new Rectangle(espaco.x + espaco.width - 280, espaco.y, 280, espaco.height);
        g.fill(direita);
        g.setPaint(new java.awt.GradientPaint(
                espaco.x, espaco.y + espaco.height - 170, transparente,
                espaco.x, espaco.y + espaco.height, escuroDir));
        Rectangle baixo = new Rectangle(espaco.x, espaco.y + espaco.height - 170,
                espaco.width, 170);
        g.fill(baixo);
    }

    private static void desenharArteDeGuarda(Graphics2D g, Rectangle espaco, JogoInfo jogo) {
        if (!jogo.temArte()) {
            desenharBonecoDaForca(g, espaco, jogo);
            return;
        }
        Rectangle cartaz = new Rectangle(espaco.x + 110, espaco.y + 110, 560, 420);
        g.setColor(new Color(COR_PAINEL));
        g.fillRoundRect(cartaz.x, cartaz.y, cartaz.width, cartaz.height, ARREDONDAMENTO, ARREDONDAMENTO);
        g.setColor(new Color(COR_BORDA_CARD));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(cartaz.x, cartaz.y, cartaz.width, cartaz.height, ARREDONDAMENTO, ARREDONDAMENTO);
        g.setColor(new Color(COR_TEXTO));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 56));
        centralizar(g, jogo.getNome(), cartaz, cartaz.y + 200);
        g.setColor(new Color(COR_TEXTO_MEDIO));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        g.drawString("JOGO DO PORTFOLIO",
                cartaz.x + (cartaz.width - largura(g, "JOGO DO PORTFOLIO")) / 2,
                cartaz.y + 260);
    }

    private static void desenharBonecoDaForca(Graphics2D g, Rectangle espaco, JogoInfo jogo) {
        Rectangle palco = new Rectangle(espaco.x + 110, espaco.y + 60, 600, 500);
        g.setColor(new Color(COR_PAINEL));
        g.fillRoundRect(palco.x, palco.y, palco.width, palco.height,
                ARREDONDAMENTO, ARREDONDAMENTO);
        g.setColor(new Color(COR_BORDA_CARD));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(palco.x, palco.y, palco.width, palco.height,
                ARREDONDAMENTO, ARREDONDAMENTO);

        int xPoste = palco.x + 150;
        int yBase = palco.y + 460;
        g.setColor(new Color(COR_TEXTO_MEDIO));
        g.fillRect(xPoste, palco.y + 40, 8, yBase - (palco.y + 40));
        g.fillRect(xPoste, palco.y + 40, 160, 10);
        g.fillRect(xPoste + 150, palco.y + 50, 8, 30);

        int hx = xPoste + 156;
        g.setColor(new Color(COR_ACENTO));
        g.fillOval(hx - 13, palco.y + 80, 26, 26);
        g.fillRect(hx - 3, palco.y + 106, 6, 96);
        g.fillRect(hx - 40, palco.y + 122, 34, 6);
        g.fillRect(hx + 6, palco.y + 122, 34, 6);
        g.fillRect(hx - 30, palco.y + 296, 30, 6);
        g.fillRect(hx, palco.y + 296, 30, 6);

        g.setColor(new Color(COR_TEXTO));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
        String nome = jogo.getNome();
        g.drawString(nome, palco.x + (palco.width - largura(g, nome)) / 2, palco.y + 400);
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
        int yNome = painel.y + 34;
        g.setColor(new Color(COR_TEXTO));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
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
        int y = painel.y + 64;
        for (String linha : quebrar(g, jogo.getDescricao(), painel.width - 48)) {
            g.drawString(linha, x, y);
            y += 20;
        }

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        g.setColor(new Color(COR_TEXTO_MEDIO));
        g.drawString("Controles", x, painel.y + 118);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        int yCtrl = painel.y + 140;
        for (String linha : quebrar(g, jogo.getControles(), painel.width - 48)) {
            g.drawString(linha, x, yCtrl);
            yCtrl += 18;
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

        int tx = icone.x + icone.width + 16;
        g.setColor(new Color(disponivel ? COR_TEXTO : COR_TEXTO_MEDIO));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        g.drawString(jogo.getNome(), tx, card.y + 32);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        g.drawString(jogo.getTag(), tx, card.y + 52);

        if (!disponivel) {
            g.setColor(new Color(COR_TEXTO_MEDIO));
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            String aviso = "nao instalado";
            g.drawString(aviso, card.x + card.width - largura(g, aviso) - 12, card.y + 32);
        }
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
        String rotulo = "JOGAR";
        g.setColor(new Color(COR_TEXTO));
        centralizar(g, rotulo, botao, botao.y + 38);
    }

    private static void desenharDica(Graphics2D g, Rectangle area) {
        g.setColor(new Color(COR_TEXTO_MEDIO));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        g.drawString("↑↓ escolhe o jogo  ·  Enter joga  ·  Esc sai",
                area.x + 36, area.y + area.height - 14);
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