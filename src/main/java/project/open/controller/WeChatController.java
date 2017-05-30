package project.open.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.ServerAdvice.util.LogUtil;
import common.WeChat.util.WeChatOAuth2Util;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import project.navigator.service.CacheManager;
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
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(WeChatOAuth2Util.getRequestUrlForAccessToken(code));
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            LogUtil.debug("获取response---------------------------------------------------------------------");
            LogUtil.debug(response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
//            LogUtil.debug("调用EntityUtils.toString(entity)输出");
//            LogUtil.debug(EntityUtils.toString(entity));
            LogUtil.debug("调用EntityUtils.toString(entity, utf-8)输出");
            String tokens = EntityUtils.toString(entity, "utf-8");
            LogUtil.debug(tokens);
            Map<String, String> map = DataManager.string2Map(tokens);
            if (map!=null) {
                String openId = map.get("openid");
                if (openId!=null){//获取成功
                    LogUtil.debug("获取openId 成功，openid为："+openId);
                    request.getSession().setAttribute("openId", openId);
                    ClientCache clientCache = cacheManager.getClientCache(openId);
                    if (clientCache == null) {
                        LogUtil.debug("ClientCache为null，返回至userVerify");
                        //需登记核验
                        return ServerProperties.getInstance().getRemote()+"index.html#/page/user/verify";
                    } else {
                        LogUtil.debug("ClientCache不为null，返回至userCenter");
                        //已核验登记
                        return ServerProperties.getInstance().getRemote()+"index.html#/page/user/center";
                    }
                }else {
                    LogUtil.debug("获取openid失败");
                    LogUtil.debug("errcode: "+map.get("errcode"));
                    LogUtil.debug("errmsg: "+map.get("errmsg"));
                }
            }else {
                LogUtil.debug("map为null");
            }
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return "";
    }

}
