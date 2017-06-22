package common.WeChat.util;

import common.HttpClient.util.HttpClientUtil;
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
        if (new Date().getTime() > expire) {
            refreshAccessToken();
        }
        return access_token;
    }

    private static void refreshAccessToken() {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                WeChatProperties.getInstance().getAppId(), WeChatProperties.getInstance().getAppSecret());
        Map<String, String> map = HttpClientUtil.get(url);
        if (map != null) {
            String token = map.get("access_token");
            if (token != null) {
                access_token = token;
                String expires_in = String.valueOf(map.get("expires_in"));
                expire = new Date().getTime() + (Long.parseLong(expires_in) - Long.parseLong("10")) * 1000;
            }
        }
    }
}
