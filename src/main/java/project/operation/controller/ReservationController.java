package project.operation.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.DataManager;
import common.DataFormatter.Result;
import common.Util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.route.Components;
import project.navigator.route.Forms;
import project.navigator.route.Lists;
import project.navigator.service.CacheManager;
import project.operation.entity.Reservation;
import project.operation.model.ReservationForm;
import project.operation.model.ReservationList;
import project.operation.pojo.ReservationStatus;
import project.system.entity.AdminLog;
import project.system.pojo.OperationTargets;
import project.system.pojo.OperationTypes;
import project.system.util.AdminValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Errol on 17/6/2.
 */
@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @ResponseBody
    @RequestMapping(value = Lists.ReservationList + "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getReservationList(HttpServletRequest request) throws Exception {
        int tarPageNum = Integer.parseInt(request.getParameter("tarPageNum"));
        int perPageNum = Integer.parseInt(request.getParameter("perPageNum"));
        Map<String, String> map = DataManager.string2Map(request.getParameter("query"));
        StringBuffer con = new StringBuffer();
        if (map != null) {
            String status = map.get("status");
            String start = DateUtil.stamp2String(String.valueOf(map.get("start")), DateUtil.PATTERN_C);
            String end = DateUtil.stamp2String(String.valueOf(map.get("end")), DateUtil.PATTERN_C);
            if (!status.equals("")) {
                if (status.equals("expire")) {
                    con.append("status='" + ReservationStatus.BORROW + "' and expireTime<'" + DateUtil.date2String(new Date(), DateUtil.PATTERN_A) + "'");
                } else {
                    con.append("status='" + ReservationStatus.valueOf(status) + "'");
                }
            }
            if (start != null) {
                con.append((con.length() == 0 ? "" : " and ") + "createTime>'" + start + "'");
            }
            if (end != null) {
                con.append((con.length() == 0 ? "" : " and ") + "createTime<='" + end + " 23:59:59'");
            }
        }
        List<Reservation> reservations = comService.getList(Reservation.class, tarPageNum, perPageNum, con.toString(), "id desc");
        List<ReservationList> list = new ArrayList<>();
        for (Reservation reservation : reservations) {
            list.add(new ReservationList(reservation, cacheManager));
        }
        long total = comService.getCount(Reservation.class, con.toString());
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.SUCCESS(result);
    }

    @ResponseBody
    @RequestMapping(value = Forms.ReservationForm + "/form", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getReservationForm(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Reservation reservation = comService.getDetail(Reservation.class, Long.parseLong(dataId));
        ReservationForm form = new ReservationForm(reservation, cacheManager);
        return Result.SUCCESS(form);
    }

//    @ResponseBody
//    @RequestMapping(value = Forms.ReservationForm + "/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//    public Object submitReservationForm(HttpServletRequest request) throws Exception {
//        String data = request.getParameter("data");
//        ReservationForm form = DataManager.string2Object(data, ReservationForm.class);
//        if (form == null) {
//            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
//        }
//        AdminSession adminSession = AdminValidator.getAdminSession(request);
//        if (form.getId().equals("")) { //新增
//            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
//        } else { //编辑
//            Reservation reservation = comService.getDetail(Reservation.class, Long.parseLong(form.getId()));
//            reservation.modify(form);
//            comService.saveDetail(reservation);
//            comService.saveDetail(new AdminLog(adminSession, OperationTargets.Reservation, OperationTypes.Update, String.valueOf(reservation.getId()), "书名： " + reservation.getName()));
//            return Result.SUCCESS();
//        }
//    }

    @ResponseBody
    @RequestMapping(value = Lists.ReservationList + "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object deleteReservationList(HttpServletRequest request) throws Exception {
        String dataId = request.getParameter("dataId");
        Reservation reservation = comService.getDetail(Reservation.class, Long.parseLong(dataId));
        comService.deleteDetail(reservation);
        comService.saveDetail(new AdminLog(AdminValidator.getAdminSession(request), OperationTargets.Reservation, OperationTypes.Delete, String.valueOf(reservation.getId()),
                "用户 " + cacheManager.getClientCache(reservation.getClientId()).getName() + " 对图书  的借阅申请"));
        return Result.SUCCESS();
    }

    @ResponseBody
    @RequestMapping(value = Components.Reservation_Query_status + "/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getReservationStatusData() throws Exception {
        return Result.SUCCESS(ReservationStatus.getBookReservationStatusSelect());
    }

//    @ResponseBody
//    @RequestMapping(value = Components.Reservations_Query_status + "/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    public Object getReservationStatusData() throws Exception {
//        return Result.SUCCESS(ReservationStatus.getReservationStatusSelect());
//    }
//
//    @ResponseBody
//    @RequestMapping(value = Components.ReservationForm_ReservationClass + "/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    public Object getReservationClassData() throws Exception {
//        return Result.SUCCESS(cacheManager.getReservationClassificationSelect());
//    }


}
