package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 名片关注记录表
 * Created by Administrator on 2017/7/12.
 */
public interface BusinessCardRecordReposity extends JpaRepository<BusinessCardRecord , Long > {

    void deleteByCustomerIdAndUserIdAndFollowId(Long customerId , Long userId , Long followId);

    Integer getNumberOfFollowerByCustomerIdAndUserId(Long customerId , Long userId );

    /***
     * 是否关注了指定的销售员名片
     * @param customerId
     * @param userId
     * @param followId
     * @return
     */
    Boolean existsByCustomerIdAndUserIdAndFollowId(Long customerId , Long userId , Long followId);

    //根据用户ID查询关注人数
    long countByUserId(Long userId);
}
