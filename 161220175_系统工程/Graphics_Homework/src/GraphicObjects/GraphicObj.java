package GraphicObjects;

import Coordinates.Coordinate;

import java.util.HashSet;
import java.util.Set;

public class GraphicObj { // 一般的图形对象

    public Set<Coordinate> drawPath = new HashSet<>(); // 图形对象路径收集器

    public GraphicObj() {
        drawPath.clear();
    }

}
