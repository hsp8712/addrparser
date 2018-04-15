package tech.spiro.addrparser.parser;

import java.io.*;
import java.util.*;

import tech.spiro.addrparser.common.*;
import tech.spiro.addrparser.io.RegionDataInput;

public class LocationParserEngine {

    private RegionDataInput regionDataInput;

    /**
     * Province region info set
     */
    private Set<RegionInfo> provinceSet = new HashSet<>();

    /**
     * Province and city set mapping
     * Key: province code  Value: province provCitySetMap
     */
    private Map<Integer, Set<RegionInfo>> provCitySetMap = new HashMap<>();

    /**
     * City and district or street set mapping
     * Key: city code Value: district or street
     */
    private Map<Integer, Set<RegionInfo>> cityDistrictSetMap = new HashMap<>();

    public LocationParserEngine(RegionDataInput regionDataInput) {
        this.regionDataInput = regionDataInput;
    }

    public void init() throws ParserEngineException {
        try {
            regionDataInput.init();
        } catch (IOException e) {
            throw new ParserEngineException(e.getMessage(), e);
        }

        try {
            while (true) {
                RegionDTO regionDTO = regionDataInput.read();
                if (regionDTO == null) {
                    break;
                }
                sortThrough(regionDTO);
            }
        } catch (IOException e) {
            throw new ParserEngineException(e.getMessage(), e);
        } finally {
            try {
                regionDataInput.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * SortThrough region info.
     * @param regionDTO
     */
    private void sortThrough(RegionDTO regionDTO) {
        RegionInfo regionInfo = RegionConverter.convert(regionDTO);
        RegionLevel regionLevel = regionInfo.getLevel();

        if (regionLevel == RegionLevel.PROVINCE) {
            provinceSet.add(regionInfo);
            return;
        }

        if (regionLevel == RegionLevel.CITY) {
            Integer provCode = regionInfo.getParentCode();
            Set<RegionInfo> provCitySet = provCitySetMap.get(provCode);
            if (provCitySet == null) {
                provCitySet = new HashSet<>();
                provCitySetMap.put(provCode, provCitySet);
            }
            provCitySet.add(regionInfo);
            return;
        }

        if (regionLevel == RegionLevel.DISTRICT || regionLevel == RegionLevel.STREET) {
            Integer cityCode = regionInfo.getParentCode();
            Set<RegionInfo> cityDistrictSet = cityDistrictSetMap.get(cityCode);
            if (cityDistrictSet == null) {
                cityDistrictSet = new HashSet<>();
                cityDistrictSetMap.put(cityCode, cityDistrictSet);
            }
            cityDistrictSet.add(regionInfo);
        }
    }

    /**
     * Get location info by {@link Point}
     * @param point
     * @return
     */
    public Location parse(Point point) {

        if (point == null) {
            throw new IllegalArgumentException("<point> is null.");
        }

        RegionInfo prov = null;
        RegionInfo city = null;
        RegionInfo district = null;

        for (RegionInfo regionInfo : provinceSet) {
            if (regionInfo.contain(point)) {
                prov = regionInfo;
                break;
            }
        }

        if (prov == null) {
            return null;
        }

        Integer provCode = prov.getCode();
        Set<RegionInfo> citySet = provCitySetMap.get(provCode);

        if (citySet == null) {
            return new Location(prov, null, null);
        }

        for (RegionInfo regionInfo : citySet) {
            if (regionInfo.contain(point)) {
                city = regionInfo;
                break;
            }
        }

        if (city == null) {
            return new Location(prov, null, null);
        }

        Integer cityCode = city.getCode();
        Set<RegionInfo> districtSet = cityDistrictSetMap.get(cityCode);

        if (districtSet == null) {
            return new Location(prov, city, null);
        }

        for (RegionInfo regionInfo : districtSet) {
            if (regionInfo.contain(point)) {
                district = regionInfo;
                break;
            }
        }

        return new Location(prov, city, district);
    }

    /**
     * Get location info by longitude/latitude
     * @param lon
     * @param lat
     * @return
     */
    public Location parse(double lon, double lat) {
        Point point = new Point(lon, lat);
        return parse(point);
    }


    /**
     * TODO
     * @param code
     * @return
     */
    public RegionInfo getRegionInfo(int code) {
        throw new NotImplementedException();
    }

    /**
     * TODO
     * @param name
     * @return
     */
    public RegionInfo getRegionInfo(String name) {
        throw new NotImplementedException();
    }

    /**
     * TODO
     * @param code
     * @return
     */
    public List<RegionInfo> getSubRegionInfoList(int code) {
        throw new NotImplementedException();
    }
}
