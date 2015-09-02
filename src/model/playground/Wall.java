package model.playground;

/**
 * CWall
 * Created by Ferdinand Bauer on 03.12.2014.
 * This class represent the walls.
 */

public class Wall extends Items
{
    boolean m_Destroyable;

    public Wall(int _PosY, int _PosX)
    {
        super(_PosY,_PosX);
        this.setEnable(true);
        this.setCollision(true);
        this.m_Destroyable = false;
    }

}