package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.repository.BusinessCardRecordReposity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
//        businessCardRecord.setFollowDate( new Date());

        businessCardRecordReposity.saveAndFlush(businessCardRecord);

        isExist = businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId( customerId, userId,followerId );
        Assert.assertTrue( isExist );

    }

    @Test
    public void existsByCustomerIdAndFollowIdNotUserId(){
        Long customerId = 4421L;
        Long userId =1058510L;
        Long userId2 = 128579L;
        Long followerId=93551L;

        //先删除关注表记录
        businessCardRecordReposity.deleteAll();
        //在新增一条关注userId2=128579L的记录
        BusinessCardRecord record = new BusinessCardRecord();
        record.setCustomerId(customerId);
        record.setUserId( userId2 );
        record.setFollowId(followerId);
//        record.setFollowDate(new Date());
        businessCardRecordReposity.save( record );
        //检测是否关注了userId=128579L
        boolean isFollowed_128579 = businessCardRecordReposity.existsByCustomerIdAndFollowIdAndUserIdNot(customerId, followerId ,userId);
        Assert.assertTrue( isFollowed_128579 );
        //检测是否关注了userid=1058510L
        boolean isFollwed_1058510 = businessCardRecordReposity.existsByCustomerIdAndFollowIdAndUserIdNot(customerId,followerId,userId2);
        Assert.assertFalse(isFollwed_1058510);
        //
        boolean isFollowed_1 = businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId(customerId,userId , followerId);
        Assert.assertFalse(isFollowed_1);
        boolean  isFollowed_2 = businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId(customerId,userId2 , followerId);
        Assert.assertTrue(isFollowed_2);

    }
}
