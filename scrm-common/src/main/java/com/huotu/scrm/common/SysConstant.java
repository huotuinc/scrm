package com.huotu.scrm.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by helloztt on 2017-06-27.
 */
@Component("sysConstant")
public class SysConstant {
    public static String COOKIE_DOMAIN;
    public static String HUOBANMALL_RESOURCE_HOST;
    public static String HUOBANMALL_PUSH_URL;

    @Autowired
    public SysConstant(Environment env){
        COOKIE_DOMAIN = env.getProperty("cookie.domain", ".pdmall.com");
        HUOBANMALL_RESOURCE_HOST = env.getProperty("huobanmall.resourceUrl", "http://res.pdmall.com");
        HUOBANMALL_PUSH_URL = env.getProperty("huobanmall.pushUrl", "http://mallapi.pdmall.com");

    }
}
