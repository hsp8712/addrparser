package tech.spiro.addrparser.tool;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.cli.*;
import tech.spiro.addrparser.crawler.GetRegionException;
import tech.spiro.addrparser.io.IOPipeline;
import tech.spiro.addrparser.io.RegionDataInput;
import tech.spiro.addrparser.io.RegionDataOutput;
import tech.spiro.addrparser.io.file.JSONFileRegionDataInput;
import tech.spiro.addrparser.io.rdbms.RdbmsRegionDataOutput;
import tech.spiro.addrparser.io.rdbms.RdbmsSQL;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A command-line tool to stream region data from json file to mysql.
 * @author Spiro Huang
 * @since 1.0
 */
public class JSONFile2MySQL {
    private static Options options = new Options();
    static {
        options.addOption("f", "file", true, "Json region data file path");

        options.addRequiredOption("h", "host", true, "MySQL host");
        options.addOption("p", "port", true, "MySQL port, default 3306");
        options.addRequiredOption("d", "db", true, "MySQL database");
        options.addRequiredOption("u", "user", true, "MySQL user");
        options.addRequiredOption("a", "password", true, "MySQL password");

        options.addOption("t", "table-name", true, "Default: '" + RdbmsSQL.DEFAULT_TABLE_NAME + "', Region data table name");
        options.addOption("i", "init", false, "Init table schema");
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("JSONFile2MySQL", options );
    }

    public static void main(String[] args) throws IOException, GetRegionException {

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            DataSource dataSource = buildDataSource(cmd);

            String tableName = cmd.getOptionValue('t');
            if (cmd.hasOption('i')) {
                try {
                    MySQLTableInit.init(dataSource, tableName);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }

            String file = cmd.getOptionValue('f');
            if (file != null) {
                RegionDataInput dataInput = new JSONFileRegionDataInput(file);
                RegionDataOutput dataOutput = null;
                if (tableName == null) {
                    dataOutput = new RdbmsRegionDataOutput(dataSource);
                } else {
                    dataOutput = new RdbmsRegionDataOutput(dataSource, tableName);
                }

                IOPipeline pipeline = new IOPipeline(dataInput, dataOutput);
                pipeline.start();
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp();
            System.exit(-1);
        }
    }

    public static DataSource buildDataSource(CommandLine cmd) throws ParseException {
        String mysqlHost = cmd.getOptionValue('h');
        int mysqlPort = 3306;

        if (cmd.hasOption('p')) {
            try {
                mysqlPort = Integer.valueOf(cmd.getOptionValue('p'));
            } catch (NumberFormatException e) {
                throw new ParseException(e.getMessage());
            }
        }
        String mysqlDB = cmd.getOptionValue('d');
        String mysqlUser = cmd.getOptionValue('u');
        String mysqlPassword = cmd.getOptionValue('a');

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(mysqlHost);
        dataSource.setPort(mysqlPort);
        dataSource.setDatabaseName(mysqlDB);
        dataSource.setUser(mysqlUser);
        dataSource.setPassword(mysqlPassword);

        try {
            Connection connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new ParseException("DataSource get connection failed:" + e.getMessage());
        }
        return dataSource;
    }
}
