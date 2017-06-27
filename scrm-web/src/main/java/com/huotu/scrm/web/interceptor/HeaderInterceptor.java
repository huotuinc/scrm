package com.huotu.scrm.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 嵌入伙伴商城的页面，头部需要设置为 ALLOW-FROM
 * Created by helloztt on 2016/7/7.
 */
@Component
public class HeaderInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        response.setHeader("X-Frame-Options","ALLOW-FROM");
    }
}
