package GraphicObjects;


public class LineObj extends GraphicObj { // 直线对象

    public int x1,y1, x2,y2; // 两个端点

    public String p1Code; // 端点1编码
    public String p2Code; // 端点2编码

    public LineObj(int x1, int y1, int x2, int y2) {
        super();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.drawPath.clear();
    }

    public LineObj() {
        super();
        x1 = -1; y1 = -1; x2 = -1; y2 = -1;
        this.drawPath.clear();
    }

    public void setP1Code(String p1Code) {
        this.p1Code = p1Code;
    }

    public void setP2Code(String p2Code) {
        this.p2Code = p2Code;
    }
}