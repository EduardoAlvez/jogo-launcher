# Jogo Launcher — Portfólio Java

Launcher dos jogos do portfólio, no estilo do cliente antigo do League of
Legends: um **banner em destaque** no topo ocupando a largura da janela, com a
**info do jogo escolhido** à esquerda por cima da arte, os **cards dos quatro
jogos** numa faixa embaixo e o botão **JOGAR** à direita dessa faixa.

Cada jogo abre em **processo externo**, com o `java` da própria JVM (`java.home`).
Não é firula: o Snake e o Campo Minado chamam `System.exit(0)`, então embutir o
jogo na mesma JVM fecharia o launcher junto.

Java 17 + Swing, sem dependências de execução — o mesmo `pom.xml` de sempre
(JUnit 4.13.2 + surefire + JaCoCo + launch4j).

## Os jogos

| id | Jogo | Arte |
|----|------|------|
| `pong` | Pong | logo do próprio jogo |
| `forca` | Forca | **desenhada por código** (o jogo não tem logo) |
| `snake` | Snake | logo do próprio jogo |
| `campominado` | Campo Minado | logo do próprio jogo |

O launcher procura cada jogo em `jogos/<id>.jar`, ao lado de onde ele roda.
A disponibilidade é conferida na hora: um jogo sem jar aparece como **não
instalado** e o botão JOGAR fica apagado (a Forca só entra depois de publicada).

## Controles

| Entrada | Ação |
|---------|------|
| Mouse sobre o card | Seleciona o jogo |
| `←` / `→` (ou `A` / `D`) ou `↑` / `↓` / `W` / `S` | Move a seleção |
| `Enter` / `Espaço` ou clique em JOGAR | Abre o jogo selecionado |
| `Esc` | Fecha o launcher |

Se o jogo escolhido não estiver instalado, o launcher mostra um aviso e não
abre nada.

## Como rodar

Requer **Java 17** ou mais novo.

1. Compile o launcher:

```
mvn clean package
```

Isso gera `target/jogo-launcher.exe` (Windows, ícone embutido) e
`target/jogo-launcher-1.0-SNAPSHOT.jar`.

2. Reúna os jogos numa pasta `jogos/`, com um jar por id:

```
jogos/pong.jar
jogos/forca.jar          (quando publicado)
jogos/snake.jar
jogos/campominado.jar
```

Cada jar vem do build do respectivo repositório (`mvn clean package`).

3. Rode com a pasta `jogos/` ao lado:

- **Windows**: `target\jogo-launcher.exe` (o executável já define `jogos/` como
  diretório de trabalho, então a pasta tem que ficar ao lado dele).
- **Qualquer plataforma**: `java -jar target/jogo-launcher-1.0-SNAPSHOT.jar`,
  a partir da pasta que contém `jogos/`.

## Testes

**76 testes** com JUnit 4, cobertura com JaCoCo:

```
mvn test
```

A suíte cobre o catálogo e a disponibilidade, o comando de lançamento (com o
`java` da própria JVM), o lançador por `ProcessBuilder`, a leitura dos logos
**de dentro de um jar real**, o logo/máscara do `.ico` e a interface — geometria
do `LayoutLauncher`, pixels do `DesenhoLauncher` e comportamentos da janela —
tudo sem abrir janela.

## Arquitetura

Nenhuma regra mora no desenho:

```
src/main/java/com/portfolio/launcher/
├── core/       Catálogo, disponibilidade, comando e lançador (sem Swing)
├── recursos/   Leitura dos logos de dentro dos jars dos jogos
└── ui/         A interface gráfica
    ├── LayoutLauncher.java   A geometria: onde cada coisa é desenhada
    ├── DesenhoLauncher.java  O desenho, sem janela (pinta em qualquer Graphics2D)
    └── TelaLauncher.java     A janela: seleção, mouse, teclado e lançamento
```

O `core/` não sabe que existe janela; o desenho recebe o estado (jogo
selecionado, hover, disponibilidade) e só traduz em cor; a janela liga clique e
tecla ao estado e não decide nada.

## Licença

MIT — ver [LICENSE](LICENSE).