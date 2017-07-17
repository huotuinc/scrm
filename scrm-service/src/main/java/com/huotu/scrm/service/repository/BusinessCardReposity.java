package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 名片
 * Created by Jinxiangdong on 2017/7/11.
 */
public interface BusinessCardReposity extends JpaRepository<BusinessCard , Long>{
    /***
     * 获取指定商户id和用户id的名片信息
     * @param userId
     * @param customerId
     * @return
     */
    BusinessCard getByUserIdAndCustomerId( Long userId , Long customerId);

}
