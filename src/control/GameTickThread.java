package control;


/**
 * Created by Ferdinand Bauer on 01.04.2015.
 * timer thread - it allow to execute functions every second
 * with system nanoTime()
 */
public class GameTickThread implements Runnable
{
    private final double        m_AmountOfTicks = 30.0; // Ticks per Second
    private double              m_NanoSecond = 1000000000 / m_AmountOfTicks;
    private double              m_Delta = 0;
    private Thread              m_CGameTickThread;
    private Game                m_Game;
    private BombListTick        m_BombListTick = new BombListTick();
    private GameStatus          m_GameStatus;
    private boolean             m_IsRunning = false;
    private int                 m_Second = 1000;

    public void run()
    {
        m_IsRunning= true;
        long LastTime = System.nanoTime();
        long Timer = System.currentTimeMillis();
        m_GameStatus = GameStatus.GetInstance();
        m_Game = Game.getInstance();

        while (m_IsRunning == true)
        {
            long now = System.nanoTime();
            m_Delta += (now - LastTime) / m_NanoSecond;
            LastTime = now;
            if (m_Delta >= 1)
            {
                m_Delta--;
            }
            if (System.currentTimeMillis() - Timer > m_Second)
            {
                if(m_GameStatus.getGameStatus() == true)
                {
                    Timer += m_Second;
                    tick();
                }
            }
        }
    }
    public void endGame()
    {
        m_IsRunning = false;
    }

    /**
     * insert here instruction which have to be executed every second...
     */
    private void tick()
    {
        m_BombListTick.checkForBombs();
    }

    public void start()
    {
        if (m_CGameTickThread == null)
        {
            m_CGameTickThread = new Thread(this);
            m_CGameTickThread.start();
        }
    }

}
