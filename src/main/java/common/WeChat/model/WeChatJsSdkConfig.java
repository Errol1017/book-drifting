package common.WeChat.model;

import project.resource.properties.WeChatProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Errol on 17/5/30.
 */
public class WeChatJsSdkConfig {

    private boolean debug;
    private String appId;
    private String timestamp;
    private String nonceStr;
    private String signature;
    private List<String> jsApiList;

    public WeChatJsSdkConfig(boolean debug) {
        this.debug = debug;
        this.appId = WeChatProperties.getInstance().getAppId();
        this.timestamp = "";
        this.nonceStr = "";
        this.signature = "";
        this.jsApiList = new ArrayList<>();
        this.jsApiList.add("chooseImage");
        this.jsApiList.add("scanQRCode");
    }

    public void refresh(String timestamp, String nonceStr, String signature) {
        this.timestamp = timestamp;
        this.nonceStr = nonceStr;
        this.signature = signature;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getAppId() {
        return appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public List<String> getJsApiList() {
        return jsApiList;
    }
}
