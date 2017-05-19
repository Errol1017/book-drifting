package project.open.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.basic.entity.Agency;
import project.basic.model.AgencyCache;
import project.navigator.service.CacheManager;
import project.open.model.BookInfoForm;
import project.open.model.BookList;
import project.open.model.CommentForm;
import project.open.model.DataFormerLabelValue;
import project.open.util.ClientValidator;
import project.operation.entity.*;
import project.operation.model.ClientCache;
import project.operation.pojo.BookStatus;
import project.operation.pojo.OwnerType;
import project.operation.pojo.ReservationStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Errol on 17/5/9.
 */
@Controller
@RequestMapping("/public/book")
public class PublicBookController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookList(HttpServletRequest request) throws Exception {
        String p = request.getParameter("page");
        String q1 = request.getParameter("q1");
        String q2 = request.getParameter("q2");
        String q3 = request.getParameter("q3");
        String s = request.getParameter("search");
        StringBuffer sb = new StringBuffer();
        if (q1 != null && q1.equals("1")) {
            ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
            sb.append("createTime>'" + cc.getLoginTime() + "'");
        }
        if (q2 != null && (q2.equals("1") || q2.equals("2"))) {
            sb.append((sb.length() == 0 ? "" : " and ") + ("status='" + (q2.equals("1") ? BookStatus.IN_STOCK : BookStatus.BORROWED) + "'"));
        }
        if (q3 != null && !q3.equals("0")) {
            sb.append((sb.length() == 0 ? "" : " and ") + ("classificationId in (" + q3 + ")"));
        }
        if (s != null && !s.equals("")) {
            s = s.replace("《", "");
            String[] a = s.split("》 ");
            if (a.length == 1) {
                sb = new StringBuffer("name like '%" + a[0] + "%' or author like '%" + a[0] + "%'");
            } else {
                sb = new StringBuffer("name like '%" + a[0] + "%' or author like '%" + a[1] + "%'");
            }
        }
        List<Book> books = comService.getList(Book.class, p == null ? 1 : Integer.parseInt(p), 10, sb.toString(), "id desc");
        List<BookList> list = new ArrayList<>();
        for (Book book : books) {
            list.add(new BookList(book, comService));
        }
        return Result.SUCCESS(list);
    }

    @ResponseBody
    @RequestMapping(value = "/match", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookMatch(HttpServletRequest request) throws Exception {
        String m = request.getParameter("m");
        m = m.replace(" ", "");
        return Result.SUCCESS(cacheManager.getBookMatch(m));
    }

    @ResponseBody
    @RequestMapping(value = "/add/ag/pick", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getAgencyStackPicked(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        AgencyCache agencyCache = cacheManager.getAgencyCache(Integer.parseInt(id));
        Map<String, Object> map = new HashMap<>();
        map.put("name", agencyCache.getName());
        map.put("stack", agencyCache.getStack());
        return Result.SUCCESS(map);
    }

    @ResponseBody
    @RequestMapping(value = "/add/st/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getIndividualStacksList(HttpServletRequest request) throws Exception {
        ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
        Client client = comService.getDetail(Client.class, cc.getId());
        if (client.getStackIds().equals("")) {
            return Result.SUCCESS(new ArrayList<>());
        }
        List<Stacks> stacksList = comService.getList(Stacks.class, "id in (" + client.getStackIds() + ")");
        return Result.SUCCESS(stacksList);
    }

    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookInfo(HttpServletRequest request) throws Exception {
        Book book = comService.getDetail(Book.class, Long.parseLong(request.getParameter("id")));
        BookInfoForm form = new BookInfoForm(book, comService);
        return Result.SUCCESS(form);
    }

    @ResponseBody
    @RequestMapping(value = "/info/reserve", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookInfoReserve(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        Map<String, Object> map = new HashMap<>();
        if (id != null && !id.equals("")) {
            List<Comment> comments = comService.getList(Comment.class, 1, 2, "bookId=" + id, "id desc");
            List<CommentForm> list0 = new ArrayList<>();
            for (Comment c : comments) {
                list0.add(new CommentForm(c, cacheManager));
            }
            map.put("list0", list0);
            List<Reservation> readers = comService.getList(Reservation.class, 1, 5,
                    "bookId=" + id + " and (status='" + ReservationStatus.BORROW + "' or status='" + ReservationStatus.RECEDE + "')", "borrowedTime desc");
            List<CommentForm> list1 = new ArrayList<>();
            for (Reservation r : readers) {
                list1.add(new CommentForm(r, cacheManager));
            }
            map.put("list1", list1);
            List<Reservation> reservations = comService.getList(Reservation.class, 1, 5,
                    "bookId=" + id + " and status='" + ReservationStatus.RESERVE + "'", "createTime asc");
            List<CommentForm> list2 = new ArrayList<>();
            for (Reservation r : reservations) {
                list2.add(new CommentForm(r, cacheManager));
            }
            map.put("list2", list2);
        }
        return Result.SUCCESS(map);
    }

    @ResponseBody
    @RequestMapping(value = "/order/info", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookOrderInfo(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        List<DataFormerLabelValue> res = new ArrayList<>();
        DataFormerLabelValue map;
        if (id != null && !id.equals("")) {
            Book book = comService.getDetail(Book.class, Long.parseLong(id));
            res.add(new DataFormerLabelValue("书名", book.getName()));
            res.add(new DataFormerLabelValue("图书状态", book.getStatus().getName()));
            if (book.getStatus().equals(BookStatus.IN_STOCK)){
                if (book.getStackType().equals(OwnerType.AGENCY)) {
                    AgencyCache ac = cacheManager.getAgencyCache((int)book.getStackId());
                    res.add(new DataFormerLabelValue("管理机构", ac.getName()));
                    res.add(new DataFormerLabelValue("所在地", ac.getStack().getLocation()));
                    res.add(new DataFormerLabelValue("开放时间", ac.getStack().getOpenTime()));
//                    res.add(new DataFormerLabelValue("注意", "当前在架，请您自行前往相关机构借阅"));
                }else {
                    Client c = comService.getDetail(Client.class, book.getOwnerId());
                    Stacks s = comService.getDetail(Stacks.class, book.getStackId());
                    res.add(new DataFormerLabelValue("管理者", c.getName()));
                    res.add(new DataFormerLabelValue("联系电话", c.getMobile()));
                    res.add(new DataFormerLabelValue("建议地点", s.getLocation()));
                    res.add(new DataFormerLabelValue("建议时间", s.getOpenTime()));
                }
            }else if (book.getStatus().equals(BookStatus.UNPREPARED)){
                Client c = comService.getDetail(Client.class, book.getOwnerId());
                res.add(new DataFormerLabelValue("管理者", c.getName()));
                res.add(new DataFormerLabelValue("联系电话", c.getMobile()));
            } else {
                Reservation r = comService.getDetail(Reservation.class, book.getReservationId());
                Client c = comService.getDetail(Client.class, r.getClientId());
                res.add(new DataFormerLabelValue("管理者", c.getName()));
                res.add(new DataFormerLabelValue("联系电话", c.getMobile()));
            }
        }
        return Result.SUCCESS(res);
    }

    @ResponseBody
    @RequestMapping(value = "/order/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitOrder(HttpServletRequest request) throws Exception{
        String id = request.getParameter("id");
        long cid = ClientValidator.getClientId(request, cacheManager);
        if (comService.hasExist(Reservation.class, "bookId="+id+" and clientId="+cid+" and status!='"+ReservationStatus.RECEDE+"'")){
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "您已预约");
        }
        Reservation r = new Reservation(Long.parseLong(id), cid);
        comService.saveDetail(r);
        return Result.SUCCESS();
    }



}
