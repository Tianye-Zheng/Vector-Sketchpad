package InputDialogs;

import Main.DrawPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class TranslationInput implements ActionListener {

    private JFrame inputFrame;
    private JLabel tip,tip1;
    private JTextField tfx, tfy;
    private JButton bOK;
    private DrawPanel managedPanel;

    public TranslationInput(DrawPanel managedPanel) {
        this.managedPanel = managedPanel;
        // 创建输入框
        inputFrame = new JFrame();
        tip = new JLabel("第一个框水平，第二个框垂直");
        tip1 = new JLabel("(整数，正负皆可)");
        tip.setBounds(50,25,200,20);
        tip1.setBounds(50,50,200,20);
        tfx = new JTextField();
        tfx.setBounds(50,100,150,20);
        tfy = new JTextField();
        tfy.setBounds(50,150,150,20);
        bOK = new JButton("确定");
        bOK.setBounds(50, 200, 50, 30);
        bOK.addActionListener(this);
        inputFrame.add(tip);
        inputFrame.add(tip1);
        inputFrame.add(tfx);
        inputFrame.add(tfy);
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
        String numPattern = "[-0123456789]*";
        if((s1!=null)&&(!s1.equals("")) && (s2!=null)&&(!s2.equals("")))
        {
            if((Pattern.matches(numPattern,s1)) && (Pattern.matches(numPattern,s2))) // 匹配数字
            {
                int n1 = Integer.parseInt(s1);
                int n2 = Integer.parseInt(s2);
                managedPanel.Translation(n1,n2);
                JOptionPane.showMessageDialog(inputFrame,"平移完成！","成功",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/icons/ok.png"));
            }
            else {
                JOptionPane.showMessageDialog(inputFrame,"无法识别","非法输入",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/icons/info.png"));
            }
        }
    }
}
