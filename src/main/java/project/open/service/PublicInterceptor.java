package project.open.service;

import common.DataFormatter.Result;
import common.ServerAdvice.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import project.navigator.service.CacheManager;
import project.open.util.ClientValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Errol on 17/5/6.
 */
@Component
public class PublicInterceptor  extends HandlerInterceptorAdapter {

    @Autowired
    private CacheManager cacheManager;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LogUtil.debug("--------------------------------");
        String route = request.getServletPath();
        LogUtil.debug("publicInterceptor拦截的路由：  "+route);
        LogUtil.debug("请求携带的openId：  "+ClientValidator.getOpenId(request));
        Result result = ClientValidator.ClientValidate(request, cacheManager);
        LogUtil.debug("身份检查返回的result的code：  "+result.getCode());
        if (result.getCode() == 0) {
            return true;
        } else {
            request.getRequestDispatcher("/public/user/check/result").forward(request, response);
            return false;
        }
    }
//
//    public boolean postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        system.out.print("\r\n=========================\r\n");
//        system.out.print(request.getServletPath()+" | "+new Date()+"\r\n\r\n");
//        return true;
//    }

}