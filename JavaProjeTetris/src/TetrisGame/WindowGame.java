//WİNDOW GAME CLASS
package TetrisGame;
import javax.swing.*;
public class WindowGame {

    //WIDTH, HEIGHT: Width and height of the game window.
    public static final int WIDTH=455,HEIGHT=640;
    private JFrame gameWindow;
    private gameBoard gameBoard;


    public WindowGame(){

        //Initializes a JFrame object named gameWindow for the game.
        //Sets the size, default close operation, resizable property, and location of the game window.
        //Creates a gameBoard object named gameBoard.
        //Adds the gameBoard to the gameWindow.
        //Sets the visibility of the gameWindow to true.

        gameWindow = new JFrame ("TETRIS");
        gameWindow.setSize(WIDTH,HEIGHT);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);
        gameWindow.setLocationRelativeTo(null);

        gameBoard = new gameBoard();
        gameWindow.add(gameBoard);
        gameWindow.addKeyListener(gameBoard);
        gameWindow.addMouseListener(gameBoard);
        gameWindow.setVisible(true);
    }
    public static class Main {
        //initializes the game
        public static void main(String[] args) {
            new WindowGame();
        }
    }
}
