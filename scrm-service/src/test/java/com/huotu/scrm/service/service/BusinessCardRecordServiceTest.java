package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.repository.CustomerRepository;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/13.
 */
public class BusinessCardRecordServiceTest extends CommonTestBase {

    @Autowired
    BusinessCardRecordRepository businessCardRecordReposity;
    @Autowired
    BusinessCardService businessCardService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    UserRepository userRepository;

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
        record.setFollowDate(new Date());
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

    @Test
    public void deleteByCustomerIdAndUserIdAndFollowId()throws UnsupportedEncodingException{

//        Customer customer = new Customer();
//        customer.setLoginName(UUID.randomUUID().toString());
//        customer.setEnabled(true);
//        customer.setLoginPassword(DigestUtils.md5DigestAsHex( "123456".getBytes("utf-8")));
//        customer.setNickName(UUID.randomUUID().toString());
//        customer.set
//        customer = customerRepository.save( customer );
        Long customerId=4421L;

        Long salesmanId = 128335L;
        Long followerId = 128560L;
        Long followerId2 = 3242L;

//        User salesman =new User();
//        salesman.setCustomerId(customerId);
//        salesman.setLoginName(UUID.randomUUID().toString());
//        salesman.setUserGender("男");
//        salesman.setNickName(UUID.randomUUID().toString());
//        salesman = userRepository.save(salesman);

        businessCardService.updateBusinessCard( customerId , salesmanId , BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_JOB , UUID.randomUUID().toString());

//        User follower =new User();
//        follower.setCustomerId(customerId);
//        follower.setLoginName(UUID.randomUUID().toString());
//        follower.setUserGender("女");
//        follower.setNickName(UUID.randomUUID().toString());
//        follower = userRepository.save(follower);

        BusinessCardRecord businessCardRecord = new BusinessCardRecord();
        businessCardRecord.setFollowDate(new Date());
        businessCardRecord.setFollowId( followerId );
        businessCardRecord.setUserId( salesmanId );
        businessCardRecord.setCustomerId(customerId);
        businessCardRecord = businessCardRecordReposity.save( businessCardRecord );

        int count = businessCardRecordReposity.deleteByCustomerIdAndUserIdAndFollowId( customerId , salesmanId , followerId );
        Assert.assertEquals( 1 ,count  );

        count = businessCardRecordReposity.deleteByCustomerIdAndUserIdAndFollowId( customerId , salesmanId , followerId2 );
        Assert.assertEquals( 0 , count );

    }
}
