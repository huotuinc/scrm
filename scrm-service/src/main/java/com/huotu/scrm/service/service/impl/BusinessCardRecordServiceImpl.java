package com.huotu.scrm.service.service.impl;

import com.huotu.scrm.service.repository.BusinessCardRecordReposity;
import com.huotu.scrm.service.service.BusinessCardRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/12.
 */
@Service
public class BusinessCardRecordServiceImpl implements BusinessCardRecordService {

    @Autowired
    BusinessCardRecordReposity businessCardRecordReposity;

    @Override
    public int getFollowCountByCustomerIdAndUserId(long customerId, long userId) {
        return businessCardRecordReposity.getFollowCountByCustomerIdAndUserId(customerId , userId);
    }

    @Override
    public void deleteByCustomerIdAndUserIdAndFollowId(long customerId, long userId, long followId) {
        businessCardRecordReposity.deleteByCustomerIdAndUserIdAndFollowId(customerId,userId, followId);
    }
}
