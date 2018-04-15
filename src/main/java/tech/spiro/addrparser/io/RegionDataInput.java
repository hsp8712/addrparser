package tech.spiro.addrparser.io;

import tech.spiro.addrparser.common.RegionDTO;

import java.io.Closeable;
import java.io.IOException;

/**
 * Input region data from external storage, just like local file or rdbms.
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/12/2018
 */
public interface RegionDataInput extends Closeable {
    void init() throws IOException;

    /**
     * Read region data
     * @return if null, implicit to the end.
     * @throws IOException
     */
    RegionDTO read() throws IOException;
}
