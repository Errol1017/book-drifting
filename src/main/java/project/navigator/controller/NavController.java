package project.navigator.controller;

import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import common.FileProcessor.FileManager;
import common.Util.Base64Util;
import common.Util.ValidateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.model.Navigation;
import project.navigator.route.Components;
import project.navigator.route.Forms;
import project.navigator.route.Lists;
import project.navigator.route.Types;
import project.navigator.service.CacheManager;
import project.resource.properties.ServerProperties;
import project.system.util.AdminValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Errol on 2016/9/28.
 */
@Controller
@RequestMapping("/navigator")
public class NavController {

//    @Value("${project.file.path}")
//    private String fileBasePath;
    @Autowired
    private CacheManager cacheManager;

    @ResponseBody
    @RequestMapping(value = "/validator", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object validate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result result = AdminValidator.AdminValidator(request);
        if (result.getCode() == 0) {
            request.getRequestDispatcher("/navigator/" + result.getData()).forward(request, response);
            return "";
        } else {
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = Types.page, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getPage(HttpServletRequest request) throws Exception {
        String reqId = request.getParameter("reqId");
        try {
            String[] pageNameAndPageId = Navigation.getInstance().getPageNameAndPageId(reqId);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
            File page = new File(request.getSession().getServletContext().getRealPath("/") + "WEB-INF/pages/" + pageNameAndPageId[0] + ".html");
            /**
             * 下面输出： E:\WORKSPACE\books-drifting\target\books-drifting\WEB-INF/pages/Admin.html
             * 放到服务器后可能需要更改？
             */
//            System.out.println(request.getSession().getServletContext().getRealPath("/") + "WEB-INF/pages/" + pageNameAndPageId[0] + ".html");
            Document document = Jsoup.parse(page, "UTF-8", baseUrl);
            Element element = document.getElementById(pageNameAndPageId[1]);
            return Result.SUCCESS(element.toString());
        } catch (Exception e) {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
    }

    @ResponseBody
    @RequestMapping(value = Types.list, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String listId = Lists.getList(request.getParameter("listId"));
        if (listId != null
                && ValidateUtil.checkPositiveNumber(request.getParameter("tarPageNum"))
                && ValidateUtil.checkPositiveNumber(request.getParameter("perPageNum"))) {
            request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + listId + "/list").forward(request, response);
            return "";
        } else {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
    }

    @ResponseBody
    @RequestMapping(value = Types.form, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String formId = Forms.getForm(request.getParameter("formId"));
        if (formId != null
                && ValidateUtil.checkPositiveNumber(request.getParameter("dataId"))) {
            request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + formId + "/form").forward(request, response);
            return "";
        } else {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
    }

    @ResponseBody
    @RequestMapping(value = Types.submit, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object submitForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String formId = Forms.getForm(request.getParameter("formId"));
        if (formId != null) {
            request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + formId + "/submit").forward(request, response);
            return "";
        } else {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
    }

    @ResponseBody
    @RequestMapping(value = Types.delete, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object deleteData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String listId = Lists.getList(request.getParameter("listId"));
        if (listId != null
                && ValidateUtil.checkPositiveNumber(request.getParameter("dataId"))) {
            request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + listId + "/delete").forward(request, response);
            return "";
        } else {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
    }

    @ResponseBody
    @RequestMapping(value = Types.data, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String componentId = Components.getComponent(request.getParameter("compId"));
        if (componentId != null) {
            request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + componentId + "/data").forward(request, response);
            return "";
        } else {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
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

    @ResponseBody
    @RequestMapping(value = Types.handle, method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reqId = request.getParameter("reqId");
        String handle = request.getParameter("handle");
        String componentId = Components.getComponent(request.getParameter("compId"));
        if (componentId != null) {
            request.getRequestDispatcher("/" + reqId.toLowerCase() + "/" + componentId + "/" + handle).forward(request, response);
            return "";
        } else {
            return Result.ERROR(ErrorCode.ILLEGAL_OPERATION);
        }
    }

    @ResponseBody
    @RequestMapping(value = Types.image, method = RequestMethod.POST, produces = "application/json;chasrset=utf-8")
    public Object getImage(HttpServletRequest request) throws Exception {
        String imgs = request.getParameter("imgs");
        List<String> res = new ArrayList<>();
        for (String s: imgs.split(",")) {
            res.add(Base64Util.img2String(ServerProperties.getInstance().getFileBasePath(), s));
        }
        return Result.SUCCESS(res);
    }

    @RequestMapping(value = "/download", produces = "text/html;charset=utf-8")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String key = request.getParameter("key");
        if (key != null && !key.equals("")) {
            String filename = cacheManager.getElement(key);
            if (filename != null) {
                cacheManager.removeElement(key);
                FileManager.output(filename, response);
                FileManager.deleteFile(filename);
            }
        }
    }

}
