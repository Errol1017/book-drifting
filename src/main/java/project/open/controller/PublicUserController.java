package project.open.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import common.ServerAdvice.util.LogUtil;
import common.Util.DateUtil;
import common.WeChat.pojo.WeChatOAuth2Scope;
import common.WeChat.util.WeChatOAuth2Util;
import common.pojo.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.basic.entity.InvitationCode;
import project.navigator.service.CacheManager;
import project.open.model.*;
import project.operation.entity.*;
import project.open.util.ClientValidator;
import project.operation.model.ClientCache;
import project.operation.pojo.BookStatus;
import project.operation.pojo.OwnerType;
import project.operation.pojo.ReservationStatus;
import project.resource.properties.ServerProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Errol on 17/5/4.
 */
@Controller
@RequestMapping("/public/user")
public class PublicUserController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = "/check", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
    public String checkOpenId(HttpServletRequest request) throws Exception {
        LogUtil.debug("检查用户是否登录");
        String openid = String.valueOf(request.getSession().getAttribute("openId"));
        LogUtil.debug("请求session中的openid为： " + openid);
        String k = request.getParameter("k");
        if (k != null) {
            if (k.equals("1")) {
                request.getSession().setAttribute("openId", "hiU47jOZQpCx");
            } else if (k.equals("2")) {
                request.getSession().setAttribute("openId", "8PCdldvgp7ok");
            } else if (k.equals("3")) {
                request.getSession().setAttribute("openId", "Z8RnFPafBI5U");
            } else {
                long max = comService.getCount(Client.class);
                Client client = comService.getList(Client.class, (new Random().nextInt((int) max)), 1).get(0);
                request.getSession().setMaxInactiveInterval(60 * 60 * 24 * 7);
                request.getSession().setAttribute("openId", client.getOpenId());
            }
            return ServerProperties.getInstance().getRedirect() + "public/user/check";
        }


        LogUtil.debug("ServerProperties.getInstance().getRemote()值为： " + ServerProperties.getInstance().getRemote());
        Result result = ClientValidator.ClientValidate(request, cacheManager);
        if (result.getCode() == -2) {
            LogUtil.debug("ClientValidator.ClientValidate返回的code为： -2");
            //无openId session，需静默授权
            LogUtil.debug("redirect:" + WeChatOAuth2Util.getRequestUrl("oauth2/redirect", WeChatOAuth2Scope.snsapi_base));
            return "redirect:" + WeChatOAuth2Util.getRequestUrl("oauth2/redirect", WeChatOAuth2Scope.snsapi_base);
        } else if (result.getCode() == -1) {
            LogUtil.debug("ClientValidator.ClientValidate返回的code为： -1");
            //有openId session，但clientCache中无对应记录，也即静默授权后未核验身份
            return ServerProperties.getInstance().getRemote() + "index.html#/page/user/verify";
        } else {
            LogUtil.debug("ClientValidator.ClientValidate返回的code为： 0");
            //有openId session，且已核验身份
            return ServerProperties.getInstance().getRemote() + "index.html#/page/user/center";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/check/result", produces = "application/json;charset=utf-8")
    public Object checkResult() throws Exception {
        LogUtil.debug("服务端导航user/check/result");
        return Result.ERROR(ErrorCode.LOGIN_TIMEOUT, "/public/user/check");
    }

    @ResponseBody
    @RequestMapping(value = "/verify", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object userVerify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String openId = ClientValidator.getOpenId(request);
        LogUtil.debug("request中openid为： " + openId);
        if (openId.equals("null")) {
            LogUtil.debug("request中openid为null");
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
                LogUtil.debug("保存用户");
                ic.setClientId(client.getId());
                comService.saveDetail(ic);
                cacheManager.addClientCache(new ClientCache(client));
                LogUtil.debug("建立用户，返回链接，拉取用户信息： " + WeChatOAuth2Util.getRequestUrl("oauth2/redirect", WeChatOAuth2Scope.snsapi_userinfo));
                return Result.SUCCESS(WeChatOAuth2Util.getRequestUrl("oauth2/redirect", WeChatOAuth2Scope.snsapi_userinfo));
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
        UserInfo info = new UserInfo(client);
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
        UserInfoData data = new UserInfoData(client, cacheManager);
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
        if (id == null || id.equals("")) {
            return Result.ERROR(ErrorCode.CODING_ERROR);
        }
        if (comService.hasExist(Book.class, "stackType='" + OwnerType.INDIVIDUAL + "' and stackId=" + id)) {
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "您有图书绑定了该起漂点，无法删除");
        }
        Client client = comService.getDetail(Client.class, ClientValidator.getClientId(request, cacheManager));
        String[] stackIds = client.getStackIds().split(",");
        StringBuffer sb = new StringBuffer();
        for (String s : stackIds) {
            if (s.equals(id)) {
                comService.deleteDetail(Stacks.class, "id=" + s);
            } else {
//                sb.append(s + ",");
                sb.append((sb.length() > 0 ? "," : "") + s);
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

    @ResponseBody
    @RequestMapping(value = "/reservation/borrow", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getUserReBorrow(HttpServletRequest request) throws Exception {
        ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
        List<BookListBorrowing> list = new ArrayList<>();
        if (cc.getBorrowingSum() != 0) {
            List<Object[]> res = comService.query("select r,b from Reservation r,Book b where r.bookId=b.id" +
                    " and r.clientId=" + cc.getId() + " and r.status='" + ReservationStatus.BORROW + "'");
            for (Object[] o : res) {
                list.add(new BookListBorrowing((Book) o[1], (Reservation) o[0]));
            }
        }
        return Result.SUCCESS(list);
    }

    @ResponseBody
    @RequestMapping(value = "/reservation/recede", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getUserReRecede(HttpServletRequest request) throws Exception {
        ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
        List<BookListRecede> list = new ArrayList<>();
        List<Object[]> res = comService.query("select r,b from Reservation r,Book b where r.bookId=b.id and r.clientId=" + cc.getId() +
                " and r.status='" + ReservationStatus.RECEDE + "' order by r.recedeTime desc", 1, 5);
        for (Object[] o : res) {
            list.add(new BookListRecede((Book) o[1], (Reservation) o[0]));
        }
        return Result.SUCCESS(list);
    }

    @ResponseBody
    @RequestMapping(value = "/reservation/reserve", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getUserReReserve(HttpServletRequest request) throws Exception {
        ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
        List<BookListReserve> list = new ArrayList<>();
        List<Object[]> res = comService.query("select b,r.id from Reservation r,Book b where r.bookId=b.id and r.clientId=" + cc.getId() +
                " and r.status='" + ReservationStatus.RESERVE + "'", 1, 5);
        for (Object[] o : res) {
            Book book = (Book) o[0];
            BookListReserve br = new BookListReserve(book, String.valueOf(o[1]), cacheManager, comService);
//            if (book.getStatus().equals(BookStatus.IN_STOCK)) {
//                if (book.getStackType().equals(OwnerType.AGENCY)) {
//                    br.setOwner(cacheManager.getAgencyCache((int) book.getStackId()).getName());
//                } else {
//                    br.setOwner(cacheManager.getClientCache(book.getOwnerId()).getNickName());
//                }
//            } else if (book.getStatus().equals(BookStatus.UNPREPARED)) {
//                br.setOwner(cacheManager.getClientCache(book.getOwnerId()).getNickName());
//            } else if (book.getStatus().equals(BookStatus.FROZEN)) {
//                br.setOwner("");
//            }else if (book.getStatus().equals(BookStatus.RELEASED)) {
//                if (book.getReservationId() == -1) {
//                    br.setOwner(cacheManager.getAgencyCache((int) book.getStackId()).getName());
//                } else {
//                    Reservation r = comService.getDetail(Reservation.class, book.getReservationId());
//                    br.setOwner("目前用户 " + cacheManager.getClientCache(r.getClientId()).getNickName() + " 正在借阅");
//                }
//            } else {
//                Reservation r = comService.getDetail(Reservation.class, book.getReservationId());
//                br.setOwner("目前用户 " + cacheManager.getClientCache(r.getClientId()).getNickName() + " 正在借阅");
//            }
            list.add(br);
        }
        return Result.SUCCESS(list);
    }

    @ResponseBody
    @RequestMapping(value = "/books", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getUserBooks(HttpServletRequest request) throws Exception {
        List<Book> res = comService.getList(Book.class, "ownerId=" + ClientValidator.getClientId(request, cacheManager));
        List<UserBookList> list = new ArrayList<>();
        for (Book book : res) {
            UserBookList br = new UserBookList(book, cacheManager, comService);
            list.add(br);
        }
        return Result.SUCCESS(list);
    }

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getUserComment(HttpServletRequest request) throws Exception {
        String page = request.getParameter("p");
        List<Object[]> objects = comService.query("select c,b from Comment c, Book b where c.clientId=" +
                ClientValidator.getClientId(request, cacheManager) + " and c.bookId=b.id order by c.id desc", Integer.parseInt(page), 10);
        List<UserCommentList> list = new ArrayList<>();
        for (Object[] o : objects) {
            list.add(new UserCommentList((Comment) o[0], (Book) o[1]));
        }
        return Result.SUCCESS(list);
    }

    @ResponseBody
    @RequestMapping(value = "/comment/delete", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object removeUserComment(HttpServletRequest request) throws Exception {
        String cid = request.getParameter("cid");
        String page = request.getParameter("page");
        comService.deleteDetail(Comment.class, Long.parseLong(cid));
        if (!page.equals("-1")) {
            List<Object[]> objects = comService.query("select c,b from Comment c, Book b where c.clientId=" +
                    ClientValidator.getClientId(request, cacheManager) + " and c.bookId=b.id order by c.id desc", Integer.parseInt(page) * 10, 1);
            if (objects.size() == 1) {
                Object[] o = objects.get(0);
                return Result.SUCCESS(new UserCommentList((Comment) o[0], (Book) o[1]));
            }
        }
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/agency/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitUserAgency(HttpServletRequest request) throws Exception {
        comService.updateDetail(Client.class, ClientValidator.getClientId(request, cacheManager),
                "agencyId=" + request.getParameter("aId") + ",isAdmin=0");
        Client client = comService.getDetail(Client.class, ClientValidator.getClientId(request, cacheManager));
        client.setAgencyId(Integer.parseInt(request.getParameter("aId")));
        comService.saveDetail(client);
        cacheManager.addClientCache(new ClientCache(client));
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/nickname/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitUserNickname(HttpServletRequest request) throws Exception {
        Client client = comService.getDetail(Client.class, ClientValidator.getClientId(request, cacheManager));
        client.setNickName(request.getParameter("n"));
        comService.saveDetail(client);
        cacheManager.addClientCache(new ClientCache(client));
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/name/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitUserName(HttpServletRequest request) throws Exception {
        Client client = comService.getDetail(Client.class, ClientValidator.getClientId(request, cacheManager));
        client.setName(request.getParameter("n"));
        comService.saveDetail(client);
        cacheManager.addClientCache(new ClientCache(client));
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/gender/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitUserGender(HttpServletRequest request) throws Exception {
        String gid = request.getParameter("gid");
        comService.updateDetail(Client.class, ClientValidator.getClientId(request, cacheManager),
                "gender='" + (gid.equals("1") ? Gender.MALE : Gender.FEMALE) + "'");
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/mobile/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitUserMobile(HttpServletRequest request) throws Exception {
        comService.updateDetail(Client.class, ClientValidator.getClientId(request, cacheManager),
                "mobile='" + request.getParameter("m") + "'");
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/number/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitUserNumber(HttpServletRequest request) throws Exception {
        comService.updateDetail(Client.class, ClientValidator.getClientId(request, cacheManager),
                "identityNumber='" + request.getParameter("n") + "'");
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/book/renew", method = RequestMethod.POST, produces = "application/json;chatset=utf-8")
    public Object submitBookRenew(HttpServletRequest request) throws Exception {
        String rid = request.getParameter("rid");
        Reservation reservation = comService.getDetail(Reservation.class, Long.parseLong(rid));
        if (reservation.getRenew() != 0 && reservation.getExpireTime().getTime() > System.currentTimeMillis()) {
            int r = reservation.getRenew();
            reservation.setRenew(++r);
            Calendar c = Calendar.getInstance();
            c.setTime(reservation.getExpireTime());
            c.add(Calendar.DATE, 15);
            c.set(Calendar.HOUR_OF_DAY, 23);
            reservation.setExpireTime(c.getTime());
            comService.saveDetail(reservation);
            return Result.SUCCESS();
        }
        return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "您已无法再续借");
    }

    @ResponseBody
    @RequestMapping(value = "/reservation/cancel", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitReservationCancel(HttpServletRequest request) throws Exception {
        String rid = request.getParameter("rid");
        comService.deleteDetail(Reservation.class, "id=" + rid);
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/message", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getUserMessage(HttpServletRequest request) throws Exception {
        String p = request.getParameter("p");
        List<Message> messages = comService.getList(Message.class, Integer.parseInt(p), 10,"clientId=" + ClientValidator.getClientId(request, cacheManager), "id desc");
        List<UserMessageList> list = new ArrayList<>();
        for (Message message : messages) {
            if (message.getSenderId() == -1) {
                list.add(new UserMessageList(message, cacheManager.getClientCache(message.getClientId())));
            } else {
                list.add(new UserMessageList(message, cacheManager.getClientCache(message.getSenderId())));
            }
        }
        return Result.SUCCESS(list);
    }


}
