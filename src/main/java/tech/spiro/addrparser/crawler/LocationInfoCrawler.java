package tech.spiro.addrparser.crawler;

import tech.spiro.addrparser.common.RegionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 8/2/2017
 */
public class LocationInfoCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(LocationInfoCrawler.class);

    public static final String COUNTRY_CODE = "100000";
//    public static final String AMAP_KEY = "444f3432eda1209dfe4cb8edfd0211f9";
    public static final String AMAP_KEY = "b504cbfb3664f21235dd413fc73b44c5";

    private AtomicInteger invokerCount = new AtomicInteger(0);

    private RegionOutput regionOutput;

    public LocationInfoCrawler() {
    }

    public LocationInfoCrawler(RegionOutput regionOutput) {
        this.regionOutput = regionOutput;
    }

    private List<RegionDTO> getSubRegionDTOs(String code) throws GetDistrictsException {

        RestClient restClient = new RestClient();
        restClient.setKeywords(code);
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
                regionDTO.setCode(district.getAdcode());
                regionDTO.setLevel(district.getLevel());
                regionDTO.setCenter(district.getCenter());
                regionDTO.setParentCode(code);
                regionDTO.setName(district.getName());
                subRegionDTOs.add(regionDTO);
            }
        }

        // Fill boundary info
        for (RegionDTO subRegionDTO : subRegionDTOs) {
            RestClient subRestClient = new RestClient();
            subRestClient.setExtensions("all");
            subRestClient.setKey(AMAP_KEY);
            subRestClient.setKeywords(subRegionDTO.getCode());
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

    public void loadCountry() throws GetDistrictsException {
        if (regionOutput == null || !regionOutput.isAvailable()) {
            LOG.warn("Have no <regionOutput> or not available, do not execute.");
            return;
        }
        // Get provinces
        List<RegionDTO> provRegionDTOs = getSubRegionDTOs(COUNTRY_CODE);
        regionOutput.output(provRegionDTOs);

        for (RegionDTO provRegionDTO : provRegionDTOs) {
            loadProv(provRegionDTO.getCode());
        }
    }

    public void loadProv(String provCode) throws GetDistrictsException {
        if (regionOutput == null || !regionOutput.isAvailable()) {
            LOG.warn("Have no <regionOutput> or not available, do not execute.");
            return;
        }

        // Get cities
        List<RegionDTO> cityRegionDTOs = getSubRegionDTOs(provCode);
        regionOutput.output(cityRegionDTOs);

        for (RegionDTO cityRegionDTO : cityRegionDTOs) {
            loadCity(cityRegionDTO.getCode());
        }
    }

    public void loadCity(String cityCode) throws GetDistrictsException {
        if (regionOutput == null || !regionOutput.isAvailable()) {
            LOG.warn("Have no <regionOutput> or not available, do not execute.");
            return;
        }

        // Get districts

        List<RegionDTO> districtRegionDTOs = getSubRegionDTOs(cityCode);
        regionOutput.output(districtRegionDTOs);
    }

    public RegionOutput getRegionOutput() {
        return regionOutput;
    }

}
