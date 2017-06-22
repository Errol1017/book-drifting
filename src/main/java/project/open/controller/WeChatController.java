package project.open.controller;

import common.CRUD.service.ComService;
import common.FileProcessor.FileManager;
import common.FileProcessor.FileType;
import common.FileProcessor.image.ImgUtil;
import common.HttpClient.util.HttpClientUtil;
import common.ServerAdvice.util.LogUtil;
import common.Util.Base64Util;
import common.Util.EncryptUtil;
import common.WeChat.pojo.WeChatOAuth2Scope;
import common.WeChat.util.WeChatOAuth2Util;
import common.pojo.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.service.CacheManager;
import project.operation.entity.Client;
import project.operation.model.ClientCache;
import project.resource.pojo.UploadFolders;
import project.resource.properties.ServerProperties;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by Errol on 17/5/29.
 */
@Controller
@RequestMapping("/wechat")
public class WeChatController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = "/oauth2/redirect", produces = "text/html;charset=utf-8")
    public String OAuth2Redirect(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        Map<String, String> map = HttpClientUtil.get(WeChatOAuth2Util.getRequestUrlForAccessToken(code));
        if (map != null) {
            String scope = map.get("scope");
            if (scope != null) {
                String openId = map.get("openid");
                if (scope.equals(WeChatOAuth2Scope.snsapi_base)) {
                    request.getSession().setMaxInactiveInterval(60 * 60 * 24 * 7);
                    request.getSession().setAttribute("openId", openId);
                    ClientCache clientCache = cacheManager.getClientCache(openId);
                    if (clientCache == null) {
                        return ServerProperties.getInstance().getRemote() + "index.html#/page/user/verify";
                    } else {
                        return ServerProperties.getInstance().getRemote() + "index.html#/page/user/center";
                    }
                } else {
                    String accessToken = map.get("access_token");
                    Map<String, String> data = HttpClientUtil.get(WeChatOAuth2Util.getRequestUrlForUserInfo(accessToken, openId));
                    if (data != null) {
                        String openid = data.get("openid");
                        if (openid != null) {
                            Client client = comService.getFirst(Client.class, "openId='" + openid + "'");
                            client.setNickName(data.get("nickname"));
                            String g = String.valueOf(data.get("sex"));
                            client.setGender(g.equals("0") ? Gender.UNKNOWN : g.equals("1") ? Gender.MALE : Gender.FEMALE);
                            String headimgurl = data.get("headimgurl");
                            if (!headimgurl.equals("")) {
                                String filename = HttpClientUtil.fetch(headimgurl.substring(0, headimgurl.lastIndexOf("/")) + "/0", FileType.jpg);
                                client.setPortrait(FileManager.save(filename, UploadFolders.avatar));
                                client.setAvatar(FileManager.save(ImgUtil.scale(filename, 132), UploadFolders.avatar));
                            }
                            comService.saveDetail(client);
                            ClientCache cc = cacheManager.getClientCache(client.getId());
                            cc.setNickName(client.getNickName());
                            cc.setAvatar(Base64Util.img2String(client.getAvatar()));
                            return ServerProperties.getInstance().getRemote() + "index.html#/page/user/center";
                        } else {
                            LogUtil.debug("获取userInfo失败");
                            LogUtil.debug("errcode: " + map.get("errcode"));
                            LogUtil.debug("errmsg: " + map.get("errmsg"));
                        }
                    } else {
                        LogUtil.debug("获取userInfo的data为null");
                    }
                }
            } else {
                LogUtil.debug("获取openid失败");
                LogUtil.debug("errcode: " + map.get("errcode"));
                LogUtil.debug("errmsg: " + map.get("errmsg"));
            }
        } else {
            LogUtil.debug("oauth授权回调获得的map为null");
        }
        return "";
    }


}
