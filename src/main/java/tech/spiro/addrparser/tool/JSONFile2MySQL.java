package tech.spiro.addrparser.tool;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.cli.*;
import tech.spiro.addrparser.crawler.GetDistrictsException;
import tech.spiro.addrparser.io.IOPipeline;
import tech.spiro.addrparser.io.RegionDataInput;
import tech.spiro.addrparser.io.RegionDataOutput;
import tech.spiro.addrparser.io.file.JSONFileRegionDataInput;
import tech.spiro.addrparser.io.rdbms.RdbmsRegionDataOutput;

import java.io.IOException;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/15/2018
 */
public class JSONFile2MySQL {
    private static Options options = new Options();
    static {
        options.addRequiredOption("f", "file", true, "Json region data file path");
        options.addRequiredOption("h", "host", true, "MySQL host");
        options.addRequiredOption("p", "port", false, "MySQL port, default 3306");
        options.addRequiredOption("d", "db", true, "MySQL database");
        options.addRequiredOption("u", "user", true, "MySQL user");
        options.addRequiredOption("a", "password", true, "MySQL password");
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("JSONFile2MySQL", options );
    }

    public static void main(String[] args) throws IOException, GetDistrictsException {

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String file = cmd.getOptionValue('f');
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

            RegionDataInput dataInput = new JSONFileRegionDataInput(file);

            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(mysqlHost);
            dataSource.setPort(mysqlPort);
            dataSource.setDatabaseName(mysqlDB);
            dataSource.setUser(mysqlUser);
            dataSource.setPassword(mysqlPassword);
            RegionDataOutput dataOutput = new RdbmsRegionDataOutput(dataSource);

            IOPipeline pipeline = new IOPipeline(dataInput, dataOutput);
            pipeline.start();

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp();
            System.exit(-1);
        }
    }
}
