package control;

/**
 * Created by Ferdinand Bauer on 17.04.2015.
 * -provides information about game status (running, allowed to start)
 *
 */
public class GameStatus
{
    private static GameStatus c_Instance = null;
    private boolean m_GameStatus;
    private boolean m_IsRunning;
    private boolean m_PlayerWereSett= false;

    public static GameStatus GetInstance()
    {
        if(c_Instance ==null){
            c_Instance = new GameStatus();
        }
        return c_Instance;
    }
    public GameStatus()
    {
        this.m_GameStatus = false;
        this.m_PlayerWereSett = false;
    }
    public boolean getGameStatus()
    {
        return m_GameStatus;
    }
    public void setGameStatus(boolean _status)
    {
        m_GameStatus = _status;
    }
    public void setGameRunning(boolean _status)
    {
        m_IsRunning = _status;
    }
    public boolean getGameIsRunning()
    {
        return m_IsRunning;
    }
    public boolean getPlayerWereSet()
    {
        return this.m_PlayerWereSett;
    }
    public void setPlayerWereSet(boolean _Status)
    {
        this.m_PlayerWereSett =_Status;
    }
}
