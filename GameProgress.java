
/**
 *
 * @author natal
 */
import java.io.Serializable;

public class GameProgress implements GameMenu,Serializable {
    private Bomber bomber;

    public GameProgress(Bomber bomber) {
        this.bomber = bomber;
    }

    @Override
    public void start() {
    }

    @Override
    public void pause() {
        bomber.setGameState(new GamePause(bomber)); // Define o estado como pausado
        bomber.botaoIniciar.setEnabled(true);
        bomber.botaoIniciar.setText("Continue");
        bomber.gameRunning = false;
    }
}



