//BLOCK SHAPE CLASS

package TetrisGame;
import java.awt.*;

import static TetrisGame.gameBoard.*;

public class BlockShape {

    //x, y: Current position of the block on the game board.
    private int x=4,y=0;
    //deltaX: Change in the x-direction for horizontal movement.
    private int deltaX;

    //borderBoard: Flag to check if the block is at the border of the game board.
    private boolean borderBoard =false;

    //normalTime, fast: Time delays for normal and fast movement.
    private int  normalTime=1000;//büyütürsen yavaşlar>1000,küçültürsen hızlanır>10
    private int fast = 50;
    //delayTimeForMovement: Current delay time for block movement.
    private int delayTimeForMovement = normalTime;
    //beginTime: Time when the last movement started.
    private long beginTime;
        private int[][] block; //block: 2D array representing the shape of the block.
    private  gameBoard Board;//Board: Reference to the game board.
    private Color color;//color: Color of the block.

    public BlockShape(int[][] block,gameBoard Board,Color color)//initializes fields with the provided parameters.
    {
        this.block=block;
        this.Board=Board;
        this.color=color;
    }

    //Reset Method:
    //Resets the position and border flag of the block.
    public void Reset()
    {
        this.x=4;
        this.y=0;
        borderBoard=false;
    }
    //Update Method:
    //Updates the position of the block based on user input and time.
    //Checks for collisions and updates the game board.
    public void Update()
    {

        //fill the color for board
        if(borderBoard)
        {
            for(int row =0;row<block.length;row++)
            {
                for(int col=0;col<block[0].length;col++)
                {
                    if(block[row][col]!=0)
                    {
                        Board.getGameBoard()[y+row][x+col]=color;
                    }
                }
            }
            checkLine();
            Board.addScore();
            //set current shape
            Board.setCurrentshape();
            return;//eğer tablo sınırındaysa hareket sonlanır
        }

        //check moving horizantal
        boolean moveX=true;

        if(!(x+deltaX+block[0].length>BOARD_WIDTH)&&(!(x+deltaX<0)))//iç tablo dışına çıkma durum kontrolü
        {for(int row=0;row<block.length;row++)
        {
            for(int col =0;col<block[0].length;col++)
            {
                if(block[row][col]!=0)
                {
                    if(Board.getGameBoard()[y+row][x+deltaX+col]!=null)
                    {
                        moveX=false;
                    }
                }
            }
        }
            if(moveX)
            {x+=deltaX;}

        }
        //her seferinde deltaX sıfırlanmasın diye döngü dışına alınır
        deltaX=0;

        if(System.currentTimeMillis()-beginTime>delayTimeForMovement)
        {
            //x buraya konursa y aşağı giderken x sağa veya sola kayar
            //vertical movement
            if (!(y + 1 + block.length > BOARD_HEIGHT))
            {
                for(int row=0;row< block.length;row++)
                {
                    for(int col=0;col<block[0].length;col++)
                    {
                        if(block[row][col]!=0)
                        {
                            if(Board.getGameBoard()[y+1+row][x+deltaX+col]!=null)
                            {
                                borderBoard=true;
                            }
                        }
                    }
                }
                if(!borderBoard)
                {
                    //vertical  movement
                    y++;
                }

            }
            else {
                borderBoard=true; //tablo sınırında demektir
            }
            beginTime=System.currentTimeMillis();
        }
    }

    private void checkLine() {
        int size = Board.getGameBoard().length - 1;

        for(int i = Board.getGameBoard().length - 1; i > 0; --i) {
            int count = 0;

            for(int j = 0; j < Board.getGameBoard()[0].length; ++j) {
                if (Board.getGameBoard()[i][j] != null) {
                    ++count;
                }

                Board.getGameBoard()[size][j] = Board.getGameBoard()[i][j];
            }

            if (count < Board.getGameBoard()[0].length) {
                --size;
            }
        }
    }

    //TransposeMatrix Method:
    //Transposes the given matrix.
    private int[][] TransposeMatrix(int[][] Matrix){
        int[][] temp=new int[Matrix[0].length][Matrix.length];
        for(int row =0;row<Matrix.length;row++)
        {
            for(int col=0;col<Matrix[0].length;col++)
            {
                temp[col][row]=Matrix[row][col];
            }
        }
        return temp;
    }
    //ReverseRows Method:
    //Reverses the rows of the given matrix.
    private void ReverseRows(int[][] Matrix)
    {
        int middle =Matrix.length/2;
        for(int  row=0;row<middle;row++)
        {
            int[] temp = Matrix[row];
            Matrix[row]=Matrix[Matrix.length-row-1];
            Matrix[Matrix.length-row-1]=temp;
        }
    }
    //RotateShape Method:
    //Rotates the block shape if there is enough space.
    public void RotateShape(){
        int[][] RotateShape=TransposeMatrix(block);
        ReverseRows(RotateShape);
        //check for right side and bottom
        if(x+RotateShape[0].length>Board.BOARD_WIDTH || y+RotateShape.length>BOARD_HEIGHT)
        {
            return;
        }
        //check for collision with other shapes before rotated
        for(int row=0;row<RotateShape.length;row++)
        {
            for(int col=0;col<RotateShape[row].length;col++)
            {
                if(RotateShape[row][col]!=0)
                {
                    if(Board.getGameBoard()[y+row][x+col]!=null)
                    {
                        return;
                    }
                }
            }
        }
        block = RotateShape;
    }

    //Render Method:
    //Renders the block on the screen using the provided Graphics object.

    public void Render(Graphics g)
    {
        g.setColor(color);
        for(int row=0;row<block.length;row++)
        {
            for(int col=0;col<block[0].length;col++)
            {
                if(block[row][col]!=0)
                {

                    g.fillRect(col*BLOCK_SIZE+x*BLOCK_SIZE,row*BLOCK_SIZE+y*BLOCK_SIZE,BLOCK_SIZE,BLOCK_SIZE);

                }
            }
        }
    }
    public int[][] getBlock()
    {
        return block;
    }
    //SpeedUp, SpeedDown, MoveRight, MoveLeft Methods:
    //Change the speed and movement direction of the block based on user input.
    public void SpeedUp() //speedUp
    {
        delayTimeForMovement=fast;
    }
    public void SpeedDown()
    {
        delayTimeForMovement=normalTime;
    }
    public void MoveRight()
    {
        deltaX=1;
    }
    public void MoveLeft()
    {
        deltaX=-1;
    }

    //getY, getX Methods:
    //Return the current y and x positions of the block.
    public int getY() {
        return y;
    }
    public int getX()
    {
        return x;
    }
}
