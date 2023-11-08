
/**
 *
 * @author natal
 */

/*
 * A classe GameState é responsável por armazenar e fornecer informações sobre o estado atual do jogo
 * para que o jogo possa ser salvo.
 */
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class GameStateMemento implements Serializable {
    private int[][] tabuleiro;
    private List<Bomba> bombas;
    private int[] jogador1State;
    private int[] jogador2State;
    private Bomber jogador1Game;
    private Bomber jogador2Game;

    /**
     * Construtor da classe GameState. Inicializa o estado com informações relevantes do jogo.
     */
    public GameStateMemento(int[][] tabuleiro, List<Bomba> bombas, Player jogador1, Player jogador2) {
        this.tabuleiro = tabuleiro;
        this.bombas = bombas;
        this.jogador1State = new int[] { jogador1.getX(), jogador1.getY(), jogador1.getVida(), jogador1.getScore() };
        this.jogador2State = new int[] { jogador2.getX(), jogador2.getY(), jogador2.getVida(), jogador2.getScore() };
        jogador1Game = jogador1.getGameInstance();
        jogador2Game = jogador2.getGameInstance();
        // Aqui, você deve inicializar outros campos do estado do jogo, se houver.
    }

    // Métodos para acessar informações específicas do jogo:

    public int getxJogador1() {
        return jogador1State[0];
    }

    public int getyJogador1() {
        return jogador1State[1];
    }

    public int getVidaJogador1() {
        return jogador1State[2]; // Índice 2 representa a vida do jogador 1
    }

    public int getScoreJogador1() {
        return jogador1State[3];
    }

    public int getxJogador2() {
        return jogador2State[0];
    }

    public int getyJogador2() {
        return jogador2State[1];
    }

    public int getVidaJogador2() {
        return jogador2State[2]; // Índice 2 representa a vida do jogador 2
    }

    public int getScoreJogador2() {
        return jogador2State[3];
    }

    public int[][] getTabuleiro() {
        return tabuleiro;
    }

    public List<Bomba> getBombas() {
        return bombas;
    }

    public int[] getJogador1State() {
        return jogador1State;
    }

    public int[] getJogador2State() {
        return jogador2State;
    }

    public Bomber getJogador1Game() {
        return jogador1Game;
    }

    public Bomber getJogador2Game() {
        return jogador2Game;
    }
}
