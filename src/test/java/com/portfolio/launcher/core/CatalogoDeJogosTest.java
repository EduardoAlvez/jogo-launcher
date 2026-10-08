package com.portfolio.launcher.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class CatalogoDeJogosTest {

    @Test
    public void temOsQuatroJogosNaOrdemDoPortfolio() {
        List<JogoInfo> jogos = CatalogoDeJogos.lista();
        assertEquals(4, jogos.size());
        assertEquals("pong", jogos.get(0).getId());
        assertEquals("forca", jogos.get(1).getId());
        assertEquals("snake", jogos.get(2).getId());
        assertEquals("campominado", jogos.get(3).getId());
    }

    @Test
    public void idsSaoUnicos() {
        Set<String> ids = new HashSet<>();
        for (JogoInfo jogo : CatalogoDeJogos.lista()) {
            assertTrue(ids.add(jogo.getId()));
        }
        assertEquals(4, ids.size());
    }

    @Test
    public void cadaJogoTemNomeETagPreenchidos() {
        for (JogoInfo jogo : CatalogoDeJogos.lista()) {
            assertTrue(jogo.getNome() + " sem nome", !jogo.getNome().isBlank());
            assertTrue(jogo.getNome() + " sem tag", !jogo.getTag().isBlank());
        }
    }

    @Test
    public void cadaJogoTemDescricaoEListaDeControles() {
        for (JogoInfo jogo : CatalogoDeJogos.lista()) {
            assertTrue(jogo.getNome() + " sem descricao", !jogo.getDescricao().isBlank());
            assertTrue(jogo.getNome() + " sem controles", !jogo.getControles().isBlank());
        }
    }

    @Test
    public void pongSnakeECampoMinadoTemArteMasAForcaNao() {
        assertTrue(CatalogoDeJogos.porId("pong").get().temArte());
        assertTrue(CatalogoDeJogos.porId("snake").get().temArte());
        assertTrue(CatalogoDeJogos.porId("campominado").get().temArte());
        assertFalse(CatalogoDeJogos.porId("forca").get().temArte());
    }

    @Test
    public void porIdConhecidoDevolveOJogoCerto() {
        assertEquals("Snake", CatalogoDeJogos.porId("snake").get().getNome());
        assertEquals("Pong", CatalogoDeJogos.porId("pong").get().getNome());
        assertEquals("Jogo da Forca", CatalogoDeJogos.porId("forca").get().getNome());
        assertEquals("Campo Minado", CatalogoDeJogos.porId("campominado").get().getNome());
    }

    @Test
    public void porIdDesconhecidoDevolveVazio() {
        assertTrue(CatalogoDeJogos.porId("minecraft").isEmpty());
        assertTrue(CatalogoDeJogos.porId("").isEmpty());
    }

    @Test
    public void quantidadeBateComALista() {
        assertEquals(CatalogoDeJogos.lista().size(), CatalogoDeJogos.quantidade());
    }
}