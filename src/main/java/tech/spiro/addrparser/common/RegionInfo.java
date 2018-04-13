package tech.spiro.addrparser.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 8/8/2017
 */
public class RegionInfo {

    private final int parentCode;
    private final int code;
    private final String name;
    private final RegionLevel level;
    private final Point center;
    private final List<List<Point>> polyline;

    private final ContainPointJudge containPointJudge = ContainPointJudgeFactory.create();

    private RegionInfo(Builder builder) {
        this.parentCode = builder.parentCode;
        this.code = builder.code;
        this.name = builder.name;
        this.level = builder.level;
        this.center = builder.center;
        this.polyline = Collections.unmodifiableList(builder.polyline);
        containPointJudge.initPolygons(this.polyline);
    }

    public int getParentCode() {
        return parentCode;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public RegionLevel getLevel() {
        return level;
    }

    public Point getCenter() {
        return center;
    }

    public List<List<Point>> getPolyline() {
        return polyline;
    }

    public boolean contain(Point point) {
        return this.containPointJudge.contain(point);
    }

    @Override
    public String toString() {
        return "RegionInfo{" +
                "parentCode=" + parentCode +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", level=" + level +
                '}';
    }

    public static class Builder {
        private int parentCode;
        private int code;
        private String name;
        private RegionLevel level;
        private Point center;
        private List<List<Point>> polyline = new ArrayList<>();

        public Builder() {
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder parentCode(int parentCode) {
            this.parentCode = parentCode;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder level(RegionLevel level) {
            this.level = level;
            return this;
        }

        public Builder center(Point center) {
            this.center = center;
            return this;
        }

        public Builder addPolyline(List<Point> polyline) {
            if (polyline != null && (!polyline.isEmpty())) {
                this.polyline.add(Collections.unmodifiableList(polyline));
            }
            return this;
        }

        public RegionInfo build() {

            if (this.name == null) {
                throw new IllegalArgumentException("<name> cannot be null.");
            }

            if (this.level == null) {
                throw new IllegalArgumentException("<level> cannot be null.");
            }

            if (this.center == null) {
                throw new IllegalArgumentException("<center> cannot be null.");
            }

            return new RegionInfo(this);
        }

    }
}
