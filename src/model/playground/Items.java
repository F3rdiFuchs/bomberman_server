package model.playground;

import model.IBoard;
/**
 * CItems
 * Created by Ferdinand Bauer on 03.12.2014.
 * This class represent the items.
 */

public class Items implements IBoard
{
    private int         m_PosX;             // Position X
    private int         m_PosY;             // Position Y
    private boolean     m_Enable;           // visible true/false
    private boolean     m_Collison;         // can you walk over it? Items

    public Items(int Py, int Px)
    {
        this.m_PosX = Px;
        this.m_PosY = Py;
        this.m_Enable = false;
    }
    public Items GetObject()
    {
        return this;
    }
    public boolean getItemStatus()
    {
        return m_Enable;
    }
    public int getItemPositionX()
    {
        return m_PosX;
    }
    public int getItemPositionY()
    {
        return m_PosY;
    }

    public boolean getItemCollision()
    {
        return m_Collison;
    }
    public void setItemCollisionFalse(){m_Collison = false;}

    public void setEnable(boolean _Enable)
    {
        this.m_Enable = _Enable;
    }

    public void setCollision(boolean _Collision)
    {
        this.m_Collison = _Collision;
    }
}