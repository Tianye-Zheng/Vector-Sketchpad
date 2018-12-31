package Main;

import InputDialogs.RotateInput;
import InputDialogs.TranslationInput;
import InputDialogs.ZoomInput;
import MultiFunctionActions.HelpAction;
import MultiFunctionActions.OpenPictureAction;
import MultiFunctionActions.SavePictureAction;
import FileFilters.OffFileFilter;
import MultiFunctionActions.Show3DGraph;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.regex.Pattern;

public class ControlPanel extends JPanel {

    // 所管理的绘画区
    private DrawPanel managedPanel;

    // 所属主 Frame
    PaintingFrame MainFrame;

    // 工具区
    private JPanel toolPanel;
    // 颜色区
    private JPanel colorPanel;
    JLabel DrawColorLabel;     // 显示绘制颜色
    JLabel FillColorLabel;     // 显示填充颜色
    boolean currentLabelFocus; // 选中那个颜色标签，为真时是绘制颜色标签，为假时为填充颜色标签
    JButton sizeBu;  // 改变画笔大小的按钮
    // 多功能区
    private JPanel multiFunctionPanel;


    // 初始化各个区
    private void ToolPanelsInitialize()
    {
        // 初始化工具区
        addToolButton("清理画板","/resources/icons/clean.png",new CleanPanelAction(),toolPanel);
        addToolButton("画笔工具","/resources/icons/pen.png",new RandomDrawAction(),toolPanel);
        addToolButton("直线工具","/resources/icons/line.png",new DrawLineAction(),toolPanel);
        addToolButton("矩形工具","/resources/icons/rectangle.png",new RectangleAction(),toolPanel);
        addToolButton("圆工具","/resources/icons/circle.png",new CircleAction(),toolPanel);
        addToolButton("椭圆工具","/resources/icons/ellipse.png",new EllipseAction(),toolPanel);
        addToolButton("多边形工具","/resources/icons/polygon.png",new PolygonAction(),toolPanel);
        addToolButton("填充多边形工具","/resources/icons/fillpolygon.png",new FillPolygonAction(),toolPanel);
        addToolButton("Bezier曲线工具","/resources/icons/Bezier.png",new BezierCurveAction(),toolPanel);
        addToolButton("二次B样条曲线工具","/resources/icons/BSpine.png",new BspineCurveAction(),toolPanel);
        Border etched = BorderFactory.createEtchedBorder();
        toolPanel.setBorder(etched);

        // 初始化颜色区
        currentLabelFocus = true; // 默认颜色选择器改变的是绘制颜色
        DrawColorLabel = new JLabel();
        DrawColorLabel.setPreferredSize(new Dimension(25,25));
        DrawColorLabel.setOpaque(true);
        DrawColorLabel.setBackground(managedPanel.getDrawColor());
        colorPanel.add(DrawColorLabel);

        DrawColorLabel.setToolTipText("绘制色");

        FillColorLabel = new JLabel();
        FillColorLabel.setPreferredSize(new Dimension(20,20));
        FillColorLabel.setOpaque(true);
        FillColorLabel.setBackground(managedPanel.getFillColor());
        colorPanel.add(FillColorLabel);

        FillColorLabel.setToolTipText("填充色");

        DrawColorLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentLabelFocus = true;
                DrawColorLabel.setPreferredSize(new Dimension(25,25));
                FillColorLabel.setPreferredSize(new Dimension(20,20));
                colorPanel.revalidate();
            }
        });
        FillColorLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentLabelFocus = false;
                DrawColorLabel.setPreferredSize(new Dimension(20,20));
                FillColorLabel.setPreferredSize(new Dimension(25,25));
                colorPanel.revalidate();
            }
        });

        addToolButton("调色板","/resources/icons/palette.png" ,new ColorChooseAction(),colorPanel);
        colorPanel.setBorder(etched);

        sizeBu = addToolButton("画笔大小(目前仅支持涂鸦调整画笔大小)","", new PenSizeChooseAction(), colorPanel);
        sizeBu.setPreferredSize(new Dimension(70,40));
        sizeBu.setText("1 像素");

        // 初始化多功能区
        addToolButton("剪切","/resources/icons/cut.png", new CutAction(),multiFunctionPanel);
        addToolButton("平移","/resources/icons/translation.png", new TranslateAction(),multiFunctionPanel);
        addToolButton("旋转","/resources/icons/rotate.png", new RotateAction(),multiFunctionPanel);
        addToolButton("缩放","/resources/icons/zoom.png", new ZoomAction(),multiFunctionPanel);
        addToolButton("OFF文件解析","/resources/icons/threeDimension.png", new ShowThreeDimensionalPicture(),multiFunctionPanel);
        addToolButton("载入图片","/resources/icons/openFile.png",new OpenPictureAction(managedPanel),multiFunctionPanel);
        addToolButton("保存为图片","/resources/icons/save.png",new SavePictureAction(managedPanel, MainFrame),multiFunctionPanel);
        addToolButton("帮助","/resources/icons/help.png",new HelpAction(),multiFunctionPanel);

        multiFunctionPanel.setBorder(etched);
    }
    private JButton addToolButton(String label, String iconPath, ActionListener listener, JPanel addToPanel)
    {
        JButton button = new JButton();      // 创建新按钮
        button.addActionListener(listener);  // 添加响应函数
        Icon icon = new ImageIcon();         // 设置图标
        ((ImageIcon) icon).setImage(Toolkit.getDefaultToolkit().
                getImage(getClass().getResource(iconPath)));
        button.setIcon(icon);
        button.setToolTipText(label);        // 设置按钮提示文字
        addToPanel.add(button);              // 加到工具按钮组里
        addToPanel.add(button);              // 加到工具区里
        return button;
    }

    // ************** 整个控制面板的初始化 ******************

    public ControlPanel(DrawPanel managedPanel, PaintingFrame MainFrame)
    {
        this.MainFrame = MainFrame;

        this.setBorder(BorderFactory.createRaisedBevelBorder());
        this.managedPanel = managedPanel; // 添加所管理的面板
        setPreferredSize(new Dimension(170,800)); // 整个东侧控制面板的大小
        //setLayout(new GridLayout(3,1));  // 整个东侧控制面板的布局
        toolPanel = new JPanel();    // 工具区
        toolPanel.setPreferredSize(new Dimension(165,350));
        colorPanel = new JPanel();   // 颜色区
        colorPanel.setPreferredSize(new Dimension(165,100));
        multiFunctionPanel = new JPanel(); // 多功能区
        multiFunctionPanel.setPreferredSize(new Dimension(165,300));
        ToolPanelsInitialize();      // 初始化各个区的具体内容
        add(toolPanel);
        add(colorPanel);
        add(multiFunctionPanel);
    }

    private class ColorChooseAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            Color newColor = JColorChooser.showDialog(managedPanel,"调色板",Color.black);
            // 用户取消或关闭窗口时返回null
            if(newColor == null)
                return;
            if(currentLabelFocus)
            {
                DrawColorLabel.setBackground(newColor);
                managedPanel.setDrawColor(newColor);
            }
            else
            {
                FillColorLabel.setBackground(newColor);
                managedPanel.setFillColor(newColor);
            }
        }
    }

    private class PenSizeChooseAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            String s = JOptionPane.showInputDialog(colorPanel,
                    "请输入需要设置的画笔大小(正整数,不超过50,单位:像素):");
            if((s!=null)&&(!s.equals("")))
            {
                String numPattern = "[0123456789]*";
                if(Pattern.matches(numPattern,s)) // 匹配数字
                {
                    int num = Integer.parseInt(s);
                    if((num <= 50)&&(num > 0)) // 合法输入
                    {
                        sizeBu.setText(num + " 像素");
                        managedPanel.setPenSize(num);
                        return;
                    }
                    JOptionPane.showMessageDialog(colorPanel,"输入值过大","非法输入",
                            JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
                                    getClass().getResource("/resources/icons/info.png")));
                }
                else {
                    JOptionPane.showMessageDialog(colorPanel,"无法识别","非法输入",
                            JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
                                    getClass().getResource("/resources/icons/info.png")));
                }
            }
        }
    }

    private class ShowThreeDimensionalPicture implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            // 选择文件
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter(new OffFileFilter());
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.showOpenDialog(null);
            File file = jfc.getSelectedFile();
            if(file == null)
            {
                JOptionPane.showMessageDialog(colorPanel,"取消显示三维图形","提示",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
                                getClass().getResource("/resources/icons/info.png")));
                return;
            }
            String path = file.getPath();

            final GLProfile profile = GLProfile.get( GLProfile.GL2 );
            GLCapabilities capabilities = new GLCapabilities( profile );
            // The canvas
            final GLCanvas glcanvas = new GLCanvas( capabilities );
            Show3DGraph show3DGraph = new Show3DGraph(path);
            glcanvas.addGLEventListener( show3DGraph );
            glcanvas.setSize( 400, 400 );
            final JFrame frame = new JFrame ( "三维图形展示" );
            frame.getContentPane().add( glcanvas );
            frame.setSize( frame.getContentPane().getPreferredSize() );
            frame.setLocationRelativeTo(managedPanel);
            frame.setVisible( true );
            final FPSAnimator animator = new FPSAnimator( glcanvas, 300,true );
            animator.start();
        }
    }

    private class CleanPanelAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) { managedPanel.clearPanel(); }
    }

    private class RandomDrawAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            managedPanel.adjustMode(1);
        }
    }

    private class DrawLineAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            managedPanel.adjustMode(2);
        }
    }

    private class RectangleAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            managedPanel.adjustMode(3);
        }
    }

    private class CircleAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) { managedPanel.adjustMode(4); }
    }

    private class EllipseAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) { managedPanel.adjustMode(5); }
    }

    private class PolygonAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) { managedPanel.adjustMode(6); }
    }

    private class FillPolygonAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) { managedPanel.adjustMode(7); }
    }

    private class BezierCurveAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) { managedPanel.adjustMode(8); }
    }

    private class BspineCurveAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) { managedPanel.adjustMode(9); }
    }

    private class CutAction implements ActionListener // 裁剪
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            JOptionPane.showMessageDialog(managedPanel,
                    "请在画板中用鼠标拖出一个矩形裁剪框(中途不显示)，\n完成后所有直线在框中的部分就会保留，" +
                            "其余的会被裁剪掉", "提示",
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
                            getClass().getResource("/resources/icons/info.png")));
            if(!managedPanel.haveLines())
            {
                JOptionPane.showMessageDialog(managedPanel,
                        "请先使用直线工具画直线用于裁剪", "提示",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
                                getClass().getResource("/resources/icons/info.png")));
            }
            else
                managedPanel.adjustMode(10);
        }
    }

    private class TranslateAction implements ActionListener // 平移
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            // 初始化示例对象
            managedPanel.InitExampleObj();
            new TranslationInput(managedPanel);
        }
    }

    private class RotateAction implements ActionListener // 旋转
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            managedPanel.InitExampleObj();
            new RotateInput(managedPanel);
        }
    }

    private class ZoomAction implements ActionListener // 缩放
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            managedPanel.InitExampleObj();
            new ZoomInput(managedPanel);
        }
    }
}