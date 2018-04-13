package tech.spiro.addrparser.crawler;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/3/2018
 */
public class CrawlerServer {

    private static Options options = new Options();
    static {
        options.addRequiredOption("l", "level", true, "Root region level: 0-country, 1-province, 2-city");
        options.addRequiredOption("c", "code", true, "Root region code");
        options.addRequiredOption("o", "out", true, "Output file.");
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CrawlerServer", options );
    }

    public static void main(String[] args) throws IOException, GetDistrictsException {

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String level = cmd.getOptionValue('l');
            String code = cmd.getOptionValue('c');

            if (!Arrays.asList("0", "1", "2").contains(level)) {
                throw new ParseException("option:level invalid.");
            }

            String out = cmd.getOptionValue('o');

            execute(level, code, out);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp();
            System.exit(-1);
        }
    }

    private static void execute(String level, String code, String out) throws IOException, GetDistrictsException {
        try (RegionOutput regionOutput = new JsonFileRegionOutput(out)) {
            LocationInfoCrawler infoLoader = new LocationInfoCrawler(regionOutput);

            if ("0".equals(level)) {
                infoLoader.loadCountry();
            } else if ("1".equals(level)) {
                infoLoader.loadProv(code);
            } else if ("2".equals(level)) {
                infoLoader.loadCity(code);
            }
        }
    }
}
