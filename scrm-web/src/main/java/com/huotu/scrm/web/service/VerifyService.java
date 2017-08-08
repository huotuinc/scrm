/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.service;

import com.huotu.verification.VerificationType;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

/**
 * 验证码相关
 * Created by helloztt on 2017-08-08.
 */
public interface VerifyService {
    /**
     * 校验手机号码或ip发送短信的次数
     *
     * @param str ip或手机号码
     * @param map map集合，key为ip或手机号，value为发送短信的次数
     * @return true 验证通过，false 失败
     */
    Boolean verifyCount(String str, Map<String, Integer> map);
    /**
     * 发送密码短信验证码
     *
     * @param mobile 手机号码
     * @throws IOException               通道异常
     */
    @Transactional
    void sendVerificationCode(String mobile) throws IOException;

    /**
     * 检查注册所用的手机验证码
     *
     * @param mobile 手机号码
     * @param code   验证码
     * @see com.huotu.verification.service.VerificationCodeService#verify(String, String, VerificationType)
     */
    @Transactional(readOnly = true)
    void verifyVerificationCode(String mobile, String code);
}
