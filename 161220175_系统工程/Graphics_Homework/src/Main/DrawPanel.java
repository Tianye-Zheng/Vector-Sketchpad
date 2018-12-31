package Main;

import Coordinates.ColorTempCoor;
import Coordinates.CoorDouble;
import Coordinates.Coordinate;
import GraphicObjects.GraphicObj;
import GraphicObjects.LineObj;
import Util.PlayMusic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class DrawPanel extends JPanel {

    // 默认绘画区大小
    public static final int DRAW_WIDTH = 1018;//800;
    public static final int DRAW_HEIGHT = 800;//700;
    // 控制点圆圈半径
    public static final int CPR = 4;
    // 最近绘制的图形对象，用于几何变换
    private GraphicObj lattestObj;
    // 坐标
    private int x;
    private int y;
    // 辅助坐标
    private int x1;
    private int y1;
    // 鼠标动作适配器
    private MouseAdapter ma;

    // 全局路径收集器
    private Set<Coordinate> drawPath = new HashSet<>();

    // 直线对象收集器，所画的直线都会加入，用于裁剪
    private Set<LineObj> lineObjs = new LinkedHashSet<>();

    // 画板颜色记录，避免画件中途已画被擦除
    private ColorTempCoor[][] colorCanvas;

    // 默认背景颜色
    private Color defaultBackgroundColor; // 不会变，只有一个
    private Color drawColor;              // 会变，只有一个
    private Color fillColor;
    // 画笔大小
    private int penSize;

    // **************************   绘制区初始化  *****************************************
    public DrawPanel()
    {
        // 设置背景颜色和尺寸
        defaultBackgroundColor = new Color(250,245,236); // 浅黄色
        drawColor = Color.BLACK;  // 默认绘制颜色为黑色
        fillColor = Color.WHITE;  // 默认填充颜色为白色
        setBackground(defaultBackgroundColor);
        setPreferredSize(new Dimension(DRAW_WIDTH,DRAW_HEIGHT));
        penSize = 1; // 初始化画笔大小为1个像素

        // 设置鼠标适配器
        ma = new RandomDrawMouseAdapter();
        addMouseListener(ma);
        addMouseMotionListener(ma);

        // 初始化画板颜色记录数组
        colorCanvas = new ColorTempCoor[DRAW_WIDTH][DRAW_HEIGHT];
        for(int i = 0; i < DRAW_WIDTH; i++)
        {
            for(int j = 0; j < DRAW_HEIGHT; j++)
            {
                colorCanvas[i][j] = new ColorTempCoor(defaultBackgroundColor,defaultBackgroundColor);
            }
        }
    }

    public void InitExampleObj() {
        // 最近对象初始化为空
        lattestObj = null;
        // 这里使用了示例图形
        lattestObj = new GraphicObj();
        int pointNum = DRAW_HEIGHT/2;
        for (int i = 0; i < pointNum; i++) {
            lattestObj.drawPath.add(new Coordinate(DRAW_WIDTH/4+i, 3*DRAW_HEIGHT/4-i));
        }
        for (int i = 0; i < pointNum / 2; i++) {
            lattestObj.drawPath.add(new Coordinate(DRAW_WIDTH/2+i,DRAW_HEIGHT/2+i));
        }
        Graphics g = getGraphics();
        g.setColor(drawColor);
        Translation(0,0);
    }

    // 颜色
    public void setDrawColor(Color newColor) { drawColor = newColor; }
    public Color getDrawColor() { return drawColor; }
    public void setFillColor(Color newColor) { fillColor = newColor; }
    public Color getFillColor() { return fillColor; }
    // 画笔大小
    public void setPenSize(int penSize) { this.penSize = penSize; }
    public int getPenSize() { return penSize; }
    // 有没有直线
    public boolean haveLines() { return !lineObjs.isEmpty(); }

    // 调整模式
    public void adjustMode(int mode)
    {
        if(ma instanceof DrawLineMouseAdapter)        // 直线收尾
            ((DrawLineMouseAdapter) ma).StopDrawing();
        else if(ma instanceof CircleMouseAdapter)     // 圆收尾
            ((CircleMouseAdapter) ma).StopDrawing();
        else if(ma instanceof EllipseMouseAdapter)    // 椭圆收尾
            ((EllipseMouseAdapter) ma).StopDrawing();
        else if(ma instanceof BezierCurveMouseAdapter) // 如果是贝塞尔曲线模式，需要收尾
            ((BezierCurveMouseAdapter) ma).StopDrawing();
        else if(ma instanceof BspineCurveMouseAdapter) // 如果是B样条曲线模式，也需要收尾
            ((BspineCurveMouseAdapter) ma).StopDrawing();
        switch (mode)
        {
            case 1:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new RandomDrawMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 1 为任意画
            case 2:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new DrawLineMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 2 为画直线
            case 3:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new RectangleMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 3 为画矩形
            case 4:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new CircleMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 4 为画圆
            case 5:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new EllipseMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 5 为画椭圆
            case 6:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new PolygonMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 6 为画多边形
            case 7:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new FillPolygonMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 7 为画填充多边形
            case 8:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new BezierCurveMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 8 为画贝塞尔曲线
            case 9:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new BspineCurveMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 9 为画B样条曲线
            case 10:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new CutMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 10 为裁剪
            default:
                removeMouseListener(ma);
                removeMouseMotionListener(ma);
                ma = new RandomDrawMouseAdapter();
                addMouseListener(ma);
                addMouseMotionListener(ma);
                break; // 默认 1 为任意画
        }
    }

    // 清理画板
    public void clearPanel()
    {
        for(int i = 0; i < DRAW_WIDTH; i++)
        {
            for(int j = 0; j < DRAW_HEIGHT; j++)
            {
                colorCanvas[i][j].org = defaultBackgroundColor;
                colorCanvas[i][j].cur = defaultBackgroundColor;
            }
        }
        repaint();
        drawPath.clear();
        lineObjs.clear();
        new PlayMusic().playMusic("/resources/audios/cleanPanel.mov");
    }

    // 画板颜色记录检查更新函数，每画一个点都用绘制颜色来更新
    private void UpdateColorCanvas(int x, int y, Color color) {
        if((x >= 0) && (x < DRAW_WIDTH) && (y >= 0) && (y < DRAW_HEIGHT))
        {
            if(!colorCanvas[x][y].cur.equals(colorCanvas[x][y].org))
            {
                colorCanvas[x][y].org = colorCanvas[x][y].cur;
            }
            colorCanvas[x][y].cur = new Color(color.getRed(),color.getGreen(),color.getBlue());
        }
    }

    // 任意形状
    private class RandomDrawMouseAdapter extends MouseAdapter
    {
        //添加点击事件
        @Override
        public void mousePressed (MouseEvent me) {
            //鼠标按下，则把当前点坐标设定为鼠标位置
            x = me.getX();
            y = me.getY();
            //画线，从点 (x,y) 到点 (x,y)
            Graphics g = getGraphics();
            g.setColor(drawColor);
            //g.drawLine(x, y, x, y);
            g.fillOval(x-penSize,y-penSize,2*penSize,2*penSize); // 考虑画笔大小
        }
        //添加拖拽事件
        @Override
        public void mouseDragged (MouseEvent me) {
            //画线，从点 (x,y) 到鼠标当前位置
            Graphics g = getGraphics();
            g.setColor(drawColor);
            //g.drawLine(x, y, me.getX(), me.getY());
            drawLine(x, y, me.getX(), me.getY(), g,false);
            //更新当前点，这是为鼠标位置
            x = me.getX();
            y = me.getY();
        }
    }

    private LineObj currentObj;  // 当前正在操作的直线对象

    // 普通直线工具
    private class DrawLineMouseAdapter extends MouseAdapter  // Bresenham 算法
    {
        private Coordinate nearWhichPoint; // 是否处于某个控制点的范围内，没有则为null
        List<Coordinate> controlPoints = new ArrayList<>(); // 控制点
        DrawLineMouseAdapter() { nearWhichPoint = null; controlPoints.clear(); }

        // 添加点击事件
        @Override
        public void mousePressed(MouseEvent me) {

            if(nearWhichPoint != null) // 如果指着某个控制点，点击无效
            {
                return;
            }
            Graphics g = getGraphics();
            if((currentObj != null) && (currentObj.x2 != -1)) // 上一次绘制收尾
            {
                StopDrawing();
            }
            if(currentObj == null) // 开始一次新的绘制
            {
                // 新建直线对象
                currentObj = new LineObj();
                // 先清空路径
                drawPath.clear();
                // 鼠标按下，设置对象
                currentObj.x1 = me.getX();
                currentObj.y1 = me.getY();
                addSign(currentObj.x1,currentObj.y1,g); // 加入新控制点
                // 画点
                g.setColor(drawColor);
                g.drawLine(currentObj.x1,currentObj.y1,currentObj.x1,currentObj.y1);
            }
            else if((currentObj != null) && (currentObj.x2 == -1)) // 第二个点
            {
                currentObj.x2 = me.getX();
                currentObj.y2 = me.getY();
                addSign(currentObj.x2,currentObj.y2,g); // 加入新控制点
                g.setColor(drawColor);
                // 绘制新直线
                drawLine(currentObj.x1,currentObj.y1,currentObj.x2,currentObj.y2, g,true);
            }
        }

        // 添加移动事件
        @Override
        public void mouseMoved(MouseEvent me) {
            int tempx = me.getX();
            int tempy = me.getY();
            for(Coordinate c : controlPoints)
            {
                if((Math.abs(c.x-tempx) < 5) && (Math.abs(c.y-tempy) < 5))
                {
                    nearWhichPoint = c;
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    return;
                }
            }
            nearWhichPoint = null;
            setCursor(Cursor.getDefaultCursor());
        }

        // 添加拖拽事件
        @Override
        public void mouseDragged(MouseEvent me) {

            if(nearWhichPoint != null) // 指着某个控制点
            {
                Graphics g = getGraphics();
                x1 = me.getX();
                y1 = me.getY();
                modifySign(nearWhichPoint.x,nearWhichPoint.y,x1,y1,g); // 修改控制点
                // 清除旧直线
                for(Coordinate c : drawPath)
                {
                    g.setColor(colorCanvas[c.x][c.y].org);
                    UpdateColorCanvas(c.x,c.y,colorCanvas[c.x][c.y].org);
                    g.drawLine(c.x,c.y,c.x,c.y);
                }
                g.setColor(drawColor);
                drawPath.clear();
                currentObj.drawPath.clear();
                // 绘制新直线
                drawLine(currentObj.x1,currentObj.y1,currentObj.x2,currentObj.y2, g,true);
            }
        }

        private void addSign(int x, int y, Graphics g) { // 添加控制点
            g.setColor(drawColor);
            g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
            controlPoints.add(new Coordinate(x,y));
        }
        private void modifySign(int x, int y, int newX, int newY, Graphics g) {
            // 抹去旧的控制点
            g.setColor(defaultBackgroundColor);
            g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
            if((currentObj.x1 == x) && (currentObj.y1 == y))
            {
                currentObj.x1 = newX;
                currentObj.y1 = newY;
                // 绘制修改后的控制点
                g.setColor(drawColor);
                g.drawArc(currentObj.x1-CPR,currentObj.y1-CPR, 2*CPR, 2*CPR,0,360);
            }
            else if((currentObj.x2 == x) && (currentObj.y2 == y))
            {
                currentObj.x2 = newX;
                currentObj.y2 = newY;
                // 绘制修改后的控制点
                g.setColor(drawColor);
                g.drawArc(currentObj.x2-CPR,currentObj.y2-CPR, 2*CPR, 2*CPR,0,360);
            }
            // 更新控制点
            for(Iterator<Coordinate> it = controlPoints.iterator(); it.hasNext(); )
            {
                Coordinate c = it.next();
                if((c.x == x) && (c.y == y))
                {
                    c.x = newX;
                    c.y = newY;
                }
            }
        }

        public void StopDrawing() { // 绘制结束
            if(currentObj!= null)
            {
                Graphics g = getGraphics();
                g.setColor(defaultBackgroundColor);
                g.drawArc(currentObj.x1-CPR, currentObj.y1-CPR, 2*CPR, 2*CPR, 0, 360);
                g.drawArc(currentObj.x2-CPR, currentObj.y2-CPR, 2*CPR, 2*CPR, 0, 360);
                lineObjs.add(currentObj); // 添加直线对象
                currentObj = null; // 置空，避免潜在的 bug
                controlPoints.clear();
            }
        }
    }
    // 在两点之间画直线，并把点都记录在收集器里(包含首尾)
    private void drawLine(int x1, int y1, int x2, int y2, Graphics g, boolean ignoreSize)
    {
        int x0 = x1, y0 = y1;
        int dx = Math.abs(x0 - x2), sx = x0 < x2? 1 : -1;
        int dy = Math.abs(y0 - y2), sy = y0 < y2? 1 : -1;
        int err = (dx > dy ? dx : -dy)/2, e2;
        while(true) {
            if(ignoreSize) // 忽略画笔大小
            {
                if((x0 >= 0) && (x0 < DRAW_WIDTH) && (y0 >= 0) && (y0 < DRAW_HEIGHT)) // 下标不越界
                {
                    UpdateColorCanvas(x0,y0,drawColor);
                    g.drawLine(x0,y0,x0,y0);
                    drawPath.add(new Coordinate(x0,y0));
                    if(currentObj != null)
                        currentObj.drawPath.add(new Coordinate(x0,y0)); // 为当前直线对象添加路径点
                }
            }
            else // 考虑画笔大小
            {
                if((x0 >= 0) && (x0 < DRAW_WIDTH) && (y0 >= 0) && (y0 < DRAW_HEIGHT)) // 下标不越界
                {
                    UpdateColorCanvas(x0,y0,drawColor);
                    g.fillOval(x0-penSize,y0-penSize,2*penSize,2*penSize);
                    drawPath.add(new Coordinate(x0,y0));
                    if(currentObj != null)
                        currentObj.drawPath.add(new Coordinate(x0,y0)); // 为当前直线对象添加路径点
                }
            }
            // 下标越界的点将会被忽略
            if((x0 == x2) && (y0 == y2)) break;
            e2 = err;
            if (e2 >-dx) { err -= dy; x0 += sx; }
            if (e2 < dy) { err += dx; y0 += sy; }
        }
    }

    // 画矩形
    private class RectangleMouseAdapter extends MouseAdapter
    {
        // 添加点击事件
        @Override
        public void mousePressed(MouseEvent me) {
            // 清除上一次画记录的路径
            drawPath.clear();
            // 鼠标按下，则把当前点坐标设定为鼠标位置
            x = me.getX();
            y = me.getY();
            // 初始化辅助坐标点
            x1 = x;
            y1 = y;
            // 画点
            //Graphics g = getGraphics();
            //g.setColor(drawColor);
            //drawLine(x, y, x, y, g,true); // 画第一个点
        }
        // 添加拖拽事件
        @Override
        public void mouseDragged(MouseEvent me) {
            Graphics g = getGraphics();
            // 清除旧矩形
            for(Coordinate c : drawPath)
            {
                g.setColor(colorCanvas[c.x][c.y].org);
                UpdateColorCanvas(c.x,c.y,colorCanvas[c.x][c.y].org);
                g.drawLine(c.x,c.y,c.x,c.y);
            }
            g.setColor(drawColor);
            drawPath.clear();
            // 绘制新矩形
            x1 = me.getX();
            y1 = me.getY();
            int tempx1 = (x > x1? x1:x); // 临时辅助坐标，左上和右下
            int tempy1 = (y > y1? y1:y);
            int tempx2 = (x < x1? x1:x);
            int tempy2 = (y < y1? y1:y);
            if((tempx1 == tempx2) || (tempy1 == tempy2))
            {
                drawLine(tempx1,tempy1,tempx2,tempy2, g,true);
                return;
            }
            drawLine(tempx1,tempy1,tempx1,tempy2-1, g,true); // 注意同一个点不能加两次
            drawLine(tempx1,tempy2,tempx2-1,tempy2, g,true);
            drawLine(tempx2,tempy2,tempx2,tempy1+1, g,true);
            drawLine(tempx2,tempy1,tempx1+1,tempy1, g,true);
        }
    }

    // 圆工具
    private class CircleMouseAdapter extends MouseAdapter // Bresenham 算法
    {
        boolean nearCenter;  // 是否在圆心附近
        CircleMouseAdapter() { x = -1; y = -1; nearCenter = false; }

        // 添加点击事件
        @Override
        public void mousePressed(MouseEvent me) {
            if(nearCenter)
                return;
            // 先清空路径
            drawPath.clear();
            StopDrawing();
            // 画点
            Graphics g = getGraphics();
            g.setColor(drawColor);
            // x 和 y 是圆心
            addCenter(me.getX(),me.getY(),g);
            g.drawLine(x,y,x,y);
            drawPath.add(new Coordinate(x,y));
        }

        // 添加移动事件
        @Override
        public void mouseMoved(MouseEvent me) {
            if(x != -1)
            {
                int tempx = me.getX();
                int tempy = me.getY();
                if((Math.abs(x-tempx) < 5) && (Math.abs(y-tempy) < 5))
                {
                    nearCenter = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    return;
                }
                nearCenter = false;
                setCursor(Cursor.getDefaultCursor());
            }
        }

        // 添加拖拽事件
        @Override
        public void mouseDragged(MouseEvent me) {

            if(nearCenter)
            {
                Graphics g = getGraphics();
                int tempx = me.getX();
                int tempy = me.getY();
                x1 += (tempx - x);
                y1 += (tempy - y);
                modifyCenter(tempx, tempy, g);
                CleanAndDrawCircle();
            }
            else
            {
                x1 = me.getX();
                y1 = me.getY();
                CleanAndDrawCircle();
            }
        }

        private void addCenter(int x0, int y0, Graphics g) { // 添加控制点
            g.setColor(drawColor); // 绘制
            g.drawArc(x0-CPR,y0-CPR, 2*CPR, 2*CPR,0,360);
            x = x0; y = y0;
        }
        private void modifyCenter(int newX, int newY, Graphics g) {
            if(x != -1)
            {
                // 抹去旧的控制点
                g.setColor(defaultBackgroundColor);
                g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
                x = newX;
                y = newY;
                // 绘制修改后的控制点
                g.setColor(drawColor);
                g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
            }
        }
        public void StopDrawing() { // 绘制结束
            if(x != -1)
            {
                // 清除控制点
                Graphics g = getGraphics();
                g.setColor(defaultBackgroundColor);
                g.drawArc(x-CPR, y-CPR, 2*CPR, 2*CPR, 0, 360);
                x = -1; y = -1;
            }
        }

        private void CleanAndDrawCircle() { // 画圆核心算法
            Graphics g = getGraphics();
            // 清除旧圆
            g.setColor(defaultBackgroundColor);
            for(Coordinate coordinate : drawPath)
            {
                g.drawLine(coordinate.x,coordinate.y,coordinate.x,coordinate.y);
            }
            g.setColor(drawColor);
            drawPath.clear();
            // 绘制新圆
            int r = (int)Math.sqrt((x1-x)*(x1-x)+(y1-y)*(y1-y)); // 计算半径
            int p = 1 - r; // 决策参数
            Coordinate drawPoint = new Coordinate(0,r); // 画的第一组点
            g.drawLine(x +drawPoint.x,y+drawPoint.y,x +drawPoint.x,y+drawPoint.y);
            drawPath.add(new Coordinate(x +drawPoint.x,y+drawPoint.y));
            g.drawLine(x +drawPoint.x,y-drawPoint.y,x +drawPoint.x,y-drawPoint.y);
            drawPath.add(new Coordinate(x +drawPoint.x,y-drawPoint.y));
            g.drawLine(x +drawPoint.y,y+drawPoint.x,x +drawPoint.y,y+drawPoint.x);
            drawPath.add(new Coordinate(x +drawPoint.y,y+drawPoint.x));
            g.drawLine(x -drawPoint.y,y+drawPoint.x,x -drawPoint.y,y+drawPoint.x);
            drawPath.add(new Coordinate(x -drawPoint.y,y+drawPoint.x));

            while(drawPoint.x < drawPoint.y)
            {
                if(p < 0)
                {
                    drawPoint.x += 1;
                    p = p + 2 * drawPoint.x + 1;
                }
                else // p >= 0
                {
                    drawPoint.x += 1;
                    drawPoint.y -= 1;
                    p = p + 2 * drawPoint.x - 2 * drawPoint.y + 1;
                }
                g.drawLine(x +drawPoint.x,y+drawPoint.y,x +drawPoint.x,y+drawPoint.y);
                drawPath.add(new Coordinate(x +drawPoint.x,y+drawPoint.y));
                g.drawLine(x +drawPoint.y,y+drawPoint.x,x +drawPoint.y,y+drawPoint.x);
                drawPath.add(new Coordinate(x +drawPoint.y,y+drawPoint.x));

                g.drawLine(x -drawPoint.x,y+drawPoint.y,x -drawPoint.x,y+drawPoint.y);
                drawPath.add(new Coordinate(x -drawPoint.x,y+drawPoint.y));
                g.drawLine(x -drawPoint.y,y+drawPoint.x,x -drawPoint.y,y+drawPoint.x);
                drawPath.add(new Coordinate(x -drawPoint.y,y+drawPoint.x));

                g.drawLine(x +drawPoint.x,y-drawPoint.y,x +drawPoint.x,y-drawPoint.y);
                drawPath.add(new Coordinate(x +drawPoint.x,y-drawPoint.y));
                g.drawLine(x +drawPoint.y,y-drawPoint.x,x +drawPoint.y,y-drawPoint.x);
                drawPath.add(new Coordinate(x +drawPoint.y,y-drawPoint.x));

                g.drawLine(x -drawPoint.x,y-drawPoint.y,x -drawPoint.x,y-drawPoint.y);
                drawPath.add(new Coordinate(x -drawPoint.x,y-drawPoint.y));
                g.drawLine(x -drawPoint.y,y-drawPoint.x,x -drawPoint.y,y-drawPoint.x);
                drawPath.add(new Coordinate(x -drawPoint.y,y-drawPoint.x));
            }
        }
    }

    // 椭圆工具
    private class EllipseMouseAdapter extends MouseAdapter
    {
        boolean nearCenter;  // 是否在圆心附近
        EllipseMouseAdapter() { x = -1; y = -1; nearCenter = false; }

        // 添加点击事件
        @Override
        public void mousePressed(MouseEvent me) {
            if(nearCenter)
                return;
            // 先清空路径
            drawPath.clear();
            StopDrawing();
            // 画点
            Graphics g = getGraphics();
            g.setColor(drawColor);
            // x 和 y 是圆心
            addCenter(me.getX(),me.getY(),g);
            g.drawLine(x,y,x,y);
            drawPath.add(new Coordinate(x,y));
        }

        // 添加移动事件
        @Override
        public void mouseMoved(MouseEvent me) {
            if(x != -1)
            {
                int tempx = me.getX();
                int tempy = me.getY();
                if((Math.abs(x-tempx) < 5) && (Math.abs(y-tempy) < 5))
                {
                    nearCenter = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    return;
                }
                nearCenter = false;
                setCursor(Cursor.getDefaultCursor());
            }
        }

        // 添加拖拽事件
        @Override
        public void mouseDragged(MouseEvent me) {

            if(nearCenter)
            {
                Graphics g = getGraphics();
                int tempx = me.getX();
                int tempy = me.getY();
                x1 += (tempx - x);
                y1 += (tempy - y);
                modifyCenter(tempx, tempy, g);
                CleanAndDrawEllipse();
            }
            else
            {
                x1 = me.getX();
                y1 = me.getY();
                CleanAndDrawEllipse();
            }
        }

        private void addCenter(int x0, int y0, Graphics g) { // 添加控制点
            g.setColor(drawColor); // 绘制
            g.drawArc(x0-CPR,y0-CPR, 2*CPR, 2*CPR,0,360);
            x = x0; y = y0;
        }
        private void modifyCenter(int newX, int newY, Graphics g) {
            if(x != -1)
            {
                // 抹去旧的控制点
                g.setColor(defaultBackgroundColor);
                g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
                x = newX;
                y = newY;
                // 绘制修改后的控制点
                g.setColor(drawColor);
                g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
            }
        }
        public void StopDrawing() { // 绘制结束
            if(x != -1)
            {
                // 清除控制点
                Graphics g = getGraphics();
                g.setColor(defaultBackgroundColor);
                g.drawArc(x-CPR, y-CPR, 2*CPR, 2*CPR, 0, 360);
                x = -1; y = -1;
            }
        }
        private void CleanAndDrawEllipse() {
            Graphics g = getGraphics();
            // 清除旧椭圆
            g.setColor(defaultBackgroundColor);
            for(Coordinate coordinate : drawPath)
            {
                g.drawLine(coordinate.x,coordinate.y,coordinate.x,coordinate.y);
            }
            g.setColor(drawColor);
            drawPath.clear();
            // 绘制新椭圆
            int rx = Math.abs(x - x1);
            int ry = Math.abs(y - y1);
            int p = (int)(ry*ry - rx*rx*ry + 0.25*rx*rx);

            Coordinate drawPoint = new Coordinate(0,ry); // 画的第一组点
            g.drawLine(x,y+ry,x,y+ry);
            drawPath.add(new Coordinate(x,y+ry));
            g.drawLine(x,y-ry,x,y-ry);
            drawPath.add(new Coordinate(x,y-ry));
            g.drawLine(x+rx,y,x+rx,y);
            drawPath.add(new Coordinate(x+rx,y));
            g.drawLine(x-rx,y,x-rx,y);
            drawPath.add(new Coordinate(x-rx,y));
            // 斜率大于 -1 区域
            while(2*ry*ry*drawPoint.x < 2*rx*rx*drawPoint.y)
            {
                if(p < 0)
                {
                    drawPoint.x++;
                    p = p + 2*ry*ry*drawPoint.x + ry*ry;
                }
                else // p >= 0
                {
                    drawPoint.x++;
                    drawPoint.y--;
                    p = p + 2*ry*ry*drawPoint.x - 2*rx*rx*drawPoint.y + ry*ry;
                }
                g.drawLine(x +drawPoint.x,y+drawPoint.y,x +drawPoint.x,y+drawPoint.y);
                drawPath.add(new Coordinate(x +drawPoint.x,y+drawPoint.y));
                g.drawLine(x +drawPoint.x,y-drawPoint.y,x +drawPoint.x,y-drawPoint.y);
                drawPath.add(new Coordinate(x +drawPoint.x,y-drawPoint.y));
                g.drawLine(x -drawPoint.x,y+drawPoint.y,x -drawPoint.x,y+drawPoint.y);
                drawPath.add(new Coordinate(x -drawPoint.x,y+drawPoint.y));
                g.drawLine(x -drawPoint.x,y-drawPoint.y,x -drawPoint.x,y-drawPoint.y);
                drawPath.add(new Coordinate(x -drawPoint.x,y-drawPoint.y));
            }
            // 斜率小于 -1 区域
            //p = (int)(ry*ry*(drawPoint.x+0.5)*(drawPoint.x+0.5) + rx*rx*(drawPoint.y-1)*(drawPoint.y-1) - rx*rx*ry*ry);
            while(drawPoint.y > 0)
            {
                if(p > 0)
                {
                    drawPoint.y--;
                    p = p - 2*rx*rx*drawPoint.y + rx*rx;
                }
                else // p <= 0
                {
                    drawPoint.x++;
                    drawPoint.y--;
                    p = p + 2*ry*ry*drawPoint.x - 2*rx*rx*drawPoint.y +rx*rx;
                }
                g.drawLine(x +drawPoint.x,y+drawPoint.y,x +drawPoint.x,y+drawPoint.y);
                drawPath.add(new Coordinate(x +drawPoint.x,y+drawPoint.y));
                g.drawLine(x +drawPoint.x,y-drawPoint.y,x +drawPoint.x,y-drawPoint.y);
                drawPath.add(new Coordinate(x +drawPoint.x,y-drawPoint.y));
                g.drawLine(x -drawPoint.x,y+drawPoint.y,x -drawPoint.x,y+drawPoint.y);
                drawPath.add(new Coordinate(x -drawPoint.x,y+drawPoint.y));
                g.drawLine(x -drawPoint.x,y-drawPoint.y,x -drawPoint.x,y-drawPoint.y);
                drawPath.add(new Coordinate(x -drawPoint.x,y-drawPoint.y));
            }
        }
    }

    // 多边形工具
    private class PolygonMouseAdapter extends MouseAdapter
    {
        private boolean started = false;
        private boolean inFinalArea = false;

        // 添加点击事件
        @Override
        public void mousePressed(MouseEvent me) {

            // 如果是第一下按
            if(!started)
            {
                drawPath.clear(); // 先清空路径
                x = me.getX();    // 记录起始坐标，在每一次多边形绘制中固定
                y = me.getY();
                x1 = x; y1 = y;   // 当前线段的起始
                Graphics g = getGraphics();
                g.setColor(drawColor);
                g.drawLine(x,y,x,y); // 画起始点
                started = true;   // 开始
            }
            else
            {
                if(inFinalArea) // 最后一下按
                {
                    Graphics g = getGraphics();
                    // 清除旧直线
                    g.setColor(defaultBackgroundColor);
                    for(Coordinate coordinate : drawPath)
                    {
                        g.drawLine(coordinate.x,coordinate.y,coordinate.x,coordinate.y);
                    }
                    g.setColor(drawColor);
                    drawPath.clear();
                    // 绘制新直线
                    drawLine(x1,y1,x,y,g,true); // 起始点与最后一个顶点连起来
                    started = false;
                    setCursor(Cursor.getDefaultCursor());
                    drawPath.clear();
                }
                else
                {
                    x1 = me.getX();
                    y1 = me.getY();
                    drawPath.clear();
                }
            }
        }

        // 添加移动事件
        @Override
        public void mouseMoved(MouseEvent me)
        {
            if(started)
            {
                // 记录当前坐标
                int x2 = me.getX();
                int y2 = me.getY();
                if((Math.abs(x2-x) <= 4)&&(Math.abs(y2-y) <= 4)) // 如果在初始光标附近区域，改变鼠标样式
                {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    inFinalArea = true;
                }
                else if(inFinalArea) // 如果不在区域，回复默认鼠标样式
                {
                    setCursor(Cursor.getDefaultCursor());
                    inFinalArea = false;
                }
                Graphics g = getGraphics();
                // 清除旧直线
                g.setColor(defaultBackgroundColor);
                for(Coordinate coordinate : drawPath)
                {
                    g.drawLine(coordinate.x,coordinate.y,coordinate.x,coordinate.y);
                }
                g.setColor(drawColor);
                drawPath.clear();
                // 绘制新直线
                drawLine(x1,y1,x2,y2,g,true);
            }
        }
    }

    // 填充多边形工具
    private class FillPolygonMouseAdapter extends MouseAdapter  // 扫描填充算法
    {
        private boolean started = false;     // 开始与否
        private boolean inFinalArea = false; // 是否在起始点附近的区域
        private Set<Coordinate> vertex;      // 记录多边形顶点
        char [][]isDrawn;      // 标识是否被画，0为没有被画，1为多边形边上的点，2为多边形顶点
        Coordinate Northwest;  // 扫描区域最左上的点，xy都最小
        Coordinate Southeast;  // 扫描区域最右下的点，xy都最大


        // 添加点击事件
        @Override
        public void mousePressed(MouseEvent me) {
            // 如果是第一下按
            if(!started) {
                vertex = new LinkedHashSet<>(); // 初始化 保证有序
                drawPath.clear();    // 先清空路径
                vertex.clear();      // 清空顶点记录

                x = me.getX();       // 记录起始坐标，在每一次多边形绘制中固定
                y = me.getY();

                Northwest = new Coordinate(x,y);  // 初始化扫描区域
                Southeast = new Coordinate(x,y);
                vertex.add(new Coordinate(x,y));  // 将起始点加入顶点集

                x1 = x; y1 = y;   // 当前线段的起始
                Graphics g = getGraphics();
                g.setColor(drawColor);
                g.drawLine(x,y,x,y); // 画起始点
                started = true;   // 开始
            }
            else {
                if(inFinalArea) // 最后一下按
                {
                    Graphics g = getGraphics();
                    // 清除旧直线
                    g.setColor(defaultBackgroundColor);
                    for(Coordinate coordinate : drawPath)
                    {
                        g.drawLine(coordinate.x,coordinate.y,coordinate.x,coordinate.y);
                    }
                    g.setColor(drawColor);
                    drawPath.clear();
                    // 绘制新直线
                    drawLine(x1,y1,x,y,g,true); // 起始点与最后一个顶点连起来
                    started = false;
                    setCursor(Cursor.getDefaultCursor());
                    drawPath.clear();
                    // 开始染色过程
                    // 初始化判断区域
                    int areaHeight = Southeast.y-Northwest.y+1;
                    int areaWidth = Southeast.x-Northwest.x+1;
                    isDrawn = new char[areaHeight][areaWidth];
                    for (int i = 0; i < areaHeight; i++) {
                        for (int j = 0; j < areaWidth; j++) {
                            isDrawn[i][j] = 0;
                        }
                    }
                    // 根据每对顶点添加设置线上的点
                    boolean setLinePointStarted = false;
                    Coordinate former = new Coordinate(-1,-1);
                    Coordinate current = new Coordinate(-1,-1);
                    vertex.add(new Coordinate(x,y)); // 最后再加上起点
                    for (Coordinate coor:vertex) {   // 遍历每个顶点
                        if(!setLinePointStarted) {   // 如果是第一个顶点
                            former.x = coor.x - Northwest.x;
                            former.y = coor.y - Northwest.y;
                            setLinePointStarted = true;
                        }
                        else { // 如果不是第一个顶点
                            current.x = coor.x - Northwest.x;
                            current.y = coor.y - Northwest.y;

                            int x1 = current.y, y1 = current.x; // 注意要反过来
                            int x2 = former.y, y2 = former.x;
                            // 开始在 (x1,y1) 和 (x2,y2) 之间设置直线
                            int dx = Math.abs(x1-x2), sx=x1<x2? 1:-1;
                            int dy = Math.abs(y1-y2), sy=y1<y2? 1:-1;
                            int err = (dx>dy? dx:-dy)/2, e2;
                            while(true) {
                                isDrawn[x1][y1] = 1;
                                if((x1 == x2) && (y1 == y2)) break;
                                e2 = err;
                                if (e2 >-dx) { err -= dy; x1 += sx; }
                                if (e2 < dy) { err += dx; y1 += sy; }
                            }
                            former.x = current.x; // 更新
                            former.y = current.y;
                        }
                    }
                    // 添加顶点
                    for (Coordinate coor:vertex) {
                        isDrawn[coor.y-Northwest.y][coor.x-Northwest.x] = 2;
                    }
                    // 开始扫描
                    for (int i = 0; i < areaHeight; i++) {
                        // 对第 i 行，先设置点
                        Set<Coordinate> judgementPoints = new LinkedHashSet<>();
                        // 遍历第 i 行判断矩阵的每一个点
                        for (int j = 0; j < areaWidth; j++) {
                            if(isDrawn[i][j] == 2) {  // 单个顶点 2
                                if(j-1<0) { // 左边界
                                    if((j+1>=areaWidth)||(i-1<0)||(i+1>=areaHeight)||
                                            ((isDrawn[i-1][j+1]==0)||(isDrawn[i+1][j+1]==0))){
                                        judgementPoints.add(new Coordinate(i,j));
                                        judgementPoints.add(new Coordinate(i,j));
                                    }
                                    else {
                                        judgementPoints.add(new Coordinate(i,j));
                                    }
                                }
                                else if(j+1>=areaWidth) { // 右边界
                                    if((j-1<0)||(i-1<0)||(i+1>=areaHeight)||
                                            ((isDrawn[i-1][j-1]==0)||(isDrawn[i+1][j-1]==0))){
                                        judgementPoints.add(new Coordinate(i,j));
                                        judgementPoints.add(new Coordinate(i,j));
                                    }
                                    else {
                                        judgementPoints.add(new Coordinate(i,j));
                                    }
                                }
                                else { // 非左右边界的尖点
                                    judgementPoints.add(new Coordinate(i,j));
                                    judgementPoints.add(new Coordinate(i,j));
                                }
                            }
                            else if(isDrawn[i][j] == 1) { // 连续的 1(2) 则加入第一个
                                if(judgementPoints.size()%2 == 0) {
                                    while((j+1 < areaWidth)&&
                                            ((isDrawn[i][j+1]==1)||(isDrawn[i][j+1]==2))) // 右边还有一个1或2
                                    {
                                        j++;
                                    }
                                    judgementPoints.add(new Coordinate(i,j));
                                }
                                else {
                                    judgementPoints.add(new Coordinate(i,j));
                                    while((j+1 < areaWidth)&&
                                            ((isDrawn[i][j+1]==1)||(isDrawn[i][j+1]==2))) // 右边还有一个1或2
                                    {
                                        j++;
                                    }
                                }
                            }
                        }
                        // 根据 judgementPoints 对第 i 行进行染色
                        // 如果是单数个点，忽略最后一个点
                        g.setColor(fillColor);
                        // 不断取两个点
                        Iterator<Coordinate> it = judgementPoints.iterator();
                        while(it.hasNext()) {
                            Coordinate point1 = it.next();
                            if(!it.hasNext())
                                break;
                            Coordinate point2 = it.next();
                            // 在 point1 和 point2 之间绘制
                            g.drawLine(Northwest.x+point1.y, Northwest.y+point1.x,
                                    Northwest.x+point2.y, Northwest.y+point2.x);
                        }
                    }
                }
                else // 中途按下
                {
                    x1 = me.getX(); // 获取中途顶点
                    y1 = me.getY();
                    vertex.add(new Coordinate(x1,y1)); // 中途点加入顶点集
                    if(x1 < Northwest.x)
                        Northwest.x = x1;
                    if(y1 < Northwest.y)
                        Northwest.y = y1;
                    if(x1 > Southeast.x)
                        Southeast.x = x1;
                    if(y1 > Southeast.y)
                        Southeast.y = y1;
                    drawPath.clear();
                }
            }
        }

        // 添加移动事件
        @Override
        public void mouseMoved(MouseEvent me)
        {
            if(started)
            {
                // 记录当前坐标
                int x2 = me.getX();
                int y2 = me.getY();
                if((Math.abs(x2-x) <= 4)&&(Math.abs(y2-y) <= 4)) // 如果在初始光标附近区域，改变鼠标样式
                {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    inFinalArea = true;
                }
                else if(inFinalArea) // 如果不在区域，回复默认鼠标样式
                {
                    setCursor(Cursor.getDefaultCursor());
                    inFinalArea = false;
                }
                Graphics g = getGraphics();
                // 清除旧直线
                g.setColor(defaultBackgroundColor);
                for(Coordinate coordinate : drawPath)
                {
                    g.drawLine(coordinate.x,coordinate.y,coordinate.x,coordinate.y);
                }
                g.setColor(drawColor);
                drawPath.clear();
                // 绘制新直线
                drawLine(x1,y1,x2,y2,g,true);
            }
        }
    }

    // Bezier曲线工具
    private class BezierCurveMouseAdapter extends MouseAdapter
    {
        private int order; // 阶数 (控制点数-1)
        private Coordinate nearWhichPoint; // 是否处于某个控制点的范围内，没有则为null
        List<Coordinate> controlPoints = new ArrayList<>(); // 控制点

        BezierCurveMouseAdapter() {
            order = -1;
            nearWhichPoint = null;
            controlPoints.clear();
            drawPath.clear();
        }

        // 添加移动事件
        @Override
        public void mouseMoved(MouseEvent me) {
            int tempx = me.getX();
            int tempy = me.getY();
            for(Coordinate c : controlPoints)
            {
                if((Math.abs(c.x-tempx) < 5) && (Math.abs(c.y-tempy) < 5))
                {
                    nearWhichPoint = c;
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    return;
                }
            }
            nearWhichPoint = null;
            setCursor(Cursor.getDefaultCursor());
        }

        // 添加拖拽事件
        @Override
        public void mouseDragged(MouseEvent me) {
            if(nearWhichPoint != null) // 指着某个控制点
            {
                Graphics g = getGraphics();
                int tempx = me.getX();
                int tempy = me.getY();
                modifySign(nearWhichPoint.x,nearWhichPoint.y,
                        tempx,tempy,g);
                CleanOldCurveAndDrawBezier(g);
            }
        }

        // 添加点击事件
        @Override
        public void mousePressed(MouseEvent me) {
            Graphics g = getGraphics();
            if(nearWhichPoint != null) // 指着某个控制点
            {
                return;
            }
            order++;
            int x = me.getX();
            int y = me.getY();
            addSign(x,y,g);
            CleanOldCurveAndDrawBezier(g);
        }

        private void CleanOldCurveAndDrawBezier(Graphics g) {
            if(order < 1) // 起码有2点
                return;
            // 清除旧曲线
            g.setColor(defaultBackgroundColor);
            for(Coordinate c : drawPath)
            {
                g.drawLine(c.x,c.y,c.x,c.y);
            }
            g.setColor(drawColor);
            drawPath.clear();
            // 根据控制点，绘制新曲线
            int pointNum = (order+1)*100; // 每增加一个控制点增加100个绘制上去的点
            CoorDouble cd1, cd2;
            CoorDouble cd;
            for (int t = 0; t < pointNum; t++) {
                double t1 = 1-(double)t/pointNum;
                double t2 = (double)t/pointNum;
                cd1 = getBt(t1,t2,0,order-1);
                cd2 = getBt(t1,t2,1, order);
                cd = new CoorDouble(t1*cd1.x + t2*cd2.x, t1*cd1.y + t2*cd2.y); // 理论结果
                Coordinate coo = new Coordinate((int)cd.x,(int)cd.y);
                g.drawLine(coo.x, coo.y, coo.x, coo.y);
                drawPath.add(coo);
            }
        }

        private CoorDouble getBt(double t1, double t2, int start, int end) { // 根据递推关系
            if((end == start+1)||(end == start))
            {
                return new CoorDouble(t1*controlPoints.get(start).x + t2*controlPoints.get(end).x,
                        t1*controlPoints.get(start).y + t2*controlPoints.get(end).y);
            }
            CoorDouble result1 = getBt(t1,t2,start,end-1);
            CoorDouble result2 = getBt(t1,t2,start+1,end);
            return new CoorDouble(t1*result1.x+t2*result2.x, t1*result1.y+t2*result2.y);
        }

        private void addSign(int x, int y, Graphics g) { // 添加控制点
            g.setColor(drawColor);
            g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
            controlPoints.add(new Coordinate(x,y));
        }

        private void modifySign(int x, int y, int newX, int newY, Graphics g) {
            // 抹去旧的控制点
            g.setColor(defaultBackgroundColor);
            g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
            for(Iterator<Coordinate> it = controlPoints.iterator(); it.hasNext(); )
            {
                Coordinate c = it.next();
                if((c.x == x) && (c.y == y))
                {
                    c.x = newX;
                    c.y = newY;
                    // 绘制修改后的控制点
                    g.setColor(drawColor);
                    g.drawArc(c.x-CPR,c.y-CPR, 2*CPR, 2*CPR,0,360);
                    return;
                }
            }
        }

        public void StopDrawing() { // 绘制结束
            Graphics g = getGraphics();
            g.setColor(defaultBackgroundColor);
            for(Coordinate c : controlPoints)
            {
                g.drawArc(c.x-CPR, c.y-CPR, 2*CPR, 2*CPR, 0, 360);
            }
            order = -1;
            controlPoints.clear();
        }
    }

    // 2次B样条曲线工具
    private class BspineCurveMouseAdapter extends MouseAdapter
    {
        private int order; // 阶数 (控制点数-1)
        private Coordinate nearWhichPoint; // 是否处于某个控制点的范围内，没有则为null
        List<Coordinate> controlPoints = new ArrayList<>(); // 控制点

        BspineCurveMouseAdapter() {
            order = -1;
            nearWhichPoint = null;
            controlPoints.clear();
            drawPath.clear();
        }

        // 添加移动事件
        @Override
        public void mouseMoved(MouseEvent me) {
            int tempx = me.getX();
            int tempy = me.getY();
            for(Coordinate c : controlPoints)
            {
                if((Math.abs(c.x-tempx) < 5) && (Math.abs(c.y-tempy) < 5))
                {
                    nearWhichPoint = c;
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    return;
                }
            }
            nearWhichPoint = null;
            setCursor(Cursor.getDefaultCursor());
        }

        // 添加拖拽事件
        @Override
        public void mouseDragged(MouseEvent me) {
            if(nearWhichPoint != null) // 指着某个控制点
            {
                Graphics g = getGraphics();
                int tempx = me.getX();
                int tempy = me.getY();
                modifySign(nearWhichPoint.x,nearWhichPoint.y,
                        tempx,tempy,g);
                CleanOldCurveAndDrawBSpine(g);
            }
        }

        // 添加点击事件
        @Override
        public void mousePressed(MouseEvent me) {
            Graphics g = getGraphics();
            if(nearWhichPoint != null) // 指着某个控制点
            {
                return;
            }
            order++;
            int x = me.getX();
            int y = me.getY();
            addSign(x,y,g);
            CleanOldCurveAndDrawBSpine(g);
        }

        private void CleanOldCurveAndDrawBSpine(Graphics g) {
            if(order < 1) // 起码有2点
                return;
            // 清除旧曲线
            g.setColor(defaultBackgroundColor);
            for(Coordinate c : drawPath)
            {
                g.drawLine(c.x,c.y,c.x,c.y);
            }
            g.setColor(drawColor);
            drawPath.clear();
            // 根据控制点，绘制新曲线
            int pointNum = (order+1)*150;
            double u = 1;
            for (int i = 0; i < pointNum; i++) {
                u += 0.005; // u 的下限是曲线的次数
                CoorDouble cd = Cu(u);
                Coordinate coo = new Coordinate((int)cd.x,(int)cd.y);
                g.drawLine(coo.x, coo.y, coo.x, coo.y);
                drawPath.add(coo);
            }
        }

        private CoorDouble Cu(double u) // 根据控制点和自变量u求B样条曲线
        {
            CoorDouble result = new CoorDouble(0.0,0.0);
            int index = 0;
            for(Coordinate c : controlPoints)
            {
                double bf = BasisFunc(index, u);
                result.x += bf * (double)c.x;
                result.y += bf * (double)c.y;
                index++;
            }
            return result;
        }

        private double BasisFunc(int index, double u) // 计算第index+1个函数，x轴为u时的基函数
        {
            double xAxis = u - (double) index;
            if((xAxis >= 0)&&(xAxis < 1))
            {
                return 0.5 * xAxis * xAxis;
            }
            if((xAxis >= 1)&&(xAxis < 2))
            {
                return 0.5 * (6 * xAxis - 2 * xAxis * xAxis - 3);
            }
            if((xAxis >= 2)&&(xAxis < 3))
            {
                return 0.5 * (3 - xAxis) * (3 - xAxis);
            }
            else
                return 0.0;
        }

        private void addSign(int x, int y, Graphics g) { // 添加控制点
            g.setColor(drawColor);
            g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
            controlPoints.add(new Coordinate(x,y));
        }

        private void modifySign(int x, int y, int newX, int newY, Graphics g) {
            // 抹去旧的控制点
            g.setColor(defaultBackgroundColor);
            g.drawArc(x-CPR,y-CPR, 2*CPR, 2*CPR,0,360);
            for(Iterator<Coordinate> it = controlPoints.iterator(); it.hasNext(); )
            {
                Coordinate c = it.next();
                if((c.x == x) && (c.y == y))
                {
                    c.x = newX;
                    c.y = newY;
                    // 绘制修改后的控制点
                    g.setColor(drawColor);
                    g.drawArc(c.x-CPR,c.y-CPR, 2*CPR, 2*CPR,0,360);
                    return;
                }
            }
        }

        public void StopDrawing() { // 绘制结束
            Graphics g = getGraphics();
            g.setColor(defaultBackgroundColor);
            for(Coordinate c : controlPoints)
            {
                g.drawArc(c.x-CPR, c.y-CPR, 2*CPR, 2*CPR, 0, 360);
            }
            order = -1;
            controlPoints.clear();
        }
    }

    // 裁剪工具
    private class CutMouseAdapter extends MouseAdapter
    {
        // 裁剪框左上和右下的坐标
        int ulx, uly;
        int lrx, lry;

        // 添加点击事件
        @Override
        public void mousePressed(MouseEvent me) {
            ulx = me.getX();
            uly = me.getY();
        }

        // 添加拖拽事件
        @Override
        public void mouseDragged(MouseEvent me) {}

        // 添加释放事件
        @Override
        public void mouseReleased(MouseEvent me) {
            // 得到裁剪框的左上和右下
            lrx = me.getX();
            lry = me.getY();
            int tempx1 = ulx > lrx ? lrx : ulx;
            int tempy1 = uly > lry ? lry : uly;
            int tempx2 = ulx < lrx ? lrx : ulx;
            int tempy2 = uly < lry ? lry : uly;
            ulx = tempx1; uly = tempy1;
            lrx = tempx2; lry = tempy2;
            Graphics g = getGraphics();
            for(Iterator<LineObj> it = lineObjs.iterator(); it.hasNext();) // 完全情况
            {
                LineObj lo = it.next();
                // 为所有直线对象定区域码
                lo.setP1Code(JudgeArea(lo.x1,lo.y1));
                lo.setP2Code(JudgeArea(lo.x2,lo.y2));
                // 开始裁剪
                if(lo.p1Code.equals("0000") && lo.p2Code.equals("0000"))
                    continue; // 完全在框内
                if(CompletelyNotIn(lo)) // 完全在框外
                {
                    // 将该直线抹去并在直线对象中除名
                    for(Coordinate c : lo.drawPath)
                    {
                        if(colorCanvas[c.x][c.y].org.equals(colorCanvas[c.x][c.y].cur))
                            g.setColor(defaultBackgroundColor);
                        else
                            g.setColor(colorCanvas[c.x][c.y].org);
                        UpdateColorCanvas(c.x,c.y,colorCanvas[c.x][c.y].org);
                        g.drawLine(c.x,c.y,c.x,c.y);
                    }
                    it.remove();
                }
            }
            for(LineObj lo : lineObjs)
            {
                // 对特定一条直线
                for(Iterator<Coordinate> it = lo.drawPath.iterator(); it.hasNext();)
                {
                    Coordinate c = it.next();
                    if(!InArea(c.x,c.y))
                    {
                        // 抹掉该点
                        if(colorCanvas[c.x][c.y].org.equals(colorCanvas[c.x][c.y].cur))
                            g.setColor(defaultBackgroundColor);
                        else
                            g.setColor(colorCanvas[c.x][c.y].org);
                        UpdateColorCanvas(c.x,c.y,colorCanvas[c.x][c.y].org);
                        g.drawLine(c.x,c.y,c.x,c.y);
                        it.remove();
                    }
                }
                // 重新评估顶点
                lo.x1 = Integer.MAX_VALUE;  lo.x2 = Integer.MIN_VALUE;
                for(Iterator<Coordinate> it = lo.drawPath.iterator(); it.hasNext();)
                {
                    Coordinate c = it.next();
                    if(c.x < lo.x1)
                    {
                        lo.x1 = c.x;
                        lo.y1 = c.y;
                    }
                    if(c.x > lo.x2)
                    {
                        lo.x2 = c.x;
                        lo.y2 = c.y;
                    }
                }
            }
        }
        private String JudgeArea(int x, int y) {
            // 根据坐标相对于裁剪框的位置获得编码
            if((x <= ulx) && (y <= uly))
                return "1001";
            if((x > ulx) && (x < lrx) && (y < uly))
                return "1000";
            if((x >= lrx) && (y <= uly))
                return "1010";
            if((x < ulx) && (y < lry))
                return "0001";
            if((x > lrx) && (y < lry))
                return "0010";
            if((x <= ulx) && (y >= lry))
                return "0101";
            if((x > ulx) && (x < lrx) && (y >= lry))
                return "0100";
            if((x >= lrx) && (y >= lry))
                return "0110";
            return "0000"; // 在中间
        }
        private boolean CompletelyNotIn(LineObj lo) {
            if((lo.p1Code.charAt(0) == '1') && lo.p2Code.charAt(0) == '1')
                return true;
            if((lo.p1Code.charAt(1) == '1') && lo.p2Code.charAt(1) == '1')
                return true;
            if((lo.p1Code.charAt(2) == '1') && lo.p2Code.charAt(2) == '1')
                return true;
            if((lo.p1Code.charAt(3) == '1') && lo.p2Code.charAt(3) == '1')
                return true;
            return false;
        }
        private boolean InArea(int x, int y) {
            return ((x >= ulx) && (x <= lrx) && (y >= uly) && (y <= lry));
        }
    }

    // 平移
    public void Translation(int x0, int y0) { // 参数是水平与垂直位移量
        if(lattestObj != null)
        {
            Set<Coordinate> newPath = new HashSet<>();
            for(Iterator<Coordinate> it = lattestObj.drawPath.iterator(); it.hasNext();) {
                Coordinate c = it.next();
                newPath.add(new Coordinate(c.x + x0, c.y + y0)); // 水平坐标运算
            }
            // 抹去旧图形
            Graphics g = getGraphics();
            g.setColor(defaultBackgroundColor);
            for(Iterator<Coordinate> it = lattestObj.drawPath.iterator(); it.hasNext();) {
                Coordinate c = it.next();
                g.setColor(defaultBackgroundColor);
                drawLine(c.x,c.y,c.x,c.y,g,true);
            }
            // 绘制新图形
            g.setColor(drawColor);
            for(Iterator<Coordinate> it = newPath.iterator(); it.hasNext();) {
                Coordinate c = it.next();
                drawLine(c.x,c.y,c.x,c.y,g,true);
            }
            // 更新 lattestObj
            lattestObj.drawPath = newPath;
        }
    }

    // 旋转
    public void Rotation(int x0, int y0, int angle) { // 基准点是(x0,y0)，角度是度数
        if(lattestObj != null)
        {
            Set<Coordinate> newPath = new HashSet<>();
            for(Iterator<Coordinate> it = lattestObj.drawPath.iterator(); it.hasNext();) {
                Coordinate c = it.next();
                // 旋转计算
                double radian = angle*(Math.PI/180);
                newPath.add(new Coordinate(x0 + (int)((c.x-x0) * Math.cos(radian) - (c.y-y0) * Math.sin(radian)),
                        y0 + (int)((c.x-x0) * Math.sin(radian) + (c.y-y0) * Math.cos(radian)) ));
            }
            // 抹去旧图形
            Graphics g = getGraphics();
            g.setColor(defaultBackgroundColor);
            for(Iterator<Coordinate> it = lattestObj.drawPath.iterator(); it.hasNext();) {
                Coordinate c = it.next();
                if((c.x>=0) && (c.x<DRAW_WIDTH) && (c.y>=0) && (c.y<DRAW_HEIGHT))
                {
                    g.setColor(defaultBackgroundColor);
                    drawLine(c.x,c.y,c.x,c.y,g,true);
                }
            }
            // 绘制新图形
            g.setColor(drawColor);
            for(Iterator<Coordinate> it = newPath.iterator(); it.hasNext();) {
                Coordinate c = it.next();
                drawLine(c.x,c.y,c.x,c.y,g,true);
            }
            // 更新 lattestObj
            lattestObj.drawPath = newPath;
        }
    }

    // 缩放
    public void Scaling(double Sx, double Sy) {
        if(lattestObj != null)
        {
            Set<Coordinate> newPath = new HashSet<>();
            for(Iterator<Coordinate> it = lattestObj.drawPath.iterator(); it.hasNext();) {
                Coordinate c = it.next();
                // 缩放计算
                newPath.add(new Coordinate((int)(Sx*c.x),(int)(Sy*c.y)));
            }
            // 抹去旧图形
            Graphics g = getGraphics();
            g.setColor(defaultBackgroundColor);
            for(Iterator<Coordinate> it = lattestObj.drawPath.iterator(); it.hasNext();) {
                Coordinate c = it.next();
                if((c.x>=0) && (c.x<DRAW_WIDTH) && (c.y>=0) && (c.y<DRAW_HEIGHT))
                {
                    g.setColor(defaultBackgroundColor);
                    drawLine(c.x,c.y,c.x,c.y,g,true);
                }
            }
            // 绘制新图形
            g.setColor(drawColor);
            for(Iterator<Coordinate> it = newPath.iterator(); it.hasNext();) {
                Coordinate c = it.next();
                drawLine(c.x,c.y,c.x,c.y,g,true);
            }
            // 更新 lattestObj
            lattestObj.drawPath = newPath;
        }
    }
}
