
/**
 *
 * @author natal
 */
import java.io.Serializable;

public class PlayerFactory implements Serializable {
    public Player createPlayer(int x, int y, int vida, int score, Bomber jogo) {
        Player player = new Player(x, y, vida, score, jogo);
        player.setPlayerState(x, y, vida, score); // Configura o estado do jogador
        return player;
    
    }
}
