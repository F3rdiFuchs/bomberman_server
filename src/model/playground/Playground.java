
package model.playground;

import model.IBoard;
import model.player.Player;
import java.util.Random;

/**
 * CPlayground
 * Created by Ferdinand Bauer on 27.11.2014.
 * This class represent the playground of the game.
 */
public class Playground
{
    private static final int        m_Height = 10;     //specify the height of the playground
    private static final int        m_Width = 15;       //specify the width of the playground
    private static IBoard[][][]     m_3DBoard;          /*[0][y][x] = Player + Items,
                                                          [1][y][x] Only for Bombs,
                                                          [2][y][x] Only for the PowerUps   */
    private static Playground c_Instance = null;
    private int                     BlockItCount=0;

    public static Playground getInstance()
    {
        if(c_Instance ==null){
            c_Instance = new Playground();
        }
        return c_Instance;
    }

    private Playground()
    {
        this.m_3DBoard = new IBoard[3][this.m_Height][this.m_Width];
        for(int CountArray = 0; CountArray < 3; CountArray++) {
            for (int CountY = 0; CountY < m_Height; CountY++) {
                for (int CountX = 0; CountX < m_Width; CountX++) {
                    if(CountArray == 0) {
                        Items Item = new Items(CountY, CountX);
                        IBoard ItemOnBoard = (IBoard) Item;
                        this.m_3DBoard[CountArray][CountY][CountX] = ItemOnBoard;
                    } else if (CountArray == 1) {
                        Bomb Bomb = new Bomb(CountY,CountX,1,null);
                        Bomb.setBombDisable();
                        IBoard BombOnBoard = (IBoard)Bomb;
                        this.m_3DBoard[CountArray][CountY][CountX] = BombOnBoard;
                    }else {
                        PowerUp PowerUp = new PowerUp(CountY,CountX);
                        PowerUp.setEnable(false);
                        this.m_3DBoard[CountArray][CountY][CountX] = PowerUp;
                    }
                }
            }
        }

    }

    /**
     * This method set a wall on the playground.
     *
     * @param PosY
     * @param PosX
     */
    public void setWall(int PosY,int PosX)
    {
        Wall Wall = new Wall(PosY,PosX);
        IBoard WallOnBoard = (IBoard)Wall;
        this.m_3DBoard[0][PosY][PosX]= WallOnBoard;
    }

    /**
     * This method place a destroyable block on the playground.
     *
     * @param PosY
     * @param PosX
     */
    public void setBlock(int PosY,int PosX)
    {
        Block Block = new Block(BlockItCount,PosY,PosX);
        IBoard BlockOnBoard = (IBoard) Block;
        this.m_3DBoard[0][PosY][PosX]= BlockOnBoard;
        BlockItCount++;
    }

    /**
     * This method is called by CGame
     * (pBomb with attributes of position,explrds and player)
     *
     * @param PosY
     * @param PosX
     * @param pBomb
     */
    public void setBomb(int PosY,int PosX,Bomb pBomb)
    {
        IBoard BombOnBoard = (IBoard) pBomb;
        this.m_3DBoard[1][PosY][PosX] = BombOnBoard;
    }

    public void setPowerUp(int PosY,int PosX)
    {
        PowerUp PowerUp = new PowerUp(PosY,PosX);
        IBoard PowerUpOnBoard = (IBoard) PowerUp;
        this.m_3DBoard[2][PosY][PosX] = PowerUpOnBoard;
    }

    public void setPlayer(Player _Player)
    {
        IBoard PlayerOnBoard = (IBoard) _Player;
        this.m_3DBoard[0][_Player.getPosY()][_Player.getPosX()] = PlayerOnBoard;
    }

    public void setItem(int _PosY, int _PosX)
    {
        Items Item = new Items(_PosY,_PosX);
        IBoard ItemOnBoard = (IBoard) Item;
        this.m_3DBoard[0][_PosY][_PosX] = ItemOnBoard;
    }
    public void setDisabledItem(int _PosY, int _PosX)
    {
        Items Item = new Items(_PosY,_PosX);
        Item.setEnable(false);
        IBoard ItemOnBoard = (IBoard) Item;
        this.m_3DBoard[0][_PosY][_PosX] = ItemOnBoard;
    }

    /**
     * This method return a particular cell content.
     *
     * @param ArrayNumber
     * @param PosY
     * @param PosX
     * @return
     */
    public IBoard getCell(int ArrayNumber, int PosY,int PosX)
    {
        return this.m_3DBoard[ArrayNumber][PosY][PosX];
    }

    /**
     * This method disable a item.
     *
     * @param ArrayNumber
     * @param Py
     * @param Px
     */
    public void destroyCellItem(int ArrayNumber, int Py,int Px)
    {
        if (this.m_3DBoard[ArrayNumber][Py][Px] instanceof Items) {
            if (this.m_3DBoard[ArrayNumber][Py][Px] instanceof Block) {
                Items Item = (Items) this.m_3DBoard[ArrayNumber][Py][Px];
                Item.setItemCollisionFalse();
                Item.setEnable(false);
                if(randomPowerUp()==true){
                    setPowerUp(Py, Px);
                }
            }
        }
    }
    public int getHeight()
    {
        return m_Height;
    }

    public int getWidth()
    {
        return m_Width;
    }

    /**
     * set a random power up
     * @return
     */
    private static boolean randomPowerUp()
    {
        Random Rand = new Random();
        int RandomNum = Rand.nextInt((4 - 1) + 1) + 1;  // The probability is 1/4 to be powerUp.

        if(RandomNum ==1){
            return true;                                //It is a power up -> true
        }else {
            return false;                               //It's nothing -> false
        }

    }
}
