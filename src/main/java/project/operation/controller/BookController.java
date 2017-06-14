package project.operation.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.route.Components;
import project.navigator.route.Forms;
import project.navigator.route.Lists;
import project.navigator.service.CacheManager;
import project.operation.entity.Book;
import project.operation.model.BookForm;
import project.operation.model.BookList;
import project.operation.pojo.BookStatus;
import project.operation.pojo.OwnerType;
import project.system.entity.AdminLog;
import project.system.model.AdminSession;
import project.system.pojo.OperationTargets;
import project.system.pojo.OperationTypes;
import project.system.util.AdminValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Errol on 17/5/2.
 */
@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @ResponseBody
    @RequestMapping(value = Lists.BookList + "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getBookList(HttpServletRequest request) throws Exception {
        int tarPageNum = Integer.parseInt(request.getParameter("tarPageNum"));
        int perPageNum = Integer.parseInt(request.getParameter("perPageNum"));
        Map<String, String> map = DataManager.string2Map(request.getParameter("query"));
        StringBuffer con = new StringBuffer();
        if (map != null) {
            String status = map.get("status");
            String manager = map.get("manager");
            String agency = map.get("agency");
            if (!status.equals("")) {
                con.append("status='"+BookStatus.valueOf(status)+"'");
            }
            if (!manager.equals("")){
                con.append((con.length()==0?"":" and ")+"stackType='"+ (manager.equals("1")?OwnerType.AGENCY:OwnerType.INDIVIDUAL)+"'");
            }
            if (!agency.equals("")){
                con.append((con.length()==0?"":" and ")+"stackId="+agency);
            }
        }
        List<Book> books = comService.getList(Book.class, tarPageNum, perPageNum, con.toString(), "id desc");
        List<BookList> list = new ArrayList<>();
        for (Book book : books) {
            list.add(new BookList(book, cacheManager));
        }
        long total = comService.getCount(Book.class, con.toString());
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.SUCCESS(result);
    }

    @ResponseBody
    @RequestMapping(value = Forms.BookForm + "/form", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getBookForm(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Book book = comService.getDetail(Book.class, Long.parseLong(dataId));
        BookForm form = new BookForm(book, cacheManager);
        return Result.SUCCESS(form);
    }

    @ResponseBody
    @RequestMapping(value = Forms.BookForm + "/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object submitBookForm(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        BookForm form = DataManager.string2Object(data, BookForm.class);
        if (form == null) {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
        AdminSession adminSession = AdminValidator.getAdminSession(request);
        if (form.getId().equals("")) { //新增，暂未开放
//            Book book = new Book(form);
//            comService.saveDetail(book);
//            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Book, OperationTypes.Create, String.valueOf(book.getId()), "书名： " + book.getName()));
//            return Result.SUCCESS(book.getId());
            return Result.SUCCESS();
        } else { //编辑
            Book book = comService.getDetail(Book.class, Long.parseLong(form.getId()));
            book.modify(form);
//            book.setId(Long.parseLong(form.getId()));
            comService.saveDetail(book);
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Book, OperationTypes.Update, String.valueOf(book.getId()), "书名： " + book.getName()));
            return Result.SUCCESS();
        }
    }

    @ResponseBody
    @RequestMapping(value = Lists.BookList + "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object deleteBookList(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Book book = comService.getDetail(Book.class, Long.parseLong(dataId));
        if (!book.getStatus().equals(BookStatus.FROZEN)) {
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "图书尚未出库，无法删除");
        }
        comService.deleteDetail(book);
        comService.saveDetail(new AdminLog(AdminValidator.getAdminSession(request), OperationTargets.Book, OperationTypes.Delete, String.valueOf(book.getId()), "书名： " + book.getName()));
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = Components.Books_Query_agency + "/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getAgencyData() throws Exception {
        return Result.SUCCESS(cacheManager.getAgencySelect());
    }

    @ResponseBody
    @RequestMapping(value = Components.Books_Query_status + "/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookStatusData() throws Exception {
        return Result.SUCCESS(BookStatus.getBookStatusSelect());
    }

    @ResponseBody
    @RequestMapping(value = Components.BookForm_bookClass + "/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookClassData() throws Exception {
        return Result.SUCCESS(cacheManager.getBookClassificationSelect());
    }

}
