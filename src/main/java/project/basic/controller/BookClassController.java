package project.basic.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.basic.entity.BookClassification;
import project.basic.model.ClassList;
import project.navigator.route.Forms;
import project.navigator.route.Lists;
import project.navigator.service.CacheManager;
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
 * Created by Errol on 17/5/1.
 */
@Controller
@RequestMapping("/class")
public class BookClassController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = Lists.ClassList + "/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getAgencyList(HttpServletRequest request) throws Exception {
        long total = comService.getCount(BookClassification.class);
        int tarPageNum = Integer.parseInt(request.getParameter("tarPageNum"));
        int perPageNum = Integer.parseInt(request.getParameter("perPageNum"));
        List<BookClassification> bcs = comService.getList(BookClassification.class, tarPageNum, perPageNum);
        List<ClassList> list = new ArrayList<>();
        for (BookClassification bc: bcs) {
            list.add(new ClassList(bc));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.SUCCESS(result);
    }

    @RequestMapping(value = Forms.ClassForm + "/form", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getAgencyForm(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        BookClassification bc = comService.getDetail(BookClassification.class, Integer.parseInt(dataId));
        return Result.SUCCESS(new ClassList(bc));
    }

    @RequestMapping(value = Forms.ClassForm + "/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object submitAgency(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        ClassList form = DataManager.string2Object(data, ClassList.class);
        if (form == null) {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
        AdminSession adminSession = AdminValidator.getAdminSession(request);
        if (form.getId().equals("")) {
            if (comService.hasExist(BookClassification.class, "name='" + form.getName() + "'")) {
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "分类名已存在");
            }
            BookClassification bc = new BookClassification(form);
            comService.saveDetail(bc);
            cacheManager.resetBookClassificationCache();
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.BookClassification, OperationTypes.Create, String.valueOf(bc.getId()), "分类名称： "+bc.getName()));
            return Result.SUCCESS(bc.getId());
        } else {
            if (comService.hasExist(BookClassification.class, "name='" + form.getName() + "' and id!="+Integer.parseInt(form.getId()))) {
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "分类名已存在");
            }
            BookClassification bc = new BookClassification(form);
            bc.setId(Integer.parseInt(form.getId()));
            comService.saveDetail(bc);
            cacheManager.resetBookClassificationCache();
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.BookClassification, OperationTypes.Update, String.valueOf(bc.getId()), "分类名称： "+bc.getName()));
            return Result.SUCCESS();
        }
    }

    @RequestMapping(value = Lists.ClassList + "/delete", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object deleteAgency(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        BookClassification bc = comService.getDetail(BookClassification.class, Integer.parseInt(dataId));
        comService.deleteDetail(bc);
        cacheManager.resetBookClassificationCache();
        AdminSession adminSession = AdminValidator.getAdminSession(request);
        comService.saveDetail(new AdminLog(adminSession, OperationTargets.BookClassification, OperationTypes.Delete, String.valueOf(bc.getId()), "分类名称： "+bc.getName()));
        return Result.SUCCESS();
    }

}
