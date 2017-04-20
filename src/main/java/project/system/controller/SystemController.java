package project.system.controller;

import project.system.entity.Admin;
import project.system.entity.AdminLog;
import project.system.entity.AdminRole;
import project.system.model.*;
import project.system.util.AdminValidator;
import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.system.pojo.OperationTargets;
import project.system.pojo.OperationTypes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Errol on 2016/10/8.
 */
@Controller
public class SystemController {

    @Autowired
    private ComService comService;

    @RequestMapping(value = "/admin/data", produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getAdminData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/admin/list").forward(request, response);
        return "";
    }



    @RequestMapping(value = "/adminlog/data", produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getAdminLogData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/adminlog/list").forward(request, response);
        return "";
    }

    @RequestMapping(value = "/adminlog/list", produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getAdminLogList(HttpServletRequest request) throws Exception {
        String pageNum = request.getParameter("pageNum");
        String perPageNum = request.getParameter("perPageNum");
        List<AdminLog> list = comService.getList(AdminLog.class, pageNum==null?1:Integer.parseInt(pageNum), perPageNum==null?15:Integer.parseInt(perPageNum), "createTime desc");
        long total = comService.getCount(AdminLog.class);
        List<AdminLogList> adminLogLists = new ArrayList<>();
        for (AdminLog log: list ){
            adminLogLists.add(new AdminLogList(log));
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("AdminLogList", adminLogLists);
        result.put("AdminLogListTotal", total);
        return Result.SUCCESS(result);
    }


}
