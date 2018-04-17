package tech.spiro.addrparser.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.spiro.addrparser.io.rdbms.RdbmsSQL;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLTableInit {
    private static final Logger LOG = LoggerFactory.getLogger(MySQLTableInit.class);

    private static final String CREATE_SQL_FORMAT = "CREATE TABLE `%s` (" +
            "  `code` int(11) NOT NULL," +
            "  `parent_code` int(11) NOT NULL," +
            "  `name` varchar(100) NOT NULL," +
            "  `level` tinyint(4) NOT NULL," +
            "  `center` varchar(30) DEFAULT NULL," +
            "  `polyline` mediumtext NOT NULL" +
            ")";

    public static void init(DataSource ds, String tableName) throws SQLException {
        String createSql = null;
        if (tableName == null) {
            createSql = String.format(CREATE_SQL_FORMAT, RdbmsSQL.DEFAULT_TABLE_NAME);
        } else {
            createSql = String.format(CREATE_SQL_FORMAT, tableName);
        }

        LOG.info("Initialize mysql table, DDL: {}", createSql);

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createSql);
        }

        LOG.info("Initialize mysql table successfully.");
    }
}
