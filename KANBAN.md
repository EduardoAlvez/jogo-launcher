# Kanban — Jogo Launcher

Quinto projeto do portfólio, depois do Pong, da Forca, do Snake e do Campo
Minado. Não é um jogo: é a **vitrine** que reúne os outros quatro, no estilo do
cliente antigo do League of Legends — banner do jogo escolhido ocupando a janela
toda, cards por cima e o botão JOGAR.

Cada jogo abre em **processo externo** com o `java` da própria JVM. É a decisão
central do projeto: o Snake e o Campo Minado chamam `System.exit(0)`, então rodar
o jogo na mesma JVM mataria o launcher.

Java 17 + Swing, sem dependências externas: o mesmo `pom.xml` de sempre (JUnit
4.13.2 + surefire + JaCoCo + launch4j).

## Estrutura (subpacotes por camada)

```
src/main/java/com/portfolio/launcher/
├── core/       Núcleo puro e testável (sem Swing)
│   ├── JogoInfo.java / CatalogoDeJogos.java
│   ├── DisponibilidadeDeJogos.java      Procura jogos/<id>.jar
│   ├── ComandoDeLancamento.java         Monta o comando com o java da JVM
│   └── Lancador.java / LancadorProcesso.java   Abre o jogo em processo externo
├── recursos/   Leitura dos logos de dentro dos jars dos jogos
│   └── CarregadorDeArtes.java
└── ui/         Interface gráfica Swing
    ├── LayoutLauncher.java   Geometria full-bleed: onde cada coisa é desenhada
    ├── DesenhoLauncher.java  O desenho, sem janela (pinta em qualquer Graphics2D)
    ├── EstadoLauncher.java   Seleção, hover, disponibilidade e aviso
    └── TelaLauncher.java     A janela: mouse, teclado e lançamento

src/main/resources/
├── logo-{16,24,32,48,64,128,256}.png   # Ícone da janela, uma por resolução
└── logo.ico                              # Ícone do executável (.exe)

tools/   # Geradores de recursos (fora do build do Maven)
├── DesenharLogoLauncher.java / GerarLogo.java
```

## Decisões já fechadas

- **Jogo em processo externo** (`ProcessBuilder`), com o `java` da própria JVM
  (`java.home/bin/java`) — não depende do `PATH`. Motivo: `System.exit(0)`.
- **Catálogo fixo dos quatro jogos**, na ordem `pong`, `forca`, `snake`,
  `campominado`. A disponibilidade é conferida por `jogos/<id>.jar` a cada
  montagem; jogo sem jar aparece como **não instalado** e o JOGAR fica apagado.
- **Estilo full-bleed**: a arte do jogo selecionado ocupa a janela toda; rail de
  cards (280px) à direita, descrição embaixo e o botão JOGAR sobre a arte.
- **Reaproveitar os logos dos jogos**: o launcher lê o `logo-256.png` de **dentro
  do jar** de cada jogo (o mesmo arquivo que o jogo usa no `.exe`), em vez de
  manter cópias. A Forca, que não tem logo, ganha uma **arte desenhada por
  código** no banner e no card.
- **Hover no modelo, não no componente** (regra 9 da skill): o estado guarda qual
  alvo está sob o mouse; os listeners só trocam o estado e chamam `repaint`.
- **Nada de regra no `paintComponent`** (regra 4): `DesenhoLauncher` só traduz
  estado em cor.
- **Nada de estado no desenho**: a janela decide a tela (menu/sair) e o desenho
  recebe o resultado pronto.
- **Logo/ícones por código** (`tools/DesenharLogoLauncher.java` +
  `tools/GerarLogo.java`), com máscara AND de verdade no `.ico`, conferida no
  arquivo final por `LogoLauncherTest`.

## Pronto (por fase)

- [x] **1 — Núcleo e catálogo**: `JogoInfo`, `CatalogoDeJogos`, disponibilidade,
  comando de lançamento e lançador por `ProcessBuilder` — testados.
- [x] **2 — Interface**: geometria full-bleed com alvos clicáveis, desenho por
  pixels (banner, cards, seleção, hover, JOGAR, arte da Forca por código) e a
  janela com mouse, teclado, hover e lançamento — validada visualmente.
- [x] **3 — Leitura das artes**: logos lidos de dentro dos jars dos jogos.
- [x] **4 — Acabamento**: logo/ícones gerados por código e embutidos no exe,
  README e este kanban, push e conferência do artefato.

## A Fazer (Backlog)

| # | Prioridade | Camada | Tarefa | Detalhes |
|---|-----------|--------|--------|----------|
| 5 | Baixa | ui | Animação de transição | Fade ao trocar o destaque |
| 6 | Baixa | core | Múltiplas pastas de jogos | Além de `jogos/`, aceitar um caminho configurável |
| 7 | Baixa | ui | Estado de carregando | Feedback enquanto o processo sobe |