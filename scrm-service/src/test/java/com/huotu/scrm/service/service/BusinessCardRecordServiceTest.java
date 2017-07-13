package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.repository.BusinessCardRecordReposity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/13.
 */
public class BusinessCardRecordServiceTest extends CommonTestBase {

    @Autowired
    BusinessCardRecordReposity businessCardRecordReposity;
    @Autowired
    BusinessCardService businessCardService;

    @Test
    public void existsByCustomerIdAndUserIdAndFollowId(){

        Long customerId = 4886L;
        Long userId = 298533L;
        Long followerId=470818L;

        boolean  isExist = businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId( customerId, userId,followerId );
        Assert.assertFalse( isExist );

        BusinessCardRecord businessCardRecord = new BusinessCardRecord();
        businessCardRecord.setCustomerId(customerId);
        businessCardRecord.setUserId(userId);
        businessCardRecord.setFollowId(followerId);
        businessCardRecord.setFollowDate( new Date());

        businessCardRecordReposity.saveAndFlush(businessCardRecord);

        isExist = businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId( customerId, userId,followerId );
        Assert.assertTrue( isExist );

    }
}
