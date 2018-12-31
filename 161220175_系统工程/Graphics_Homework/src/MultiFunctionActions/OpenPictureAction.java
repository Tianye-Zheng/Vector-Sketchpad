package MultiFunctionActions;

import FileFilters.ImageFileFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class OpenPictureAction implements ActionListener {

    JPanel managedPanel;  // 所管理的Panel

    public OpenPictureAction(JPanel managedPanel) { this.managedPanel = managedPanel; }

    @Override
    public void actionPerformed(ActionEvent event) {

        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new ImageFileFilter());
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showOpenDialog(managedPanel);
        File file = jfc.getSelectedFile();
        if(file == null)
            return;
        String path = file.getPath(); // 读图路径
        boolean success = true;
        try {

            Image image = ImageIO.read(new File(path));
            managedPanel.getGraphics().drawImage(
                    image,0,0,managedPanel.getWidth(),managedPanel.getHeight(),null
            );

        } catch (IOException e) {
            success = false;
            JOptionPane.showMessageDialog(null,"图片读取失败","错误",
                    JOptionPane.ERROR_MESSAGE, new ImageIcon("resources/icons/fail.png"));
            e.printStackTrace();
        }
        if(success) {
            JOptionPane.showMessageDialog(null,"图片读取成功","提示",
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/icons/success.png"));
        }
    }

}