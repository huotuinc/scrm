package com.huotu.scrm.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商城前端页面获取userId
 * Created by helloztt on 2017-07-07.
 */
@Component
public class UserInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private Environment environment;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO: 2017-07-07 先从cookies 中获取userId,如果未空则调用商城接口获取userId
        return super.preHandle(request, response, handler);
    }
}
