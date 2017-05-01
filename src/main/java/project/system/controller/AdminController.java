package project.system.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.model.Navigation;
import project.navigator.route.Components;
import project.navigator.route.Forms;
import project.navigator.route.Lists;
import project.system.entity.Admin;
import project.system.entity.AdminLog;
import project.system.entity.AdminRole;
import project.system.model.AdminForm;
import project.system.model.AdminList;
import project.system.model.AdminSession;
import project.system.pojo.OperationTargets;
import project.system.pojo.OperationTypes;
import project.system.util.AdminValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Errol on 17/4/17.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ComService comService;

    @RequestMapping(value = Lists.AdminList + "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getAdminList(HttpServletRequest request) throws Exception {
        int tarPageNum = Integer.parseInt(request.getParameter("tarPageNum"));
        int perPageNum = Integer.parseInt(request.getParameter("perPageNum"));
        List<Admin> admins = comService.getList(Admin.class, tarPageNum, perPageNum);
        List<AdminRole> roles = comService.getList(AdminRole.class);
        HashMap<Integer, AdminRole> roleHashMap = new HashMap<>();
        for (AdminRole role: roles){
            roleHashMap.put(role.getId(), role);
        }
        List<AdminList> list = new ArrayList<>();
        for (Admin admin: admins){
            list.add(new AdminList(admin, roleHashMap.get(admin.getRoleId())));
        }
        long total = comService.getCount(Admin.class);
        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.SUCCESS(result);
    }

    @RequestMapping(value = Forms.AdminForm + "/form", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getAdminForm(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Admin admin = comService.getDetail(Admin.class, Integer.parseInt(dataId));
        AdminRole role = comService.getDetail(AdminRole.class, admin.getRoleId());
        AdminForm form = new AdminForm(admin, role);
        return Result.SUCCESS(form);
    }

    @RequestMapping(value = Forms.AdminForm + "/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object submitAdminForm(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        AdminForm form = DataManager.string2Object(data, AdminForm.class);
        if (form == null || form.getName().isEmpty()) {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
        AdminSession adminSession = AdminValidator.getAdminSession(request);
        if (form.getId().equals("")){
            if (form.getPassword().isEmpty()) {
                return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
            }
            if (comService.hasExist(Admin.class, "adminName='"+form.getName()+"'")){
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "用户名已存在");
            }
            AdminRole role = new AdminRole(form);
            comService.saveDetail(role);
            Admin admin = new Admin(form, role);
            comService.saveDetail(admin);
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Admin, OperationTypes.Create, String.valueOf(admin.getId()), "用户名： "+admin.getAdminName()));
            return Result.SUCCESS(admin.getId());
        }else {
            if (comService.hasExist(Admin.class, "adminName='"+form.getName()+"' and id!="+Integer.parseInt(form.getId()))){
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "用户名已存在");
            }
            Admin admin = comService.getDetail(Admin.class, Integer.parseInt(form.getId()));
            comService.saveDetail(admin.modify(form));
            AdminRole role = comService.getDetail(AdminRole.class, admin.getRoleId());
            comService.saveDetail(role.modify(form));
            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Admin, OperationTypes.Update, String.valueOf(admin.getId()), "用户名： "+admin.getAdminName()));
            return Result.SUCCESS();
        }
    }

    @RequestMapping(value = Lists.AdminList + "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object deleteAdminList(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Admin admin = comService.getDetail(Admin.class, Integer.parseInt(dataId));
        AdminRole role = comService.getDetail(AdminRole.class, admin.getRoleId());
        comService.deleteDetail(admin);
        comService.deleteDetail(role);
        comService.saveDetail(new AdminLog(AdminValidator.getAdminSession(request), OperationTargets.Admin, OperationTypes.Delete, String.valueOf(admin.getId()), "用户名： "+admin.getAdminName()));
        return Result.SUCCESS();
    }

    @RequestMapping(value = Components.AdminForm_power + "/data", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getAdminPower() throws Exception {
        return Result.SUCCESS(Navigation.getInstance().getPowerCascade());
    }
}
