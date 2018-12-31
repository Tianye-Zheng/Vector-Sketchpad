package InputDialogs;

import Main.DrawPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class ZoomInput implements ActionListener {

    private JFrame inputFrame;
    private JLabel tip,tip1;
    private JTextField Sx, Sy;
    private JButton bOK;
    private DrawPanel managedPanel;

    public ZoomInput(DrawPanel managedPanel) {

        this.managedPanel = managedPanel;
        // 创建输入框
        inputFrame = new JFrame();
        tip = new JLabel("第一个框x方向缩放系数，第二");
        tip1 = new JLabel("个框y方向缩放系数(非负数)");
        tip.setBounds(50,25,200,20);
        tip1.setBounds(50,50,200,20);
        Sx = new JTextField();
        Sx.setBounds(50,100,150,20);
        Sy = new JTextField();
        Sy.setBounds(50,150,150,20);
        bOK = new JButton("确定");
        bOK.setBounds(50, 200, 50, 30);
        bOK.addActionListener(this);
        inputFrame.add(tip);
        inputFrame.add(tip1);
        inputFrame.add(Sx);
        inputFrame.add(Sy);
        inputFrame.add(bOK);
        inputFrame.setSize(300,300);
        inputFrame.setLocationRelativeTo(null);
        inputFrame.setLayout(null);
        inputFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String s1 = Sx.getText();
        String s2 = Sy.getText();
        String numPattern = "[-.0123456789]*";
        if((s1!=null)&&(!s1.equals("")) && (s2!=null)&&(!s2.equals("")))
        {
            if((Pattern.matches(numPattern,s1)) && (Pattern.matches(numPattern,s2))) // 匹配数字
            {
                double n1 = Double.parseDouble(s1);
                double n2 = Double.parseDouble(s2);
                managedPanel.Scaling(n1,n2);
                JOptionPane.showMessageDialog(inputFrame,"缩放完成！","成功",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/icons/ok.png"));
            }
        }
        else {
            JOptionPane.showMessageDialog(inputFrame,"无法识别","非法输入",
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/icons/info.png"));
        }
    }
}
