[![Build Status](https://api.travis-ci.org/hsp8712/addrparser.svg?branch=master)](https://travis-ci.org/hsp8712/addrparser)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/tech.spiro/addrparser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tech.spiro/addrparser/)
[![GitHub release](https://img.shields.io/github/release/hsp8712/addrparser.svg)](https://github.com/hsp8712/addrparser/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

# addrparser
离线高效的解析中国范围内的经纬度为省市区信息，省市区信息包括: 行政区划编码、行政区划中文名称、行政区域的中心点经纬度，行政区域的边界点经纬度集合。

# 依赖安装
***依赖 jdk1.8***
## Maven
```xml
<dependency>
    <groupId>tech.spiro</groupId>
    <artifactId>addrparser</artifactId>
    <version>1.0.1</version>
</dependency>
```
## Jar文件
[addrparser-1.0.1](http://repo1.maven.org/maven2/tech/spiro/addrparser/1.0.1/addrparser-1.0.1.jar)

# 使用说明
## 下载数据文件
[china-region-20180418.zip](https://github.com/hsp8712/addrparser/releases/download/addrparser-1.0.1/china-region-20180418.zip)
解压后得到文件：china-region.json，文件中包括了所有的省市区行政区域信息，包括编码、名称、中心点、边界点集合。

**经纬度数据基于GCJ-02坐标系**

## 方式一 （文件）
直接使用china-region.json文件作为基础数据。

示例代码：
```java

// china-region.json文件作为基础数据
RegionDataInput regionDataInput = new JSONFileRegionDataInput("path/china-region.json");

// 创建并初始化位置解析引擎，一般配置为全局单例
LocationParserEngine engine = new LocationParserEngine(regionDataInput);
// 初始化，加载数据，比较耗时
engine.init();

// 执行解析操作
Location location = engine.parse(118.750934,32.038634);

// 获取省市区信息
RegionInfo provInfo = location.getProv();
RegionInfo cityInfo = location.getCity();
RegionInfo districtInfo = location.getDistrict();
```

## 方式二 （MySQL）
将数据导入mysql数据库，使用mysql中数据作为基础数据。

下载数据导入工具：
[addrparser-1.0.tar.gz](https://github.com/hsp8712/addrparser/releases/download/addrparser-1.0/addrparser-1.0.tar.gz)

解压后，进入addrparser/bin目录，执行jsonfile2mysql.sh (Linux) 或 jsonfile2mysql.bat (Windows)

```
usage: JSONFile2MySQL
 -a,--password <arg>     MySQL password
 -d,--db <arg>           MySQL database
 -f,--file <arg>         Json region data file path  上述china-region.json文件的路径
 -h,--host <arg>         MySQL host
 -i,--init               Init table schema
 -p,--port <arg>         MySQL port, default 3306
 -t,--table-name <arg>   Default: 'region_data', Region data table name
 -u,--user <arg>         MySQL user
```

如果不指定-t，使用默认表名region_data

示例代码：
```java
// javax.sql.DataSource 数据源
DataSource ds = ...;

// 关系型数据库区域数据输入
RegionDataInput regionDataInput = new RdbmsRegionDataInput(ds);

// 如果上述命令如果指定了定制的表名，则该处也要指定表名。
// RegionDataInput regionDataInput = new RdbmsRegionDataInput(ds, customTableName);

// 创建并初始化位置解析引擎，一般配置为全局单例
LocationParserEngine engine = new LocationParserEngine(regionDataInput);
// 初始化，加载数据，比较耗时
engine.init();

// 执行解析操作
Location location = engine.parse(118.750934,32.038634);

// 获取省市区信息
RegionInfo provInfo = location.getProv();
RegionInfo cityInfo = location.getCity();
RegionInfo districtInfo = location.getDistrict();
```

