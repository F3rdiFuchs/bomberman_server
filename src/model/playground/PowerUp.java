package model.playground;

/**
 * CPowerUp
 * Created by Ferdinand Bauer on 03.12.2014.
 * This class represent the powerups.
 */

public class PowerUp extends Items
{
    boolean m_Destroyable = false;

    public PowerUp(int _PosY, int _PosX)
    {
        super(_PosY,_PosX);
        this.setEnable(true);
        this.setCollision(false);       // can you walk over it? Items
        this.m_Destroyable = false;
    }

}
