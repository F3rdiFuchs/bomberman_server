
package model.playground;

import model.player.Player;
/**
 * CBomb
 * Created by Ferdinand Bauer on 03.12.2014.
 * This class represent the bombs.
 */
public class Bomb extends Items
{
    private int     m_Validity;
    private int     m_ExplosionRadius;
    private Player c_Player;

    /**
     * If a player set a bomb, a instance of this class will be created.
     *
     * @param Py Position Y of the bomb.
     * @param Px Position X of the bomb.
     * @param ExpRds Explosionradius of the bomb.
     * @param player The player which sets the bomb.
     */
    public Bomb(int Py, int Px, int ExpRds, Player player)
    {
        super(Py,Px);
        this.setEnable(true);
        this.m_Validity = 3;               // 10 tick per seconds -> 3 seconds to explosion
        this.c_Player = player;
        this.setCollision(true);
        this.m_ExplosionRadius = ExpRds;
    }

    public void decreaseValidity()
    {
        m_Validity=m_Validity-1;
    }
    public int getValidity(){
        return m_Validity;
    }

    public int getExplosionRadius(){
        return m_ExplosionRadius;
    }
    public Player getBombPlanter() {
        return c_Player;
    }

    public void setBombDisable(){
        this.setEnable(false);
    }
}