package tech.spiro.addrparser.parser;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.com.tiza.earth4j.common.*;
import com.alibaba.fastjson.JSON;
import tech.spiro.addrparser.common.*;

public class RegionData {

    /** Root region code, i.e. 'country'  */
    public static final int ROOT_CODE = 100000;
    public static final String DEFAULT_REGION_DATA_FILE = "earth4j.dat";

    private Set<RegionInfo> provinceSet = new HashSet<>();

    /**
     * Key: province code  Value: province provCitySetMap
     */
    private Map<Integer, Set<RegionInfo>> provCitySetMap = new HashMap<>();

    /**
     * Key: city code Value: district or street code
     */
    private Map<Integer, Set<RegionInfo>> cityDistrictSetMap = new HashMap<>();


    private void parse(InputStream fileIn) throws RegionDataLoadException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileIn))) {
            String line = null;

            while ((line = br.readLine()) != null) {
                RegionDTO regionDTO = JSON.parseObject(line, RegionDTO.class);
                RegionInfo regionInfo = RegionConverter.convert(regionDTO);

                RegionLevel regionLevel = regionInfo.getLevel();

                if (regionLevel == RegionLevel.PROVINCE) {
                    provinceSet.add(regionInfo);
                    continue;
                }

                if (regionLevel == RegionLevel.CITY) {
                    Integer provCode = regionInfo.getParentCode();
                    Set<RegionInfo> provCitySet = provCitySetMap.get(provCode);
                    if (provCitySet == null) {
                        provCitySet = new HashSet<>();
                        provCitySetMap.put(provCode, provCitySet);
                    }
                    provCitySet.add(regionInfo);
                    continue;
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
        } catch (IOException e) {
            throw new RegionDataLoadException(e);
        }
    }

    /**
     * Load region data
     * 
     * @throws RegionDataLoadException
     */
    public void load() throws RegionDataLoadException {

        InputStream fileIn = this.getClass().getClassLoader()
                .getResourceAsStream(DEFAULT_REGION_DATA_FILE);

        if (fileIn == null) {
            throw new RegionDataLoadException(DEFAULT_REGION_DATA_FILE + " is not found.");
        }

        parse(fileIn);
    }

    /**
     * Load gis data
     *
     * @throws RegionDataLoadException
     */
    public void load(String filename) throws RegionDataLoadException {

        FileInputStream in = null;
        try {
            in = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new RegionDataLoadException("File: " + filename + " is not found.");
        }

        parse(in);
    }

    /**
     * Get location info by longitude/latitude
     * 
     * @param lon
     *            longitude
     * @param lat
     *            latitude
     * @return
     */
    public Location getLocation(double lon, double lat) {

        Point point = new Point(lon, lat);

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

}
