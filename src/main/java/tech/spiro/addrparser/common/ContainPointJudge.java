package tech.spiro.addrparser.common;

import java.util.List;

/**
 * Interface to judge whether a point in polygons
 * @author Spiro Huang
 * @since 1.0
 */
public interface ContainPointJudge {

    /**
     * Initialize polygons
     * @param polyline Multi polygons point list
     */
    void initPolygons(List<List<Point>> polyline);

    boolean contain(Point point);
}
