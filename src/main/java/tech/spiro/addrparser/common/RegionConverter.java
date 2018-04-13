package tech.spiro.addrparser.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/9/2018
 */
public class RegionConverter {

    public static RegionInfo convert(RegionDTO regionDTO) {
        RegionInfo.Builder builder = new RegionInfo.Builder();

        // parentCode
        builder.parentCode(Integer.valueOf(regionDTO.getParentCode()));
        // code
        builder.code(Integer.valueOf(regionDTO.getCode()));
        // name
        builder.name(regionDTO.getName());

        // level
        String levelStr = regionDTO.getLevel();
        builder.level(RegionLevel.valueOf(levelStr.toUpperCase()));

        // center
        String[] center = regionDTO.getCenter().split(",");
        Point centerPoint = new Point(Double.valueOf(center[0]), Double.valueOf(center[1]));
        builder.center(centerPoint);

        // polyline
        String polyline = regionDTO.getPolyline();
        String[] blocks = polyline.split("\\|");

        for (String block : blocks) {
            List<Point> blockObj = new ArrayList<>();
            String[] points = block.split(";");

            for (String point : points) {
                String[] lonLat = point.split(",");
                Point pointObj = new Point(Double.valueOf(lonLat[0]), Double.valueOf(lonLat[1]));
                blockObj.add(pointObj);
            }
            builder.addPolyline(blockObj);
        }

        return builder.build();
    }
}
