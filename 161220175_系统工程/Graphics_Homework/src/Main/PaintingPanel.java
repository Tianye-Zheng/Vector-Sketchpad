package Main;

import javax.swing.*;

public class PaintingPanel extends JPanel {

    DrawPanel drawPanel;
    ControlPanel controlPanel;

    PaintingFrame MainFrame; // 所属的主Frame

    public PaintingPanel(PaintingFrame MainFrame)
    {
        //setLayout(new BorderLayout());
        this.MainFrame = MainFrame;

        // 设置画图面板
        drawPanel = new DrawPanel();
        add(drawPanel);//,BorderLayout.WEST
        // 设置控制面板
        controlPanel = new ControlPanel(drawPanel,MainFrame);
        add(controlPanel);//, BorderLayout.EAST
    }
}