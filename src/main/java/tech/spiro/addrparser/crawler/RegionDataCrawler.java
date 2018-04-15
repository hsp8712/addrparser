package tech.spiro.addrparser.crawler;

import tech.spiro.addrparser.common.RegionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.spiro.addrparser.common.RegionLevel;
import tech.spiro.addrparser.io.RegionDataOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 8/2/2017
 */
public class RegionDataCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(RegionDataCrawler.class);

    public static final int COUNTRY_CODE = 100000;
//    public static final String AMAP_KEY = "444f3432eda1209dfe4cb8edfd0211f9";
    public static final String AMAP_KEY = "b504cbfb3664f21235dd413fc73b44c5";

    private AtomicInteger invokerCount = new AtomicInteger(0);

    private RegionDataOutput regionOutput;

    public RegionDataCrawler() {
    }

    public RegionDataCrawler(RegionDataOutput regionOutput) throws IOException {
        this.regionOutput = regionOutput;
        this.regionOutput.init();
    }

    private List<RegionDTO> getSubRegionDTOs(int code) throws GetDistrictsException {

        RestClient restClient = new RestClient();
        restClient.setKeywords(Integer.toString(code));
        restClient.setKey(AMAP_KEY);
        restClient.setExtensions("base");
        restClient.setSubdistrict("1");

        DataResponse dataResponse = restClient.getDistrictResponse();
        LOG.debug("Invoker.count=" + invokerCount.incrementAndGet());

        if (!dataResponse.isSuccess()) {
            throw new GetDistrictsException("Get failed, infocode="
                    + dataResponse.getInfocode() + ", info=" + dataResponse.getInfo());
        }

        List<District> districts = dataResponse.getDistricts();
        if (districts.isEmpty()) {
            LOG.warn("Have no districts return, keyword code={}", code);
            return null;
        }

        District curDistrict = districts.get(0);
        List<District> subDistricts = curDistrict.getDistricts();

        List<RegionDTO> subRegionDTOs = new ArrayList<>();

        if (subDistricts == null || subDistricts.isEmpty()) {
            LOG.warn("Have no sub districts, {}", curDistrict);
        } else {
            for (District district : subDistricts) {
                RegionDTO regionDTO = new RegionDTO();

                try {
                    regionDTO.setCode(Integer.valueOf(district.getAdcode()));
                    regionDTO.setLevel(RegionLevel.valueOf(district.getLevel().toUpperCase()));
                    regionDTO.setCenter(district.getCenter());
                    regionDTO.setParentCode(code);
                    regionDTO.setName(district.getName());
                } catch (Exception e) {
                    throw new GetDistrictsException("<district> cannot convert to RegionDTO, maybe district invalid, district:"
                            + district.toString(), e);
                }
                subRegionDTOs.add(regionDTO);
            }
        }

        // Fill boundary info
        for (RegionDTO subRegionDTO : subRegionDTOs) {
            RestClient subRestClient = new RestClient();
            subRestClient.setExtensions("all");
            subRestClient.setKey(AMAP_KEY);
            subRestClient.setKeywords(Integer.toString(subRegionDTO.getCode()));
            subRestClient.setSubdistrict("0");

            DataResponse subDataResp = subRestClient.getDistrictResponse();
            LOG.debug("Invoker.count=" + invokerCount.incrementAndGet());

            if (!subDataResp.isSuccess()) {
                throw new GetDistrictsException("Get sub district failed");
            }

            List<District> _subDistricts = subDataResp.getDistricts();
            if (_subDistricts.isEmpty()) {
                throw new GetDistrictsException("Get sub district failed");
            }

            District _subDistrict = _subDistricts.get(0);
            subRegionDTO.setPolyline(_subDistrict.getPolyline());
        }

        return subRegionDTOs;
    }

    private void regionBatchOutput(List<RegionDTO> regionDTOs) throws GetDistrictsException {
        try {
            for (RegionDTO regionDTO : regionDTOs) {
                this.regionOutput.write(regionDTO);
            }
        } catch (IOException e) {
            throw new GetDistrictsException(e.getMessage(), e);
        }
    }

    public void loadCountry() throws GetDistrictsException {
        if (regionOutput == null) {
            throw new GetDistrictsException("<regionOutput> is null.");
        }
        // Get provinces
        List<RegionDTO> provRegionDTOs = getSubRegionDTOs(COUNTRY_CODE);
        regionBatchOutput(provRegionDTOs);

        for (RegionDTO provRegionDTO : provRegionDTOs) {
            loadProv(provRegionDTO.getCode());
        }
    }

    public void loadProv(int provCode) throws GetDistrictsException {
        if (regionOutput == null) {
            throw new GetDistrictsException("<regionOutput> is null.");
        }

        // Get cities
        List<RegionDTO> cityRegionDTOs = getSubRegionDTOs(provCode);
        regionBatchOutput(cityRegionDTOs);

        for (RegionDTO cityRegionDTO : cityRegionDTOs) {
            loadCity(cityRegionDTO.getCode());
        }
    }

    public void loadCity(int cityCode) throws GetDistrictsException {
        if (regionOutput == null) {
            throw new GetDistrictsException("<regionOutput> is null.");
        }

        // Get districts
        List<RegionDTO> districtRegionDTOs = getSubRegionDTOs(cityCode);
        regionBatchOutput(districtRegionDTOs);
    }

}
