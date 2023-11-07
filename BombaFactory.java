
/**
 *
 * @author natal
 */

import java.io.Serializable;

public class BombaFactory implements Serializable {
    public Bomba createBomba(int x, int y, int dano, Bomber jogo) {
        return new Bomba(x, y, dano, jogo);
    }
}



