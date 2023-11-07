

/*
 * A classe Player representa um jogador no jogo Bomberman. Ela mantém informações sobre as coordenadas,
 * vida, pontuação e a instância do jogo a que o jogador pertence.
 */

import java.io.Serializable;

// Classe que representa o jogador
public class Player implements BoosterObserver, Serializable {
    private int x;      // Coordenada x do jogador
    private int y;      // Coordenada y do jogador
    private int vida;   // Quantidade de vida do jogador
    private int score;  // Pontuação do jogador
    private Bomber jogo; // Instância do jogo à qual o jogador pertence (dependência)

    /**
     * Construtor da classe Player para inicializar as informações do jogador.
     */
    public Player(int x, int y, int vida, int score, Bomber jogo) {
        this.x = x;
        this.y = y;
        this.vida = vida;
        this.score = score;
        this.jogo = jogo; // Injeta a dependência da instância do jogo
    }

    /**
     * Método para definir o estado do jogador, incluindo coordenadas, vida e pontuação.
    */
    public void setPlayerState(int x, int y, int vida, int score) {
        this.x = x;
        this.y = y;
        this.vida = vida;
        this.score = score;
    }

    // Referência para a instância do jogo (Bomber)
    private Bomber gameInstance;

    // Getters para as coordenadas do jogador
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Getter para a vida do jogador
    public int getVida() {
        return vida;
    }

    // Getter para a pontuação do jogador
    public int getScore() {
        return score;
    }

    public Bomber getGameInstance() {
        return gameInstance;
    }

    /**
     * Método para mover o jogador para novas coordenadas.
     */
    public void mover(int novoX, int novoY) {
        this.x = novoX;
        this.y = novoY;
    }

    /**
     * Método para diminuir a vida do jogador.
     */
    public void perderVida(int quantidade) {
        this.vida -= quantidade;

        // Verifica se a vida do jogador chegou a zero ou menos
        if (this.vida <= 0) {
            this.vida = 0;

            // Para o jogo (define gameRunning como falso)
            this.gameInstance.pararJogo();
        }
    }

    /**
     * Método para adicionar pontuação ao jogador.
     */
    public void addScore(int quantidade) {
        this.score += quantidade;
    }

    /**
     * Método para adicionar vida ao jogador.
     */
    public void addVida(int quantidade) {
        // Incrementa a vida, mas limita ao valor máximo de 20
        this.vida = Math.min(this.vida + quantidade, Bomber.VIDAS_INICIAIS_JOGADOR);
    }

    /**
     * Método de atualização do jogador após receber notificação de uso de booster.
    */
    @Override
    public void update(Player player, int boosterTipo) {
        // Implemente o que o jogador deve fazer quando receber uma notificação de uso de booster.
        if (boosterTipo == Bomber.BOOSTER_PONTUACAO) {
            // Adicione a lógica para lidar com o booster de pontuação.
            this.score += Bomber.scoreBooster;
        } else if (boosterTipo == Bomber.BOOSTER_VIDA) {
            // Adicione a lógica para lidar com o booster de vida.
            this.vida += 10;
        }
    }
}

