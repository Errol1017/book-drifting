package project.open.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import common.Util.DateUtil;
import common.Util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.basic.entity.InvitationCode;
import project.navigator.service.CacheManager;
import project.open.model.UserInfo;
import project.open.model.UserInfoData;
import project.open.model.UserVerifyForm;
import project.operation.entity.Book;
import project.operation.entity.Client;
import project.open.util.ClientValidator;
import project.operation.entity.Stacks;
import project.operation.model.ClientCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Random;

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
//            if (new Random().nextInt(10) >4) {
            System.out.println("随机>4  获取已有client");
            long max = comService.getCount(Client.class);
            Client client = comService.getList(Client.class, (new Random().nextInt((int) max)), 1).get(0);
            request.getSession().setMaxInactiveInterval(60 * 60 * 24 * 7);
            request.getSession().setAttribute("openId", client.getOpenId());
//            } else {
//                System.out.println("随机<5  新增openid");
//                request.getSession().setAttribute("openId", RandomUtil.getRandomString(12, RandomUtil.PATTEN_ALL_CHARS));
//            }
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

    @ResponseBody
    @RequestMapping(value = "/check/result", produces = "application/json;charset=utf-8")
    public Object checkResult() throws Exception {
        return Result.ERROR(ErrorCode.LOGIN_TIMEOUT, "/public/user/check");
    }

    @ResponseBody
    @RequestMapping(value = "/verify", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object userVerify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String openId = ClientValidator.getOpenId(request);
        if (openId.equals("null")) {
            request.getRequestDispatcher("/public/user/check/result").forward(request, response);
            return false;
        }
        UserVerifyForm form = DataManager.string2Object(request.getParameter("data"), UserVerifyForm.class);
        if (form != null) {
            InvitationCode ic = comService.getFirst(InvitationCode.class, "code='" + form.getCode() + "'");
            if (ic != null && ic.getClientId() == -1) {
                Client cc = comService.getFirst(Client.class, "openId='" + openId + "'");
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

    @ResponseBody
    @RequestMapping(value = "/center/info", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getUserInfo(HttpServletRequest request) throws Exception {
        ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
        Client client = comService.getFirst(Client.class, "openId='" + cc.getOpenId() + "'");
        UserInfo info = new UserInfo(client, comService);
//        long nb = comService.getCount(Book.class, "createTime>'"+cc.getLoginTime()+"'");
        Book b = comService.getFirst(Book.class, "createTime>'" + cc.getLoginTime() + "'");
        info.setNewBook(String.valueOf(b == null ? "0" : "1"));
        info.setNewMsg(String.valueOf(cc.getNews().size()));
        return Result.SUCCESS(info);
    }

    @ResponseBody
    @RequestMapping(value = "/info/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getUserInfoData(HttpServletRequest request) throws Exception {
        ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
        Client client = comService.getFirst(Client.class, "openId='" + cc.getOpenId() + "'");
        UserInfoData data = new UserInfoData(client, comService, cacheManager);
        return Result.SUCCESS(data);
    }

    @ResponseBody
    @RequestMapping(value = "/time", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object resetLoginTime(HttpServletRequest request) throws Exception {
        ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
        cc.setLoginTime(DateUtil.date2String(new Date(), DateUtil.PATTERN_A));
        comService.updateDetail(Client.class, "openId='" + cc.getOpenId() + "'", "loginTime='" + DateUtil.date2String(new Date(), DateUtil.PATTERN_A) + "'");
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/stack/delete", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object deleteUserStack(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        Client client = comService.getDetail(Client.class, ClientValidator.getClientId(request, cacheManager));
        String[] stackIds = client.getStackIds().split(",");
        StringBuffer sb = new StringBuffer();
        for (String s : stackIds) {
            if (s.equals(id)) {
                comService.deleteDetail(Stacks.class, "id=" + s);
            } else {
//                sb.append(s + ",");
                sb.append((sb.length()>0?",":"") + s);
            }
        }
//        if (sb.length() > 0) {
//            sb.deleteCharAt(sb.length() - 1);
//        }
        client.setStackIds(sb.toString());
        comService.saveDetail(client);
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/stack/edit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object editUserStack(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        String lo = request.getParameter("lo");
        String op = request.getParameter("op");
        if (id != null && lo != null && op != null) {
            Stacks stack = new Stacks(lo, op);
            if (id.equals("")) {
                comService.saveDetail(stack);
                Client client = comService.getDetail(Client.class, ClientValidator.getClientId(request, cacheManager));
                String s = client.getStackIds();
                client.setStackIds(s + (s.equals("") ? "" : ",") + stack.getId());
                comService.saveDetail(client);
                return Result.SUCCESS(stack.getId());
            } else {
                stack.setId(Long.parseLong(id));
                comService.saveDetail(stack);
                return Result.SUCCESS();
            }
        }
        return Result.ERROR(-1);
    }

}
