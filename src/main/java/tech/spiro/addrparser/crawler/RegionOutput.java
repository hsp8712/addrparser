package tech.spiro.addrparser.crawler;

import tech.spiro.addrparser.common.RegionDTO;

import java.io.Closeable;
import java.util.List;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/3/2018
 */
public interface RegionOutput extends Closeable {
    void output(RegionDTO regionDTO);
    void output(List<RegionDTO> regionDTOS);
    boolean isAvailable();
    void close();
}
