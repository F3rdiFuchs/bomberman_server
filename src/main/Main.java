package main;

import control.*;

import java.net.*;

/**
 * CMain
 * Created by Ferdinand Bauer on 06.12.2014.
 * This class handle the program.
 */
public class Main {

    public static void main(String[] args)
    {
        GameConfig conf = new GameConfig();

        GameRoom gameRoom = new GameRoom();
        gameRoom.start();

        GameControl gameControl = new GameControl();
        gameControl.start();

        GameRoomControl gameRoomControl = new GameRoomControl();
        gameRoomControl.start();
    }
}