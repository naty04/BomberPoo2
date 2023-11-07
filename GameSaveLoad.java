
/**
 *
 * @author natal
 */

/**
 * A classe GameSaveLoad lida com a funcionalidade de salvar e carregar o estado do jogo em arquivos.
 */
import java.io.*;

public class GameSaveLoad {
    /**
     * Salva um objeto GameState em um arquivo especificado.
     */
    public static void saveGameState(GameState gameState, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega um GameState de um arquivo especificado.
     */
    public static GameState loadGameState(String filename) {
        GameState gameState = null;
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            gameState = (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gameState;
    }
}


