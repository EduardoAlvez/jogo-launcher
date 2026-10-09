package com.portfolio.launcher.ui;

import com.portfolio.launcher.core.CatalogoDeJogos;
import com.portfolio.launcher.core.DisponibilidadeDeJogos;
import com.portfolio.launcher.core.JogoInfo;
import com.portfolio.launcher.core.Lancador;
import com.portfolio.launcher.core.LancadorProcesso;
import com.portfolio.launcher.recursos.CarregadorDeArtes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public final class TelaLauncher extends JPanel {

    private static final int TEMPO_DA_MENSAGEM_MS = 2200;
    private static final int INTERVALO_DA_ANIMACAO_MS = 40;

    private final EstadoLauncher estado;
    private final Lancador lancador;
    private final Path raiz;
    private final Map<String, BufferedImage> artes;
    private JFrame janela;
    private Timer timerDaMensagem;
    private Timer timerDaAnimacao;
    Runnable acaoDeSaida;

    public TelaLauncher(Lancador lancador, Path raiz, Map<String, BufferedImage> artes) {
        this.lancador = lancador;
        this.raiz = raiz;
        this.artes = new HashMap<>(artes);
        this.estado = new EstadoLauncher(disponiveisDe(raiz));
        this.acaoDeSaida = () -> {
            pararTimer();
            System.exit(0);
        };
        setPreferredSize(new Dimension(LayoutLauncher.LARGURA_BASE, LayoutLauncher.ALTURA_BASE));
        configurarTeclado();
        configurarMouse();
    }

    public TelaLauncher() {
        this(new LancadorProcesso(), raizPadrao(), CarregadorDeArtes.carregar(raizPadrao()));
    }

    private static Path raizPadrao() {
        return Path.of(System.getProperty("user.dir"));
    }

    public void abrir() {
        janela = new JFrame("Portfolio de Jogos");
        janela.setContentPane(this);
        janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        janela.setResizable(false);
        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
        timerDaAnimacao = new Timer(INTERVALO_DA_ANIMACAO_MS, evento -> repaint());
        timerDaAnimacao.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLauncher().abrir());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DesenhoLauncher.desenhar((Graphics2D) g, LayoutLauncher.areaBase(), estado, artes,
                System.currentTimeMillis());
    }

    private static Set<String> disponiveisDe(Path raiz) {
        Set<String> disponiveis = new LinkedHashSet<>();
        for (JogoInfo jogo : CatalogoDeJogos.lista()) {
            if (DisponibilidadeDeJogos.disponivel(raiz, jogo)) {
                disponiveis.add(jogo.getId());
            }
        }
        return disponiveis;
    }

    private void configurarTeclado() {
        ligar(KeyEvent.VK_LEFT, () -> moverPara(-1));
        ligar(KeyEvent.VK_RIGHT, () -> moverPara(1));
        ligar(KeyEvent.VK_A, () -> moverPara(-1));
        ligar(KeyEvent.VK_D, () -> moverPara(1));
        ligar(KeyEvent.VK_UP, () -> moverPara(-1));
        ligar(KeyEvent.VK_DOWN, () -> moverPara(1));
        ligar(KeyEvent.VK_W, () -> moverPara(-1));
        ligar(KeyEvent.VK_S, () -> moverPara(1));
        ligar(KeyEvent.VK_ENTER, () -> jogar());
        ligar(KeyEvent.VK_SPACE, () -> jogar());
        ligar(KeyEvent.VK_ESCAPE, () -> sair());
    }

    private void ligar(int codigo, Runnable acao) {
        String nome = "acaoDeLauncher" + codigo;
        InputMap entrada = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap acoes = getActionMap();
        entrada.put(KeyStroke.getKeyStroke(codigo, 0), nome);
        acoes.put(nome, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                acao.run();
            }
        });
    }

    private void configurarMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent evento) {
                estado.setHover(Alvo.NENHUM);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent evento) {
                processarClique(evento.getX(), evento.getY());
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent evento) {
                processarMovimento(evento.getX(), evento.getY());
            }

            @Override
            public void mouseDragged(MouseEvent evento) {
                processarMovimento(evento.getX(), evento.getY());
            }
        });
    }

    void processarMovimento(int x, int y) {
        Alvo alvo = LayoutLauncher.alvoEm(LayoutLauncher.areaBase(), x, y);
        if (alvo == estado.getHover()) {
            return;
        }
        estado.setHover(alvo);
        repaint();
    }

    void processarClique(int x, int y) {
        Alvo alvo = LayoutLauncher.alvoEm(LayoutLauncher.areaBase(), x, y);
        if (alvo == Alvo.JOGAR) {
            jogar();
            return;
        }
        int indice = indiceDe(alvo);
        if (indice >= 0) {
            estado.setIndiceSelecionado(indice);
            limparMensagem();
            repaint();
        }
    }

    private void moverPara(int salto) {
        int total = CatalogoDeJogos.quantidade();
        int novo = (estado.getIndiceSelecionado() + salto + total) % total;
        estado.setIndiceSelecionado(novo);
        limparMensagem();
        repaint();
    }

    boolean jogar() {
        JogoInfo jogo = estado.jogoSelecionado();
        if (!estado.disponivel(jogo)) {
            estado.setMensagem("Instale " + jogo.getId() + ".jar em jogos/ para abrir "
                    + jogo.getNome());
            agendarLimpezaDaMensagem();
            repaint();
            return false;
        }
        boolean aberto = lancador.lancar(jogo, raiz);
        estado.setMensagem(aberto ? "Abrindo " + jogo.getNome() + "..." : "Falha ao abrir " + jogo.getNome());
        agendarLimpezaDaMensagem();
        repaint();
        return aberto;
    }

    public boolean tratarTecla(int codigo) {
        switch (codigo) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
                moverPara(-1);
                return true;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
                moverPara(1);
                return true;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                jogar();
                return true;
            case KeyEvent.VK_ESCAPE:
                sair();
                return true;
            default:
                return false;
        }
    }

    private void sair() {
        pararTimer();
        acaoDeSaida.run();
    }

    private void agendarLimpezaDaMensagem() {
        if (timerDaMensagem == null) {
            timerDaMensagem = new Timer(TEMPO_DA_MENSAGEM_MS, evento -> {
                limparMensagem();
                repaint();
            });
            timerDaMensagem.setRepeats(false);
        }
        timerDaMensagem.restart();
    }

    private void limparMensagem() {
        estado.setMensagem("");
        if (timerDaMensagem != null) {
            timerDaMensagem.stop();
        }
    }

    private void pararTimer() {
        if (timerDaMensagem != null) {
            timerDaMensagem.stop();
        }
        if (timerDaAnimacao != null) {
            timerDaAnimacao.stop();
        }
    }

    private static int indiceDe(Alvo alvo) {
        switch (alvo) {
            case CARD_0:
                return 0;
            case CARD_1:
                return 1;
            case CARD_2:
                return 2;
            case CARD_3:
                return 3;
            default:
                return -1;
        }
    }

    EstadoLauncher estado() {
        return estado;
    }

    Rectangle area() {
        return LayoutLauncher.areaBase();
    }
}