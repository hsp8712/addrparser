package tech.spiro.addrparser.io;

import tech.spiro.addrparser.common.RegionDTO;
import tech.spiro.addrparser.common.RegionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Region Data Output Report
 * @author Spiro Huang
 * @since 1.0
 */
public class RegionDataReport {

    private int totalCount = 0;

    private Map<RegionLevel, Integer> levelCountMap = new HashMap<>();
    public void record(RegionDTO regionDTO) {

        if (regionDTO == null) {
            return;
        }

        totalCount++;

        // Update counter
        RegionLevel regionLevel = regionDTO.getLevel();
        Integer count = this.levelCountMap.get(regionLevel);
        if (count == null) {
            this.levelCountMap.put(regionLevel, 1);
        } else {
            this.levelCountMap.put(regionLevel, ++count);
        }
    }

    public String report() {
        StringBuilder sb = new StringBuilder("Region Data Report => ");
        sb.append("\n");
        sb.append("totalCount: " + this.totalCount);
        sb.append("\n");
        for (Map.Entry<RegionLevel, Integer> entry : levelCountMap.entrySet()) {
            sb.append(entry.getKey().toString() + ": " + entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

}
