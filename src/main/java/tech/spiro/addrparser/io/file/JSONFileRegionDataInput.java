package tech.spiro.addrparser.io.file;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.spiro.addrparser.common.RegionDTO;
import tech.spiro.addrparser.io.RegionDataInput;
import tech.spiro.addrparser.io.RegionDataReport;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/15/2018
 */
public class JSONFileRegionDataInput implements RegionDataInput {

    private static final Logger LOG = LoggerFactory.getLogger(JSONFileRegionDataInput.class);

    private Reader _reader;
    private String filename;
    private JSONReader reader;
    private RegionDataReport report = new RegionDataReport();
    private boolean initialized = false;

    public JSONFileRegionDataInput(String filename) {
        this.filename = filename;
        if (this.filename == null) {
            throw new IllegalArgumentException("<filename> cannot be null.");
        }
    }

    public JSONFileRegionDataInput(Reader _reader) {
        this._reader = _reader;
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
                this.reader = new JSONReader(this._reader);
            } else {
                this.reader = new JSONReader(new FileReader(this.filename));
            }
            this.reader.startArray();
            this.initialized = true;
        }
    }

    @Override
    public RegionDTO read() throws IOException {

        if (!initialized) {
            throw new IllegalStateException("Have not initialized already.");
        }

        if (reader.hasNext()) {
            RegionDTO regionDTO = reader.readObject(RegionDTO.class);
            this.report.record(regionDTO);
            return regionDTO;
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        if (!initialized) {
            throw new IllegalStateException("Have not initialized already.");
        }
        LOG.info(this.report.report());
        try {
            reader.endArray();
        } finally {
            reader.close();
        }
    }
}
