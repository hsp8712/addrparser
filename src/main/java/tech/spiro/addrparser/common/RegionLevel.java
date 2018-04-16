package tech.spiro.addrparser.common;

/**
 * @author Spiro Huang
 * @since 1.0
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
