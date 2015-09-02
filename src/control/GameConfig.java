package control;

/**
 * Created by Ferdinand Bauer on 28.06.2015.
 * Class to save global configurations
 */
public class GameConfig
{
    /**
     * description
     * m_amountOfPlayer = how many player to start game
     *
     */
    private static int            m_AmountOfPlayer = 4;
    private static GameConfig     c_Instance = null;

    public static GameConfig getInstance()
    {
        if(c_Instance == null)
        {
            c_Instance = new GameConfig();
        }
        return c_Instance;
    }

    public int getAmountOfPLayer()
    {
        return m_AmountOfPlayer;
    }
}
