package com.huotu.scrm.service.service.mall;

import com.huotu.scrm.service.entity.mall.UserLevel;

import java.util.List;

public interface UserLevelService {
    //根据等级和商户ID查询等级
    UserLevel findByLevelAndCustomerId(Long level, Long customerId);

    /**
     * 根据商户id和销售员标记字段，获得等级列表
     * @param customerId
     * @param isSalesman
     * @return
     */
    List<UserLevel> findByCustomerIdAndIsSalesman(Long customerId , boolean isSalesman);

    /**
     *
     * @param userLevel
     * @return
     */
    UserLevel save(UserLevel userLevel);
}
