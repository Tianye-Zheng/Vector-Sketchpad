package MultiFunctionActions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpAction implements ActionListener // 帮助界面
{
    @Override
    public void actionPerformed(ActionEvent event) {
        JOptionPane.showMessageDialog(null,"开发：郑天烨\n学号：161220175\n" +
                        "南京大学计算机科学与技术系\ncopyright © 2018\n获得帮助请查看系统使用说明书\n感谢使用！","帮助",
                JOptionPane.PLAIN_MESSAGE);
    }
}