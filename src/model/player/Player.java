package model.player;

import model.IBoard;
import model.playground.Playground;

/**
 * CPlayer
 * Created by Tim Fischer on 03.12.2014.
 * changed for multiplayer by Ferdinand Bauer
 * This class represent the player.
 */
public class Player implements IBoard
{
    private int m_PosX;
    private int m_PosY;
    private int m_PlayerNumber;
    private int m_KillCount;
    private int m_ExplosionRadius;
    private boolean m_LifeOrDeath; // 1 Life, 0 Death
    private boolean m_SetBomb;
    private int m_direction;

    /**
     * This constructor create a new player with the following attributes:
     *
     * @param _PosY         Position Y of the player.
     * @param _PosX         Position X of the player.
     * @param _PlayerNumber The number of the player.
     */
    //----------------------------
    public Player(int _PosY, int _PosX, int _PlayerNumber)
    {
        this.m_PosX = _PosX;
        this.m_PosY = _PosY;
        this.m_KillCount = 0;
        this.m_PlayerNumber = _PlayerNumber;
        this.m_SetBomb = false;
        this.m_ExplosionRadius = 1;
        this.m_direction = 1;
        this.m_LifeOrDeath = true;

    }


    public int getExplosionRadius()
    {
        return this.m_ExplosionRadius;
    }

    public Player GetObject()
    {
        return this;
    }

    public void setPlayerOnBoard()
    {
        Playground Playground = model.playground.Playground.getInstance();
        Playground.setPlayer(this);
    }

    public boolean getLifeOrDeath()
    {
        return this.m_LifeOrDeath;
    }

    public void setLifeOrDeath(boolean _LifeOrDeath)
    {
        this.m_LifeOrDeath = _LifeOrDeath;
    }

    public int getPosX()
    {
        return this.m_PosX;
    }

    public int getPosY()
    {
        return this.m_PosY;
    }

    public void setPosX(int _PosX)
    {
        this.m_PosX = _PosX;
    }

    public void setPosY(int _PosY)
    {
        this.m_PosY = _PosY;
    }

    public int getPlayerNumber()
    {
        return this.m_PlayerNumber;
    }

    public int getKillCount()
    {
        return this.m_KillCount;
    }

    public void raiseKillCount()
    {
        this.m_KillCount++;
    }

    public void increaseExplosionRadius()
    {
        this.m_ExplosionRadius++;
    }
}



