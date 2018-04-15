package tech.spiro.addrparser.io;

import tech.spiro.addrparser.common.RegionDTO;

import java.io.Closeable;
import java.io.IOException;

/**
 * Output region data to external storage, just like local file or rdbms.
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/12/2018
 */
public interface RegionDataOutput extends Closeable {
    void init() throws IOException;
    void write(RegionDTO regionDTO) throws IOException;
}
