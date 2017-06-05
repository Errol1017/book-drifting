package common.WeChat.util;

import common.HttpClient.util.HttpClientUtil;
import common.ServerAdvice.util.LogUtil;
import common.Util.EncryptUtil;
import common.Util.RandomUtil;
import common.WeChat.model.WeChatJsSdkConfig;

import java.util.Date;
import java.util.Map;

/**
 * Created by Errol on 17/5/30.
 */
public class WeChatJsSdkUtil {

    private static WeChatJsSdkConfig config = new WeChatJsSdkConfig(true);
    private static long expire = -1;
    private static String jsapi_ticket = "";

    public static WeChatJsSdkConfig getWeChatJsSdkConfig(String url) {
        LogUtil.debug("需要js-sdk config");
        if (System.currentTimeMillis() > expire) {
            LogUtil.debug("jsapi_ticket已过期，刷新ticket");
            jsapi_ticket = getJsApiTicket();
            LogUtil.debug("新的jsapi_ticket： " + jsapi_ticket);
        }
        refreshWeChatJsSdkConfig(url);
        return config;
    }

    private static String getJsApiTicket() {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi",
                WeChatApiUtil.getAccessToken());
        Map<String, String> map = HttpClientUtil.get(url);
        if (map != null) {
            String ticket = map.get("ticket");
            if (ticket != null) {
                String expires_in = String.valueOf(map.get("expires_in"));
                expire = System.currentTimeMillis() + (Long.parseLong(expires_in) - Long.parseLong("10")) * 1000;
                return ticket;
            }
        }
        return null;
    }

    private static void refreshWeChatJsSdkConfig(String url) {
        if (jsapi_ticket != null) {
            String nonceStr = RandomUtil.getRandomString(16, RandomUtil.PATTEN_ALL_CHARS);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String signature = getSignature(nonceStr, timestamp, url);
            LogUtil.debug("新的signature： " + signature);
            config.refresh(timestamp, nonceStr, signature);
        }
    }

    private static String getSignature(String nonceStr, String timestamp, String url) {
        String s = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s",
                jsapi_ticket, nonceStr, String.valueOf(timestamp), url);
        LogUtil.debug("拼接后的字符串s： " + s);
        return EncryptUtil.encryptSHA1(s);
    }

}
