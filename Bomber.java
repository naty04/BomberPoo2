/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bomberman;

/**
 *
 * @author natal
 */

    
  
    /**
     * Classe que representa o jogo Bomber.
     */
    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.KeyEvent;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.ObjectInputStream;
    import java.io.ObjectOutputStream;
    import java.util.ArrayList;
    import java.util.List;

    public class Bomber extends JPanel implements ActionListener {
        private static final int TAMANHO_CELULA = 30;
        private static final int LARGURA_TABULEIRO = 15;
        private static final int ALTURA_TABULEIRO = 18;
        private static final int QUANTIDADE_PAREDES = 120;
        private static final int POSICAO_INICIAL_JOGADOR1_X = 1;
        private static final int POSICAO_INICIAL_JOGADOR1_Y = 1;
        private static final int POSICAO_INICIAL_JOGADOR2_X = LARGURA_TABULEIRO - 2;
        private static final int POSICAO_INICIAL_JOGADOR2_Y = ALTURA_TABULEIRO - 2;
        static final int PAREDE_DESTRUTIVEL = 1;
        static final int PAREDE_INDESTRUTIVEL = 2;
        static final int BOOSTER_PONTUACAO = 6;
        static final int BOOSTER_VIDA = 7;
        static final int VIDAS_INICIAIS_JOGADOR = 20;
        private static final int COR_JOGADOR1 = Color.RED.getRGB();
        private static final int COR_JOGADOR2 = Color.BLUE.getRGB();
        private static final int COR_PAREDE = Color.GRAY.getRGB();
        private static final int COR_PAREDE_DESTRUTIVEL = Color.LIGHT_GRAY.getRGB();
        private static final int COR_BOMBA = Color.BLACK.getRGB();
        private static final int COR_BOOSTER_VIDA = Color.GREEN.getRGB();
        private static final int COR_BOOSTER_PONTUACAO = Color.YELLOW.getRGB();
        private static final int DANO_BOMBA = 5; 
        public static final int JOGADOR1 = 3;
        public static final int JOGADOR2 = 4;
        public static final int BOMBA = 5;
        private final int score_parede = 2;
        static final int scoreBooster = 10;
        private int[][] bombaCoordenadas = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        public static Player jogador1; 
        public static Player jogador2;
        private List<Bomba> bombas = new ArrayList<>();
        private int[][] tabuleiro;
        private BombaFactory bombaFactory;
        private PlayerFactory playerFactory;
        public GameMenu currentState= new GameProgress(this); // Variável para rastrear o estado atual
        private boolean repintarBomba = false;
        static boolean gameRunning;
        

        public JButton botaoIniciar;
        public JButton botaoSalvar;
        public JButton botaoCarregar;
        public JButton botaoPausar;



        /**
         * Construtor da classe Bomber.
         */
        public Bomber() {
            setLayout(null);
            
             Timer bombRepaintTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repintarBomba = !repintarBomba;
                repaint();
            }
        });
        bombRepaintTimer.start();

            botaoIniciar = new JButton("Start");
            botaoIniciar.setBounds(getWidth() + 10, ALTURA_TABULEIRO * TAMANHO_CELULA + 70, 70, 30);
            botaoIniciar.addActionListener(this);
            add(botaoIniciar);
            bombaFactory = new BombaFactory();
            playerFactory = new PlayerFactory();
            
                  botaoPausar = new JButton("Pause");
                    botaoPausar.setBounds(getWidth() + 90, ALTURA_TABULEIRO * TAMANHO_CELULA + 70, 70, 30);
                    botaoPausar.addActionListener(this);
                    add(botaoPausar);

            botaoSalvar = new JButton("Save");
            botaoSalvar.setBounds(getWidth() + 170, ALTURA_TABULEIRO * TAMANHO_CELULA + 70, 70, 30);
            botaoSalvar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    salvarJogo();
                }
            });
            add(botaoSalvar);
       

            botaoCarregar = new JButton("Load");
            botaoCarregar.setBounds(getWidth() + 250, ALTURA_TABULEIRO * TAMANHO_CELULA + 70, 70, 30);
            botaoCarregar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    carregarJogo();
                }
            });
            add(botaoCarregar);
            
            
            setFocusable(true);
            addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    if (gameRunning) {
                        processarEntradaJogador(evt);
                    }
                }
            });
            currentState = new GameProgress(this); // Defina o estado inicial, por exemplo, "Em Andamento"

            inicializarJogo();
            this.bombas = bombas;
        }
        
       public void salvarJogo() {
        GameStateMemento gameState = new GameStateMemento(tabuleiro, bombas, jogador1, jogador2);
        GameSaveLoad.saveGameState(gameState, "salvar.dat");
        System.out.println("Jogo salvo com sucesso!");
    }


        public List<Bomba> getBombas() {
        return bombas;
    }

        
        public int[][] getTabuleiro() {
        return tabuleiro;
        }

        public Player getJogador1() {
            return jogador1;
        }

        public Player getJogador2() {
            return jogador2;
        }

        /**
         * Inicializa o jogo.
         */
        public void inicializarJogo() {
            tabuleiro = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
            jogador1 = playerFactory.createPlayer(POSICAO_INICIAL_JOGADOR1_X, POSICAO_INICIAL_JOGADOR1_Y, VIDAS_INICIAIS_JOGADOR, 0, this);
            jogador2 = playerFactory.createPlayer(POSICAO_INICIAL_JOGADOR2_X, POSICAO_INICIAL_JOGADOR2_Y, VIDAS_INICIAIS_JOGADOR, 0, this);
            gameRunning = false;
            bombas = new ArrayList<>();
            colocarParedes();
            colocarBoosters();
        }
        
            public void carregarJogo() {
        try (FileInputStream fileIn = new FileInputStream("salvar.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            GameStateMemento gameState = (GameStateMemento) in.readObject();
            this.tabuleiro = gameState.getTabuleiro();
            this.bombas = gameState.getBombas();
            this.jogador1.setPlayerState(gameState.getxJogador1(), gameState.getyJogador1(), gameState.getVidaJogador1(), gameState.getScoreJogador1());
            this.jogador2.setPlayerState(gameState.getxJogador2(), gameState.getyJogador2(), gameState.getVidaJogador2(), gameState.getScoreJogador2());
            this.gameRunning = true; // Defina como true se desejar continuar o jogo carregado.
            repaint();
            requestFocus();
            System.out.println("Jogo carregado com sucesso!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não foi possível carregar o jogo salvo.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


       public void setGameState(GameMenu newState) {
        currentState = newState; // Defina o novo estado
    }

        /**
         * Para o jogo.
         */
        public static void pararJogo() {
            gameRunning = false;
        }

        
        /**
         * Processa a entrada do jogador.
         */
        private void processarEntradaJogador(KeyEvent evt) {
            int keyCode = evt.getKeyCode();
            int jogador1X = jogador1.getX();
            int jogador1Y = jogador1.getY();
            int jogador2X = jogador2.getX();
            int jogador2Y = jogador2.getY();

            if (keyCode == KeyEvent.VK_UP) {
                moverJogador(jogador1, jogador1X, jogador1Y - 1);
            } else if (keyCode == KeyEvent.VK_DOWN) {
                moverJogador(jogador1, jogador1X, jogador1Y + 1);
            } else if (keyCode == KeyEvent.VK_LEFT) {
                moverJogador(jogador1, jogador1X - 1, jogador1Y);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                moverJogador(jogador1, jogador1X + 1, jogador1Y);
            } else if (keyCode == KeyEvent.VK_W) {
                moverJogador(jogador2, jogador2X, jogador2Y - 1);
            } else if (keyCode == KeyEvent.VK_S) {
                moverJogador(jogador2, jogador2X, jogador2Y + 1);
            } else if (keyCode == KeyEvent.VK_A) {
                moverJogador(jogador2, jogador2X - 1, jogador2Y);
            } else if (keyCode == KeyEvent.VK_D) {
                moverJogador(jogador2, jogador2X + 1, jogador2Y);
            } else if (keyCode == KeyEvent.VK_SPACE) {
                colocarBomba(jogador1, DANO_BOMBA);
            } else if (keyCode == KeyEvent.VK_ENTER) {
                colocarBomba(jogador2, DANO_BOMBA);
            }
        }

        /**
         * Coloca uma bomba no tabuleiro.
         */
    private void colocarBomba(Player jogador, int dano) {
    int x = jogador.getX();
    int y = jogador.getY();
    
    Bomba bomba = bombaFactory.createBomba(x, y, dano, this);
    BombaCommand bombacommand= new BombaCommand(jogador,dano);
    bombas.add(bomba);
    tabuleiro[x][y] = BOMBA;
    bombaCoordenadas[x][y] = 1;
    
    // Passar os jogadores como argumentos no ActionListener
    new Timer(3000, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Verifica se a bomba atingiu o jogador1
        int distanciaX1 = Math.abs(jogador1.getX() - x);
        int distanciaY1 = Math.abs(jogador1.getY() - y);

        if (distanciaX1 <= 1 && distanciaY1 <= 1) {
            jogador1.perderVida(dano);
        }

        // Verifica se a bomba atingiu o jogador2
        int distanciaX2 = Math.abs(jogador2.getX() - x);
        int distanciaY2 = Math.abs(jogador2.getY() - y);

        if (distanciaX2 <= 1 && distanciaY2 <= 1) {
            jogador2.perderVida(dano);
        }

        // Remove a bomba após a explosão
        bombas.remove(bomba);
        tabuleiro[x][y] = 0;
    }
}).start();

    new java.util.Timer().schedule(
        new java.util.TimerTask() {
            @Override
            public void run() {
                // Verifica se a bomba destruiu uma parede destrutível
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int nx = x + dx;
                        int ny = y + dy;
                        if (ehValido(nx, ny)) {
                            if (tabuleiro[nx][ny] == PAREDE_DESTRUTIVEL) {
                                synchronized(jogador) {
                                    jogador.addScore(2);  // Adiciona 2 pontos ao jogador
                                }
                                tabuleiro[nx][ny] = 0;  // Remove a parede
                            }
                        }
                    }
                }
            }
        },
        3000
    );
}

            
        /**
        *Verifica se as coordenadas (x, y) são válidas dentro dos limites do tabuleiro.
        */
        private boolean ehValido(int x, int y) {
            return x >= 0 && x < LARGURA_TABULEIRO && y >= 0 && y < ALTURA_TABULEIRO;
        }

        private void update(Player player, int boosterType) {
            player.update(player, boosterType);
        }
        /**
         * Move um jogador para uma nova posicao.
         */
        private void moverJogador(Player jogador, int novoX, int novoY) {
            if (movimentoValido(novoX, novoY)) {
                int xAtual = jogador.getX();
                int yAtual = jogador.getY();
                if (bombaCoordenadas[xAtual][yAtual] == 1) {
                    tabuleiro[xAtual][yAtual] = PAREDE_DESTRUTIVEL; // substitui a célula por uma parede
                    bombaCoordenadas[xAtual][yAtual] = 0; // limpa a informação de que uma bomba foi colocada aqui
                }
                int valorCelula = tabuleiro[novoX][novoY];
                if (valorCelula == BOOSTER_PONTUACAO) {
                jogador.addScore(scoreBooster);
                if (jogador == jogador1) {
                    update(jogador1, BOOSTER_PONTUACAO); // Notifica jogador1 sobre o booster de pontuação
                } else if (jogador == jogador2) {
                    update(jogador2, BOOSTER_PONTUACAO); // Notifica jogador2 sobre o booster de pontuação
                }
            } else if (valorCelula == BOOSTER_VIDA) {
                if (jogador == jogador1) {
                    update(jogador1, BOOSTER_VIDA); // Notifica jogador1 sobre o booster de vida
                } else if (jogador == jogador2) {
                    update(jogador2, BOOSTER_VIDA); // Notifica jogador2 sobre o booster de vida
                }
                jogador.addVida(10);
            }

                tabuleiro[xAtual][yAtual] = 0;
                jogador.mover(novoX, novoY);
                tabuleiro[novoX][novoY] = (jogador == jogador1) ? JOGADOR1 : JOGADOR2;
                repaint();
            }
        }

        /**
         * Verifica se um movimento eh valido.
         */
        private boolean movimentoValido(int x, int y) {
            if (x >= 0 && x < LARGURA_TABULEIRO && y >= 0 && y < ALTURA_TABULEIRO) {
                int valorCelula = tabuleiro[x][y];
                return valorCelula == 0 || valorCelula == BOOSTER_PONTUACAO || valorCelula == BOOSTER_VIDA;
            }
            return false;
        }

        /**
         * Coloca as paredes no tabuleiro.
         */
        private void colocarParedes() {
            for (int i = 0; i < QUANTIDADE_PAREDES; i++) {
                int x = (int) (Math.random() * LARGURA_TABULEIRO);
                int y = (int) (Math.random() * ALTURA_TABULEIRO);
                int tipoParede = (int) (Math.random() * 2) + 1;

                if (tabuleiro[x][y] == 0) {
                    if (tipoParede == PAREDE_DESTRUTIVEL) {
                        tabuleiro[x][y] = PAREDE_DESTRUTIVEL;
                    } else if (tipoParede == PAREDE_INDESTRUTIVEL) {
                        tabuleiro[x][y] = PAREDE_INDESTRUTIVEL;
                    }
                } else {
                    i--; // Tentar novamente colocar a parede
                }
            }
        }

        /**
         * Coloca os boosters no tabuleiro.
         */
        private void colocarBoosters() {
            int boostersPontuacao = 3;
            int boostersVida = 2;

            while (boostersPontuacao > 0 || boostersVida > 0) {
                int x = (int) (Math.random() * LARGURA_TABULEIRO);
                int y = (int) (Math.random() * ALTURA_TABULEIRO);

                if (tabuleiro[x][y] == 0) {
                    if (boostersPontuacao > 0) {
                        tabuleiro[x][y] = BOOSTER_PONTUACAO;
                        boostersPontuacao--;
                    } else if (boostersVida > 0) {
                        tabuleiro[x][y] = BOOSTER_VIDA;
                        boostersVida--;
                    }
                }
            }
        }
        
    

        /**
         * Desenha o tabuleiro e os elementos do jogo.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color corCelula = Color.white;

            for (int x = 0; x < LARGURA_TABULEIRO; x++) {
                for (int y = 0; y < ALTURA_TABULEIRO; y++) {
                    int valorCelula = tabuleiro[x][y];

                    switch (valorCelula) {
                        case PAREDE_DESTRUTIVEL:
                            corCelula = new Color(COR_PAREDE_DESTRUTIVEL);
                            break;
                        case PAREDE_INDESTRUTIVEL:
                            corCelula = new Color(COR_PAREDE);
                            break;
                        case JOGADOR1:
                            corCelula = new Color(COR_JOGADOR1);
                            break;
                        case JOGADOR2:
                            corCelula = new Color(COR_JOGADOR2);
                            break;
                        case BOMBA:
                            if(repintarBomba){
                            corCelula = new Color(COR_BOMBA);}
                            else{corCelula = Color.white;}
                            break;
                        case BOOSTER_PONTUACAO:
                            corCelula = new Color(COR_BOOSTER_PONTUACAO);
                            break;
                        case BOOSTER_VIDA:
                            corCelula = new Color(COR_BOOSTER_VIDA);
                            break;
                        default:
                            corCelula = Color.white;
                    }

                    g.setColor(corCelula);
                    g.fillRect(x * TAMANHO_CELULA, y * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
                }
            }

            // Desenhar jogador1
            g.setColor(new Color(COR_JOGADOR1));
            g.fillRect(jogador1.getX() * TAMANHO_CELULA, jogador1.getY() * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);

            // Desenhar jogador2
            g.setColor(new Color(COR_JOGADOR2));
            g.fillRect(jogador2.getX() * TAMANHO_CELULA, jogador2.getY() * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);

            // Desenhar bombas
            g.setColor(new Color(COR_BOMBA));
            for (int u = 0; u < LARGURA_TABULEIRO; u++) {
                for (int p = 0; p < ALTURA_TABULEIRO; p++) {
                    if (tabuleiro[u][p] == BOMBA) {
                        g.fillRect(u * TAMANHO_CELULA, p * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
                    }
                }
            }

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Pontos Red: " + jogador1.getScore(), 10, ALTURA_TABULEIRO * TAMANHO_CELULA + 40);
            g.drawString("Pontos Blue: " + jogador2.getScore(), 10, ALTURA_TABULEIRO * TAMANHO_CELULA + 60);
            g.drawString("Vida Red: " + jogador1.getVida(), 150, ALTURA_TABULEIRO * TAMANHO_CELULA + 40);
            g.drawString("Vida Azul: " + jogador2.getVida(), 150, ALTURA_TABULEIRO * TAMANHO_CELULA + 60);

            // Verifica se um jogador ganhou o jogo
            if (jogador1.getVida() <= 0 || jogador2.getVida() <= 0 || jogador1.getScore() >= 100 || jogador2.getScore() >= 100) {
                gameRunning = false;

                String winner = "";
                if (jogador1.getVida() <= 0) {
                    winner = "Jogador Blue";
                } else if (jogador2.getVida() <= 0) {
                    winner = "Jogador Red";
                } else if (jogador1.getScore() >= 100) {
                    winner = "Jogador Red";
                } else if (jogador2.getScore() >= 100) {
                    winner = "Jogador Blue";
                }
                g.drawString("O " + winner + " ganhou!", 300, ALTURA_TABULEIRO * TAMANHO_CELULA + 10);
            }
        }

        /**
         * Trata os eventos do jogo.
         */
        public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botaoIniciar) {
            if (!gameRunning) {
                currentState.start(); // Iniciar o jogo
                botaoIniciar.setEnabled(false);
                botaoIniciar.setText("Running...");
                gameRunning = true;
                requestFocus();
            } else if (botaoIniciar.getText().equals("Continue")) {
                currentState.pause(); // Pausar o jogo
                botaoIniciar.setEnabled(true);
                botaoIniciar.setText("Running...");
                gameRunning = false;
            }
        } else if (e.getSource() == botaoPausar) {
            if (gameRunning) {
                currentState.pause(); // Pausar o jogo
                botaoIniciar.setEnabled(true);
                botaoIniciar.setText("Continue");
                gameRunning = false;
            }
        }
     }

        /**
         * Método principal que inicia o jogo.
         */
        public static void main(String[] args) {
            JFrame frame = new JFrame("Jogo Bomberman");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(LARGURA_TABULEIRO * TAMANHO_CELULA + 80, ALTURA_TABULEIRO * TAMANHO_CELULA + 160);
            frame.getContentPane().add(new Bomber());
            frame.setVisible(true);
        }

    }
