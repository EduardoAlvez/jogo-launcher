package com.portfolio.launcher.ui;

import java.awt.Rectangle;

public final class LayoutLauncher {

    public static final int LARGURA_BASE = 1100;
    public static final int ALTURA_BASE = 640;

    private static final int MARGEM = 20;
    private static final int ALTURA_FAIXA = 160;
    private static final int CARD_LARGURA = 196;
    private static final int CARD_ALTURA = 76;
    private static final int ENTRE_CARDS = 16;
    private static final int LARGURA_JOGAR = 196;
    private static final int ALTURA_JOGAR = 76;
    private static final int LARGURA_INFO = 460;

    private LayoutLauncher() {
    }

    public static Rectangle areaBase() {
        return new Rectangle(0, 0, LARGURA_BASE, ALTURA_BASE);
    }

    public static Rectangle areaDaArte(Rectangle area) {
        return new Rectangle(area.x, area.y, area.width, area.height - ALTURA_FAIXA);
    }

    public static Rectangle cardDoIndice(Rectangle area, int indice) {
        if (indice < 0 || indice >= 4) {
            throw new IllegalArgumentException("indice de card invalido: " + indice);
        }
        int x = area.x + MARGEM + indice * (CARD_LARGURA + ENTRE_CARDS);
        int y = area.y + (area.height - ALTURA_FAIXA) + (ALTURA_FAIXA - CARD_ALTURA) / 2;
        return new Rectangle(x, y, CARD_LARGURA, CARD_ALTURA);
    }

    public static Rectangle iconeDoCard(Rectangle card) {
        int lado = 48;
        int x = card.x + 14;
        int y = card.y + (card.height - lado) / 2;
        return new Rectangle(x, y, lado, lado);
    }

    public static Rectangle jogar(Rectangle area) {
        int x = area.x + area.width - MARGEM - LARGURA_JOGAR;
        int y = area.y + (area.height - ALTURA_FAIXA) + (ALTURA_FAIXA - ALTURA_JOGAR) / 2;
        return new Rectangle(x, y, LARGURA_JOGAR, ALTURA_JOGAR);
    }

    public static Rectangle descricao(Rectangle area) {
        int x = area.x + MARGEM;
        int y = area.y + 96;
        return new Rectangle(x, y, 460, 360);
    }

    public static Rectangle areaDeGuarda(Rectangle area) {
        Rectangle banner = areaDaArte(area);
        int x = area.x + MARGEM + 460 + 40;
        int y = area.y + 40;
        int largura = area.x + area.width - MARGEM - x;
        return new Rectangle(x, y, largura, banner.height - 80);
    }

    public static Alvo alvoEm(Rectangle area, int x, int y) {
        if (!area.contains(x, y)) {
            return Alvo.NENHUM;
        }
        if (jogar(area).contains(x, y)) {
            return Alvo.JOGAR;
        }
        for (int i = 3; i >= 0; i--) {
            if (cardDoIndice(area, i).contains(x, y)) {
                switch (i) {
                    case 0:
                        return Alvo.CARD_0;
                    case 1:
                        return Alvo.CARD_1;
                    case 2:
                        return Alvo.CARD_2;
                    default:
                        return Alvo.CARD_3;
                }
            }
        }
        return Alvo.NENHUM;
    }
}