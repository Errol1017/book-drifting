package project.open.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import common.Util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.basic.entity.InvitationCode;
import project.navigator.service.CacheManager;
import project.open.model.UserInfo;
import project.open.model.UserVerifyForm;
import project.operation.entity.Client;
import project.open.util.ClientValidator;
import project.operation.model.ClientCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Errol on 17/5/4.
 */
@Controller
@RequestMapping("/public/user")
public class UserCenterController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = "/check", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
    public String checkOpenId(HttpServletRequest request) throws Exception {
        System.out.println(request.getSession().getId());
        Result result = ClientValidator.ClientValidate(request, cacheManager);
        if (result.getCode() == -2) {
            //无openId session，需静默授权
            //模拟授权获得openId
            request.getSession().setAttribute("openId", RandomUtil.getRandomString(12, RandomUtil.PATTEN_ALL_CHARS));
            return "redirect:/public/user/check";
        } else if (result.getCode() == -1) {
            //有openId session，但clientCache中无对应记录，也即静默授权后未核验身份
//            return "redirect:/index.html#/page/user/verify";
            return "redirect:" + comService.getRemoteClientId() + "/index.html#/page/user/verify";
        } else {
            //有openId session，且已核验身份
//            return "redirect:/index.html#/page/user/center";
            return "redirect:" + comService.getRemoteClientId() + "/index.html#/page/user/center";
        }
    }

    @RequestMapping(value = "/check/result", produces = "application/json;charset=utf-8")
    public @ResponseBody Object checkResult() throws Exception {
        return Result.ERROR(ErrorCode.LOGIN_TIMEOUT, "/public/user/check");
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object userVerify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String openId = ClientValidator.getOpenId(request);
        if (openId.equals("null")) {
            request.getRequestDispatcher("/public/user/check/result").forward(request, response);
            return false;
        }
        UserVerifyForm form = DataManager.string2Object(request.getParameter("data"), UserVerifyForm.class);
        if (form != null) {
            InvitationCode ic = comService.getFirst(InvitationCode.class, "code='" + form.getCode() + "'");
            if (ic != null && ic.getClientId() == -1) {
                Client cc = comService.getFirst(Client.class, "openId='" + openId +"'");
                if (cc != null) {
                    return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "您的微信已建立用户，请勿重复建立");
                }
                Client client = new Client(form);
                client.setOpenId(openId);
                comService.saveDetail(client);
                ic.setClientId(client.getId());
                comService.saveDetail(ic);
                cacheManager.addClientCache(new ClientCache(client));
                return Result.SUCCESS();
            }
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "邀请码错误或已被使用");
        } else {
            return Result.ERROR(ErrorCode.CODING_ERROR);
        }
    }

    @RequestMapping(value = "/center/info", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getUserInfo(HttpServletRequest request) throws Exception {
        ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
        Client client = comService.getFirst(Client.class, "openId='" + cc.getOpenId() + "'");
        UserInfo info = new UserInfo(client, comService);
        return Result.SUCCESS(info);
    }

}
