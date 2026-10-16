package com.portfolio.launcher.ui;

import java.awt.Rectangle;

public final class LayoutLauncher {

    public static final int LARGURA_BASE = 1100;
    public static final int ALTURA_BASE = 640;

    private static final int MARGEM = 20;
    private static final int ALTURA_FAIXA = 150;
    private static final int CARD_LARGURA = 200;
    private static final int CARD_ALTURA = 88;
    private static final int ENTRE_CARDS = 8;
    private static final int LARGURA_JOGAR = 220;
    private static final int ALTURA_JOGAR = 104;
    private static final int LARGURA_INFO = 300;

    private LayoutLauncher() {
    }

    public static Rectangle areaBase() {
        return new Rectangle(0, 0, LARGURA_BASE, ALTURA_BASE);
    }

    public static Rectangle areaDaArte(Rectangle area) {
        return new Rectangle(area.x, area.y, area.width, area.height - ALTURA_FAIXA);
    }

    private static int yDaLinhaDeBaixo(Rectangle area, int alturaDoElemento) {
        return area.y + (area.height - ALTURA_FAIXA) + (ALTURA_FAIXA - alturaDoElemento) / 2;
    }

    public static Rectangle cardDoIndice(Rectangle area, int indice) {
        if (indice < 0 || indice >= 4) {
            throw new IllegalArgumentException("indice de card invalido: " + indice);
        }
        int x = area.x + MARGEM + indice * (CARD_LARGURA + ENTRE_CARDS);
        return new Rectangle(x, yDaLinhaDeBaixo(area, CARD_ALTURA), CARD_LARGURA, CARD_ALTURA);
    }

    public static Rectangle iconeDoCard(Rectangle card) {
        int lado = 48;
        int x = card.x + 14;
        int y = card.y + (card.height - lado) / 2;
        return new Rectangle(x, y, lado, lado);
    }

    public static Rectangle jogar(Rectangle area) {
        int x = area.x + area.width - MARGEM - LARGURA_JOGAR;
        return new Rectangle(x, yDaLinhaDeBaixo(area, ALTURA_JOGAR), LARGURA_JOGAR, ALTURA_JOGAR);
    }

    public static Rectangle descricao(Rectangle area) {
        Rectangle banner = areaDaArte(area);
        return new Rectangle(area.x + MARGEM, area.y + 48, LARGURA_INFO, banner.height - 68);
    }

    public static Rectangle areaDeGuarda(Rectangle area) {
        Rectangle banner = areaDaArte(area);
        int x = area.x + MARGEM + LARGURA_INFO + 20;
        int y = area.y + 48;
        int largura = area.x + area.width - MARGEM - x;
        return new Rectangle(x, y, largura, banner.height - 68);
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