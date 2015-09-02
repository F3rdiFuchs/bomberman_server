
package control;

import model.IBoard;
import model.playground.*;
import java.util.LinkedList;

/**
 * CBombList
 * Created by Ferdinand Bauer on 06.01.2015.
 * This class takes bombs into a list and count down the validity
 *
 */
public class BombList
{
    private static BombList     c_Instance = null;
    private LinkedList<Bomb>    l_BombList = new LinkedList<Bomb>();
    private IBoard              m_TempObject;
    private Playground          m_Playground;
    private Game m_Game =       Game.getInstance();
    private GameRoomUser        m_GameRoomUser;
    private static int          m_AmountOfPlayer = GameConfig.getInstance().getAmountOfPLayer();

    public static BombList getInstance() {
        if(c_Instance == null) {
            c_Instance = new BombList();
        }
        return c_Instance;
    }

    /**
     * put a bomb into the list
     *
     * @param pBomb bomb
     */
    public void addBomb(Bomb pBomb)
    {
        this.l_BombList.addFirst(pBomb);
    }

    /**
     * go through the list, check bomb validity. if validity = 0 bomb
     * explode and remove from list
     */
    public void CheckBombs()
    {
        for(int num=0; num < this.l_BombList.size(); num++){                        //repeat until list end
            if(this.l_BombList.get(num).getValidity()==0)
            {
                bombExplode(this.l_BombList.get(num));                               //explode bomb
                this.l_BombList.remove(this.l_BombList.get(num));                         //remove bomb out of the list
            }
            else
            {
                this.l_BombList.get(num).decreaseValidity();
            }
        }
    }

    /**
     * execute field checks and disable bomb
     * @param pBomb bomb
     */
    private void bombExplode(Bomb pBomb)
    {
        this.checkSetField(pBomb);
        this.checkLeftField(pBomb);
        this.checkRightField(pBomb);
        this.checkDownField(pBomb);
        this.checkUpField(pBomb);

        pBomb.setBombDisable();
    }

    /**
     * check field where bomb was placed
     * @param pBomb bomb
     */
    private void checkSetField(Bomb pBomb)
    {
        this.m_Playground = Playground.getInstance();
        int Px = pBomb.getItemPositionX();
        int Py = pBomb.getItemPositionY();
        int PlayerValue = checkFieldForPlayer(Py, Px);                          //0-Playernumber +1 no player
        if(PlayerValue < this.m_AmountOfPlayer)
        {                             // is her any PLayer? If no -> nothing happens
            this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPlayer().setLifeOrDeath(false);
            m_Playground.setDisabledItem(this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosY(),
                    this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosX());
            pBomb.getBombPlanter().raiseKillCount();
        }
    }

    /**
     * check left fields in the explosion radius
     * destroy Block and stop, or kill player in radius
     * @param pBomb bomb
     */
    private void checkLeftField(Bomb pBomb)
    {
        this.m_GameRoomUser = GameRoomUser.getInstance();
        m_Playground = Playground.getInstance();

        int CountRound = 1;

        int Px = pBomb.getItemPositionX();
        int Py = pBomb.getItemPositionY();
        int ExpRds = pBomb.getExplosionRadius();

        while(Px !=0 && Px !=15 && CountRound <= ExpRds){
            this.m_TempObject = this.m_Playground.getCell(0, Py, Px - CountRound);      // cell content
            if(this.m_TempObject instanceof Wall) {
                break;                                                          //cancel
            } else if(this.m_TempObject instanceof Block) {
                this.m_Playground.destroyCellItem(0, Py, Px - CountRound);
                break;                                                          //cancel
            } else {
                int PlayerValue = checkFieldForPlayer(Py, Px - CountRound);       //0-Playernumber +1 no player
                if(PlayerValue < this.m_AmountOfPlayer)                      // is her any PLayer? If no -> nothing happens
                {
                    this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPlayer().setLifeOrDeath(false);
                    this.m_Playground.setDisabledItem(this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosY(),
                            this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosX());
                    pBomb.getBombPlanter().raiseKillCount();
                }
                CountRound++;
            }
        }
    }

    /**
     * check right fields in the explosion radius
     * destroy Block and stop, or kill player in radius
     * @param pBomb bomb
     */
    private void checkRightField(Bomb pBomb)
    {
        this.m_GameRoomUser = GameRoomUser.getInstance();
        this.m_Playground = Playground.getInstance();

        int CountRound = 1;

        int Px = pBomb.getItemPositionX();
        int Py = pBomb.getItemPositionY();
        int ExpRds = pBomb.getExplosionRadius();

        while(Px !=0 && Px !=15 && CountRound <= ExpRds){
            this.m_TempObject = this.m_Playground.getCell(0, Py, Px + CountRound);      // cell content
            if(this.m_TempObject instanceof Wall) {
                break;                                                           //cancel
            }else if(this.m_TempObject instanceof Block) {
                this.m_Playground.destroyCellItem(0, Py, Px + CountRound);
                break;                                                           //cancel
            } else {
                int PlayerValue = checkFieldForPlayer(Py, Px + CountRound);       //0-Playernumber +1 no player
                if(PlayerValue < this.m_AmountOfPlayer)                      // is her any PLayer? If no -> nothing happens
                {
                    this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPlayer().setLifeOrDeath(false);
                    this.m_Playground.setDisabledItem(this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosY(),
                            this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosX());
                    pBomb.getBombPlanter().raiseKillCount();
                }
                CountRound++;
            }
        }
    }

    /**
     * check up fields in the explosion radius
     * destroy Block and stop, or kill player in radius
     * @param pBomb bomb
     */
    private void checkUpField(Bomb pBomb)
    {
        this.m_GameRoomUser = GameRoomUser.getInstance();
        this.m_Playground = Playground.getInstance();

        int CountRound = 1;

        int Px = pBomb.getItemPositionX();
        int Py = pBomb.getItemPositionY();
        int ExpRds = pBomb.getExplosionRadius();

        while(Px !=0 && Px !=15 && CountRound <= ExpRds){
            this.m_TempObject = this.m_Playground.getCell(0, Py - CountRound, Px);    //cell content
            if(this.m_TempObject instanceof Wall) {
                break;                                                          //cancel
            }else if(this.m_TempObject instanceof Block) {
                this.m_Playground.destroyCellItem(0, Py - CountRound, Px);
                break;                                                          //cancel
            } else {
                int PlayerValue = checkFieldForPlayer(Py - CountRound, Px);     //0-Playernumber +1 no player
                if(PlayerValue < this.m_AmountOfPlayer)                      // is her any PLayer? If no -> nothing happens
                {
                    this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPlayer().setLifeOrDeath(false);
                    this.m_Playground.setDisabledItem(this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosY(),
                            this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosX());
                    pBomb.getBombPlanter().raiseKillCount();
                }
                CountRound++;
            }
        }
    }

    /**
     * check down fields in the explosion radius
     * destroy Block and stop, or kill player in radius
     * @param pBomb bomb
     */
    private void checkDownField(Bomb pBomb)
    {
        this.m_GameRoomUser = GameRoomUser.getInstance();
        this.m_Playground = Playground.getInstance();

        int CountRound = 1;

        int Px = pBomb.getItemPositionX();
        int Py = pBomb.getItemPositionY();
        int ExpRds = pBomb.getExplosionRadius();

        while(Px !=0 && Px !=15 && CountRound <= ExpRds){
            this.m_TempObject = this.m_Playground.getCell(0, Py + CountRound, Px);    // cell content
            if(this.m_TempObject instanceof Wall){
                break;                                                          //cancel
            }else if(this.m_TempObject instanceof Block) {
                this.m_Playground.destroyCellItem(0, Py + CountRound, Px);
                break;                                                          //cancel
            }else {
                int PlayerValue = checkFieldForPlayer(Py + CountRound, Px);     //0-Playernumber +1 no player
                if(PlayerValue < this.m_AmountOfPlayer)                      // is her any PLayer? If no -> nothing happens
                {
                    this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPlayer().setLifeOrDeath(false);
                    this.m_Playground.setDisabledItem(this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosY(),
                            this.m_GameRoomUser.getPlayer(PlayerValue).IO_getPosX());
                    pBomb.getBombPlanter().raiseKillCount();
                }
                CountRound++;
            }
        }
    }

    /**
     * check field for player - if player is on field kill himF
     * @param _Py position bomb
     * @param _Px position bomb
     * @return
     */
    private int checkFieldForPlayer(int _Py,int _Px)
    {
        this.m_GameRoomUser = GameRoomUser.getInstance();
        int PlayerCount =0;
        while(PlayerCount < this.m_AmountOfPlayer)
        {
            if(this.m_GameRoomUser.getPlayer(PlayerCount).IO_getPosY()==_Py && this.m_GameRoomUser.getPlayer(PlayerCount).IO_getPosX()==_Px)
            {
                return PlayerCount;
            }
            PlayerCount++;
        }
        return PlayerCount;
    }

}
