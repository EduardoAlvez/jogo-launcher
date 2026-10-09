package com.portfolio.launcher.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.portfolio.launcher.core.JogoInfo;
import com.portfolio.launcher.core.Lancador;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TelaLauncherTest {

    @Rule
    public TemporaryFolder pasta = new TemporaryFolder();

    private LancadorFake fake;

    @Test
    public void cliqueNoCardSelecionaOJogo() throws IOException {
        TelaLauncher tela = novaTela();
        Rectangle card = LayoutLauncher.cardDoIndice(LayoutLauncher.areaBase(), 1);
        tela.processarClique(card.x + 100, card.y + 30);
        assertEquals("forca", tela.estado().jogoSelecionado().getId());
        assertEquals("", tela.estado().getMensagem());
    }

    @Test
    public void setasMudamASelecaoComVoltaPelosExtremos() {
        TelaLauncher tela = novaTelaSemPasta();
        tela.estado().setIndiceSelecionado(3);
        tela.tratarTecla(KeyEvent.VK_UP);
        assertEquals(2, tela.estado().getIndiceSelecionado());
        tela.estado().setIndiceSelecionado(0);
        tela.tratarTecla(KeyEvent.VK_DOWN);
        assertEquals(1, tela.estado().getIndiceSelecionado());
        tela.tratarTecla(KeyEvent.VK_S);
        assertEquals(2, tela.estado().getIndiceSelecionado());
        tela.tratarTecla(KeyEvent.VK_W);
        assertEquals(1, tela.estado().getIndiceSelecionado());
        tela.estado().setIndiceSelecionado(0);
        tela.tratarTecla(KeyEvent.VK_UP);
        assertEquals(3, tela.estado().getIndiceSelecionado());
        tela.tratarTecla(KeyEvent.VK_DOWN);
        assertEquals(0, tela.estado().getIndiceSelecionado());
    }

    @Test
    public void setasLateraisTambemMudamASelecao() {
        TelaLauncher tela = novaTelaSemPasta();
        tela.estado().setIndiceSelecionado(0);
        tela.tratarTecla(KeyEvent.VK_RIGHT);
        assertEquals(1, tela.estado().getIndiceSelecionado());
        tela.tratarTecla(KeyEvent.VK_D);
        assertEquals(2, tela.estado().getIndiceSelecionado());
        tela.tratarTecla(KeyEvent.VK_LEFT);
        assertEquals(1, tela.estado().getIndiceSelecionado());
        tela.tratarTecla(KeyEvent.VK_A);
        assertEquals(0, tela.estado().getIndiceSelecionado());
        tela.tratarTecla(KeyEvent.VK_LEFT);
        assertEquals(3, tela.estado().getIndiceSelecionado());
    }

    @Test
    public void enterJogaOSelecionadoQuandoEstaDisponivel() throws IOException {
        TelaLauncher tela = novaTela();
        assertTrue(tela.tratarTecla(KeyEvent.VK_ENTER));
        assertEquals(List.of("pong"), fake.lancados);
        assertTrue(tela.estado().getMensagem().startsWith("Abrindo"));
    }

    @Test
    public void espacoTambemJoga() throws IOException {
        TelaLauncher tela = novaTela();
        tela.tratarTecla(KeyEvent.VK_SPACE);
        assertEquals(List.of("pong"), fake.lancados);
    }

    @Test
    public void cliqueNoJogarLancaOSelecionado() throws IOException {
        TelaLauncher tela = novaTela();
        Rectangle jogar = LayoutLauncher.jogar(LayoutLauncher.areaBase());
        tela.processarClique(jogar.x + 100, jogar.y + 30);
        assertEquals(List.of("pong"), fake.lancados);
    }

    @Test
    public void jogarComOJogoSemJarNaoLancaEAvisa() {
        TelaLauncher tela = novaTelaSemPasta();
        tela.estado().setIndiceSelecionado(2);
        tela.tratarTecla(KeyEvent.VK_ENTER);
        assertTrue(fake.lancados.isEmpty());
        assertTrue(tela.estado().getMensagem().contains("Instale"));
        assertFalse(tela.jogar());
    }

    @Test
    public void teclaDesconhecidaNaoEhConsumida() {
        TelaLauncher tela = novaTelaSemPasta();
        assertFalse(tela.tratarTecla(KeyEvent.VK_X));
    }

    @Test
    public void escDisparaAAcaoDeSaida() {
        TelaLauncher tela = novaTelaSemPasta();
        boolean[] saiu = {false};
        tela.acaoDeSaida = () -> saiu[0] = true;
        assertTrue(tela.tratarTecla(KeyEvent.VK_ESCAPE));
        assertTrue(saiu[0]);
    }

    @Test
    public void movimentoDoMouseAtualizaOHoverNoModelo() {
        TelaLauncher tela = novaTelaSemPasta();
        Rectangle card = LayoutLauncher.cardDoIndice(LayoutLauncher.areaBase(), 2);
        tela.processarMovimento(card.x + 100, card.y + 30);
        assertEquals(Alvo.CARD_2, tela.estado().getHover());
        Rectangle jogar = LayoutLauncher.jogar(LayoutLauncher.areaBase());
        tela.processarMovimento(jogar.x + 100, jogar.y + 30);
        assertEquals(Alvo.JOGAR, tela.estado().getHover());
        tela.processarMovimento(40, 200);
        assertEquals(Alvo.NENHUM, tela.estado().getHover());
    }

    @Test
    public void cliqueForaDeTudoNaoAlteraASelecao() {
        TelaLauncher tela = novaTelaSemPasta();
        tela.processarClique(40, 200);
        assertEquals(0, tela.estado().getIndiceSelecionado());
    }

    private TelaLauncher novaTela() throws IOException {
        Path raiz = pasta.getRoot().toPath();
        Files.createDirectories(raiz.resolve("jogos"));
        Files.write(raiz.resolve("jogos").resolve("pong.jar"), new byte[]{1});
        fake = new LancadorFake();
        return new TelaLauncher(fake, raiz, Map.of());
    }

    private TelaLauncher novaTelaSemPasta() {
        fake = new LancadorFake();
        return new TelaLauncher(fake, pasta.getRoot().toPath(), Map.of());
    }

    private static final class LancadorFake implements Lancador {
        final List<String> lancados = new ArrayList<>();

        @Override
        public boolean lancar(JogoInfo jogo, Path raiz) {
            lancados.add(jogo.getId());
            return true;
        }
    }
}