package com.huotu.scrm.service.service.api;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.common.utils.ApiResult;

/**
 * Created by helloztt on 2017-07-12.
 */
public interface ApiService {
    /**
     * 用户微信授权登录
     *
     * @param customerId  商户ID
     * @param redirectUrl 执行成功后跳转的ID
     */
    void userLogin(Long customerId, String redirectUrl);

    /**
     * 用户增加积分
     *
     * @param customerId   商户ID
     * @param userId       用户ID
     * @param integral     增加积分
     * @param integralType 积分类型
     * @return
     */
    ApiResult rechargePoint(Long customerId, Long userId, Long integral, IntegralTypeEnum integralType);
}
