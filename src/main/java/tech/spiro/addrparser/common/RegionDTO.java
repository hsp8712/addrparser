package tech.spiro.addrparser.common;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 7/30/2017
 */
public class RegionDTO {
    private String parentCode;
    private String code;
    private String name;

    /**
     * Available value: "street", "province", "district", "city"
     */
    private String level;

    /**
     * Format:  "[longitude],[latitude]"
     */
    private String center;

    /**
     * Format:  "[longitude1],[latitude1];[longitude2],[latitude2];[longitude3],[latitude3];..."
     */
    private String polyline;

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }
}
