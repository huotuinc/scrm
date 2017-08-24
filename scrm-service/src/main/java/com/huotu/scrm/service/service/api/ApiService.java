package com.huotu.scrm.service.service.api;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.common.utils.ApiResult;

import java.io.UnsupportedEncodingException;

/**
 * Created by helloztt on 2017-07-12.
 */
public interface ApiService {
    /**
     * 用户微信授权登录请求地址
     *
     * @param customerId  商户ID
     * @param redirectUrl 执行成功后跳转的ID
     */
    String userLogin(Long customerId, String redirectUrl);

    /**
     * 用户增加积分
     *
     * @param customerId   商户ID
     * @param userId       用户ID
     * @param integral     增加积分
     * @param integralType 积分类型
     * @return 返回结果
     */
    ApiResult rechargePoint(Long customerId, Long userId, Long integral, IntegralTypeEnum integralType) throws UnsupportedEncodingException;

    /**
     * 发送短信验证码
     *
     * @param customerId 商户ID
     * @param mobile     手机号
     * @return 返回结果
     */
    ApiResult sendCode(Long customerId, String mobile) throws UnsupportedEncodingException;

    /**
     * 短息验证码校验
     *
     * @param customerId 商户ID
     * @param mobile     手机号
     * @param code       验证码
     * @return 返回结果
     */
    ApiResult checkCode(Long customerId, String mobile, String code) throws UnsupportedEncodingException;
}
