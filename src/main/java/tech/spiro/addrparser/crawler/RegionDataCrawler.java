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
 * Region data crawler, use by invoke methods:
 * <ul>
 *     <li>{@code loadCountry()}</li>
 *     <li>{@code loadProv(int provCode)}</li>
 *     <li>{@code loadCity(int cityCode)}</li>
 * </ul>
 * @author Spiro Huang
 * @since 1.0
 */
public class RegionDataCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(RegionDataCrawler.class);

    // country root code
    public static final int COUNTRY_CODE = 100000;

    private AtomicInteger invokerCount = new AtomicInteger(0);

    private final RegionDataOutput regionOutput;

    /** AMAP api key */
    private final String amapKey;

    public RegionDataCrawler(RegionDataOutput regionOutput, String amapKey) throws IOException {
        this.regionOutput = regionOutput;
        this.regionOutput.init();
        this.amapKey = amapKey;
    }

    private List<RegionDTO> getSubRegionDTOs(int code) throws GetRegionException {

        RestClient restClient = new RestClient();
        restClient.setKeywords(Integer.toString(code));
        restClient.setKey(this.amapKey);
        restClient.setExtensions("base");
        restClient.setSubdistrict("1");

        DataResp dataResponse = restClient.getDistrictResponse();
        LOG.debug("Invoker.count=" + invokerCount.incrementAndGet());

        if (!dataResponse.isSuccess()) {
            throw new GetRegionException("Get failed, infocode="
                    + dataResponse.getInfocode() + ", info=" + dataResponse.getInfo());
        }

        List<RegionResp> regionResps = dataResponse.getDistricts();
        if (regionResps.isEmpty()) {
            LOG.warn("Have no regionResps return, keyword code={}", code);
            return null;
        }

        RegionResp curRegionResp = regionResps.get(0);
        List<RegionResp> subRegionResps = curRegionResp.getDistricts();

        List<RegionDTO> subRegionDTOs = new ArrayList<>();

        if (subRegionResps == null || subRegionResps.isEmpty()) {
            LOG.warn("Have no sub regionResps, {}", curRegionResp);
        } else {
            for (RegionResp regionResp : subRegionResps) {
                RegionDTO regionDTO = new RegionDTO();

                try {
                    regionDTO.setCode(Integer.valueOf(regionResp.getAdcode()));
                    regionDTO.setLevel(RegionLevel.valueOf(regionResp.getLevel().toUpperCase()));
                    regionDTO.setCenter(regionResp.getCenter());
                    regionDTO.setParentCode(code);
                    regionDTO.setName(regionResp.getName());
                } catch (Exception e) {
                    throw new GetRegionException("<regionResp> cannot convert to RegionDTO, maybe regionResp invalid, regionResp:"
                            + regionResp.toString(), e);
                }
                subRegionDTOs.add(regionDTO);
            }
        }

        // Fill boundary info
        for (RegionDTO subRegionDTO : subRegionDTOs) {
            RestClient subRestClient = new RestClient();
            subRestClient.setExtensions("all");
            subRestClient.setKey(this.amapKey);
            subRestClient.setKeywords(Integer.toString(subRegionDTO.getCode()));
            subRestClient.setSubdistrict("0");

            DataResp subDataResp = subRestClient.getDistrictResponse();
            LOG.debug("Invoker.count=" + invokerCount.incrementAndGet());

            if (!subDataResp.isSuccess()) {
                throw new GetRegionException("Get sub district failed");
            }

            List<RegionResp> _subRegionResps = subDataResp.getDistricts();
            if (_subRegionResps.isEmpty()) {
                throw new GetRegionException("Get sub district failed");
            }

            RegionResp _subRegionResp = _subRegionResps.get(0);
            subRegionDTO.setPolyline(_subRegionResp.getPolyline());
        }

        return subRegionDTOs;
    }

    private void regionBatchOutput(List<RegionDTO> regionDTOs) throws GetRegionException {
        try {
            for (RegionDTO regionDTO : regionDTOs) {
                this.regionOutput.write(regionDTO);
            }
        } catch (IOException e) {
            throw new GetRegionException(e.getMessage(), e);
        }
    }

    public void loadCountry() throws GetRegionException {
        if (regionOutput == null) {
            throw new GetRegionException("<regionOutput> is null.");
        }
        // Get provinces
        List<RegionDTO> provRegionDTOs = getSubRegionDTOs(COUNTRY_CODE);
        regionBatchOutput(provRegionDTOs);

        for (RegionDTO provRegionDTO : provRegionDTOs) {
            loadProv(provRegionDTO.getCode());
        }
    }

    public void loadProv(int provCode) throws GetRegionException {
        if (regionOutput == null) {
            throw new GetRegionException("<regionOutput> is null.");
        }

        // Get cities
        List<RegionDTO> cityRegionDTOs = getSubRegionDTOs(provCode);
        regionBatchOutput(cityRegionDTOs);

        for (RegionDTO cityRegionDTO : cityRegionDTOs) {
            loadCity(cityRegionDTO.getCode());
        }
    }

    public void loadCity(int cityCode) throws GetRegionException {
        if (regionOutput == null) {
            throw new GetRegionException("<regionOutput> is null.");
        }

        // Get districts
        List<RegionDTO> districtRegionDTOs = getSubRegionDTOs(cityCode);
        regionBatchOutput(districtRegionDTOs);
    }

}
