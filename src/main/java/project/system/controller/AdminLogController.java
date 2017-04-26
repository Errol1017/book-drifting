package project.system.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.Result;
import common.Util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.route.Lists;
import project.system.entity.AdminLog;
import project.system.model.AdminLogList;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
        String con = "";
        Map map = DataManager.string2Map(request.getParameter("query"));
        if (map != null) {
            String start = DateUtil.stamp2String(String.valueOf(map.get("start")), DateUtil.PATTERN_C);
            String end = DateUtil.stamp2String(String.valueOf(map.get("end")), DateUtil.PATTERN_C);
            if (start != null) {
                con += "createTime>'" + start + "'";
            }
            if (end != null) {
                con += (con.equals("")?"":" and ") + "createTime<='" + end + " 23:59:59'";
            }
        }

        long total = comService.getCount(AdminLog.class, con);
        String tarPageNum = request.getParameter("tarPageNum");
        String perPageNum = request.getParameter("perPageNum");
        List<AdminLog> adminLogs = comService.getList(AdminLog.class, Integer.parseInt(tarPageNum), Integer.parseInt(perPageNum), con, "createTime desc");
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
