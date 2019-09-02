package tech.spiro.addrparser.tool;

import org.apache.commons.cli.*;
import tech.spiro.addrparser.crawler.GetRegionException;
import tech.spiro.addrparser.crawler.RegionDataCrawler;
import tech.spiro.addrparser.io.RegionDataOutput;
import tech.spiro.addrparser.io.file.JSONFileRegionDataOutput;

import java.io.IOException;
import java.util.Arrays;

/**
 * A command-line tool to crawl region data.
 * @author Spiro Huang
 * @since 1.0
 */
public class CrawlerServer {

    private static Options options = new Options();
    static {
        options.addRequiredOption("k", "key", true, "Amap enterprise dev key");
        options.addRequiredOption("l", "level", true, "Root region level: 0-country, 1-province, 2-city");
        options.addRequiredOption("c", "code", true, "Root region code");
        options.addRequiredOption("o", "out", true, "Output file.");
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CrawlerServer", options );
    }

    public static void main(String[] args) throws IOException, GetRegionException {

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String key  = cmd.getOptionValue("k");
            String level = cmd.getOptionValue('l');
            String code = cmd.getOptionValue('c');

            if (!Arrays.asList("0", "1", "2").contains(level)) {
                throw new ParseException("option:level invalid.");
            }

            String out = cmd.getOptionValue('o');
            int _code = 0;
            try {
                _code = Integer.valueOf(code);
            } catch (NumberFormatException e) {
                throw new ParseException("code must be numeric.");
            }

            execute(key, level, _code, out);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp();
            System.exit(-1);
        }
    }

    private static void execute(String amapKey, String level, int code, String out) throws IOException, GetRegionException {
        try (RegionDataOutput regionOutput = new JSONFileRegionDataOutput(out)) {
            RegionDataCrawler infoLoader = new RegionDataCrawler(regionOutput, amapKey);

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
