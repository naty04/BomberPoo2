
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author natal
 */


/*
 * Este é um comando que representa a ação de colocar uma bomba no jogo Bomberman.
 * Ele implementa a interface Command, que permite a execução de comandos.
 */

import java.util.Timer;
import java.util.TimerTask;

/**
 * Esta classe representa a ação de colocar uma bomba no jogo.
 * Ela recebe as coordenadas (x, y) onde a bomba será colocada e o dano que ela causará.
 */
public class BombaCommand implements Command {
    private int x;
    private int y;
    private int dano;
    private int aumentaScore = 10; // Aumento na pontuação ao atingir jogadores

    private Player player; // O jogador que coloca a bomba

    public BombaCommand(Player player, int dano) {
        this.player = player;
        this.dano = dano;
    }

    @Override
    public void execute() {
        Bomber gameInstance = player.getGameInstance();
        int x = player.getX();
        int y = player.getY();

        // Cria uma instância de bomba nas coordenadas (x, y) com o dano especificado
        Bomba bomba = new Bomba(x, y, dano, gameInstance);
        colocarBomba(gameInstance.getTabuleiro(), gameInstance.getJogador1(), gameInstance.getJogador2(), 3000);
    }

    // Este método agendará a explosão da bomba após um atraso de 3 segundos
    public void colocarBomba(int[][] tabuleiro, Player jogador1, Player jogador2, long atraso) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Atraso de 1 segundo para mostrar a bomba e a área de explosão
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int acertos = explodir(tabuleiro, jogador1, jogador2);
                // Retorna a quantidade de acertos
            }
        }, atraso);
    }

    // Este método lida com a explosão da bomba e verifica os acertos
    private int explodir(int[][] tabuleiro, Player jogador1, Player jogador2) {
        int acertos = 0; // Inicializa contador de acertos

        // Blocos acima e abaixo
        for (int i = x - 2; i <= x + 2; i++) {
            if (i >= 0 && i < tabuleiro.length) {
                if (tabuleiro[i][y] == Bomber.PAREDE_INDESTRUTIVEL) {
                    break; // Interrompe a explosão se encontrar uma parede indestrutível
                }
                if (tabuleiro[i][y] == Bomber.PAREDE_DESTRUTIVEL) {
                    tabuleiro[i][y] = 0;
                }
                if (verificarAcertoJogador(jogador1, i, y)) {
                    acertos++; // Incrementa o contador de acertos se o jogador1 for atingido
                    jogador2.addScore(aumentaScore); // Incrementa a pontuação do jogador2
                }
                if (verificarAcertoJogador(jogador2, i, y)) {
                    acertos++; // Incrementa o contador de acertos se o jogador2 for atingido
                    jogador1.addScore(aumentaScore); // Incrementa a pontuação do jogador1
                }
            } else {
                break; // Interrompe a explosão se sair dos limites verticalmente
            }
        }

        // Blocos à esquerda e à direita
        for (int j = y - 2; j <= y + 2; j++) {
            if (j >= 0 && j < tabuleiro[0].length) {
                if (tabuleiro[x][j] == Bomber.PAREDE_INDESTRUTIVEL) {
                    break; // Interrompe a explosão se encontrar uma parede indestrutível
                }
                if (tabuleiro[x][j] == Bomber.PAREDE_DESTRUTIVEL) {
                    tabuleiro[x][j] = 0;
                }
                if (verificarAcertoJogador(jogador1, x, j)) {
                    acertos++; // Incrementa o contador de acertos se o jogador1 for atingido
                    jogador2.addScore(aumentaScore); // Incrementa a pontuação do jogador2
                }
                if (verificarAcertoJogador(jogador2, x, j)) {
                    acertos++; // Incrementa o contador de acertos se o jogador2 for atingido
                    jogador1.addScore(aumentaScore); // Incrementa a pontuação do jogador1
                }
            } else {
                break; // Interrompe a explosão se sair dos limites horizontalmente
            }
        }

        tabuleiro[x][y] = 0;
        return acertos; // Retorna o número de acertos
    }

    // Este método verifica se um jogador foi atingido pela explosão da bomba
    private boolean verificarAcertoJogador(Player jogador, int x, int y) {
        if (jogador.getX() == x && jogador.getY() == y) {
            jogador.perderVida(dano);
            if (jogador.getVida() <= 0) {
                Bomber.gameRunning = false;
            }
            return true; // Retorna true se o jogador foi atingido
        }
        return false; // Retorna false se o jogador não foi atingido
    }
}
