package tech.spiro.addrparser.io.rdbms;

public class RdbmsSQL {

    public static final String DEFAULT_TABLE_NAME = "region_data";
    private static final String SELECT_SQL_FORMAT
            = "SELECT code, parent_code, name, level, center, polyline FROM %s";
    private static final String INSERT_SQL_FORMAT
            = "INSERT INTO %s(code, parent_code, name, level, center, polyline) VALUES (?,?,?,?,?,?)";

    public static String selectSQL(String tableName) {
        return String.format(SELECT_SQL_FORMAT, tableName);
    }

    public static String insertSQL(String tableName) {
        return String.format(INSERT_SQL_FORMAT, tableName);
    }

    public static String defaultSelectSQL() {
        return selectSQL(DEFAULT_TABLE_NAME);
    }

    public static String defaultInsertSQL() {
        return insertSQL(DEFAULT_TABLE_NAME);
    }
}
