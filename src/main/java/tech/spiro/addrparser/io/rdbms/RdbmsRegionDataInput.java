package tech.spiro.addrparser.io.rdbms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.spiro.addrparser.common.RegionDTO;
import tech.spiro.addrparser.io.RegionDataInput;
import tech.spiro.addrparser.io.RegionDataReport;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/15/2018
 */
public class RdbmsRegionDataInput implements RegionDataInput {

    private static final Logger LOG = LoggerFactory.getLogger(RdbmsRegionDataInput.class);

    private static final String SELECT_SQL
            = "SELECT code, parent_code, name, level, center, polyline FROM region_data";

    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;
    private RegionDataReport report = new RegionDataReport();
    private boolean initialized = false;

    public RdbmsRegionDataInput(DataSource ds) {
        this.ds = ds;
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
            } catch (SQLException e) {
                throw new IOException(e.getMessage(), e);
            }

            try {
                this.stmt = this.conn.prepareStatement(SELECT_SQL);
                this.rs = this.stmt.executeQuery();
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
    public RegionDTO read() throws IOException {

        if (!initialized) {
            throw new IllegalStateException("Have not initialized already.");
        }

        try {
            if (this.rs.next()) {
                RegionDTO regionDTO = new RegionDTO();
                RdbmsRegionDTOWrapper regionDTOWrapper
                        = new RdbmsRegionDTOWrapper(regionDTO);
                regionDTOWrapper.read(this.rs);
                report.record(regionDTO);
                return regionDTO;
            }
        } catch (SQLException e) {
            throw new IOException(e.getMessage(), e);
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
            this.rs.close();
        } catch (SQLException e) {
        }

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
