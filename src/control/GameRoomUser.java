package control;

import java.util.ArrayList;

/**
 * Created by Ferdinand Bauer on 04.06.2015.
 * Class game room - array list of player socket instances
 */
public class GameRoomUser
{
    private static GameRoomUser         c_Instance = null;
    private ArrayList<PlayerSocketIO>   m_PlayerList = new ArrayList<PlayerSocketIO>();

    public static GameRoomUser getInstance() {
        if(c_Instance == null) {
            c_Instance = new GameRoomUser();
        }
        return c_Instance;
    }
    public int getListSize()
    {
        return m_PlayerList.size();
    }
    public PlayerSocketIO getPlayer(int position)
    {
        return m_PlayerList.get(position);
    }
    public void setPlayer(PlayerSocketIO _player)
    {
        m_PlayerList.add(_player);
    }

    /**
     * delete client n from array list
     * @param position
     */
    public void deletePlayer(int position)
    {
        m_PlayerList.remove(position);
    }

    /**
     * delete all clients
     */
    public void clearList()
    {
        for(int Count =0; Count < m_PlayerList.size();Count++)
        {
            m_PlayerList.remove(Count);
        }
    }
}
