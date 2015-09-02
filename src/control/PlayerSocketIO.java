package control;

import model.IBoard;
import model.playground.*;
import model.player.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ferdinand Bauer on 25.06.2015.
 * -every connected client gets an own PlayerSocketIO
 * -establishes communication between server and client
 */
public class PlayerSocketIO
{
    private boolean             m_Connected;
    private Socket              m_Socket;
    private static final int    m_UserThrottle = 50;
    private static final int    m_UserThrottle2 = 100;
    private static final int    m_ArraySizeY = 10;
    private static final int    m_ArraySizeX = 15;
    private static final int    m_ArraySizeZ = 3;
    private static final int    m_TimeOutCounter = 25;
    private static final int    m_TimeOutTick = 4;
    private static final int    m_InputCharSize = 200;
    private Inport              m_Inport;
    private Outport             m_Outport;
    private static PrintWriter  m_Out;
    private GameStatus          m_GameStatus;
    private GameControl         m_GameControl;
    private GameRoomUser        m_GameRoomUser;
    private String              m_Message;
    private Boolean             m_StartSend;
    private Playground          m_Playground;
    private IBoard              m_TempObject;
    private Player              m_PLayer;
    private Game                m_Game;
    private boolean             m_TempStatus;
    private static String       m_Compare1 = "_move_";
    private static String       m_Compare2 = "_bomb_";
    private static String       m_EndString = "_endg_";
    private static String       m_Compare3 = "_aliv_";
    private int                 m_AliveCounter;
    private int                 m_RoundCounter;
    private int                 m_AmountOfPlayer =  GameConfig.getInstance().getAmountOfPLayer();

    public PlayerSocketIO(int _PosY, int _PosX, int _PlayerNumber, Socket _NewSocket)
    {
        this.m_PLayer = new Player(_PosY,_PosX,_PlayerNumber);
        this.m_Game = Game.getInstance();
        this.m_Socket = _NewSocket;
        this.m_Connected = true;
        this.m_StartSend = false;
        this.m_TempStatus = false;
        this.m_AliveCounter = 0;
        this.m_RoundCounter = 0;

        m_Inport = new Inport();
        m_Inport.start();

        m_Outport = new Outport();
        m_Outport.start();
    }
    public Player IO_getPlayer()
    {
        return this.m_PLayer;
    }
    public int IO_getPosX()
    {
        return m_PLayer.getPosX();
    }
    public int IO_getPosY()
    {
        return this.m_PLayer.getPosY();
    }
    public void IO_setPosX(int _PosX)
    {
        this.m_PLayer.setPosX(_PosX);
    }
    public void IO_setPosY(int _PosY)
    {
        this.m_PLayer.setPosY(_PosY);
    }
    public int IO_getKillCount()
    {
        return this.m_PLayer.getKillCount();
    }
    public boolean IO_getLifeOrDeath()
    {
        return this.m_PLayer.getLifeOrDeath();
    }

    /**
     * one output port thread for every player.
     */
    private class Inport extends Thread
    {
        public void run()
        {
            while (true)
            {
                if (getConnectionStatus() == true)
                {
                    try
                    {
                        m_GameStatus = GameStatus.GetInstance();
                        if (m_GameStatus.getGameStatus() == false)
                        {
                            m_Out = new PrintWriter(m_Socket.getOutputStream(), true);
                            m_Out.println(_waitList());// game not ready to start
                        }
                        else if (m_GameStatus.getGameStatus() == true)
                        {
                            //convert playground into stream
                            m_Out = new PrintWriter(m_Socket.getOutputStream(), true);
                            if (m_StartSend == false)
                            {
                                m_Out.println("_rung_");
                                m_Out.println("_plnr_" + m_PLayer.getPlayerNumber());
                                m_StartSend = true;
                            }
                            else
                            {   if(m_GameStatus.getPlayerWereSet()==true)
                                {
                                    m_Out.println(_playgroundToString());// send playground
                                    m_Out.println(_killedU());          // if dead send dead
                                    m_Out.println(_playerAlive());      // wer lebt und wer ist tot
                                    m_Out.println(_scoreBoard());       // playerscore
                                    m_Out.println(_mPos());             // eigene Position
                                    if(m_PLayer.getLifeOrDeath() == false && m_TempStatus == false)// ist der Spieler tot ...
                                    {
                                        m_Out.println(_killedU());
                                        m_TempStatus = true;
                                    }
                                    else if(m_GameStatus.getGameStatus() == true && (playerAlive() == 0 | playerAlive() == 0)) //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                    {
                                        m_Out.println(_endGame());
                                    }
                                }
                            }
                        }
                        Thread.sleep(m_UserThrottle2);
                    }
                    catch (Exception e)
                    {
                        m_StartSend = false;
                        m_Connected = false;
                        try
                        {
                            m_Socket.close();
                        }
                        catch (IOException f)
                        {
                            System.out.println("Error: " + f);
                        }
                    }
                }

            }

        }
    }

    /**
     * counts alive player in the game
     * @return
     */
    private int playerAlive()
    {
        int Temp =0;
        this.m_GameRoomUser = GameRoomUser.getInstance();
        for(int Count = 0; Count < m_AmountOfPlayer; Count++)
        {
            if(this.m_GameRoomUser.getListSize() > Count)
            {
                if (this.m_GameRoomUser.getPlayer(Count).IO_getLifeOrDeath()==true)
                {
                    Temp+=1;
                }
            }
        }
        return Temp;
    }

    /**
     * @author Ferdinand Bauer
     * one input port thread for every player.
     */
    private class Outport extends Thread
    {
        public void run()
        {
            while(true)
            {
                if (m_RoundCounter >= m_TimeOutCounter)
                {
                    if (m_AliveCounter >= m_TimeOutTick)
                    {
                            m_AliveCounter = 0;
                    }
                    else if (m_AliveCounter < m_TimeOutTick)
                    {
                        m_Connected = false;
                    }
                    m_RoundCounter = 0;
                }
                if (getConnectionStatus() == true)
                {
                    try
                    {
                        readMessage();
                        if (m_PLayer.getLifeOrDeath() == true)
                        {
                            if (m_Message.substring(0, 6).equals(m_Compare1)) // _move_
                            {
                                if (m_Message.substring(6, 7).equals("r")) // move_r
                                {
                                    m_Game.playerMoveRight(m_PLayer);
                                } else if (m_Message.substring(6, 7).equals("l"))
                                {
                                    m_Game.playerMoveLeft(m_PLayer);
                                } else if (m_Message.substring(6, 7).equals("u"))
                                {
                                    m_Game.playerMoveUp(m_PLayer);
                                } else if (m_Message.substring(6, 7).equals("d"))
                                {
                                    m_Game.playerMoveDown(m_PLayer);
                                }
                            } else if (m_Message.substring(0, 6).equals(m_Compare2))
                            {
                                m_Game.playerSetBomb(m_PLayer);
                            } else if (m_Message.substring(0, 6).equals(m_Compare3))
                            {
                                m_AliveCounter++;
                            }
                        }
                        Thread.sleep(m_UserThrottle);
                        m_Message = "";
                    } catch (Exception e)
                    {

                    }
                }
                m_RoundCounter++;
            }
        }
    }

    /**
     * protocol: m_EndString
     * @return
     */
    private String _endGame()
    {
        return this.m_EndString;
    }

    /**
     * protocol: _plag_
     * convert the 3 dimensional array into s sting chain to transmit over network
     *?
     * @return
     */
    private String _playgroundToString()
    {
        this.m_Playground = Playground.getInstance();
        String TmpString = "_plag_";
        for(int CountY = 0; CountY < m_ArraySizeY; CountY++) //Y
        {
            for(int CountX = 0; CountX < m_ArraySizeX; CountX++)//X
            {
                for(int CountZ = 0; CountZ < m_ArraySizeZ; CountZ++)//Z
                {
                    this.m_TempObject = m_Playground.getCell(CountZ, CountY, CountX);

                    if(m_Playground.getCell(CountZ, CountY, CountX).GetObject() instanceof Block)
                    {
                        TmpString= TmpString+"B";
                    }
                    else if(this.m_Playground.getCell(CountZ, CountY, CountX).GetObject() instanceof Wall)
                    {
                        TmpString= TmpString+"W";
                    }
                    else if((this.m_Playground.getCell(CountZ, CountY, CountX).GetObject() instanceof PowerUp)&& (((PowerUp) this.m_Playground.getCell(CountZ, CountY, CountX).GetObject()).getItemStatus()== true))
                    {
                        TmpString= TmpString+"P";
                    }
                    else if((this.m_Playground.getCell(CountZ, CountY, CountX).GetObject() instanceof PowerUp)&& (((PowerUp) this.m_Playground.getCell(CountZ, CountY, CountX).GetObject()).getItemStatus()== false))
                    {
                        TmpString= TmpString+"p";
                    }
                    else if(this.m_Playground.getCell(CountZ, CountY, CountX).GetObject() instanceof Player)
                    {
                        Player Temp = (Player) this.m_Playground.getCell(0, CountY, CountX);

                        if(Temp.getPlayerNumber() == 1)
                        {
                            TmpString = TmpString + "1";
                        }
                        else if(Temp.getPlayerNumber() == 2)
                        {
                            TmpString = TmpString + "2";
                        }
                        else if(Temp.getPlayerNumber() == 3)
                        {
                            TmpString = TmpString + "3";
                        }
                        else if(Temp.getPlayerNumber() == 4)
                        {
                            TmpString = TmpString + "4";
                        }
                        else
                        {
                            TmpString = TmpString + "e";
                        }

                    }
                    else if((this.m_Playground.getCell(CountZ, CountY, CountX).GetObject() instanceof Bomb) && (((Bomb) this.m_Playground.getCell(CountZ, CountY, CountX).GetObject()).getItemStatus()== true))
                    {
                        TmpString= TmpString+"O";
                    }
                    else if((this.m_Playground.getCell(CountZ, CountY, CountX).GetObject() instanceof Bomb) && (((Bomb) this.m_Playground.getCell(CountZ, CountY, CountX).GetObject()).getItemStatus()== false))
                    {
                        TmpString= TmpString+"o";
                    }
                    else
                    {
                        TmpString= TmpString+"_";
                    }
                }
            }
        }
        return TmpString;
    }

    /**
     * protocol: _wait_
     * convert the player status into string chain to transmit over network
     *
     * @return
     */
    private String _waitList()
    {
        String TmpString = "_wait_";
        this.m_GameRoomUser = GameRoomUser.getInstance();
        for(int Count = 0; Count < m_AmountOfPlayer; Count++)
        {
            if(this.m_GameRoomUser.getListSize() > Count)
            {
                if (this.m_GameRoomUser.getPlayer(Count).getConnectionStatus() == true)
                {
                    TmpString = TmpString+"t";
                }
                else if (this.m_GameRoomUser.getPlayer(Count).getConnectionStatus() == false)
                {
                    TmpString = TmpString+"f";
                }
                else
                {
                    TmpString = TmpString+"_";
                }
            }
            else
            {
                TmpString = TmpString+"f";
            }
        }
        return TmpString;
    }

    /**
     * protocol: _scor_
     * convert the sore of the players into string chain to transmit over network
     *
     * @return
     */
    private String _scoreBoard()
    {
        String TmpString = "_scor_";
        this.m_GameRoomUser = GameRoomUser.getInstance();
        for(int Count = 0; Count < m_AmountOfPlayer; Count++)
        {
            if(m_GameRoomUser.getListSize() > Count)
            {
                TmpString = TmpString + this.m_GameRoomUser.getPlayer(Count).IO_getKillCount();
            }
        }
        return TmpString;
    }

    /**
     * protocol: _plal_
     * convert the life status of the players into string chain to transmit over network
     *
     * @return
     */
    private String _playerAlive()
    {
        String TmpString = "_plal_";
        this.m_GameRoomUser = GameRoomUser.getInstance();
        for(int Count = 0; Count < m_AmountOfPlayer; Count++)
        {
            if(this.m_GameRoomUser.getListSize() > Count)
            {
                if (this.m_GameRoomUser.getPlayer(Count).IO_getLifeOrDeath() == true)
                {
                    TmpString = TmpString+"t";
                }
                else if (this.m_GameRoomUser.getPlayer(Count).IO_getLifeOrDeath() == false)
                {
                    TmpString = TmpString+"f";
                }
                else
                {
                    TmpString = TmpString+"_";
                }
            }
        }
        return TmpString;
    }

    /**
     * protocol: _kill_
     * transmit the death of this player over network
     *
     * @return
     */
    private String _killedU()
    {
        String TmpString = "_kill_";
        String TmpString2= "";
        if(this.IO_getLifeOrDeath() == false)
        {
            return TmpString;
        }
        else
        {
            return TmpString2;
        }
    }

    /**
     * protocol: _mpos_
     * convert the position of the player into string chain to transmit over network
     *
     * @return
     */
    private String _mPos()
    {
        String TmpString;
        TmpString = "_mpos_"+this.IO_getPosY()+"_"+this.IO_getPosX();
        return TmpString;
    }

    public boolean getConnectionStatus()
    {
        return this.m_Connected;
    }

    /**
     * waiting for incoming messages
     * @throws IOException
     */
    public void readMessage() throws IOException
    {
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.m_Socket.getInputStream()));
            char[] Buffer = new char[m_InputCharSize];
            int AmountOfChar = bufferedReader.read(Buffer, 0, m_InputCharSize); // blockiert bis Nachricht empfangen
            this.m_Message = new String(Buffer, 0, AmountOfChar);
        }
        catch (IOException e)
        {

        }
    }
}

