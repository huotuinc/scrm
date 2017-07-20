package com.huotu.scrm.service.service.businesscard.impl;

import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.service.businesscard.BusinessCardRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Jinxiangdong on 2017/7/12.
 */
@Service
@Transactional
public class BusinessCardRecordServiceImpl implements BusinessCardRecordService {

    @Autowired
    BusinessCardRecordRepository businessCardRecordReposity;

    @Override
    public int getFollowCountByCustomerIdAndUserId(Long customerId, Long userId) {
        return businessCardRecordReposity.getNumberOfFollowerByCustomerIdAndUserId(customerId, userId);
    }

    @Override
    public void deleteByCustomerIdAndUserIdAndFollowId(Long customerId, Long userId, Long followId) {
        businessCardRecordReposity.deleteByCustomerIdAndUserIdAndFollowId(customerId, userId, followId);
    }

    @Override
    public boolean existsByCustomerIdAndUserIdAndFollowId(Long customerId, Long userId, Long followId) {
        return businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId(customerId, userId, followId);
    }

    @Override
    public boolean existsByCustomerIdAndFollowerIdNotInSalesmanId(Long customerId, Long followerId, Long salesmanId) {
        return businessCardRecordReposity.existsByCustomerIdAndFollowIdAndUserIdNot(customerId, followerId, salesmanId);
    }

    @Override
    public BusinessCardRecord insert(BusinessCardRecord businessCardRecord) {
        return businessCardRecordReposity.save(businessCardRecord);
    }
}
