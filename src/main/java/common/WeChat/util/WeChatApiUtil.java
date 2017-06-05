package common.WeChat.util;

import common.HttpClient.util.HttpClientUtil;
import common.ServerAdvice.util.LogUtil;
import project.resource.properties.WeChatProperties;

import java.util.Date;
import java.util.Map;

/**
 * Created by Errol on 17/5/30.
 */
public class WeChatApiUtil {

    private static String access_token = "";
    private static long expire = -1;

    public static String getAccessToken() {
        LogUtil.debug("需要access_token");
        if (new Date().getTime() > expire) {
            refreshAccessToken();
        }
        LogUtil.debug("返回access_token:  "+ access_token);
        return access_token;
    }

    private static void refreshAccessToken() {
        LogUtil.debug("发送请求获取access_token");
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                WeChatProperties.getInstance().getAppId(), WeChatProperties.getInstance().getAppSecret());
        Map<String, String> map = HttpClientUtil.get(url);
        if (map != null) {
            String token = map.get("access_token");
            LogUtil.debug("map不为null，access_token为:  "+token);
            if (token != null) {
                access_token = token;
                String expires_in = String.valueOf(map.get("expires_in"));
                expire = new Date().getTime() + (Long.parseLong(expires_in) - Long.parseLong("10")) * 1000;
                LogUtil.debug(" new Date().getTime():  "+ new Date().getTime());
                LogUtil.debug("expire :  "+ expire);
            }
        }
    }

}
