package project.open.service;

import common.DataFormatter.Result;
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
        Result result = ClientValidator.ClientValidate(request, cacheManager);
        if (result.getCode() == 0) {
            return true;
        } else {
            request.getRequestDispatcher("/public/user/check/result").forward(request, response);
            return false;
        }
    }

}