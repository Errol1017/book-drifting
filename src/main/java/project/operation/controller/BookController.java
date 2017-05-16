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
import project.navigator.route.Forms;
import project.navigator.route.Lists;
import project.navigator.service.CacheManager;
import project.operation.entity.Book;
import project.operation.model.BookForm;
import project.operation.model.BookList;
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
        List<Book> books = comService.getList(Book.class, tarPageNum, perPageNum);
        List<BookList> list = new ArrayList<>();
        for (Book book : books) {
            list.add(new BookList(book, cacheManager));
        }
        long total = comService.getCount(Book.class);
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
        BookForm form = new BookForm(book);
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
        if (form.getId().equals("")) {
//            if (comService.hasExist(Book.class, "identityNumber='"+form.getIdentityNumber()+"'")){
//                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "用户身份证号已存在");
//            }
            Book book = new Book(form);
            comService.saveDetail(book);
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Book, OperationTypes.Create, String.valueOf(book.getId()), "书名： " + book.getName()));
            return Result.SUCCESS(book.getId());
        } else {
//            if (comService.hasExist(Book.class, "identityNumber='"+form.getIdentityNumber()+"' and id!="+Long.parseLong(form.getId()))){
//                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "用户身份证号已存在");
//            }
            Book book = new Book(form);
            book.setId(Long.parseLong(form.getId()));
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
        comService.deleteDetail(book);
        comService.saveDetail(new AdminLog(AdminValidator.getAdminSession(request), OperationTargets.Book, OperationTypes.Delete, String.valueOf(book.getId()), "书名： " + book.getName()));
        return Result.SUCCESS();
    }

}
