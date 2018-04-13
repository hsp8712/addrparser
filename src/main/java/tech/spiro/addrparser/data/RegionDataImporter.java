package tech.spiro.addrparser.data;

import tech.spiro.addrparser.common.RegionDTO;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/12/2018
 */
public interface RegionDataImporter {
    void init();
    void imp(RegionDTO regionDTO);
    void destroy();
}
