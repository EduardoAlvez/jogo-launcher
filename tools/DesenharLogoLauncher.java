import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 * Desenha o mestre {@code logo-256.png} do launcher de forma procedural.
 *
 * <p>O logo e um painel escuro arredondado (o fundo de uma "tela") com quatro
 * cards, um por jogo do portfolio, e um triangulo de play aceso. E gerado por
 * codigo para que o icone possa ser reproduzido e ajustado sem um editor de
 * imagem, e para que as cores venham da mesma paleta usada por
 * {@code DesenhoLauncher}.
 *
 * <p>Uso: {@code java tools/DesenharLogoLauncher.java [diretorio-de-recursos]}
 */
public class DesenharLogoLauncher {

    private static final int LADO = 256;
    private static final int MARGEM = 16;
    private static final int RAIO = 44;

    private static final Color FUNDO_INICIO = new Color(0x101318);
    private static final Color FUNDO_FIM = new Color(0x1A212A);
    private static final Color BORDA = new Color(0x333C46);
    private static final Color CARD = new Color(0x232A33);
    private static final Color CARD_DESTAQUE = new Color(0x2E3842);
    private static final Color ACENTO = new Color(0x4FA34F);
    private static final Color ACENTO_CLARO = new Color(0x62C46A);

    public static void main(String[] args) throws IOException {
        Path recursos = Path.of(args.length > 0 ? args[0] : "src/main/resources");
        BufferedImage imagem = desenhar();
        Path destino = recursos.resolve("logo-" + LADO + ".png");
        if (!ImageIO.write(imagem, "png", destino.toFile())) {
            throw new IOException("ImageIO nao tem writer de PNG");
        }
        System.out.println("Mestre: " + destino + " (" + LADO + "x" + LADO + ", "
                + Files.size(destino) + " bytes)");
    }

    private static BufferedImage desenhar() {
        BufferedImage imagem = new BufferedImage(LADO, LADO, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagem.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2.setPaint(new GradientPaint(0, MARGEM, FUNDO_INICIO, 0, LADO - MARGEM, FUNDO_FIM));
        g2.fillRoundRect(MARGEM, MARGEM, LADO - 2 * MARGEM, LADO - 2 * MARGEM, RAIO, RAIO);
        g2.setColor(BORDA);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(MARGEM, MARGEM, LADO - 2 * MARGEM, LADO - 2 * MARGEM, RAIO, RAIO);

        int card = 34;
        int vao = 14;
        int railX = MARGEM + 26;
        int total = 4 * card + 3 * vao;
        int railY = (LADO - total) / 2;
        for (int i = 0; i < 4; i++) {
            int y = railY + i * (card + vao);
            g2.setColor(i == 0 ? CARD_DESTAQUE : CARD);
            g2.fillRoundRect(railX, y, card, card, 8, 8);
            g2.setColor(i == 0 ? ACENTO : BORDA);
            g2.fillRoundRect(railX + 8, y + 8, card - 16, card - 16, 4, 4);
        }

        int centroX = (LADO + railX + card) / 2;
        int centroY = LADO / 2;
        int tamanho = 62;
        g2.setColor(ACENTO_CLARO);
        g2.fillPolygon(
                new int[] {centroX - tamanho / 3, centroX + tamanho * 2 / 3, centroX - tamanho / 3},
                new int[] {centroY - tamanho / 2, centroY, centroY + tamanho / 2}, 3);

        g2.dispose();
        return imagem;
    }
}