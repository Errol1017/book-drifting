package project.open.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.Result;
import common.HttpClient.util.HttpClientUtil;
import common.ServerAdvice.util.LogUtil;
import common.WeChat.pojo.WeChatOAuth2Scope;
import common.WeChat.util.WeChatJsSdkUtil;
import common.WeChat.util.WeChatOAuth2Util;
import common.pojo.Gender;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.service.CacheManager;
import project.operation.entity.Client;
import project.operation.model.ClientCache;
import project.resource.properties.ServerProperties;

import javax.servlet.http.HttpServletRequest;
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
        LogUtil.debug("oauth授权回调");
        String code = request.getParameter("code");
        Map<String, String> map = HttpClientUtil.get(WeChatOAuth2Util.getRequestUrlForAccessToken(code));
        if (map != null) {
            String scope = map.get("scope");
            LogUtil.debug("回调scope为： " + scope);
            if (scope != null) {//获取成功
                String openId = map.get("openid");
                LogUtil.debug("获取openId 成功，openid为：" + openId);
                if (scope.equals(WeChatOAuth2Scope.snsapi_base)) {
                    request.getSession().setMaxInactiveInterval(60 * 60 * 24 * 7);
                    request.getSession().setAttribute("openId", openId);
                    ClientCache clientCache = cacheManager.getClientCache(openId);
                    if (clientCache == null) {
                        LogUtil.debug("ClientCache为null，返回至userVerify");
                        //需登记核验
                        return ServerProperties.getInstance().getRemote() + "index.html#/page/user/verify";
                    } else {
                        LogUtil.debug("ClientCache不为null，返回至userCenter");
                        //已核验登记
                        return ServerProperties.getInstance().getRemote() + "index.html#/page/user/center";
                    }
                } else {
                    LogUtil.debug("scope为userInfo");
                    String accessToken = map.get("access_token");
                    Map<String, String> data = HttpClientUtil.get(WeChatOAuth2Util.getRequestUrlForUserInfo(accessToken, openId));
                    if (data != null) {
                        LogUtil.debug("获取用户信息，返回data不是null");
                        String openid = data.get("openid");
                        if (openid != null) {
                            Client client = comService.getFirst(Client.class, "openId='" + openid + "'");
                            client.setNickName(data.get("nickname"));
                            String g = String.valueOf(data.get("sex"));
                            client.setGender(g.equals("0") ? Gender.UNKNOWN : g.equals("1") ? Gender.MALE : Gender.FEMALE);
                            LogUtil.debug("获得的用户头像: " + map.get("headimgurl"));
                            comService.saveDetail(client);
                            cacheManager.getClientCache(client.getId()).setNickName(client.getNickName());
                            LogUtil.debug("更换cacheManager中的clientCache的nickname： " + cacheManager.getClientCache(client.getId()).getNickName());
                            LogUtil.debug("返回到userCenter");
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
