package project.navigator.service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Errol on 17/3/14.
 */
@Component
public class Interceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.print("\r\n-------------------------\r\n");
//        System.out.print(request.getServletPath()+" | "+new Date()+"\r\n\r\n");
        response.setHeader("Access-Control-Allow-Origin", "*");
        return true;
    }
//
//    public boolean postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        system.out.print("\r\n=========================\r\n");
//        system.out.print(request.getServletPath()+" | "+new Date()+"\r\n\r\n");
//        return true;
//    }

}
