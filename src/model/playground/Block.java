package model.playground;

/**
 * CBlock
 * Created by Ferdinand Bauer on 03.12.2014.
 * This class represent the blocks.
 */
public class Block extends Items
{
    private int m_Id;
    boolean     m_Destroyable;

    public Block(int Iid, int _PosY, int _PosX)
    {
        super(_PosY,_PosX);
        this.setEnable(true);
        m_Id = Iid;
        this.setCollision(true);
        m_Destroyable = true;
    }
}

