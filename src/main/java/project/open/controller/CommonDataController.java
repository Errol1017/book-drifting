package project.open.controller;

import common.DataFormatter.Result;
import common.ServerAdvice.util.LogUtil;
import common.Util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.service.CacheManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Errol on 17/5/3.
 */
@Controller
@RequestMapping("/public/common")
public class CommonDataController {

    @Autowired
    private CacheManager cacheManager;

    @ResponseBody
    @RequestMapping(value = "/agency/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getAgencySelect() throws Exception {
        return Result.SUCCESS(cacheManager.getPublicAgencySelect());
    }

    @ResponseBody
    @RequestMapping(value = "/book/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookSelect() throws Exception {
        return Result.SUCCESS(cacheManager.getPublicBookClassificationSelect());
    }

    @ResponseBody
    @RequestMapping(value = "/book/class", method = RequestMethod.POST, produces = "application/json;cahrset=utf-8")
    public Object getBookSelectMultiple() throws Exception {
        return Result.SUCCESS(cacheManager.getPublicBookClassificationSelectMultiple());
    }

    @ResponseBody
    @RequestMapping(value = "/book/data", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getBookDataBase64(HttpServletRequest request) throws Exception {
        String ids = request.getParameter("ids");
        List<String> data = new ArrayList<>();
        if (ids != null && !ids.equals("")) {
            String[] arr = ids.split(",");
            for (String s : arr) {
                data.add(Base64Util.img2String(s));
            }
        }
        return Result.SUCCESS(data);
    }

}
