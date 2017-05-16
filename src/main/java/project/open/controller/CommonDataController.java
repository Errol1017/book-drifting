package project.open.controller;

import common.DataFormatter.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.navigator.service.CacheManager;

/**
 * Created by Errol on 17/5/3.
 */
@Controller
@RequestMapping("/public/common")
public class CommonDataController {

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = "/agency/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getAgencySelect() throws Exception {
        return Result.SUCCESS(cacheManager.getPublicAgencySelect());
    }

    @RequestMapping(value = "/book/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody Object getBookSelect() throws Exception {
        return Result.SUCCESS(cacheManager.getPublicBookClassificationSelect());
    }

    @RequestMapping(value = "/book/class", method = RequestMethod.POST, produces = "application/json;cahrset=utf-8")
    public @ResponseBody Object getBookSelectMultiple() throws Exception{
        return Result.SUCCESS(cacheManager.getPublicBookClassificationSelectMultiple());
    }


}
