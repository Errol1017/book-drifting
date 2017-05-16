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
import project.operation.entity.Book;
import project.operation.entity.Stacks;
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
        List<Object[]> os = comService.query("select a.id,a.name,a.code,s.location,s.openTime from Agency a left join Stacks s on a.stackId=s.id", tarPageNum, perPageNum);
        List<AgencyList> list = new ArrayList<>();
        for (Object[] o: os) {
            list.add(new AgencyList(o));
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
        Stacks stacks = comService.getDetail(Stacks.class, agency.getStackId());
        return Result.SUCCESS(new AgencyList(agency, stacks));
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
            Stacks stack = new Stacks(form);
            comService.saveDetail(stack);
            Agency agency = new Agency(form, stack.getId());
            comService.saveDetail(agency);
            cacheManager.resetAgencyCache();
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Agency, OperationTypes.Create, String.valueOf(agency.getId()), "单位名称： "+agency.getName()));
            return Result.SUCCESS(agency.getId());
        } else {
            if (comService.hasExist(Agency.class, "name='" + form.getName() + "' and id!="+Integer.parseInt(form.getId()))) {
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "单位名已存在");
            }
            Agency agency = comService.getDetail(Agency.class, Integer.parseInt(form.getId()));
            agency.setName(form.getName());
            comService.saveDetail(agency);
            Stacks stack = new Stacks(form);
            stack.setId(agency.getStackId());
            comService.saveDetail(stack);
            cacheManager.resetAgencyCache();
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Agency, OperationTypes.Update, String.valueOf(agency.getId()), "单位名称： "+agency.getName()));
            return Result.SUCCESS();
        }
    }

    @RequestMapping(value = Lists.AgencyList + "/delete", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object deleteAgency(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Agency agency = comService.getDetail(Agency.class, Integer.parseInt(dataId));
        Book book = comService.getFirst(Book.class, "stackId=" + agency.getStackId());
        if (book != null) {
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "该机构尚有委托管理的图书未处理，请先转移这批图书！");
        }
        comService.deleteDetail(Stacks.class, "id=" + agency.getStackId());
        comService.deleteDetail(agency);
        cacheManager.resetAgencyCache();
        AdminSession adminSession = AdminValidator.getAdminSession(request);
        comService.saveDetail(new AdminLog(adminSession, OperationTargets.Agency, OperationTypes.Delete, String.valueOf(agency.getId()), "单位名称： "+agency.getName()));
        return Result.SUCCESS();
    }

}
