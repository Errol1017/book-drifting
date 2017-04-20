package project.navigator.controller;

import common.DataFormatter.ErrorCode;
import project.system.util.AdminValidator;
import common.DataFormatter.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.pojo.Forms;
import project.navigator.pojo.Lists;
import project.navigator.model.Navigation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Created by Errol on 2016/9/28.
 */
@Controller
@RequestMapping("/navigator")
public class NavController {

    @Value("${project.file.path}")
    private String fileBasePath;

    @RequestMapping(value = "/validator", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object validate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result result = AdminValidator.AdminValidator(request);
        if (result.getCode() == 0) {
            request.getRequestDispatcher("/navigator/" + result.getData()).forward(request, response);
            return "";
        } else {
            return result;
        }
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getPage(HttpServletRequest request) throws Exception {
        String reqId = request.getParameter("reqId");
        try {
            String[] pageNameAndPageId = Navigation.getInstance().getPageNameAndPageId(reqId);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
            File detail = new File(request.getSession().getServletContext().getRealPath("/") + "WEB-INF/pages/" + pageNameAndPageId[0] + ".html");
            Document document = Jsoup.parse(detail, "UTF-8", baseUrl);
            Element element = document.getElementById(pageNameAndPageId[1]);
            return Result.SUCCESS(element.toString());
        }catch (Exception e) {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
    }

//    @RequestMapping(value = "/data", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//    public @ResponseBody Object getData(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String reqId = request.getParameter("reqId");
//        request.getRequestDispatcher("/" + reqId.toLowerCase() + "/data").forward(request, response);
//        return "";
//    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String listId = request.getParameter("listId");
        try {
            Lists.valueOf(listId);
            int tarPageNum =Integer.parseInt(request.getParameter("tarPageNum"));
            int perPageNum = Integer.parseInt(request.getParameter("perPageNum"));
            if (tarPageNum < 1 || perPageNum < 1) {
                return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
            }
        }catch (Exception e){
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
        request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + listId.toLowerCase() + "/list").forward(request, response);
        return "";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object getForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String formId = request.getParameter("formId");
        try{
            Forms.valueOf(formId);
            String dataId = request.getParameter("dataId");
            if (dataId.length() < 10){
                Integer.parseInt(dataId);
            }else {
                Long.parseLong(dataId);
            }
        }catch (Exception e){
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
        request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + formId.toLowerCase() + "/form").forward(request, response);
        return "";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object submitForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String formId = request.getParameter("formId");
        try{
            Forms.valueOf(formId);
        }catch (Exception e){
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
        request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + formId.toLowerCase() + "/submit").forward(request, response);
        return "";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody Object deleteData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String listId = request.getParameter("listId");
        try {
            Lists.valueOf(listId);
            String dataId = request.getParameter("dataId");
            if (dataId.length() < 10){
                Integer.parseInt(dataId);
            }else {
                Long.parseLong(dataId);
            }
        }catch (Exception e) {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
        request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + listId.toLowerCase() + "/delete").forward(request, response);
        return "";
    }

//    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//    public @ResponseBody Object getSelectData(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String reqId = request.getParameter("reqId");
//        try {
//            Selects.valueOf(request.getParameter("key"));
//        }catch (Exception e) {
//            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
//        }
//        request.getRequestDispatcher("/" + reqId.toLowerCase() + "/select").forward(request, response);
//        return "";
//    }

//    @RequestMapping(value = "/uploader", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//    public void uploadFiles(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        request.setAttribute("type", "upload");
//        Result result = AdminValidator.AdminValidator(request);
//        if (result.getCode() == 0) {
//            List<String> fileNames = uploadManager.uploadFiles(fileBasePath, request);
//            if (fileNames.get(0).equals("-5")){
//                request.setAttribute("code", fileNames.get(0));
//                request.setAttribute("message", fileNames.get(1));
//            }else {
//                String names = new String();
//                if (fileNames.size() > 1){
//                    for (String name:fileNames){
//                        names += name + ",";
//                    }
//                    names = names.substring(0, names.length()-1);
//                }else {
//                    names = fileNames.get(0);
//                }
//                request.setAttribute("fileNames", names);
//            }
//        }else {
//            request.setAttribute("code", -5);
//        }
//        request.getRequestDispatcher("/com-res/uploader/uploader.jsp").forward(request, response);
//    }


}
