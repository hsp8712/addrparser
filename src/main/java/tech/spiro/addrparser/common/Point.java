package tech.spiro.addrparser.common;

public class Point
{
	private final double lon;
	private final double lat;

    public Point(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return "Point{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
