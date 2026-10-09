package com.portfolio.launcher.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;

import org.junit.Test;

public class LayoutLauncherTest {

    @Test
    public void areaBaseTemAResolucaoFixaDaJanela() {
        Rectangle base = LayoutLauncher.areaBase();
        assertEquals(new Rectangle(0, 0, 1100, 640), base);
    }

    @Test
    public void cardsOcupamAFaixaDeBaixoEmLinha() {
        Rectangle area = LayoutLauncher.areaBase();
        Rectangle primeiro = LayoutLauncher.cardDoIndice(area, 0);
        Rectangle ultimo = null;
        for (int i = 0; i < 4; i++) {
            Rectangle card = LayoutLauncher.cardDoIndice(area, i);
            assertEquals(primeiro.y, card.y);
            assertEquals(primeiro.height, card.height);
            assertEquals(20 + i * 208, card.x);
            assertEquals(184, card.width);
            ultimo = card;
        }
        assertEquals(535, ultimo.y);
        assertEquals(609, ultimo.y + ultimo.height);
        assertTrue(ultimo.y + ultimo.height < area.height);
    }

    @Test
    public void cardDoIndiceForaDoIntervaloRejeita() {
        Rectangle area = LayoutLauncher.areaBase();
        assertThrows(IllegalArgumentException.class, () -> LayoutLauncher.cardDoIndice(area, -1));
        assertThrows(IllegalArgumentException.class, () -> LayoutLauncher.cardDoIndice(area, 4));
    }

    @Test
    public void iconeFicaDentroDoCardParaTodosOsCards() {
        Rectangle area = LayoutLauncher.areaBase();
        for (int i = 0; i < 4; i++) {
            Rectangle card = LayoutLauncher.cardDoIndice(area, i);
            Rectangle icone = LayoutLauncher.iconeDoCard(card);
            assertEquals(48, icone.width);
            assertEquals(48, icone.height);
            assertTrue(icone.x >= card.x && icone.y >= card.y);
            assertTrue(icone.x + icone.width <= card.x + card.width);
            assertTrue(icone.y + icone.height <= card.y + card.height);
        }
    }

    @Test
    public void jogarFicaNaMesmaFaixaDosCardsADireita() {
        Rectangle area = LayoutLauncher.areaBase();
        Rectangle jogar = LayoutLauncher.jogar(area);
        Rectangle ultimoCard = LayoutLauncher.cardDoIndice(area, 3);
        assertEquals(ultimoCard.y, jogar.y);
        assertEquals(196, jogar.width);
        assertEquals(74, jogar.height);
        assertTrue(jogar.x > ultimoCard.x + ultimoCard.width);
        assertTrue(jogar.x + jogar.width <= area.width);
        assertTrue(jogar.y + jogar.height <= area.height);
    }

    @Test
    public void descricaoFicaNaEsquerdaDentroDoBanner() {
        Rectangle area = LayoutLauncher.areaBase();
        Rectangle descricao = LayoutLauncher.descricao(area);
        Rectangle banner = LayoutLauncher.areaDaArte(area);
        Rectangle primeiroCard = LayoutLauncher.cardDoIndice(area, 0);
        assertTrue(descricao.x >= area.x);
        assertTrue(descricao.y + descricao.height <= banner.y + banner.height);
        assertTrue(descricao.y + descricao.height <= primeiroCard.y);
    }

    @Test
    public void areaDaArteEhOBannerDoTopo() {
        Rectangle area = LayoutLauncher.areaBase();
        Rectangle banner = LayoutLauncher.areaDaArte(area);
        assertEquals(0, banner.x);
        assertEquals(0, banner.y);
        assertEquals(1100, banner.width);
        assertEquals(504, banner.height);
    }

    @Test
    public void areaDeGuardaFicaADireitaDaInfoDentroDoBanner() {
        Rectangle area = LayoutLauncher.areaBase();
        Rectangle guarda = LayoutLauncher.areaDeGuarda(area);
        Rectangle descricao = LayoutLauncher.descricao(area);
        Rectangle banner = LayoutLauncher.areaDaArte(area);
        assertTrue(guarda.x > descricao.x + descricao.width);
        assertTrue(guarda.y + guarda.height <= banner.y + banner.height);
        assertTrue(guarda.x + guarda.width <= area.width);
    }

    @Test
    public void cardsNaoSeSobrepoemAoJogar() {
        Rectangle area = LayoutLauncher.areaBase();
        Rectangle jogar = LayoutLauncher.jogar(area);
        for (int i = 0; i < 4; i++) {
            Rectangle card = LayoutLauncher.cardDoIndice(area, i);
            assertFalse("card " + i + " sobrepoe o JOGAR", card.intersects(jogar));
        }
    }

    @Test
    public void alvoEmEncontraQualquerCardPeloCentro() {
        Rectangle area = LayoutLauncher.areaBase();
        for (int i = 0; i < 4; i++) {
            Rectangle card = LayoutLauncher.cardDoIndice(area, i);
            assertEquals(alvoDoCard(i),
                    LayoutLauncher.alvoEm(area, card.x + 100, card.y + 30));
        }
    }

    @Test
    public void alvoEmEncontraOJogarPeloCentro() {
        Rectangle area = LayoutLauncher.areaBase();
        Rectangle jogar = LayoutLauncher.jogar(area);
        assertEquals(Alvo.JOGAR,
                LayoutLauncher.alvoEm(area, jogar.x + 100, jogar.y + 30));
    }

    @Test
    public void alvoEmNoBannerDaNenhum() {
        Rectangle area = LayoutLauncher.areaBase();
        assertEquals(Alvo.NENHUM, LayoutLauncher.alvoEm(area, 300, 200));
        assertEquals(Alvo.NENHUM, LayoutLauncher.alvoEm(area, 50, 30));
    }

    @Test
    public void alvoEmForaDaJanelaDaNenhum() {
        Rectangle area = LayoutLauncher.areaBase();
        assertEquals(Alvo.NENHUM, LayoutLauncher.alvoEm(area, -1, 5));
        assertEquals(Alvo.NENHUM, LayoutLauncher.alvoEm(area, 1100, 5));
        assertEquals(Alvo.NENHUM, LayoutLauncher.alvoEm(area, 5, 640));
    }

    @Test
    public void jogarNaoInterceptaDescricao() {
        Rectangle area = LayoutLauncher.areaBase();
        Rectangle descricao = LayoutLauncher.descricao(area);
        Rectangle jogar = LayoutLauncher.jogar(area);
        assertFalse(descricao.intersects(jogar));
    }

    private Alvo alvoDoCard(int indice) {
        switch (indice) {
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