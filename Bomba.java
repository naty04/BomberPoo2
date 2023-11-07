
/**
 *
 * @author natal
 */

import java.io.Serializable;

/**
 * Esta classe representa uma bomba que pode ser colocada no jogo.
 */

import java.io.Serializable;

public class Bomba implements Serializable {
    private int x; // A coordenada x da bomba
    private int y; // A coordenada y da bomba
    private int dano; // O dano causado pela bomba
    private int aumentaScore; // Incremento na pontuação
    private Bomber jogo; // A instância do jogo a que a bomba pertence

    // Construtor da classe Bomba que inicializa suas propriedades
    public Bomba(int x, int y, int dano, Bomber jogo) {
        this.x = x;
        this.y = y;
        this.dano = dano;
        this.jogo = jogo; // Injeta a dependência da instância do jogo
        this.aumentaScore = aumentaScore; // Talvez você queira atribuir um valor aqui
    }

    // Método para obter a coordenada x da bomba
    public int getX() {
        return x;
    }
}
