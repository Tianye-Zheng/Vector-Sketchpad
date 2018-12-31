package Main;

import Util.PlayMusic;

import javax.swing.*;
import java.awt.*;

public class MainTest {

    // 默认窗口大小
    public static final int DEFAULT_WIDTH = 1200;
    public static final int DEFAULT_HEIGHT = 800;

    public static void main(String[] args) {
        EventQueue.invokeLater(()->
        {
            JFrame frame = new PaintingFrame();
            frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);           // 设置默认窗口大小
            frame.setTitle("天烨的画板");                           // 设置标题
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 默认关闭操作
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        new PlayMusic().playMusic("/resources/audios/Nutcracker.mov");
    }

}
