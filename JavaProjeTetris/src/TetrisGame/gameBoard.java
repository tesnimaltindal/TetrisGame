
//GAME BOARD CLASS

package TetrisGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class gameBoard extends JPanel implements KeyListener,MouseListener {

    //Constants representing different states of the game.
    public static int STATE_GAME_PLAY = 0;
    public static int STATE_GAME_PAUSE = 1;
    public static int STATE_GAME_OVER = 2;
    private int state = STATE_GAME_PLAY;

    //FPS: Frames per second for the game loop.
    private static int FPS = 1000;

    //delay: Delay in milliseconds between each frame in the game loop.
    private static int delay = 1000 / FPS;

    //BOARD_WIDTH, BOARD_HEIGHT: Width and height of the game board in terms of blocks.
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;

    //BLOCK_SIZE: Size of each block in pixels.
    public static final int BLOCK_SIZE = 30;

    //gameLooper: Timer for the game loop.
    private Timer gameLooper;

    //randomNumber: Random number generator for selecting random block shapes.
    private Random randomNumber;

    //gameBoard: 2D array representing the colors of each block on the game board.

    //colors: Array of colors for different block shapes.
    private Color[][] gameBoard = new Color[BOARD_HEIGHT][BOARD_WIDTH];

    private Color[] colors = {Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"),
            Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};

    //blocks: Array of BlockShape objects representing different block shapes.

    private BlockShape[] blocks = new BlockShape[7];
    private BlockShape currentBlock;//currentBlock: Current active block in the game.
    //score
    private int Score = 0;//Score: Variable to store the player's score.

    public gameBoard() {

        //Initializes constants, arrays, and variables.
        //Initializes blocks array with different block shapes.
        //Sets the initial current block to the first block shape.
        //Sets up the game loop (gameLooper) to call the Update method and repaint the game board.

        randomNumber = new Random();
        // create shapes
        blocks[0] = new BlockShape(new int[][]{
                {1, 1, 1, 1} // I shape;
        }, this, colors[0]);

        blocks[1] = new BlockShape(new int[][]{
                {1, 1, 1},
                {0, 1, 0}, // T shape;
        }, this, colors[1]);

        blocks[2] = new BlockShape(new int[][]{
                {1, 1, 1},
                {1, 0, 0}, // L shape;
        }, this, colors[2]);

        blocks[3] = new BlockShape(new int[][]{
                {1, 1, 1},
                {0, 0, 1}, // J shape;
        }, this, colors[3]);

        blocks[4] = new BlockShape(new int[][]{
                {0, 1, 1},
                {1, 1, 0}, // S shape;
        }, this, colors[4]);

        blocks[5] = new BlockShape(new int[][]{
                {1, 1, 0},
                {0, 1, 1}, // Z shape;
        }, this, colors[5]);

        blocks[6] = new BlockShape(new int[][]{
                {1, 1},
                {1, 1}, // O shape;
        }, this, colors[6]);

        currentBlock = blocks[0];

        gameLooper = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Update();
                repaint(); //repaint it refresh old place
            }
        });
        gameLooper.start();
    }

    //Update Method:
    //Checks the game state.
    //Calls the Update method of the current block to handle block movement and collision.
    private void Update() {
        if (state == STATE_GAME_PLAY) {
            currentBlock.Update();
        }

    }


    //setCurrentshape Method:
    //Sets the current block to a random block shape.
    //Resets the position of the current block.
    //Checks if the game is over.
    public void setCurrentshape() {
         //currentBlock=blocks[0]; //belli bir şekil tek tip blok 777 yazdırmak için
        currentBlock = blocks[randomNumber.nextInt(blocks.length)];
        currentBlock.Reset();
        checkOverGame();
    }

    //checkOverGame Method:
    //Checks if the current block has reached the top of the game board.
    //If yes, sets the game state to STATE_GAME_OVER.
    private void checkOverGame() {
        int[][] block = currentBlock.getBlock();
        for (int row = 0; row < block.length; row++) {
            for (int col = 0; col < block[0].length; col++) {
                if (block[row][col] != 0) {
                    if (gameBoard[row + currentBlock.getY()][col + currentBlock.getX()] != null)//gidecek başka yeri kalmadıysa
                    {
                        state = STATE_GAME_OVER;
                    }
                }
            }
        }
    }

    //paintComponent Method:
    //Overrides the paintComponent method to paint the game board.
    //Paints the current block and filled blocks on the board.
    //Draws the grid.
    //Displays game over or pause messages.
    //Displays the score.

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        currentBlock.Render(g);

        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                if (gameBoard[row][col] != null) {
                    g.setColor(gameBoard[row][col]);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }

            }
        }
        //ızgara görünüm
        g.setColor(Color.black);
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);//x,y,width,height
            }
            if (state == STATE_GAME_OVER) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Mv Boli", Font.BOLD, 30));
                g.drawString("GAME_OVER", 25, 145);
            }
            if (state == STATE_GAME_PAUSE) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Mv Boli", Font.BOLD, 30));
                g.drawString("GAME_PAUSED", 25, 145);
            }
            {
                //for score
                g.setColor(Color.BLACK);
                g.setFont(new Font("Mv Boli", Font.BOLD, 25));
                g.drawString("SCORE: " + Score + " ", 300, 350);
            }
        }
    }

    public Color[][] getGameBoard() {
        return gameBoard;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //keyPressed Method:
    //Handles key presses for moving, rotating, and pausing the game.
    //Resets the game when the player presses the space bar after a game over.

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN)//aşağı yön tuşuna bastığında hızlan
        {
            currentBlock.SpeedUp();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentBlock.MoveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentBlock.MoveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            currentBlock.RotateShape();
        }

        //clean the board
        if (state == STATE_GAME_OVER) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                for (int row = 0; row < gameBoard.length; row++) {
                    for (int col = 0; col < gameBoard[row].length; col++) {
                        gameBoard[row][col] = null;
                    }
                }
                setCurrentshape();
                state = STATE_GAME_PLAY;
                Score=0;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (state == STATE_GAME_PLAY) {
                state = STATE_GAME_PAUSE;
            } else if (state == STATE_GAME_PAUSE) {
                state = STATE_GAME_PLAY;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentBlock.SpeedDown();
        }
    }

    //addScore Method:
    //Increments the player's score.

    public void addScore()
    {
        Score++;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (state == STATE_GAME_PLAY) {
            state = STATE_GAME_PAUSE;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (state == STATE_GAME_PAUSE) {
            state = STATE_GAME_PLAY;
        }
    }
}