package MultiFunctionActions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SavePictureAction implements ActionListener {

    JPanel managedPanel;  // 所管理的Panel
    JFrame frame; // 所管理Panel所在的Frame

    public SavePictureAction(JPanel managedPanel, JFrame MainFrame)
    {
        this.managedPanel = managedPanel;
        this.frame = MainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.showSaveDialog(managedPanel);
        File file = jfc.getSelectedFile();
        if(file == null)
            return;
        String path = file.getPath() + ".jpg"; // 画图路径
        boolean success = true;
        try {
            BufferedImage image = new Robot().createScreenCapture( new Rectangle(
                    frame.getX(),frame.getY()+22,
                    1026,778 // 手工校准，边缘不一定准
            ));
            ImageIO.write(image,"jpg",new File(path));
        } catch (AWTException e0) {
            success = false;
            JOptionPane.showMessageDialog(null,"图片保存失败","错误",
                    JOptionPane.ERROR_MESSAGE, new ImageIcon("resources/icons/fail.png"));
            e0.printStackTrace();
        } catch (IOException e1) {
            success = false;
            JOptionPane.showMessageDialog(null,"图片保存失败","错误",
                    JOptionPane.ERROR_MESSAGE, new ImageIcon("resources/icons/fail.png"));
            e1.printStackTrace();
        }
        if(success) {
            JOptionPane.showMessageDialog(null,"图片保存成功","提示",
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/icons/success.png"));
        }
    }

}
