package tech.spiro.addrparser.common;

import java.util.List;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/10/2018
 */
public interface ContainPointJudge {

    /**
     * Initialize polygons
     * @param polyline Multi polygons point list
     */
    void initPolygons(List<List<Point>> polyline);

    boolean contain(Point point);
}
