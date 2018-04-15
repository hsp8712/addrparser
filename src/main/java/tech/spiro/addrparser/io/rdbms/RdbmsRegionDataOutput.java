package tech.spiro.addrparser.io.rdbms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.spiro.addrparser.common.RegionDTO;
import tech.spiro.addrparser.common.RegionLevel;
import tech.spiro.addrparser.io.RegionDataOutput;
import tech.spiro.addrparser.io.RegionDataReport;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Region data output to rdbms, write method argument must be {@link RdbmsRegionDTOWrapper}.
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/14/2018
 */
public class RdbmsRegionDataOutput implements RegionDataOutput {

    private static final Logger LOG = LoggerFactory.getLogger(RdbmsRegionDataOutput.class);

    private static final String INSERT_SQL = "INSERT INTO region_data(code, parent_code, name, level, center, polyline) VALUES (?,?,?,?,?,?)";
    private static final int BATCH_SIZE = 100;

    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private int batchCount = 0;
    private RegionDataReport report = new RegionDataReport();
    private boolean initialized = false;

    public RdbmsRegionDataOutput(DataSource source) {
        this.ds = source;
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
            try {
                this.conn = this.ds.getConnection();
                this.conn.setAutoCommit(false);
            } catch (SQLException e) {
                throw new IOException(e.getMessage(), e);
            }

            try {
                this.stmt = this.conn.prepareStatement(INSERT_SQL);
            } catch (SQLException e) {
                throw new IOException(e.getMessage(), e);
            } finally {
                try {
                    this.conn.close();
                } catch (SQLException e) {
                }
            }

            this.initialized = true;
        }


    }

    @Override
    public void write(RegionDTO regionDTO) throws IOException {

        if (!initialized) {
            throw new IllegalStateException("Have not initialized already.");
        }

        if (regionDTO == null) {
            return;
        }
        if (!(regionDTO instanceof RdbmsRegionDTOWrapper)) {
            throw new IllegalArgumentException(
                    "<regionDTO> is not instance of <tech.spiro.addrparser.io.rdbms.RdbmsRegionDTOWrapper>");
        }

        RdbmsRegionDTOWrapper wrapper = (RdbmsRegionDTOWrapper) regionDTO;
        this.report.record(wrapper);
        try {
            wrapper.write(this.stmt);
            this.stmt.addBatch();
            batchCount++;

            if (batchCount >= BATCH_SIZE) {
                flushBatch();
            }
        } catch (SQLException e) {
            throw new IOException(e.getMessage(), e);
        }

    }

    private void flushBatch() throws SQLException {

        if (batchCount == 0) {
            return;
        }

        this.stmt.executeBatch();
        this.conn.commit();
        batchCount = 0;
    }

    @Override
    public void close() throws IOException {

        if (!initialized) {
            throw new IllegalStateException("Have not initialized already.");
        }

        LOG.info(this.report.report());
        try {
            flushBatch();
        } catch (SQLException e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            try {
                this.stmt.close();
            } catch (SQLException e) {
            }

            try {
                this.conn.close();
            } catch (SQLException e) {
            }
        }
    }
}
