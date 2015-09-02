
package control;

/**
 * CBombListThread
 * Created by Ferdinand Bauer on 17.01.2015.
 * This class represent a thread, which contains a list of bombs.
 * The thread control all bombs on the playground.
 */
public class BombListTick
{
    private BombList m_BombList;
    private BombList m_BombListlist;

    public void checkForBombs()
    {
        this.m_BombListlist = BombList.getInstance();
        this.m_BombListlist.CheckBombs();
    }
}
