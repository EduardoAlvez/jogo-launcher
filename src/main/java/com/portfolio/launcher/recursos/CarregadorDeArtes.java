package com.portfolio.launcher.recursos;

import com.portfolio.launcher.core.CatalogoDeJogos;
import com.portfolio.launcher.core.DisponibilidadeDeJogos;
import com.portfolio.launcher.core.JogoInfo;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;

public final class CarregadorDeArtes {

    private static final String ENTRADA_LOGO = "logo-256.png";

    private CarregadorDeArtes() {
    }

    public static Map<String, BufferedImage> carregar(Path raiz) {
        Map<String, BufferedImage> artes = new HashMap<>();
        for (JogoInfo jogo : CatalogoDeJogos.lista()) {
            if (!jogo.temArte()) {
                continue;
            }
            BufferedImage imagem = lerDentroDoJar(raiz, jogo);
            if (imagem != null) {
                artes.put(jogo.getId(), imagem);
            }
        }
        return artes;
    }

    private static BufferedImage lerDentroDoJar(Path raiz, JogoInfo jogo) {
        Path jar = DisponibilidadeDeJogos.caminhoDoJar(raiz, jogo.getId());
        if (!Files.isRegularFile(jar)) {
            return null;
        }
        try (JarFile arquivo = new JarFile(jar.toFile())) {
            ZipEntry entrada = arquivo.getEntry(ENTRADA_LOGO);
            if (entrada == null) {
                return null;
            }
            return ImageIO.read(new BufferedInputStream(arquivo.getInputStream(entrada)));
        } catch (IOException ignorada) {
            return null;
        }
    }
}