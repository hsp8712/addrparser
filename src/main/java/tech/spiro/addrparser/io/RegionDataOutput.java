package tech.spiro.addrparser.io;

import tech.spiro.addrparser.common.RegionDTO;

import java.io.Closeable;
import java.io.IOException;

/**
 * Output region data to external storage, just like local file or rdbms.
 * @author Spiro Huang
 * @since 1.0
 */
public interface RegionDataOutput extends Closeable {
    void init() throws IOException;

    /**
     * Write region data
     * @param regionDTO region data to write.
     * @throws IOException  if an I/O error occurs.
     */
    void write(RegionDTO regionDTO) throws IOException;
}
