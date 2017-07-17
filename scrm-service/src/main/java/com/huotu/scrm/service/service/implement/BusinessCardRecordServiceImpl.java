package com.huotu.scrm.service.service.implement;

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
    public int getFollowCountByCustomerIdAndUserId(Long customerId, Long userId) {
        return businessCardRecordReposity.getNumberOfFollowerByCustomerIdAndUserId(customerId , userId);
    }

    @Override
    public void deleteByCustomerIdAndUserIdAndFollowId(Long customerId,Long userId, Long followId) {
        businessCardRecordReposity.deleteByCustomerIdAndUserIdAndFollowId(customerId,userId, followId);
    }

    @Override
    public boolean existsByCustomerIdAndUserIdAndFollowId(Long customerId, Long userId, Long followId) {
        return businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId(customerId, userId,followId);
    }
}
