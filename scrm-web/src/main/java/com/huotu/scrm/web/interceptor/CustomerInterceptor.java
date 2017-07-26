package com.huotu.scrm.web.interceptor;

import com.huotu.scrm.common.SysConstant;
import com.huotu.scrm.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 嵌入伙伴商城的页面获取customerId
 * Created by helloztt on 2016/7/4.
 */
@Component
public class CustomerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Environment environment;
    private static final String CUSTOMER_ID = "customerId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer customerId, loginType;
        String funcAuthorize, requestCustomerId = null;
        //遍历 parameter keySet,找到 customerId
        Enumeration<String> paramKeys = request.getParameterNames();
        while (paramKeys.hasMoreElements()) {
            String paramKey = paramKeys.nextElement();
            if (CUSTOMER_ID.equalsIgnoreCase(paramKey)) {
                requestCustomerId = request.getParameter(paramKey);
                break;
            }
        }
        if (StringUtils.isEmpty(requestCustomerId)) {
            customerId = CookieUtils.getCookieValInteger(request, "UserID");
            if (customerId <= 1 && environment.acceptsProfiles("!container")) {
                customerId = 4421;
            }
            if (customerId == 0) {
                response.sendRedirect("http://login." + SysConstant.COOKIE_DOMAIN);
                return false;
            }
        } else {
            customerId = Integer.valueOf(requestCustomerId);
        }
        //用户登录类型：0:超级管理员;1:商家总账号;3:操作员
        loginType = CookieUtils.getCookieValInteger(request, "LoginType");
        if (environment.acceptsProfiles("development")) {
            loginType = 1;
        }
        funcAuthorize = CookieUtils.getCookieVal(request, "MM_FuncAuthorize");
        request.setAttribute("customerId", customerId);
        request.setAttribute("loginType", loginType);
        request.setAttribute("funAuthorize", funcAuthorize);
        return true;
    }


}
