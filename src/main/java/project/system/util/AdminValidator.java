package project.system.util;

import common.DataFormatter.ErrorCode;
import project.navigator.route.Types;
import project.system.model.AdminSession;
import common.DataFormatter.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Errol on 2016/10/2.
 */
public class AdminValidator {

    public static Result AdminValidator(HttpServletRequest request) {
        AdminSession adminSession = (AdminSession) request.getSession().getAttribute("admin");
        if (adminSession == null) {
            return Result.ERROR(-2);
        } else {
            String reqId = request.getParameter("reqId");
            String type = Types.getType(request.getParameter("type"));
            if (type == null) {
                return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
            }
            ArrayList<String> powerList = adminSession.getPowerList();
            if (powerList.contains(reqId)) {
                return Result.SUCCESS(type);
            } else {
//                return Result.ERROR(-5);
                return Result.ERROR(ErrorCode.INSUFFICIENT_PERMISSION);
            }
        }
    }

    public static AdminSession getAdminSession(HttpServletRequest request) {
        return (AdminSession) request.getSession().getAttribute("admin");
    }

}
