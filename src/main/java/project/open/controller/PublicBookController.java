package project.open.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import common.FileProcessor.FileType;
import common.HttpClient.util.HttpClientUtil;
import common.Util.Base64Util;
import common.WeChat.util.WeChatMediaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.basic.entity.Agency;
import project.basic.entity.BookQrCode;
import project.basic.model.AgencyCache;
import project.navigator.service.CacheManager;
import project.open.model.*;
import project.open.util.ClientValidator;
import project.open.util.ScanUtil;
import project.operation.entity.*;
import project.operation.model.BookCache;
import project.operation.model.ClientCache;
import project.operation.pojo.BookStatus;
import project.operation.pojo.OwnerType;
import project.operation.pojo.ReservationStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
        if (q2 != null && (q2.equals("1") || q2.equals("2"))) {
            sb.append("status='" + (q2.equals("1") ? BookStatus.IN_STOCK : BookStatus.BORROWED) + "'");
        } else {
            sb.append("status='" + BookStatus.UNPREPARED + "' or status='" + BookStatus.IN_STOCK + "' or status='" + BookStatus.BORROWED + "'");
        }
        if (q1 != null && q1.equals("1")) {
            ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
            sb.append(" and createTime>'" + cc.getLoginTime() + "'");
        }
        if (q3 != null && !q3.equals("0")) {
            sb.append(" and classificationId in (" + q3 + ")");
        }
        if (s != null && !s.equals("")) {
            s = s.replace("《", "");
            String[] a = s.split("》 ");
            if (a.length == 1) {
                sb = new StringBuffer("name like '%" + a[0] + "%' or author like '%" + a[0] + "%'");
            } else {
                sb = new StringBuffer("name like '%" + a[0] + "%' or author like '%" + a[1] + "%'");
            }
            sb.append(" and status='" + BookStatus.UNPREPARED + "' or status='" + BookStatus.IN_STOCK + "' or status='" + BookStatus.BORROWED + "'");
        }
        List<Book> books = comService.getList(Book.class, p == null ? 1 : Integer.parseInt(p), 10, sb.toString(), "id desc");
        List<BookList> list = new ArrayList<>();
        for (Book book : books) {
            list.add(new BookList(book, cacheManager.getBookCache(book.getId())));
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
    @RequestMapping(value = "/add/check/qrcode", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookQrCodeCheck(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        BookQrCode bookQrCode = comService.getFirst(BookQrCode.class, "code='" + code + "'");
        if (bookQrCode == null || bookQrCode.getBookId() != -1) {
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "二维码错误或已被使用");
        }
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/add/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitBookAdd(HttpServletRequest request) throws Exception {
        BookAddForm form = DataManager.string2Object(request.getParameter("data"), BookAddForm.class);
        BookQrCode bookQrCode = comService.getFirst(BookQrCode.class, "code='" + form.getQrCode() + "'");
        if (bookQrCode == null || bookQrCode.getBookId() != -1) {
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "二维码错误或已被使用");
        }
        if (form != null) {
            Book book;
            if (form.getId().equals("")) {
                book = new Book(form, ClientValidator.getClientId(request, cacheManager));
                comService.saveDetail(book);
                cacheManager.addBookCache(new BookCache(book));
                bookQrCode.setBookId(book.getId());
                comService.saveDetail(bookQrCode);
            } else {
                book = comService.getDetail(Book.class, Long.parseLong(form.getId()));
                book.modify(form);
                comService.saveDetail(book);
                cacheManager.getBookCache(book.getId()).modify(book);
            }
            return Result.SUCCESS();
        }
        return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "数据有误");
    }

    @ResponseBody
    @RequestMapping(value = "/edit/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookEditData(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (id != null && !id.equals("")) {
            Book book = comService.getDetail(Book.class, Long.parseLong(id));
            BookAddForm form = new BookAddForm(book);
            Map<String, Object> map = new HashMap<>();
            map.put("book", form);
            if (book.getStackType().equals(OwnerType.AGENCY)) {
                AgencyCache agencyCache = cacheManager.getAgencyCache((int) book.getStackId());
                Map<String, String> agency = new HashMap<>();
                agency.put("name", agencyCache.getName());
                agency.put("addr", agencyCache.getStack().getLocation());
                agency.put("stackId", String.valueOf(agencyCache.getId()));
                map.put("agency", agency);
            } else {
                Stacks stack = comService.getDetail(Stacks.class, book.getStackId());
                Map<String, String> stacks = new HashMap<>();
                stacks.put("addr", stack.getLocation());
                stacks.put("stackId", String.valueOf(stack.getId()));
                map.put("stacks", stacks);
            }
            return Result.SUCCESS(map);
        }
        return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "无效请求");
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
        BookInfoForm form = new BookInfoForm(book, cacheManager);
        return Result.SUCCESS(form);
    }

    @ResponseBody
    @RequestMapping(value = "/info/reserve", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookInfoReserve(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        Map<String, Object> map = new HashMap<>();
        if (id != null && !id.equals("")) {
            List<Comment> comments = comService.getList(Comment.class, 1, 2, "bookId=" + id, "id desc");
            List<BookInfoCommentForm> list0 = new ArrayList<>();
            for (Comment c : comments) {
                list0.add(new BookInfoCommentForm(c, cacheManager));
            }
            map.put("list0", list0);
            List<Reservation> readers = comService.getList(Reservation.class, 1, 5,
                    "bookId=" + id + " and (status='" + ReservationStatus.BORROW + "' or status='" + ReservationStatus.RECEDE + "')", "borrowedTime desc");
            List<BookInfoCommentForm> list1 = new ArrayList<>();
            for (Reservation r : readers) {
                list1.add(new BookInfoCommentForm(r, cacheManager));
            }
            map.put("list1", list1);
            List<Reservation> reservations = comService.getList(Reservation.class, 1, 5,
                    "bookId=" + id + " and status='" + ReservationStatus.RESERVE + "'", "createTime asc");
            List<BookInfoCommentForm> list2 = new ArrayList<>();
            for (Reservation r : reservations) {
                list2.add(new BookInfoCommentForm(r, cacheManager));
            }
            map.put("list2", list2);
        }
        return Result.SUCCESS(map);
    }

    @ResponseBody
    @RequestMapping(value = "/order/info", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookOrderInfo(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        BookCache bookCache = cacheManager.getBookCache(Long.parseLong(id));
        if (bookCache.getStatus().equals(BookStatus.FROZEN) || bookCache.getStatus().equals(BookStatus.RELEASED)) {
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "该图书已出库，无法预约");
        }
        List<DataFormerLabelValue> res = new ArrayList<>();
        if (id != null && !id.equals("")) {
            Book book = comService.getDetail(Book.class, Long.parseLong(id));
            res.add(new DataFormerLabelValue("书名", book.getName()));
            res.add(new DataFormerLabelValue("图书状态", book.getStatus().getName()));
            if (book.getStatus().equals(BookStatus.IN_STOCK)) {
                if (book.getStackType().equals(OwnerType.AGENCY)) {
                    AgencyCache ac = cacheManager.getAgencyCache((int) book.getStackId());
                    res.add(new DataFormerLabelValue("管理机构", ac.getName()));
                    res.add(new DataFormerLabelValue("所在地", ac.getStack().getLocation()));
                    res.add(new DataFormerLabelValue("开放时间", ac.getStack().getOpenTime()));
//                    res.add(new DataFormerLabelValue("", "当前在架，您可自行前往相关机构借阅"));
                } else {
                    Client c = comService.getDetail(Client.class, book.getOwnerId());
                    Stacks s = comService.getDetail(Stacks.class, book.getStackId());
                    res.add(new DataFormerLabelValue("管理者", c.getName()));
                    res.add(new DataFormerLabelValue("联系电话", c.getMobile()));
                    res.add(new DataFormerLabelValue("建议地点", s.getLocation()));
                    res.add(new DataFormerLabelValue("建议时间", s.getOpenTime()));
                }
            } else if (book.getStatus().equals(BookStatus.UNPREPARED)) {
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
    public Object submitOrder(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        long cid = ClientValidator.getClientId(request, cacheManager);
        BookCache bookCache = cacheManager.getBookCache(Long.parseLong(id));
        if (bookCache.getStatus().equals(BookStatus.RELEASED) || bookCache.getStatus().equals(BookStatus.FROZEN)) {
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "图书已出库，无法预约");
        }
        if (comService.hasExist(Reservation.class, "bookId=" + id + " and clientId=" + cid + " and status!='" + ReservationStatus.RECEDE + "'")) {
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "您已预约过该图书");
        }
        Reservation r = new Reservation(Long.parseLong(id), cid);
        comService.saveDetail(r);
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = "/scan/info", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getScanBookInfo(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        BookCache bookCache = cacheManager.getBookCache(code);
        System.out.println(bookCache);
        if (bookCache != null) {
            Result result;
            ClientCache clientCache = ClientValidator.getClientCache(request, cacheManager);
            if (ScanUtil.isFirstScan(bookCache)) {  //第一次扫描
                result = ScanUtil.getFirstScanDirection(bookCache, clientCache);
            } else { //第二次扫描
                result = ScanUtil.getSecondScanDirection(bookCache, clientCache);
            }
            if (result.getCode() == 0) {
                Random random = new Random();
                String key = String.valueOf(random.nextInt(1000));
                clientCache.setRandom(key);
                Map<String, Object> map = new HashMap<>();
                map.put("key", key);
                map.put("data", result.getData());
                map.put("bid", bookCache.getId());
                return Result.SUCCESS(map);
            } else {
                return result;
            }
        } else {
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "无效操作。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/scan/confirm", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitScanConfirm(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        String key = request.getParameter("key");
        ClientCache clientCache = ClientValidator.getClientCache(request, cacheManager);
        if (key.equals(clientCache.getRandom())) {
            clientCache.setRandom("");
            BookCache bookCache = cacheManager.getBookCache(code);
            if (bookCache != null) {
                if (ScanUtil.isFirstScan(bookCache)) {  //第一次扫描
                    return ScanUtil.setFirstScanConfirm(bookCache, clientCache, comService, cacheManager);
                } else { //第二次扫描
                    return ScanUtil.setSecondScanConfirm(bookCache, clientCache, comService, cacheManager);
                }
            }
        }
        return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "无效操作。");
    }

    @ResponseBody
    @RequestMapping(value = "/comment/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookCommentList(HttpServletRequest request) throws Exception {
        String bookId = request.getParameter("bookId");
        List<Comment> res = comService.getList(Comment.class, "bookId=" + bookId, "id desc");
        List<BookCommentList> list = new ArrayList<>();
        for (Comment c : res) {
            list.add(new BookCommentList(c, cacheManager));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("title", cacheManager.getBookCache(Long.parseLong(bookId)).getName());
        return Result.SUCCESS(map);
    }

    @ResponseBody
    @RequestMapping(value = "/comment/save", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitBookComment(HttpServletRequest request) throws Exception {
        String bookId = request.getParameter("bookId");
        String comment = request.getParameter("comment");
        if (bookId != null && !bookId.equals("") && comment != null && !comment.equals("")) {
            Comment c = new Comment(Long.parseLong(bookId), ClientValidator.getClientId(request, cacheManager), comment);
            comService.saveDetail(c);
            return Result.SUCCESS();
        }
        return Result.ERROR(ErrorCode.LOGIN_TIMEOUT);
    }

    @ResponseBody
    @RequestMapping(value = "/borrow/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookBorrowList(HttpServletRequest request) throws Exception {
        String bookId = request.getParameter("bookId");
        List<Reservation> res = comService.getList(Reservation.class, "bookId=" + bookId +
                " and status='" + ReservationStatus.RECEDE + "'", "borrowedTime desc");
        List<BookInfoCommentForm> list = new ArrayList<>();
        for (Reservation r : res) {
            list.add(new BookInfoCommentForm(r, cacheManager));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        Reservation reservation = comService.getFirst(Reservation.class, "bookId=" + bookId + " and status='" + ReservationStatus.BORROW + "'");
        if (reservation != null) {
            Client client = comService.getDetail(Client.class, reservation.getClientId());
            map.put("one", new ReservationListWithMobile(reservation, client, cacheManager));
        }
        return Result.SUCCESS(map);
    }

    @ResponseBody
    @RequestMapping(value = "/reserve/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookReserveList(HttpServletRequest request) throws Exception {
        String bookId = request.getParameter("bookId");
//        List<Reservation> res = comService.getList(Reservation.class, "bookId=" + bookId + " and status='" + ReservationStatus.RESERVE + "'", "createTime asc");
        List<Object[]> res = comService.query("select r,c from Reservation r,Client c where r.clientId=c.id and r.bookId=" + bookId + " and r.status='" + ReservationStatus.RESERVE + "'");
        List<ReservationListWithMobile> list = new ArrayList<>();
        for (Object[] o : res) {
            list.add(new ReservationListWithMobile((Reservation) o[0], (Client) o[1], cacheManager));
        }
        return Result.SUCCESS(list);
    }

    @ResponseBody
    @RequestMapping(value = "/wechat/media", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getWeChatMediaDownload(HttpServletRequest request) throws Exception {
        String ids = request.getParameter("ids");
        List<String> pictures = new ArrayList<>();
        List<String> pics = new ArrayList<>();
        if (ids != null && !ids.equals("")) {
            String[] arr = ids.split(",");
            for (String s : arr) {
                String filename = HttpClientUtil.fetch(WeChatMediaUtil.getRequestUrlForDownloadMedia(s), FileType.jpg);
                pictures.add(filename);
                pics.add(Base64Util.img2String(filename));
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pictures", pictures);
        map.put("pics", pics);
        return Result.SUCCESS(map);
    }


}
