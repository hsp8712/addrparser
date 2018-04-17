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
 * Input region data from rdbms.
 * @author Spiro Huang
 * @since 1.0
 */
public class RdbmsRegionDataInput implements RegionDataInput {

    private static final Logger LOG = LoggerFactory.getLogger(RdbmsRegionDataInput.class);

    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;
    private RegionDataReport report = new RegionDataReport();
    private boolean initialized = false;
    private final String sql;

    public RdbmsRegionDataInput(DataSource ds, String tableName) {
        if (ds == null) {
            throw new IllegalArgumentException("DataSource:<ds> is null.");
        }
        if (tableName == null) {
            throw new IllegalArgumentException("<tableName> is null.");
        }
        this.ds = ds;
        this.sql = RdbmsSQL.selectSQL(tableName);
    }

    public RdbmsRegionDataInput(DataSource ds) {
        if (ds == null) {
            throw new IllegalArgumentException("DataSource:<ds> is null.");
        }
        this.ds = ds;
        this.sql = RdbmsSQL.defaultSelectSQL();
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

            LOG.debug("Initializing...sql: {}", this.sql);

            try {
                this.conn = this.ds.getConnection();
            } catch (SQLException e) {
                throw new IOException(e.getMessage(), e);
            }

            LOG.debug("Initializing: Get connection completely.");

            try {
                this.stmt = this.conn.prepareStatement(this.sql);
                this.rs = this.stmt.executeQuery();
            } catch (SQLException e) {
                try {
                    this.conn.close();
                } catch (SQLException e1) {
                }
                throw new IOException(e.getMessage(), e);
            }

            LOG.debug("Initializing: Get preparedStatement/resultSet completely.");
            this.initialized = true;
            LOG.debug("Initialized.");
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
