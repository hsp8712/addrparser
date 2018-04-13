package tech.spiro.addrparser.crawler;

import tech.spiro.addrparser.common.RegionDTO;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/3/2018
 */
public class JsonFileRegionOutput implements RegionOutput {

    private PrintWriter out;
    private String filename;
    private Map<String, Integer> levelCountMap = new HashMap<>();

    /**
     * Filename
     * @param filename
     */
    public JsonFileRegionOutput(String filename) throws IOException {
        this.filename = filename;
        this.out = new PrintWriter(this.filename, "UTF-8");
    }

    @Override
    public void output(RegionDTO regionDTO) {
        String level = regionDTO.getLevel();
        Integer levelCount = this.levelCountMap.get(level);
        if (levelCount == null) {
            this.levelCountMap.put(level, 0);
        } else {
            this.levelCountMap.put(level, ++levelCount);
        }
        String regionJSON = JSON.toJSONString(regionDTO);
        out.println(regionJSON);
    }

    @Override
    public void output(List<RegionDTO> regionDTOS) {
        for (RegionDTO regionDTO : regionDTOS) {
            output(regionDTO);
        }
        out.flush();
    }

    @Override
    public boolean isAvailable() {
        return this.filename != null;
    }

    @Override
    public void close() {

        System.out.println("------------- Report -----------");
        for (Map.Entry<String, Integer> entry : levelCountMap.entrySet()) {
            System.out.println("Level-" + entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("--------------------------------");

        this.out.close();
    }
}
