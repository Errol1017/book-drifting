package project.open.controller;

import common.DataFormatter.Result;
import common.ServerAdvice.util.LogUtil;
import common.WeChat.util.WeChatJsSdkUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Errol on 17/5/30.
 */
@Controller
@RequestMapping("/public/wechat")
public class PublicWeChatController {

    @ResponseBody
    @RequestMapping(value = "/jsapi/config", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getJsApiConfig(HttpServletRequest request) throws Exception {
        String url = request.getParameter("url");
        return Result.SUCCESS(WeChatJsSdkUtil.getWeChatJsSdkConfig(url));
    }


}
