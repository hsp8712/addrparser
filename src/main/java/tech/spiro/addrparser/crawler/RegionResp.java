package tech.spiro.addrparser.crawler;

import java.util.List;

/**
 *
 * @author Spiro Huang
 * @since 1.0
 */
public class RegionResp {
    private String adcode;
    private String name;
    private String center;
    private String level;
    private String polyline;
    private List<RegionResp> regionResps;

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public List<RegionResp> getRegionResps() {
        return regionResps;
    }

    public void setRegionResps(List<RegionResp> regionResps) {
        this.regionResps = regionResps;
    }

    @Override
    public String toString() {
        return "RegionResp{" +
                "adcode='" + adcode + '\'' +
                ", name='" + name + '\'' +
                ", center='" + center + '\'' +
                ", level='" + level + '\'' +
                ", polyline='" + polyline + '\'' +
                ", regionResps=" + regionResps +
                '}';
    }
}
