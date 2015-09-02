package control;

/**
 * Created by Ferdinand Bauer on 10.06.2015.
 * Threat to control game status
 * start game if gameStatus == true and
 * stop game ifg ameStatus == false
 */
public class GameControl implements Runnable
{
    private Thread              m_GameControl;
    private GameStatus          m_GameStatus;
    private GameTickThread      m_MainGame;
    private GameRoomUser        m_GameRoomUser;
    private static final int    m_Server_throttle = 500;
    private Game                m_Game;
    private static int          m_AmountOfPlayer = GameConfig.getInstance().getAmountOfPLayer();

    public void run()
    {
        while(true)
        {
            this.m_GameStatus = GameStatus.GetInstance();
            //if every lobby slot is used by a player, start a new game
            if(this.m_GameStatus.getGameStatus()==true && this.m_GameStatus.getGameIsRunning()== false)
            {
                this.m_MainGame = new GameTickThread();
                this.m_MainGame.start();
                this.m_GameStatus.setGameRunning(true);
                this.m_Game = Game.getInstance();
                this.m_Game.setSpawnPositions();
                this.m_Game.setPlayerOnBoard();
                this.m_GameStatus.setPlayerWereSet(true);
                System.out.println("Game is starting");
            }
            // if all dead or only one player alive then stop the game
            else if(this.m_GameStatus.getGameIsRunning()== true && (playerAlive()==0 | playerAlive()==1))
            {
                this.m_MainGame.endGame();
                this.m_MainGame = null;
                this.m_GameRoomUser.clearList();
                this.m_GameStatus.setGameStatus(false);
                this.m_GameStatus.setGameRunning(false);
                System.out.println("Game ends");
            }
            else
            {
                //do nothing
            }
            try
            {
                Thread.sleep(this.m_Server_throttle);
            }
            catch (Exception e)
            {
                System.out.println("Error GameControl: "+ e);
            }
        }
    }
    public void start()
    {
        if (this.m_GameControl == null)
        {
            this.m_GameControl = new Thread(this);
            this.m_GameControl.start();
        }
    }

    /**
     * count the alive players in the game
     * @return
     */
    private int playerAlive()
    {
        int Temp =0;
        this.m_GameRoomUser = GameRoomUser.getInstance();
        for(int Counter = 0; Counter < m_AmountOfPlayer; Counter++)
        {
            if (this.m_GameRoomUser.getPlayer(Counter).IO_getLifeOrDeath() == true)
            {
                Temp += 1;
            }

        }
        return Temp;
    }
}
