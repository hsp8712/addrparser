package tech.spiro.addrparser.crawler;

import java.util.List;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 7/31/2017
 */
public class DataResponse {

    private String status;
    private String info;
    private String infocode;
    private String count;
    private List<District> districts;

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

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    @Override
    public String toString() {
        return "DataResponse{" +
                "status='" + status + '\'' +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", count='" + count + '\'' +
                ", districts=" + districts +
                '}';
    }

    public boolean isSuccess() {
        return "1".equals(this.getStatus());
    }
}
