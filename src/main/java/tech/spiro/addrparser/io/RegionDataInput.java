package tech.spiro.addrparser.io;

import tech.spiro.addrparser.common.RegionDTO;

import java.io.Closeable;
import java.io.IOException;

/**
 * Input region data from external storage, just like local file or rdbms.
 * @author Spiro Huang
 * @since 1.0
 */
public interface RegionDataInput extends Closeable {
    void init() throws IOException;

    /**
     * Read region data
     * @return if null, implicit to the end.
     * @throws IOException  if an I/O error occurs.
     */
    RegionDTO read() throws IOException;
}
