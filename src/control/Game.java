package control;

import model.IBoard;
import model.playground.Playground;
import model.player.Player;
import model.playground.*;



/**
 * CGame
 * Created by Tim Fischer on 03.12.2014.
 * edited by Ferdinand Bauer
 * This class represent the central class of the program.
 * The control of the game takes place here.
 */
public class Game
{
    private static int      m_AmountOfPlayer  = GameConfig.getInstance().getAmountOfPLayer();
    private static Game     c_Instance = null;
    private Playground      m_Playground;
    private GameRoomUser    m_GameRoomUser;

    public static Game getInstance()
    {
        if(c_Instance ==null){
            c_Instance = new Game();
        }
        return c_Instance;
    }

    /**
     * This constructor create the game.
     * @author Tim Fischer
     *
     */
    public Game()
    {
        this.m_Playground = Playground.getInstance();
        this.createPlayground();
    }

    /**
     * This method set the start positions of the player.
     * @author Ferdinand Bauer
    /*/
    public void setSpawnPositions()
    {
        m_GameRoomUser = GameRoomUser.getInstance();

        m_GameRoomUser.getPlayer(0).IO_setPosX(1);
        m_GameRoomUser.getPlayer(0).IO_setPosY(1);


        m_GameRoomUser.getPlayer(1).IO_setPosX(13);
        m_GameRoomUser.getPlayer(1).IO_setPosY(1);

        m_GameRoomUser.getPlayer(2).IO_setPosX(13);
        m_GameRoomUser.getPlayer(2).IO_setPosY(8);

        m_GameRoomUser.getPlayer(0).IO_setPosX(1);
        m_GameRoomUser.getPlayer(0).IO_setPosY(8);

    }

    /**
     * Set player on playground
     * @author Ferdinand Bauer
     */
    public void setPlayerOnBoard()
    {
        for(int Count =0;Count < m_AmountOfPlayer;Count++)
        {
            this.m_Playground.setPlayer(m_GameRoomUser.getPlayer(Count).IO_getPlayer());
        }
    }

    /**
     * This function is used to set a bomb on the playground and add it to the bomb list.
     * @author Ferdinand Bauer
     * @param _Player The player which set the bomb.
     */
    public void playerSetBomb(Player _Player)
    {
        Bomb pBomb = new Bomb(_Player.getPosY(),
                _Player.getPosX(),
                _Player.getExplosionRadius(),_Player);
        this.m_Playground.setBomb(_Player.getPosY(),_Player.getPosX(),pBomb);
        BombList pBombList = BombList.getInstance();
        pBombList.addBomb(pBomb);
    }

    /**
     * This method is used of a player to collect a powerup.
     * @author Michael Kaufmann
     * @param _Player The player which collect the bomb.
     */
    public void playerCollectPowerUp(Player _Player)
    {
        IBoard pPowerUp = this.m_Playground.getCell(2,_Player.getPosY(), _Player.getPosX());
        PowerUp PowerUp = (PowerUp) pPowerUp;

        if (PowerUp.getItemStatus() == true)
        {
            _Player.increaseExplosionRadius();
            PowerUp.setEnable(false);
        }
    }

    /**
     * This function is used of a player to move right on the playground.
     * @author Tim Fischer
     * @param _Player The player which can go right.
     */
    public void playerMoveRight(Player _Player)
    {
        if(this.m_Playground.getCell(0,_Player.getPosY(), _Player.getPosX()+1) instanceof Player){

        } else
        {
            if (this.m_Playground.getCell(0, _Player.getPosY(), _Player.getPosX() + 1) instanceof Items
                    || this.m_Playground.getCell(0, _Player.getPosY(), _Player.getPosX() + 1) instanceof Block)
            {
                if (((Items) this.m_Playground.getCell(0, _Player.getPosY(), _Player.getPosX() + 1).GetObject()).getItemCollision() == false
                        || ((Items) this.m_Playground.getCell(2, _Player.getPosY(), _Player.getPosX() + 1).GetObject()).getItemStatus() == true)
                {
                    this.m_Playground.setItem(_Player.getPosY(), _Player.getPosX());
                    _Player.setPosX(_Player.getPosX() + 1);
                    this.m_Playground.setPlayer(_Player);
                }
            }

            this.playerCollectPowerUp(_Player);
        }
    }

    /**
     * This function is used of a player to move left on the playground.
     * @author Tim Fischer
     * @param _Player The player which can go left.
     */
    public void playerMoveLeft(Player _Player)
    {
        if (this.m_Playground.getCell(0, _Player.getPosY(), _Player.getPosX() - 1) instanceof Player)
        {

        }
        else
        {
            if (this.m_Playground.getCell(0, _Player.getPosY(), _Player.getPosX() - 1) instanceof Items
                    || this.m_Playground.getCell(0, _Player.getPosY(), _Player.getPosX() + 1) instanceof Block)
            {
                if (((Items) this.m_Playground.getCell(0, _Player.getPosY(), _Player.getPosX() - 1).GetObject()).getItemCollision() == false)
                {
                    this.m_Playground.setItem(_Player.getPosY(), _Player.getPosX());
                    _Player.setPosX(_Player.getPosX() - 1);
                    this.m_Playground.setPlayer(_Player);
                }
            }
            this.playerCollectPowerUp(_Player);
        }
    }

    /**
     * This function is used of a player to move up on the playground.
     * @author Tim Fischer
     * @param _Player The player which can go up.
     */
    public void playerMoveUp(Player _Player)
    {
        if (this.m_Playground.getCell(0, _Player.getPosY() - 1, _Player.getPosX()) instanceof Player)
        {

        } else
        {
            if (this.m_Playground.getCell(0, _Player.getPosY() - 1, _Player.getPosX()) instanceof Items
                    || this.m_Playground.getCell(0, _Player.getPosY(), _Player.getPosX() + 1) instanceof Block)
            {
                if (((Items) this.m_Playground.getCell(0, _Player.getPosY() - 1, _Player.getPosX()).GetObject()).getItemCollision() == false)
                {
                    this.m_Playground.setItem(_Player.getPosY(), _Player.getPosX());
                    _Player.setPosY(_Player.getPosY() - 1);
                    this.m_Playground.setPlayer(_Player);
                }
            }
            this.playerCollectPowerUp(_Player);
        }
    }

    /**
     * This function is used of a player to move down on the playground.
     * @author Tim Fischer
     * @param _Player The player which can go down.
     */
    public void playerMoveDown(Player _Player)
    {
        if (this.m_Playground.getCell(0, _Player.getPosY() + 1, _Player.getPosX()) instanceof Player)
        {

        } else
        {
            if (this.m_Playground.getCell(0, _Player.getPosY() + 1, _Player.getPosX()) instanceof Items
                    || this.m_Playground.getCell(0, _Player.getPosY(), _Player.getPosX() + 1) instanceof Block)
            {
                if (((Items) this.m_Playground.getCell(0, _Player.getPosY() + 1, _Player.getPosX()).GetObject()).getItemCollision() == false)
                {
                    this.m_Playground.setItem(_Player.getPosY(), _Player.getPosX());
                    _Player.setPosY(_Player.getPosY() + 1);
                    this.m_Playground.setPlayer(_Player);
                }
            }
            this.playerCollectPowerUp(_Player);
        }
    }

    /**
     * player SetBomb on position X,Y,ExplosionsRadius
     * saved in CPlayer,and Pointer on Player
     * @author Ferdinand Bauer
     */
    private void createPlayground()
    {
        for(int Count = 0; Count < this.m_Playground.getHeight(); Count++ )
        {
            this.m_Playground.setWall(Count,0);
        }

        for(int Count = 1; Count < this.m_Playground.getWidth(); Count++)
        {
            this.m_Playground.setWall(0,Count);
        }

        for(int Count = 1; Count < this.m_Playground.getHeight(); Count++)
        {
            this.m_Playground.setWall(Count,this.m_Playground.getWidth()-1);
        }

        for(int Count = 1; Count < this.m_Playground.getWidth()-1; Count++)
        {
            this.m_Playground.setWall(this.m_Playground.getHeight()-1,Count);
        }

        this.m_Playground.setWall(2, 2);
        this.m_Playground.setWall(2, 4);
        this.m_Playground.setWall(2, 6);
        this.m_Playground.setWall(2, 8);
        this.m_Playground.setWall(2, 10);
        this.m_Playground.setWall(2, 12);

        this.m_Playground.setWall(4, 2);
        this.m_Playground.setWall(4, 4);
        this.m_Playground.setWall(4, 6);
        this.m_Playground.setWall(4, 8);
        this.m_Playground.setWall(4, 10);
        this.m_Playground.setWall(4, 12);

        this.m_Playground.setWall(6, 2);
        this.m_Playground.setWall(6, 4);
        this.m_Playground.setWall(6, 6);
        this.m_Playground.setWall(6, 8);
        this.m_Playground.setWall(6, 10);
        this.m_Playground.setWall(6, 12);

        this.m_Playground.setWall(6, 2);
        this.m_Playground.setWall(6, 6);
        this.m_Playground.setWall(6, 8);
        this.m_Playground.setWall(6, 12);

        this.m_Playground.setBlock(1, 3);
        this.m_Playground.setBlock(1, 4);
        this.m_Playground.setBlock(1, 5);
        this.m_Playground.setBlock(1, 9);
        this.m_Playground.setBlock(1, 10);
        this.m_Playground.setBlock(1, 11);

        this.m_Playground.setBlock(3, 2);
        this.m_Playground.setBlock(3, 3);
        this.m_Playground.setBlock(3, 5);
        this.m_Playground.setBlock(3, 6);
        this.m_Playground.setBlock(3, 8);
        this.m_Playground.setBlock(3, 9);
        this.m_Playground.setBlock(3,11);
        this.m_Playground.setBlock(3,12);

        this.m_Playground.setBlock(4, 2);
        this.m_Playground.setBlock(4, 3);
        this.m_Playground.setBlock(4, 5);
        this.m_Playground.setBlock(4, 6);
        this.m_Playground.setBlock(4, 8);
        this.m_Playground.setBlock(4, 9);
        this.m_Playground.setBlock(4,11);
        this.m_Playground.setBlock(4,12);

        this.m_Playground.setBlock(6, 3);
        this.m_Playground.setBlock(6, 5);
        this.m_Playground.setBlock(6, 7);
        this.m_Playground.setBlock(6, 9);
        this.m_Playground.setBlock(6,11);
        this.m_Playground.setBlock(6,13);

        this.m_Playground.setBlock(8, 3);
        this.m_Playground.setBlock(8, 4);
        this.m_Playground.setBlock(8,10);
        this.m_Playground.setBlock(8,11);
    }
}