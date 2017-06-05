package common.WeChat.util;

import project.resource.properties.WeChatProperties;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Errol on 17/5/29.
 */
public class WeChatOAuth2Util {

    public static String getRequestUrl(String redirectUri, String scope) {
        try {
            return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s#wechat_redirect",
                    WeChatProperties.getInstance().getAppId(), URLEncoder.encode(WeChatProperties.getInstance().getRedirectBaseUri() + redirectUri, "UTF-8"), scope);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getRequestUrl(String redirectUri, String scope, String state) {
        try {
            return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
                    WeChatProperties.getInstance().getAppId(), URLEncoder.encode(WeChatProperties.getInstance().getRedirectBaseUri() + redirectUri, "UTF-8"), scope, state);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getRequestUrlForAccessToken(String code) {
        return String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                WeChatProperties.getInstance().getAppId(), WeChatProperties.getInstance().getAppSecret(), code);
    }

    public static String getRequestUrlForUserInfo(String accessToken, String openid) {
        return String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN",
                accessToken, openid);
    }

}
