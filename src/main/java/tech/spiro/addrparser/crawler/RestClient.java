package tech.spiro.addrparser.crawler;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 7/30/2017
 */
public class RestClient {

    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

    private String key;

    private String keywords;
    private String subdistrict;
    private String extensions;

    private CloseableHttpClient httpClient;


    public RestClient() {
        this.httpClient = HttpClients.createDefault();
    }

    public DataResponse getDistrictResponse() throws GetDistrictsException {
        URI uri = null;
        try {
             uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("restapi.amap.com")
                    .setPath("/v3/config/district")
                    .setParameter("key", this.getKey())
                    .setParameter("keywords", this.getKeywords())
                    .setParameter("subdistrict", this.getSubdistrict())
                    .setParameter("extensions", this.getExtensions())
                    .build();
        } catch (URISyntaxException e) {
            throw new GetDistrictsException("uri syntax error.");
        }

        HttpGet httpGet = new HttpGet(uri);

        CloseableHttpResponse response = null;
        try {
            response = this.httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new GetDistrictsException("Execute get request failed, http-code="
                        + response.getStatusLine().getStatusCode());
            }

            String responseContent = EntityUtils.toString(response.getEntity());
            return JSON.parseObject(responseContent, DataResponse.class);
        } catch (IOException e) {
            throw new GetDistrictsException("Execute get request exception.");
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

}
