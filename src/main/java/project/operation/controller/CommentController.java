package project.operation.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.Result;
import common.Util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.route.Forms;
import project.navigator.route.Lists;
import project.navigator.service.CacheManager;
import project.operation.entity.Comment;
import project.operation.model.ClientCache;
import project.operation.model.CommentForm;
import project.operation.model.CommentList;
import project.system.entity.AdminLog;
import project.system.pojo.OperationTargets;
import project.system.pojo.OperationTypes;
import project.system.util.AdminValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Errol on 17/6/2.
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @ResponseBody
    @RequestMapping(value = Lists.CommentList + "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getCommentList(HttpServletRequest request) throws Exception {
        int tarPageNum = Integer.parseInt(request.getParameter("tarPageNum"));
        int perPageNum = Integer.parseInt(request.getParameter("perPageNum"));
        Map<String, String> map = DataManager.string2Map(request.getParameter("query"));
        StringBuffer con = new StringBuffer();
        if (map != null) {
//            String name = map.get("bookName");
            String start = DateUtil.stamp2String(String.valueOf(map.get("start")), DateUtil.PATTERN_C);
            String end = DateUtil.stamp2String(String.valueOf(map.get("end")), DateUtil.PATTERN_C);
//            if (!name.equals("")) {
//                con.append("bookId in (" + cacheManager.searchBook(name) + ")");
//            }
            if (start != null) {
                con.append((con.length() == 0 ? "" : " and ") + "createTime>'" + start + "'");
            }
            if (end != null) {
                con.append((con.length() == 0 ? "" : " and ") + "createTime<='" + end + " 23:59:59'");
            }
        }
        List<Comment> comments = comService.getList(Comment.class, tarPageNum, perPageNum, con.toString(), "id desc");
        List<CommentList> list = new ArrayList<>();
        for (Comment comment : comments) {
            list.add(new CommentList(comment, cacheManager));
        }
        long total = comService.getCount(Comment.class, con.toString());
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.SUCCESS(result);
    }

    @ResponseBody
    @RequestMapping(value = Forms.CommentForm + "/form", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getCommentForm(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Comment comment = comService.getDetail(Comment.class, Long.parseLong(dataId));
        CommentForm form = new CommentForm(comment, cacheManager);
        return Result.SUCCESS(form);
    }

//    @ResponseBody
//    @RequestMapping(value = Forms.CommentForm + "/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//    public Object submitCommentForm(HttpServletRequest request) throws Exception {
//        String data = request.getParameter("data");
//        CommentForm form = DataManager.string2Object(data, CommentForm.class);
//        if (form == null) {
//            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
//        }
//        AdminSession adminSession = AdminValidator.getAdminSession(request);
//        if (form.getId().equals("")) { //新增
//            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
//        } else { //编辑
//            Comment comment = comService.getDetail(Comment.class, Long.parseLong(form.getId()));
//            comment.modify(form);
//            comService.saveDetail(comment);
//            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Comment, OperationTypes.Update, String.valueOf(comment.getId()), "书名： " + comment.getName()));
//            return Result.SUCCESS();
//        }
//    }

    @ResponseBody
    @RequestMapping(value = Lists.CommentList + "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object deleteCommentList(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Comment comment = comService.getDetail(Comment.class, Long.parseLong(dataId));
        comService.deleteDetail(comment);
        ClientCache cc = cacheManager.getClientCache(comment.getClientId());
        comService.saveDetail(new AdminLog(AdminValidator.getAdminSession(request), OperationTargets.Comment, OperationTypes.Delete, String.valueOf(comment.getClientId()),
                "用户：" + cc.getNickName() + "（" + cc.getName() + "） 对图书 《" + cacheManager.getBookCache(comment.getBookId()).getName() + "》 的评论"));
        return Result.SUCCESS();
    }
}
