package Coordinates;

import java.awt.*;

public class ColorTempCoor {

    public Color org;  // 原先坐标的颜色
    public Color cur;  // 现在坐标的颜色

    public ColorTempCoor(Color org, Color cur) {
        this.org = org;
        this.cur = cur;
    }

    @Override
    // 重载函数，判断两个ColorTempCoor对象是否在值上相等
    public boolean equals(Object obj) {
        if(obj==null) { return false; }
        if(this==obj) { return true; }
        if(obj instanceof ColorTempCoor){
            ColorTempCoor temp = (ColorTempCoor)obj;
            if((temp.cur.getRGB()==this.cur.getRGB())&&(temp.org.getRGB()==this.org.getRGB()))
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