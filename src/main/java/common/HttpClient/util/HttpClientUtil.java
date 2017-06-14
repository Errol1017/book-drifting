package common.HttpClient.util;

import common.DataFormatter.DataManager;
import common.FileProcessor.FileType;
import common.ServerAdvice.util.LogUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import project.resource.properties.ServerProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Random;

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

    public static String fetch(String url, FileType fileType) {
        String filename = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            String path = "temp/" + System.currentTimeMillis() + "_" + String.format("%02d", new Random().nextInt(100)) + "." + fileType.toString();
            File file = new File(ServerProperties.getInstance().getFileBasePath() + path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int hasRead;
            while ((hasRead = inputStream.read(bytes)) > 0) {
                fileOutputStream.write(bytes, 0, hasRead);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            EntityUtils.consume(entity);
            response.close();
            filename = path;
        } catch (IOException e) {
            LogUtil.debug("HttpClientUtil中get方法异常");
            LogUtil.capture(e.getMessage(), e);
        }
        return filename;
    }

}
