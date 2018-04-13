package tech.spiro.addrparser.common;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 7/29/2017
 */
public enum RegionLevel {

    COUNTRY(0),
    PROVINCE(1),
    CITY(2),
    DISTRICT(3),
    STREET(4);

    private int value;

    RegionLevel(int value) {
        this.value = value;
    }

    public static RegionLevel getByValue(int value) {
        RegionLevel[] values = RegionLevel.values();
        for (RegionLevel regionLevel : values) {
            if (regionLevel.value == value) {
                return regionLevel;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
