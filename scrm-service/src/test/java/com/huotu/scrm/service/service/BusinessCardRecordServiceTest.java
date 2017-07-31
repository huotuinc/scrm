package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by Jinxiangdong on 2017/7/13.
 */
public class BusinessCardRecordServiceTest extends CommonTestBase {

    @Autowired
    private BusinessCardRecordRepository businessCardRecordReposity;
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

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
        businessCardRecord.setFollowDate( LocalDateTime.now() );

        businessCardRecordReposity.saveAndFlush(businessCardRecord);

        isExist = businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId( customerId, userId,followerId );
        Assert.assertTrue( isExist );

    }

    @Test
    public void existsByCustomerIdAndFollowIdNotUserId(){
        Customer customer = mockCustomer();
        customer = customerRepository.save( customer );

        Customer customer1 = customerRepository.findOne( customer.getId() );
        Assert.assertEquals( customer.getId() , customer1.getId() );
        Long customerId = customer.getId();

        User user1 = mockUser(customerId);
        user1 = userRepository.save(user1);
        Long userId =user1.getId();

        User user2 = mockUser(customerId);
        user2 = userRepository.save(user2);
        Long userId2 = user2.getId();


        User follower =mockUser(customerId);
        follower = userRepository.save(follower);
        Long followerId=follower.getId();

        //先删除关注表记录
        businessCardRecordReposity.deleteAll();
        //在新增一条关注userId2=128579L的记录
        BusinessCardRecord record = new BusinessCardRecord();
        record.setCustomerId(customerId);
        record.setUserId( userId2 );
        record.setFollowId(followerId);
        record.setFollowDate( LocalDateTime.now());
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
        //新增商户
        Customer customer = mockCustomer();
        customer = customerRepository.save( customer );

        Customer customer1 = customerRepository.findOne( customer.getId() );
        Assert.assertEquals( customer.getId() , customer1.getId() );
        //新增销售员
        Long customerId=customer1.getId();
        User salesman = this.mockUser( customerId);
        salesman = userRepository.saveAndFlush(salesman);

        User salesman1 = userRepository.findOne(salesman.getId());
        Assert.assertEquals(salesman.getId() , salesman1.getId());
        User salesman2 = userRepository.getByIdAndCustomerId(salesman.getId() , salesman.getCustomerId());
        Assert.assertEquals( salesman.getId() , salesman2.getId());

        Long salesmanId = salesman1.getId();
        //保存销售员的名片信息
        businessCardService.updateBusinessCard( customerId , salesmanId , BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_JOB , UUID.randomUUID().toString());
        //新增关注者信息
        User follower = mockUser(customerId);
        follower = userRepository.save(follower);

        User follower1 = userRepository.findOne(follower.getId());
        Assert.assertEquals(follower.getId() , follower1.getId());

        Long followerId = follower1.getId();
        //新增关注记录
        BusinessCardRecord businessCardRecord = new BusinessCardRecord();
        businessCardRecord.setFollowDate(LocalDateTime.now());
        businessCardRecord.setFollowId( followerId );
        businessCardRecord.setUserId( salesmanId );
        businessCardRecord.setCustomerId(customerId);
        businessCardRecordReposity.save( businessCardRecord );
        //删除关注记录 判断=1
        int count = businessCardRecordReposity.deleteByCustomerIdAndUserIdAndFollowId( customerId , salesmanId , followerId );
        Assert.assertEquals( 1 ,count  );

        //新增关注信息
        User follower2 = mockUser(customerId);
        follower2 = userRepository.save(follower2);
        Long followerId2 = follower2.getId();
        //删除关注记录 判断=0；
        count = businessCardRecordReposity.deleteByCustomerIdAndUserIdAndFollowId( customerId , salesmanId , followerId2 );
        Assert.assertEquals( 0 , count );

    }
}
