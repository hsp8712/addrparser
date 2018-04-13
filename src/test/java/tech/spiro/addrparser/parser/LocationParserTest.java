package tech.spiro.addrparser.parser;

import tech.spiro.addrparser.common.Point;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/10/2018
 */
public class LocationParserTest {

    public static final double MIN_LON = 99.545478;
    public static final double MAX_LON = 117.01384;

    public static final double MIN_LAT = 24.564905;
    public static final double MAX_LAT = 41.324743;

    public static final double LON_SPAN = MAX_LON - MIN_LON;
    public static final double LAT_SPAN = MAX_LAT - MIN_LAT;


    public Point getRandPoint() {
        double lon = MIN_LON + Math.random() * LON_SPAN;
        double lat = MIN_LAT + Math.random() * LAT_SPAN;

        return new Point(lon, lat);
    }

    @Test
    public void randPoint() {
        for (int i = 0; i < 100; i++) {
            System.out.println(getRandPoint());
        }
    }

    @Before
    public void before() {
        long t1 = System.currentTimeMillis();
        LocationParser.getInstance().init();
        System.out.println("init finish, take " + (System.currentTimeMillis() - t1) + " ms.");
    }

    @Test
    public void parse() {

        int testTotalCount = 10000;
        for (int j = 0; j < 5; j++) {
            int allRegionCount = 0;

            long t2 = System.currentTimeMillis();

            for (int i = 0; i < testTotalCount; i++) {
                Location location = LocationParser.getInstance().parse(getRandPoint());
                if (location != null
                        && location.getProv() != null
                        && location.getCity() != null
                        && location.getDistrict() != null) {
                    allRegionCount++;
                }
            }

            System.out.println("Parsing take " + (System.currentTimeMillis() - t2) + " ms, allRegionCount=" + allRegionCount);
        }
    }
}