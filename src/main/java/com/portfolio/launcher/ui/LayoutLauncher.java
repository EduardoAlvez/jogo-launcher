package com.portfolio.launcher.ui;

import java.awt.Rectangle;

public final class LayoutLauncher {

    public static final int LARGURA_BASE = 1100;
    public static final int ALTURA_BASE = 640;

    private static final int MARGEM_LATERAL = 20;
    private static final int LARGURA_RAIL = 280;
    private static final int CARD_LARGURA = 260;
    private static final int CARD_ALTURA = 76;
    private static final int ENTRE_CARDS = 16;
    private static final int CARDS_TOP = 90;
    private static final int JOGAR_ALTURA = 60;
    private static final int JOGAR_TOP = 470;

    private LayoutLauncher() {
    }

    public static Rectangle areaBase() {
        return new Rectangle(0, 0, LARGURA_BASE, ALTURA_BASE);
    }

    public static Rectangle areaDaArte(Rectangle area) {
        return new Rectangle(area.x, area.y, area.width - LARGURA_RAIL, area.height);
    }

    private static int xDaColuna(Rectangle area) {
        return area.x + area.width - MARGEM_LATERAL - CARD_LARGURA;
    }

    public static Rectangle cardDoIndice(Rectangle area, int indice) {
        if (indice < 0 || indice >= 4) {
            throw new IllegalArgumentException("indice de card invalido: " + indice);
        }
        int x = xDaColuna(area);
        int y = area.y + CARDS_TOP + indice * (CARD_ALTURA + ENTRE_CARDS);
        return new Rectangle(x, y, CARD_LARGURA, CARD_ALTURA);
    }

    public static Rectangle iconeDoCard(Rectangle card) {
        int lado = 48;
        int x = card.x + 14;
        int y = card.y + (card.height - lado) / 2;
        return new Rectangle(x, y, lado, lado);
    }

    public static Rectangle jogar(Rectangle area) {
        int x = xDaColuna(area);
        return new Rectangle(x, area.y + JOGAR_TOP, CARD_LARGURA, JOGAR_ALTURA);
    }

    public static Rectangle descricao(Rectangle area) {
        int x = area.x + 36;
        int y = area.y + 468;
        int largura = jogar(area).x - MARGEM_LATERAL - x;
        return new Rectangle(x, y, largura, 150);
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