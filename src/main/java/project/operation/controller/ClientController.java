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
import project.operation.entity.Client;
import project.operation.model.ClientForm;
import project.operation.model.ClientList;
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
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @ResponseBody
    @RequestMapping(value = Lists.ClientList + "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getClientList(HttpServletRequest request) throws Exception {
        int tarPageNum = Integer.parseInt(request.getParameter("tarPageNum"));
        int perPageNum = Integer.parseInt(request.getParameter("perPageNum"));
        Map<String, String> map = DataManager.string2Map(request.getParameter("query"));
        StringBuffer con = new StringBuffer();
        if (map != null) {
            String isAdmin = map.get("isAdmin");
            String agency = map.get("agency");
            if (!isAdmin.equals("")) {
                con.append("isAdmin="+(isAdmin.equals("1")?1:0));
            }
            if (!agency.equals("")){
                con.append((con.length()==0?"":" and ")+"agencyId="+agency);
            }
        }
        List<Client> clients = comService.getList(Client.class, tarPageNum, perPageNum, con.toString(), "id desc");
        List<ClientList> list = new ArrayList<>();
        for (Client client : clients) {
            list.add(new ClientList(client, cacheManager));
        }
        long total = comService.getCount(Client.class, con.toString());
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.SUCCESS(result);
    }

    @ResponseBody
    @RequestMapping(value = Forms.ClientForm + "/form", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getClientForm(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Client client = comService.getDetail(Client.class, Long.parseLong(dataId));
        ClientForm form = new ClientForm(client);
        return Result.SUCCESS(form);
    }

    @ResponseBody
    @RequestMapping(value = Forms.ClientForm + "/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object submitClientForm(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        ClientForm form = DataManager.string2Object(data, ClientForm.class);
        if (form == null || form.getIdentityNumber().isEmpty() || form.getMobile().isEmpty()) {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
        AdminSession adminSession = AdminValidator.getAdminSession(request);
        if (form.getId().equals("")) {
//            if (comService.hasExist(Client.class, "identityNumber='" + form.getIdentityNumber() + "'")) {
//                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "用户身份证号已存在");
//            }
//            Client client = new Client(form);
//            comService.saveDetail(client);
//            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Client, OperationTypes.Create, String.valueOf(client.getId()), "用户姓名： " + client.getName()));
//            return Result.SUCCESS(client.getId());
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "为避免脏数据，管理系统不允许添加用户");
        } else {
            if (comService.hasExist(Client.class, "identityNumber='" + form.getIdentityNumber() + "' and id!=" + Long.parseLong(form.getId()))) {
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "用户身份证号已存在");
            }
            Client client = new Client(form);
            client.setId(Long.parseLong(form.getId()));
            comService.saveDetail(client);
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Client, OperationTypes.Update, String.valueOf(client.getId()), "用户姓名： " + client.getName()));
            return Result.SUCCESS();
        }
    }

    @ResponseBody
    @RequestMapping(value = Lists.ClientList + "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object deleteClientList(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Client client = comService.getDetail(Client.class, Long.parseLong(dataId));
        comService.deleteDetail(client);
        comService.saveDetail(new AdminLog(AdminValidator.getAdminSession(request), OperationTargets.Client, OperationTypes.Delete, String.valueOf(client.getId()), "用户姓名： " + client.getName()));
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = Components.ClientList_appoint + "/appoint", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object submitClientAppoint(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        Client client = comService.getDetail(Client.class, Long.parseLong(id));
        if (client.isAdmin()) {
            client.setAdmin(false);
        } else {
            client.setAdmin(true);
        }
        comService.saveDetail(client);
        return Result.SUCCESS();
    }


    @ResponseBody
    @RequestMapping(value = Components.ClientForm_agencyId + "/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getAgencyData() {
        return Result.SUCCESS(cacheManager.getAgencySelect());
    }
}
