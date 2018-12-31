package Coordinates;

// 坐标类，用于显示路径
public class Coordinate {

    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {    // 重载函数，判断两个 Coordinate 对象是否在值上相等
        if(obj==null) { return false; }    // 空对象
        if(this==obj) { return true; }     // 同一个对象
        if(obj instanceof ColorTempCoor){
            Coordinate temp = (Coordinate) obj;
            if((temp.x==this.x) && (temp.y==this.y)) // 比较值
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }
}
