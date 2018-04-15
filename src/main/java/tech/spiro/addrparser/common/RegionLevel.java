package tech.spiro.addrparser.common;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 7/29/2017
 */
public enum RegionLevel {

    COUNTRY,
    PROVINCE,
    CITY,
    DISTRICT,
    STREET;

    public static RegionLevel valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
