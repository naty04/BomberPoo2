
/**
 *
 * @author natal
 */
/*
 * Este é o início da definição da classe GamePause, que representa o estado de pausa no jogo Bomberman.
 */

import java.io.Serializable;

public class GamePause implements GameMenu, Serializable {
    private Bomber bomber;
   
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
    }
}
