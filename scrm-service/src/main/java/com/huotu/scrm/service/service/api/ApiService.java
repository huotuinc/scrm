package com.huotu.scrm.service.service.api;

/**
 * Created by helloztt on 2017-07-12.
 */
public interface ApiService {
    /**
     *
     * @param customerId 商户ID
     * @param redirectUrl 执行成功后跳转的ID
     */
    void userLogin(Long customerId,String redirectUrl);
}
