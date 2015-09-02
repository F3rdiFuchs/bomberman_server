package control;

/**
 * Created by Ferdinand Bauer on 01.06.2015.
 * -control Game room. looking for disconnected clients and delete them from the lobby
 * -set GameStatus ==true if all clients were connected
 */
public class GameRoomControl implements Runnable
{
    private Thread              m_GameRoomControl;
    private GameRoomUser        m_GameRoomUser;
    private GameStatus          m_GameStatus;
    private static final int    m_ServerThrottle = 2000;
    private int                 m_AmountOfPlayer = GameConfig.getInstance().getAmountOfPLayer();

    public void run()
    {
        while(true)
        {
            checkOfflineUser();
            checkGameStatus();
            try
            {
                Thread.sleep(this.m_ServerThrottle);
            }
            catch (Exception e)
            {
                //
            }
        }
    }
    public void start()
    {
        if (this.m_GameRoomControl == null)
        {
            this.m_GameRoomControl = new Thread(this);
            this.m_GameRoomControl.start();
        }
    }

    /**
     * go through the game room, look for offline clients and delete them
     */
    private void checkOfflineUser()
    {
        this.m_GameRoomUser = GameRoomUser.getInstance();
        System.out.println("User in Lobby: " + this.m_GameRoomUser.getListSize());
        for (int Count = 0; Count < this.m_GameRoomUser.getListSize(); Count++)
        {
            if(m_GameRoomUser.getListSize() !=0)
            {

                if (this.m_GameRoomUser.getPlayer(Count).getConnectionStatus() == false)
                {
                    System.out.println("Delete User " + m_GameRoomUser.getPlayer(Count).IO_getPlayer().getPlayerNumber()+" from Lobby!");
                    this.m_GameRoomUser.deletePlayer(Count);
                }
                else
                {
                    // do nothing
                }
            }
        }
    }

    /**
     * count connected clients - if enough clients were connected, set GameStatus == true
     */
    private void checkGameStatus()
    {
        this.m_GameStatus = GameStatus.GetInstance();
        this.m_GameRoomUser = GameRoomUser.getInstance();
        if(this.m_GameRoomUser.getListSize()== this.m_AmountOfPlayer && this.m_GameStatus.getGameIsRunning() == false)
        {
            this.m_GameStatus.setGameStatus(true);
        }
        this.m_GameStatus.getGameStatus();
        this.m_GameStatus.getGameIsRunning();
    }
}
