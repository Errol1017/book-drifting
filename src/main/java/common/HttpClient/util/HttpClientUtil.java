package common.HttpClient.util;

import common.DataFormatter.DataManager;
import common.ServerAdvice.util.LogUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Errol on 17/5/30.
 */
public class HttpClientUtil {

    public static Map get(String url) {
        Map map = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            response.close();
            map = DataManager.string2Map(json);
        } catch (IOException e) {
            LogUtil.debug("HttpClientUtil中get方法异常");
            LogUtil.capture(e.getMessage(), e);
        }
        return map;
    }

}
