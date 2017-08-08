/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.service.impl;

import com.huotu.scrm.web.service.VerifyService;
import com.huotu.verification.VerificationType;
import com.huotu.verification.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Created by helloztt on 2017-08-08.
 */
@Service
public class VerifyServiceImpl implements VerifyService {
    @Autowired
    private VerificationCodeService verificationCodeService;
    private VerificationType register = new VerificationType() {
        @Override
        public int id() {
            return 0;
        }

        @Override
        public String message(String code) {
            return "注册短信验证码为：" + code + "，如果没有提交请求请忽略";
        }
    };
    @Override
    public Boolean verifyCount(String str, Map<String, Integer> map) {
        if (map.containsKey(str)) {
            int counts = map.get(str);
            if (counts >= 3) {
                //同一个手机号码一天不能发送超过三次
                return false;
            } else {
                map.put(str, counts + 1);
            }
        } else {
            map.put(str, 1);
        }
        return true;
    }

    @Override
    public void sendVerificationCode(String mobile) throws IOException {
        verificationCodeService.sendCode(mobile, register);
    }

    @Override
    public void verifyVerificationCode(String mobile, String code) {
        verificationCodeService.verify(mobile, code, register);
    }
}
