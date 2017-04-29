package project.basic.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.basic.entity.InvitationCode;
import project.basic.model.InvitationList;
import project.navigator.route.Components;
import project.navigator.route.Lists;
import project.navigator.service.CacheManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Errol on 17/4/26.
 */
@Controller
@RequestMapping("/invitation")
public class InvitationController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = Lists.InvitationList + "/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getInvitationList(HttpServletRequest request) throws Exception {
//        comService.saveDetail(new Client());
        System.out.println(request.getParameter("query"));

        long total = comService.getCount(InvitationCode.class);
        int tarPageNum = Integer.parseInt(request.getParameter("tarPageNum"));
        int perPageNum = Integer.parseInt(request.getParameter("perPageNum"));
        List<Object[]> os = comService.query("select i.code,c.name,c.mobile,c.agencyId from InvitationCode i left join Client c on i.clientId=c.id", tarPageNum, perPageNum);
        List<InvitationList> list = new ArrayList<>();
        for (Object[] o: os) {
            list.add(new InvitationList(o, cacheManager));
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.SUCCESS(result);
    }

    @RequestMapping(value = Components.Invitation_Query_status + "/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getStatusData() {
        return Result.SUCCESS(cacheManager.getBookClassificationSelect());
    }

}
