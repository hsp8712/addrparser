package tech.spiro.addrparser.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.spiro.addrparser.common.RegionDTO;

import java.io.IOException;

/**
 * Connect input to output.
 * @author Spiro Huang
 * @since 1.0
 */
public class IOPipeline {

    private static final Logger LOG = LoggerFactory.getLogger(IOPipeline.class);

    private final RegionDataInput input;
    private final RegionDataOutput output;

    public IOPipeline(RegionDataInput input, RegionDataOutput output) {
        this.input = input;
        this.output = output;
    }

    public void start() throws IOException {
        input.init();
        output.init();

        try {
            while (true) {
                RegionDTO regionDTO = input.read();
                if (regionDTO == null) {
                    break;
                }
                output.write(regionDTO);
            }
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }

            try {
                output.close();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
