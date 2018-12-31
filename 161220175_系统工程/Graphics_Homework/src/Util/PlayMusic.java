package Util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class PlayMusic extends Thread{

    private String path;

    public void run() {
        try {

            BufferedInputStream buffer = new BufferedInputStream(
                    getClass().getResourceAsStream(path));
            new Player(buffer).play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMusic(String path)
    {
        this.path = path;
        start();
    }
}
