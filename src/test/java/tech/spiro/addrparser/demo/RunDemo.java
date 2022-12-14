package tech.spiro.addrparser.demo;

import com.mysql.cj.jdbc.MysqlDataSource;
import tech.spiro.addrparser.common.Point;
import tech.spiro.addrparser.io.RegionDataInput;
import tech.spiro.addrparser.io.rdbms.RdbmsRegionDataInput;
import tech.spiro.addrparser.parser.Location;
import tech.spiro.addrparser.parser.LocationParserEngine;
import tech.spiro.addrparser.parser.ParserEngineException;

import java.io.IOException;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2022/12/14 9:51
 */
public class RunDemo {

    public static void main(String[] args) throws ParserEngineException, IOException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("fat-mysql.i");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("express_i");
        dataSource.setUser("express");
        dataSource.setPassword("2c7d7847f82071");
        RegionDataInput regionDataInput = new RdbmsRegionDataInput(dataSource);
        LocationParserEngine engine = new LocationParserEngine(regionDataInput);
        engine.init();
        System.out.println("初始化完成");
        Location location = engine.parse(new Point(116.708463,23.37102));
        System.out.println(location);

    }

}
