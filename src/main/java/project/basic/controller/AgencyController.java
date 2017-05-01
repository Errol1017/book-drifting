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
import project.basic.entity.Agency;
import project.basic.model.AgencyList;
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
@RequestMapping("/stacks")
public class AgencyController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = Lists.AgencyList + "/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getAgencyList(HttpServletRequest request) throws Exception {
        long total = comService.getCount(Agency.class);
        int tarPageNum = Integer.parseInt(request.getParameter("tarPageNum"));
        int perPageNum = Integer.parseInt(request.getParameter("perPageNum"));
        List<Agency> agencies = comService.getList(Agency.class, tarPageNum, perPageNum);
        List<AgencyList> list = new ArrayList<>();
        for (Agency agency: agencies) {
            list.add(new AgencyList(agency));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.SUCCESS(result);
    }

    @RequestMapping(value = Forms.AgencyForm + "/form", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getAgencyForm(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Agency agency = comService.getDetail(Agency.class, Integer.parseInt(dataId));
        return Result.SUCCESS(new AgencyList(agency));
    }

    @RequestMapping(value = Forms.AgencyForm + "/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object submitAgency(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        AgencyList form = DataManager.string2Object(data, AgencyList.class);
        if (form == null) {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
        AdminSession adminSession = AdminValidator.getAdminSession(request);
        if (form.getId().equals("")) {
            if (comService.hasExist(Agency.class, "name='" + form.getName() + "'")) {
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "单位名已存在");
            }
            Agency agency = new Agency(form);
            comService.saveDetail(agency);
            cacheManager.resetAgencyCache();
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Agency, OperationTypes.Create, String.valueOf(agency.getId()), "单位名称： "+agency.getName()));
            return Result.SUCCESS(agency.getId());
        } else {
            if (comService.hasExist(Agency.class, "name='" + form.getName() + "' and id!="+Integer.parseInt(form.getId()))) {
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "单位名已存在");
            }
            Agency agency = new Agency(form);
            agency.setId(Integer.parseInt(form.getId()));
            comService.saveDetail(agency);
            cacheManager.resetAgencyCache();
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Agency, OperationTypes.Update, String.valueOf(agency.getId()), "单位名称： "+agency.getName()));
            return Result.SUCCESS();
        }
    }

    @RequestMapping(value = Lists.AgencyList + "/delete", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object deleteAgency(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Agency agency = comService.getDetail(Agency.class, Integer.parseInt(dataId));
        comService.deleteDetail(agency);
        cacheManager.resetAgencyCache();
        AdminSession adminSession = AdminValidator.getAdminSession(request);
        comService.saveDetail(new AdminLog(adminSession, OperationTargets.Agency, OperationTypes.Delete, String.valueOf(agency.getId()), "单位名称： "+agency.getName()));
        return Result.SUCCESS();
    }

}
