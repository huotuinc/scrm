package com.huotu.scrm.service.service;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Jinxiangdong on 2017/7/13.
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
    @Autowired
    UserLevelRepository userLevelRepository;

    @Test
    public void existsByCustomerIdAndUserIdAndFollowId(){
        Customer customer = mockCustomer();
        Long customerId = customer.getId();
        User user = mockUser( customerId  );
        Long userId = user.getId();
        User follower = mockUser(customerId);
        Long followerId=follower.getId();

        boolean  isExist = businessCardRecordReposity.countByCustomerIdAndUserIdAndFollowId( customerId, userId,followerId ) > 0;
        Assert.assertFalse( isExist );

        BusinessCardRecord businessCardRecord = new BusinessCardRecord();
        businessCardRecord.setCustomerId(customerId);
        businessCardRecord.setUserId(userId);
        businessCardRecord.setFollowId(followerId);
        businessCardRecord.setFollowDate( LocalDateTime.now() );

        businessCardRecordReposity.saveAndFlush(businessCardRecord);

        isExist = businessCardRecordReposity.countByCustomerIdAndUserIdAndFollowId( customerId, userId,followerId ) > 0;
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
        //在新增一条关注userId2的记录
        BusinessCardRecord record = new BusinessCardRecord();
        record.setCustomerId(customerId);
        record.setUserId( userId2 );
        record.setFollowId(followerId);
        record.setFollowDate( LocalDateTime.now());
        businessCardRecordReposity.save( record );
        //检测是否关注了除userId的其他记录
        boolean isFollowed_128579 = businessCardRecordReposity.countByCustomerIdAndFollowIdAndUserIdNot(customerId, followerId ,userId) > 0;
        Assert.assertEquals( true , isFollowed_128579 );
        //检测是否关注了除userid2的其他记录
        boolean isFollwed_1058510 = businessCardRecordReposity.countByCustomerIdAndFollowIdAndUserIdNot(customerId,followerId,userId2) > 0;
        Assert.assertEquals( false , isFollwed_1058510);
        //检测是否关注了userid
        boolean isFollowed_1 = businessCardRecordReposity.countByCustomerIdAndUserIdAndFollowId(customerId,userId , followerId) > 0;
        Assert.assertEquals( false , isFollowed_1);
        //检测是否关注了userid2
        boolean  isFollowed_2 = businessCardRecordReposity.countByCustomerIdAndUserIdAndFollowId(customerId,userId2 , followerId) > 0;
        Assert.assertEquals( true , isFollowed_2);
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
        salesman = userRepository.save(salesman);

        User salesman1 = userRepository.findOne(salesman.getId());
        Assert.assertEquals(salesman.getId() , salesman1.getId());
        //User salesman2 = userRepository.getByIdAndCustomerId(salesman.getId() , salesman.getCustomerId());
        //Assert.assertEquals( salesman.getId() , salesman2.getId());

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


    @Test
    public void countNumberOfFollowerByCustomerIdAndUserId(){
        //mock 商户
        Customer customer = mockCustomer();
        //mock 用户等级
        UserLevel userLevel = new UserLevel();
        userLevel.setCustomerId(customer.getId());
        userLevel.setLevel(0);
        userLevel.setLevelName(UUID.randomUUID().toString());
        userLevel.setSalesman(true);
        userLevel.setType(UserType.buddy);
        userLevelRepository.save(userLevel);

        List<UserLevel> userLevelList = userLevelRepository.findByCustomerIdAndIsSalesman(customer.getId(), true);
        Assert.assertTrue( userLevelList.size() == 1 );

        //mock 销售员类型的用户
        User salesman = mockUser( customer.getId() , UserType.buddy , userLevelList.get(0).getId() );
        //mock 会员
        User follower = mockUser(customer.getId() , UserType.normal);

        long customerId=customer.getId();
        long salesmanId=salesman.getId();

        //mock 名片信息
        String job = UUID.randomUUID().toString();
        businessCardService.updateBusinessCard( customerId , salesmanId , BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_JOB , job );

        //检测 关注人数 是否正确
        int count = businessCardRecordReposity.countNumberOfFollowerByCustomerIdAndUserId(customerId , salesmanId);
        Assert.assertEquals(0 , count );

        //mock 关注记录
        BusinessCardRecord record = new BusinessCardRecord();
        record.setCustomerId(customerId);
        record.setFollowDate(LocalDateTime.now());
        record.setFollowId(follower.getId());
        record.setUserId(salesmanId);
        businessCardRecordReposity.save( record );

        //检测 关注人数 是否正确
        count = businessCardRecordReposity.countNumberOfFollowerByCustomerIdAndUserId(customerId , salesmanId);
        Assert.assertEquals(1 , count );

        //mock 关注记录
        User follower2 = mockUser( customerId );
        record = new BusinessCardRecord();
        record.setCustomerId(customerId);
        record.setFollowDate(LocalDateTime.now());
        record.setFollowId(follower2.getId());
        record.setUserId(salesmanId);
        businessCardRecordReposity.save( record );
        count = businessCardRecordReposity.countNumberOfFollowerByCustomerIdAndUserId(customerId , salesmanId);
        Assert.assertEquals(2 , count );

        //mock 不是当前销售员的关注记录
        User notFollower = mockUser( customerId );
        record = new BusinessCardRecord();
        record.setCustomerId(customerId);
        record.setFollowDate(LocalDateTime.now());
        record.setFollowId(notFollower.getId());
        record.setUserId(Long.valueOf( new Random().nextInt()) );
        businessCardRecordReposity.save( record );

        count = businessCardRecordReposity.countNumberOfFollowerByCustomerIdAndUserId(customerId , salesmanId);
        Assert.assertEquals(2 , count );

    }
}
