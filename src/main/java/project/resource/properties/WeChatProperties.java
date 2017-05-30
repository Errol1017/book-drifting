package project.resource.properties;

/**
 * Created by Errol on 17/5/29.
 */
public class WeChatProperties {

    private String appId;
    private String appSecret;
    private String redirectBaseUri;

    private static WeChatProperties instance = new WeChatProperties();

    public static WeChatProperties getInstance() {
        return instance;
    }

    public void init(String appId, String appSecret, String redirecBaseUri) {
        if (this.appId == null) {
            this.appId = appId;
            this.appSecret = appSecret;
            this.redirectBaseUri = redirecBaseUri;
        }
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getRedirectBaseUri() {
        return redirectBaseUri;
    }
}
