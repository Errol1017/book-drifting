package project.system.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.route.Lists;
import project.system.entity.AdminLog;
import project.system.model.AdminLogList;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Errol on 17/4/17.
 */
@Controller
@RequestMapping("/adminlog")
public class AdminLogController {

    @Autowired
    private ComService comService;

    @RequestMapping(value = Lists.AdminLogList + "/list", produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getAdminLogList(HttpServletRequest request) throws Exception {
        String tarPageNum = request.getParameter("tarPageNum");
        String perPageNum = request.getParameter("perPageNum");
        System.out.println(request.getParameter("query"));
        List<AdminLog> adminLogs = comService.getList(AdminLog.class, Integer.parseInt(tarPageNum), Integer.parseInt(perPageNum), "createTime desc");
        long total = comService.getCount(AdminLog.class);
        List<AdminLogList> list = new ArrayList<>();
        for (AdminLog log: adminLogs ){
            list.add(new AdminLogList(log));
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.SUCCESS(result);
    }

}
