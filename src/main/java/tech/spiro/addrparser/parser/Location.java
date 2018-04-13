package tech.spiro.addrparser.parser;

import tech.spiro.addrparser.common.RegionInfo;

public class Location {
    private final RegionInfo prov;
    private final RegionInfo city;
    private final RegionInfo district;

    public Location(RegionInfo prov, RegionInfo city, RegionInfo district) {
        this.prov = prov;
        this.city = city;
        this.district = district;
    }

    public RegionInfo getProv() {
        return prov;
    }

    public RegionInfo getCity() {
        return city;
    }

    public RegionInfo getDistrict() {
        return district;
    }

    @Override
    public String toString() {
        return "Location{" +
                "prov=" + prov +
                ", city=" + city +
                ", district=" + district +
                '}';
    }
}
