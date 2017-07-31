package com.huotu.scrm.service.service.mall;

import com.huotu.scrm.service.entity.mall.User;

public interface UserService {

    /***
     * 通过userid和customerid获得用户信息
     * @param id
     * @param customerId
     * @return
     */
    User getByIdAndCustomerId(Long id, Long customerId);

    User save(User user);
}
