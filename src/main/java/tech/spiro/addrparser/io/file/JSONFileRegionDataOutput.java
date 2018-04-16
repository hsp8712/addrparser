package tech.spiro.addrparser.io.file;

import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.spiro.addrparser.common.RegionDTO;
import tech.spiro.addrparser.io.RegionDataOutput;
import tech.spiro.addrparser.io.RegionDataReport;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Output region data to json file.
 * @author Spiro Huang
 * @since 1.0
 */
public class JSONFileRegionDataOutput implements RegionDataOutput {

    private static final Logger LOG = LoggerFactory.getLogger(JSONFileRegionDataOutput.class);

    private Writer _writer;
    private String filename;
    private JSONWriter writer = null;
    private RegionDataReport report = new RegionDataReport();

    private boolean initialized = false;

    public JSONFileRegionDataOutput(String filename) {
        this.filename = filename;
        if (this.filename == null) {
            throw new IllegalArgumentException("<filename> cannot be null.");
        }
    }

    public JSONFileRegionDataOutput(Writer _writer) {
        this._writer = _writer;
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

            if (this.filename == null) {
                this.writer = new JSONWriter(_writer);
            } else {
                this.writer = new JSONWriter(new FileWriter(filename));
            }

            this.writer.config(SerializerFeature.WriteEnumUsingName, false);
            this.writer.config(SerializerFeature.SortField, false);
            this.writer.startArray();

            this.initialized = true;
        }
    }

    @Override
    public void write(RegionDTO regionDTO) throws IOException {

        if (!initialized) {
            throw new IllegalStateException("Have not initialized already.");
        }

        this.writer.writeValue(regionDTO);
        report.record(regionDTO);
    }

    @Override
    public void close() throws IOException {

        if (!initialized) {
            throw new IllegalStateException("Have not initialized already.");
        }

        LOG.info(this.report.report());
        try {
            this.writer.endArray();
        } finally {
            this.writer.close();
        }


    }
}
