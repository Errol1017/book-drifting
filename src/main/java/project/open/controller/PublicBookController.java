package project.open.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.Result;
import common.Util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.service.CacheManager;
import project.open.model.BookList;
import project.open.util.ClientValidator;
import project.operation.entity.Book;
import project.operation.entity.Client;
import project.operation.model.ClientCache;
import project.operation.pojo.BookStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getBookList(HttpServletRequest request) throws Exception {
        String p = request.getParameter("page");
        String q1 = request.getParameter("q1");
        String q2 = request.getParameter("q2");
        String q3 = request.getParameter("q3");
        String s = request.getParameter("search");
        StringBuffer sb = new StringBuffer();
        if (q1 != null && q1.equals("1")) {
            ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
            sb.append("createTime>'"+cc.getLoginTime()+"'");
        }
        if (q2 !=null && (q2.equals("1") || q2.equals("2"))) {
            sb.append((sb.length()==0?"":" and ")+("status='" + (q2.equals("1")? BookStatus.IN_STOCK:BookStatus.BORROWED)+"'"));
        }
//        if (q3 !=null &&(q3.equals("1")||q3.equals("2"))){
//            sb.append(" ")
//        }
        if (s !=null && !s.equals("")){
            s=s.replace("《","");
            String[] a = s.split("》 ");
            if (a.length == 1){
                sb = new StringBuffer("name like '%"+a[0]+"%' or author like '%"+a[0]+"%'");
            }else {
                sb = new StringBuffer("name like '%"+a[0]+"%' or author like '%"+a[1]+"%'");
            }
        }
        List<Book> books = comService.getList(Book.class, p==null?1:Integer.parseInt(p), 10, sb.toString(),"id desc");
        List<BookList> list = new ArrayList<>();
        for (Book book: books) {
            list.add(new BookList(book, comService));
        }
        return Result.SUCCESS(list);
    }

    @RequestMapping(value = "/match", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getBookMatch(HttpServletRequest request) throws Exception {
        String m = request.getParameter("m");
        m = m.replace(" ", "");
        return Result.SUCCESS(cacheManager.getBookMatch(m));
    }

    @RequestMapping(value = "/time", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object resetLoginTime(HttpServletRequest request) throws Exception {
        ClientCache cc = ClientValidator.getClientCache(request, cacheManager);
        cc.setLoginTime(DateUtil.date2String(new Date(), DateUtil.PATTERN_A));
        comService.updateDetail(Client.class, "openId='" + cc.getOpenId() + "'", "loginTime='" + DateUtil.date2String(new Date(), DateUtil.PATTERN_A) + "'");
        return Result.SUCCESS();
    }

}
