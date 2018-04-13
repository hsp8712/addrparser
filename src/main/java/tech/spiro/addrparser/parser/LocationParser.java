package tech.spiro.addrparser.parser;

import tech.spiro.addrparser.common.Point;

public class LocationParser {

    private static Object _lock = new Object();
    private volatile RegionData regionData = null;

    private static LocationParser parser = new LocationParser();

    public static LocationParser getInstance() {
        return parser;
    }

    private LocationParser() {
    }

    public void init(String filename) throws RegionDataLoadException {

        if(this.regionData != null) {
            return;
        }

        synchronized (_lock) {
            if(this.regionData != null) {
                return;
            }

            RegionData _regionData = new RegionData();
            _regionData.load(filename);

            this.regionData = _regionData;
        }
    }

    public void init() throws RegionDataLoadException {

        if(this.regionData != null) {
            return;
        }

        synchronized (_lock) {
            if(this.regionData != null) {
                return;
            }

            RegionData _regionData = new RegionData();
            _regionData.load();

            this.regionData = _regionData;
        }
    }

    public boolean isAvailable() {
        return this.regionData != null;
    }

    /**
     * Parse Location info by longitude/latitude
     * @param lon  longitude
     * @param lat  latitude
     * @return  
     */
    public Location parse(double lon, double lat) {
        if(!isAvailable()) {
            throw new IllegalStateException("Location parser is not available, please initialized first.");
        }
        return this.regionData.getLocation(lon, lat);
    }


    public Location parse(Point point) {
        return parse(point.getLon(), point.getLat());
    }

}
