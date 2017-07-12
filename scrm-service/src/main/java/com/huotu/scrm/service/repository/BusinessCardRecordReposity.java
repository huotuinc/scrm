package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 名片关注记录表
 * Created by Administrator on 2017/7/12.
 */
public interface BusinessCardRecordReposity extends JpaRepository<BusinessCardRecord , Long > {

    void deleteByCustomerIdAndUserIdAndFollowId(long customerId , long userId , long followId);

    int getFollowCountByCustomerIdAndUserId(long customerId , long userId );
}
