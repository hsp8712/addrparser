package tech.spiro.addrparser.io.rdbms;

import org.junit.Test;

import static org.junit.Assert.*;

public class RdbmsSQLTest {

    @Test
    public void selectSQL() {
        assertEquals("SELECT code, parent_code, name, level, center, polyline FROM test",
                RdbmsSQL.selectSQL("test"));
    }

    @Test
    public void insertSQL() {
        assertEquals("INSERT INTO test(code, parent_code, name, level, center, polyline) VALUES (?,?,?,?,?,?)",
                RdbmsSQL.insertSQL("test"));
    }

    @Test
    public void defaultSelectSQL() {
        assertEquals("SELECT code, parent_code, name, level, center, polyline FROM region_data",
                RdbmsSQL.defaultSelectSQL());
    }

    @Test
    public void defaultInsertSQL() {
        assertEquals("INSERT INTO region_data(code, parent_code, name, level, center, polyline) VALUES (?,?,?,?,?,?)",
                RdbmsSQL.defaultInsertSQL());
    }
}