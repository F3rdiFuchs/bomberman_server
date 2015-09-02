package control;

import java.io.IOException;
import java.net.*;

/**
 * Created by Ferdinand Bauer on 05.04.2015.
 * Thread which wait for incoming client connection requests and add them into
 * the game lobby
 */
public class GameRoom extends Thread
{
    private static GameRoom     c_Instance = null;
    private static final int    m_Port = 1234;              // server port
    private static final int    m_Throttle = 200;
    private ServerSocket        m_ServerSocket;
    private InetAddress         m_HostAdress;
    private Socket              m_Socket;
    private GameRoomUser        m_GameRoomUser;
    private GameStatus          m_GameStatus;

    public static GameRoom getInstance() {
        if(c_Instance == null)
        {
            c_Instance = new GameRoom();
        }
        return c_Instance;
    }

    /**
     * create a server socket whereto clients could send connection requests
     */
    public GameRoom()
    {
        this.m_GameRoomUser = GameRoomUser.getInstance();
        try
        {
            this.m_HostAdress = InetAddress.getLocalHost();
        }
        catch(UnknownHostException e)
        {
            System.out.println("Error " +e);
            return;
        }
        System.out.println("Server address is: "+ m_HostAdress);
        try
        {
            this.m_ServerSocket = new ServerSocket(this.m_Port,0,this.m_HostAdress);
        }
        catch(IOException e)
        {
            System.out.println("Could not open server socket:" + e);
            return;
        }
        System.out.println("Socket " + this.m_ServerSocket + " created");
    }

    /**
     * waiting for incoming client connection requests.
     */
    public void run()
    {
        while(true)
        {
            try
            {
                this.m_Socket = m_ServerSocket.accept();
                this.addPLayer();
            }
            catch(IOException e)
            {
                System.out.println("Error: "+e);
            }

            try
            {
                Thread.sleep(m_Throttle);
            }
            catch(InterruptedException e)
            {
                System.out.println("Error: "+e);
            }
        }
    }

    /**
     * after incoming request:
     * if game is not running add player to game lobby and
     * create its communication thread
     */
    private void addPLayer()
    {
        this.m_GameRoomUser = GameRoomUser.getInstance();
        this.m_GameStatus = GameStatus.GetInstance();

        if(this.m_GameStatus.getGameStatus() == false)
        {
            if (this.m_GameRoomUser.getListSize() == 0)
            {
                this.m_GameRoomUser.setPlayer(new PlayerSocketIO(0, 0, 1, this.m_Socket));//Player 1
            }
            else if (this.m_GameRoomUser.getListSize() == 1)
            {
                this.m_GameRoomUser.setPlayer(new PlayerSocketIO(0, 0, 2, this.m_Socket));////Player 2
            }
            else if (this.m_GameRoomUser.getListSize() == 2)
            {
                this.m_GameRoomUser.setPlayer(new PlayerSocketIO(0, 0, 3, this.m_Socket));////Player 3
            }
            else if (this.m_GameRoomUser.getListSize() == 3)
            {
                this.m_GameRoomUser.setPlayer(new PlayerSocketIO(0, 0, 4, this.m_Socket));////Player 4
            }
            else
            {
                // do nothing - connection not accepted
            }
        }
    }
}
