package common.WeChat.util;

/**
 * Created by Errol on 17/6/12.
 */
public class WeChatMediaUtil {

    public static String getRequestUrlForDownloadMedia(String media_id) {
        return String.format("https://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s",
                WeChatApiUtil.getAccessToken(), media_id);
    }


}
