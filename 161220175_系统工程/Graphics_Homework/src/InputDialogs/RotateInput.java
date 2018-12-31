package InputDialogs;

import Main.DrawPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class RotateInput implements ActionListener {

    private JFrame inputFrame;
    private JLabel tip,tip1;
    private JTextField tfx, tfy, tfangle;
    private JButton bOK;
    private DrawPanel managedPanel;

    public RotateInput(DrawPanel managedPanel) {
        this.managedPanel = managedPanel;
        // 创建输入框
        inputFrame = new JFrame();
        tip = new JLabel("第一个框轴水平，第二个轴垂直");
        tip1 = new JLabel("第三个度数(整数，正负皆可)");
        tip.setBounds(50,25,200,20);
        tip1.setBounds(50,50,200,20);
        tfx = new JTextField("255");
        tfx.setBounds(50,100,150,20);
        tfy = new JTextField("600");
        tfy.setBounds(50,125,150,20);
        tfangle = new JTextField("0");
        tfangle.setBounds(50,150,150,20);
        bOK = new JButton("确定");
        bOK.setBounds(50, 200, 50, 30);
        bOK.addActionListener(this);
        inputFrame.add(tip);
        inputFrame.add(tip1);
        inputFrame.add(tfx);
        inputFrame.add(tfy);
        inputFrame.add(tfangle);
        inputFrame.add(bOK);
        inputFrame.setSize(300,300);
        inputFrame.setLocationRelativeTo(null);
        inputFrame.setLayout(null);
        inputFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String s1 = tfx.getText();
        String s2 = tfy.getText();
        String s3 = tfangle.getText();
        String numPattern = "[0123456789]*";
        String anglePattern = "[-0123456789]*";
        if((s1!=null)&&(!s1.equals("")) && (s2!=null)&&(!s2.equals("")) && (s3!=null)&&(!s3.equals("")))
        {
            if((Pattern.matches(numPattern,s1))
                    && (Pattern.matches(numPattern,s2))
                    && (Pattern.matches(anglePattern,s3))) // 匹配数字
            {
                int n1 = Integer.parseInt(s1);
                int n2 = Integer.parseInt(s2);
                int n3 = Integer.parseInt(s3);
                managedPanel.Rotation(n1,n2,n3);
                JOptionPane.showMessageDialog(inputFrame,"旋转完成！","成功",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/icons/ok.png"));
            }
            else {
                JOptionPane.showMessageDialog(inputFrame,"无法识别","非法输入",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/icons/info.png"));
            }
        }
    }
}