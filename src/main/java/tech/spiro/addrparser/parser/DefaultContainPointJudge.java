package tech.spiro.addrparser.parser;

import tech.spiro.addrparser.common.ContainPointJudge;
import tech.spiro.addrparser.common.Point;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/10/2018
 */
public class DefaultContainPointJudge implements ContainPointJudge {

    private Set<Polygon> polygonSet = new HashSet<>();

    @Override
    public void initPolygons(List<List<tech.spiro.addrparser.common.Point>> polyline) {
        for (List<tech.spiro.addrparser.common.Point> points : polyline) {
            Polygon polygon = new Polygon();
            for (tech.spiro.addrparser.common.Point point : points) {
                polygon.addPoint(double2Int(point.getLon()), double2Int(point.getLat()));
            }
            polygon.getBounds();
            polygonSet.add(polygon);
        }
    }

    @Override
    public boolean contain(Point point) {

        int x = double2Int(point.getLon());
        int y = double2Int(point.getLat());

        for (Polygon polygon : polygonSet) {
            if (polygon.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public int double2Int(double d) {
        return new Double((d * 1000000)).intValue();
    }
}
