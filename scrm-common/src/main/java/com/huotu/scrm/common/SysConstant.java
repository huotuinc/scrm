package com.huotu.scrm.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by helloztt on 2017-06-27.
 */
@Component
public class SysConstant {
    public static String COOKIE_DOMAIN;

    @Autowired
    public SysConstant(Environment env){
        COOKIE_DOMAIN = env.getProperty("cookie.domain", ".pdmall.com");

    }
}
