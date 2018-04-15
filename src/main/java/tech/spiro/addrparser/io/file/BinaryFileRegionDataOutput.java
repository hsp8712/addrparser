package tech.spiro.addrparser.io.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.spiro.addrparser.common.RegionDTO;
import tech.spiro.addrparser.io.RegionDataOutput;
import tech.spiro.addrparser.io.RegionDataReport;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Region data output to file
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/14/2018
 */
public abstract class BinaryFileRegionDataOutput implements RegionDataOutput {

    private static final Logger LOG = LoggerFactory.getLogger(BinaryFileRegionDataOutput.class);

    protected OutputStream out;
    protected String filename;
    protected long writeBytes = 0;
    private boolean initialized = false;

    private RegionDataReport report = new RegionDataReport();

    public BinaryFileRegionDataOutput(String filename) {
        this.filename = filename;
    }

    @Override
    public void init() throws IOException {
        if (initialized) {
            return;
        }
        synchronized (this) {
            if (initialized) {
                return;
            }
            this.out = new BufferedOutputStream(new FileOutputStream(this.filename));
            this.initialized = true;
        }
    }

    @Override
    public void write(RegionDTO regionDTO) throws IOException {
        if (!initialized) {
            throw new IllegalStateException("Have not initialized already.");
        }
        if (regionDTO == null) {
            throw new IllegalArgumentException("<regionDTO> cannot be null.");
        }
        // write object bytes
        byte[] serializedBytes = serialize(regionDTO);
        this.out.write(serializedBytes);
        writeBytes += serializedBytes.length;

        report.record(regionDTO);
    }

    @Override
    public void close() throws IOException {
        if (!initialized) {
            throw new IllegalStateException("Have not initialized already.");
        }
        LOG.info("------------------ BinaryFile Report ----------------");
        LOG.info("Write byte size: {}.", writeBytes);
        LOG.info(report.report());
        LOG.info("-----------------------------------------------------");
        this.out.close();
    }


    protected abstract byte[] serialize(RegionDTO regionDTO);
}
