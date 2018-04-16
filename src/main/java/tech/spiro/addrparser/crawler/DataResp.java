package tech.spiro.addrparser.crawler;

import java.util.List;

/**
 * Response data object for rest request.
 * @author Spiro Huang
 * @since 1.0
 */
public class DataResp {

    private String status;
    private String info;
    private String infocode;
    private String count;
    private List<RegionResp> regionResps;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<RegionResp> getRegionResps() {
        return regionResps;
    }

    public void setRegionResps(List<RegionResp> regionResps) {
        this.regionResps = regionResps;
    }

    @Override
    public String toString() {
        return "DataResp{" +
                "status='" + status + '\'' +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", count='" + count + '\'' +
                ", regionResps=" + regionResps +
                '}';
    }

    public boolean isSuccess() {
        return "1".equals(this.getStatus());
    }
}
