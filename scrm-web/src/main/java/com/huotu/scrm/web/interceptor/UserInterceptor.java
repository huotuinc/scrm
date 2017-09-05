package com.huotu.scrm.web.interceptor;

import com.huotu.scrm.common.utils.CookieUtils;
import com.huotu.scrm.common.utils.EncryptUtils;
import com.huotu.scrm.service.service.api.ApiService;
import com.huotu.scrm.service.service.api.impl.ApiServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商城前端页面获取userId
 * Created by helloztt on 2017-07-07.
 */
@Component
public class UserInterceptor extends HandlerInterceptorAdapter {
    private static final Log log = LogFactory.getLog(ApiServiceImpl.class);
    private static final String CUSTOMER_ID = "customerId";
    public static final String USER_ID_PREFIX = "mem_authcode_";
    public static final String USER_ID_SECRET_KEY = "XjvDhKLvCsm9y7G7";
    @Autowired
    private Environment environment;
    @Autowired
    private ApiService apiService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (environment.acceptsProfiles("development")) {
            //小伙伴，非销售员
//            request.setAttribute("userId", 1058823);
            //小伙伴且是销售员
            request.setAttribute("userId", 1058510);
            //普通会员
//            request.setAttribute("userId",1043079);

            return true;
        }
        //先获取customerId
        String customerId = request.getParameter(CUSTOMER_ID);
        if (StringUtils.isEmpty(customerId)) {
            return false;
        }
        //加密的userId
        String userIdKey = USER_ID_PREFIX + customerId;
        String userIdValue = CookieUtils.getCookieVal(request, userIdKey);
        if (StringUtils.isEmpty(userIdValue)) {
            //调用商城接口
            // TODO: 2017-07-12 等待测试
            String userLoginUrl = apiService.userLogin(Long.valueOf(customerId), request.getRequestURL().toString());
            Map<String, String[]> requestParameterMap =  request.getParameterMap();
            String origin = requestParameterMap.entrySet().stream()
                    .map(pair -> {
                        try {
                            return pair.getKey() + "=" + URLDecoder.decode(pair.getValue()[0], "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw  new IllegalStateException(e);
                        }
                    })
                    .collect(Collectors.joining("&"));

            response.sendRedirect(userLoginUrl+origin);
            return false;
        }
        userIdValue = URLDecoder.decode(userIdValue, "UTF-8");
        log.info("userId:" + userIdValue);
        Long userId = Long.valueOf(EncryptUtils.aesDecrypt(userIdValue, USER_ID_SECRET_KEY));
        request.setAttribute("userId", userId);
        return true;
    }
}
