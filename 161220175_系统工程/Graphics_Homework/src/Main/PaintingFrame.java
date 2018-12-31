package Main;

import javax.swing.*;


public class PaintingFrame extends JFrame {

    private PaintingPanel mainPanel;

    public PaintingFrame()
    {
        mainPanel = new PaintingPanel(this );
        add(mainPanel);
    }

}
