package com.portfolio.launcher.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JogoInfoTest {

    @Test
    public void recusaIdEmBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new JogoInfo("  ", "Nome", "tag", "desc", "ctrl", true));
    }

    @Test
    public void recusaNomeEmBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new JogoInfo("id", null, "tag", "desc", "ctrl", true));
    }

    @Test
    public void aceitaCamposOpcionaisVazios() {
        JogoInfo jogo = new JogoInfo("id", "Nome", "", "", "", true);
        assertEquals("", jogo.getTag());
        assertEquals("", jogo.getDescricao());
        assertEquals("", jogo.getControles());
    }

    @Test
    public void gettersDevolvemOsValoresPassados() {
        JogoInfo jogo = new JogoInfo("snake", "Snake", "Nostalgia",
                "A cobra na grade.", "Setas / WASD", true);
        assertEquals("snake", jogo.getId());
        assertEquals("Snake", jogo.getNome());
        assertEquals("Nostalgia", jogo.getTag());
        assertEquals("A cobra na grade.", jogo.getDescricao());
        assertEquals("Setas / WASD", jogo.getControles());
        assertTrue(jogo.temArte());
        assertFalse(new JogoInfo("forca", "Forca", "", "", "", false).temArte());
    }

    @Test
    public void doisJogosComMesmosDadosNaoCompartilhamEstado() {
        JogoInfo original = new JogoInfo("id", "Nome", "tag", "desc", "ctrl", true);
        JogoInfo copia = new JogoInfo("id", "Nome", "tag", "desc", "ctrl", true);
        assertEquals(original.getNome(), copia.getNome());
        assertEquals(original.getId(), copia.getId());
    }
}