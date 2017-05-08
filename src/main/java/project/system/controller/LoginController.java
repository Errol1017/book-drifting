package project.system.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.model.Navigation;
import project.system.entity.Admin;
import project.system.entity.AdminRole;
import project.system.model.AdminSession;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Errol on 2016/9/27.
 */
@Controller
public class LoginController {

    @Autowired
    private ComService comService;

    @RequestMapping(value = "/admin/login", produces = "application/json;charset=UTF-8")
    public @ResponseBody Object login(HttpServletRequest request) throws Exception {
        String adminName = request.getParameter("adminName");
        String password = request.getParameter("password");
        Admin admin = comService.getFirst(Admin.class, "adminName='" + adminName + "'");
        if (admin != null && admin.checkPassword(password)) {
            AdminRole adminRole = comService.getDetail(AdminRole.class, admin.getRoleId());
            if (adminRole != null) {
                ArrayList<String> powerList = Navigation.getInstance().getPowerList(adminRole.getPower());
                request.getSession().setAttribute("admin", new AdminSession(admin, powerList));
                return Result.SUCCESS();
            }
        }
        return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR);
    }

    @RequestMapping(value = "/dashboard", produces = "text/html;charset=UTF-8")
    public String dashboard() {
        return "dashboard.html";
    }

    @RequestMapping(value = "/admin/initialization", produces = "application/json;charset=UTF-8")
    public @ResponseBody Object initialization(HttpServletRequest request) throws Exception {
        AdminSession adminSession = (AdminSession) request.getSession().getAttribute("admin");
        if (adminSession == null) {
            return Result.ERROR(ErrorCode.LOGIN_TIMEOUT);
        }
        HashMap<String, Object> res = new HashMap<>();
        res.put("adminName", adminSession.getName());
        res.put("sidebarList", Navigation.getInstance().getSidebarList(adminSession.getPowerList()));
        return Result.SUCCESS(res);
    }

    @RequestMapping(value = "/admin/login_out", produces = "text/html;charset=UTF-8")
    public String loginOut(HttpServletRequest request) throws Exception {
        request.getSession().invalidate();
        return "redirect:/login.html";
    }

}
