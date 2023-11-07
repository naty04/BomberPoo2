
/**
 *
 * @author natal
 */
/*
 * Este é o início da definição da classe GamePause, que representa o estado de pausa no jogo Bomberman.
 */

/**
 * Esta classe implementa a interface GameMenu e representa o estado de pausa no jogo Bomberman.
 */
import java.io.Serializable;

public class GamePause implements GameMenu, Serializable {
    private Bomber bomber;
    
    /**
     * Construtor da classe GamePause que recebe uma instância de Bomber para gerenciar o estado do jogo.
     *
     * @param bomber A instância de Bomber associada ao estado de pausa.
     */
    public GamePause(Bomber bomber) {
        this.bomber = bomber;
    }
    
    /**
     * Inicia o jogo a partir do estado de pausa. Volta ao estado "Em Andamento".
     */
    @Override
    public void start() {
        bomber.setGameState(new GameProgress(bomber)); // Retorna ao estado "Em Andamento"
        bomber.botaoIniciar.setEnabled(true);
        bomber.botaoIniciar.setText("Rodando...");
        bomber.gameRunning = true;
    }

    /**
     * Pausa o jogo. Como o jogo já está em pausa, este método não faz nada aqui.
     */
    @Override
    public void pause() {
        // O jogo já está pausado, então não é necessário fazer nada aqui.
    }
}
