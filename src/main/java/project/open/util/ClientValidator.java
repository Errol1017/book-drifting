package project.open.util;

import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import project.navigator.service.CacheManager;
import project.operation.model.ClientCache;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Errol on 17/5/6.
 */
public class ClientValidator {

    public static Result ClientValidate(HttpServletRequest request, CacheManager cacheManager) {
        String openId = String.valueOf(request.getSession().getAttribute("openId"));
        if (openId.equals("null")) {
            //需静默授权
            return Result.ERROR(ErrorCode.LOGIN_TIMEOUT);
        } else {
            ClientCache clientCache = cacheManager.getClientCache(openId);
            if (clientCache == null) {
                //需登记核验
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR);
            } else {
                //已核验登记
                return Result.SUCCESS();
            }
        }
    }

    public static ClientCache getClientCache(HttpServletRequest request, CacheManager cacheManager) {
        return cacheManager.getClientCache(String.valueOf(request.getSession().getAttribute("openId")));
    }

    public static String getOpenId(HttpServletRequest request) {
        return String.valueOf(request.getSession().getAttribute("openId"));
    }

}
