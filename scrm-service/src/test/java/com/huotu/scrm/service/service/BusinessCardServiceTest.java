package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * Created by Administrator on 2017/7/11.
 */
public class BusinessCardServiceTest extends CommonTestBase {
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void getSalesmanBusinessCard(){
        //测试不存在的信息
        Customer customer = mockCustomer();
        customer = customerRepository.save(customer);
        Random random =new Random();
        Long customerId = customer.getId();
        Long userId= random.nextLong();
        Long followerId = random.nextLong();
        SalesmanBusinessCard userBusinessCard = businessCardService.getSalesmanBusinessCard(customerId , userId , followerId );
        Assert.assertEquals(null , userBusinessCard.getBusinessCard());
        Assert.assertEquals(null , userBusinessCard.getSalesman());
        //测试存在用户基本信息和不存在名片信息

        User user = mockUser(customerId);
        user = userRepository.save(user);
        User user1 = userRepository.findOne(user.getId());
        User user2 = userRepository.getByIdAndCustomerId(user.getId(),customerId);
        Assert.assertEquals(user.getId(),user2.getId());
        userId=user.getId();
        //User follower = mockUser(customerId);
        //followerId = random.nextLong();


        userBusinessCard = businessCardService.getSalesmanBusinessCard( customerId , userId , followerId);
        Assert.assertTrue( userBusinessCard !=null );
        Assert.assertTrue( userBusinessCard.getSalesman() !=null);
        Assert.assertTrue( userBusinessCard.getBusinessCard() ==null );
        Assert.assertEquals( userId ,userBusinessCard.getSalesman().getId() );
        Assert.assertEquals( customerId , userBusinessCard.getSalesman().getCustomerId());

        //先 新增名片信息
        BusinessCardUpdateTypeEnum type = BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR;
        String text = "名片头像地址";
        BusinessCard businessCard = businessCardService.updateBusinessCard( customerId , userId , type , text );
        //再 测试 是否存在 名片信息
        userBusinessCard = businessCardService.getSalesmanBusinessCard( customerId , userId , followerId);
        Assert.assertTrue( userBusinessCard !=null );
        Assert.assertTrue( userBusinessCard.getSalesman() !=null);
        Assert.assertTrue( userBusinessCard.getBusinessCard() !=null );
        Assert.assertEquals( userId ,userBusinessCard.getSalesman().getId() );
        Assert.assertEquals( customerId , userBusinessCard.getSalesman().getCustomerId());
        Assert.assertEquals( userId , userBusinessCard.getBusinessCard().getUserId() );
        Assert.assertEquals(customerId , userBusinessCard.getBusinessCard().getCustomerId());
        Assert.assertEquals( text , userBusinessCard.getBusinessCard().getAvatar() );

    }

    @Test
    public void getBusinessCard(){
        long userid=0;
        long customerid=0;
        BusinessCard businessCard = businessCardService.getBusinessCard( userid , customerid );
        Assert.assertEquals( null , businessCard );
    }

    @Test
    public void  updateBusinessCard() {
        long customerId = 4886;
        long userId = 531757;
        //
        BusinessCardUpdateTypeEnum type = BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR;
        String text = "名片头像地址";

        BusinessCard businessCard = businessCardService.updateBusinessCard(customerId, userId, type, text);
        BusinessCard businessCard1=businessCardService.getBusinessCard( userId , customerId );
        Assert.assertTrue( businessCard1!=null);
        Assert.assertEquals( text , businessCard1.getAvatar() );

        type = BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_COMPANYADDRESS;
        text="企业地址";
        businessCard = businessCardService.updateBusinessCard( customerId , userId , type , text);
        businessCard1 = businessCardService.getBusinessCard(userId , customerId);
        Assert.assertTrue( businessCard1!=null);
        Assert.assertEquals( text , businessCard1.getCompanyAddress() );

        type = BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_COMPANYNAME;
        text="企业名称";
        businessCard = businessCardService.updateBusinessCard( customerId , userId , type , text);
        businessCard1 = businessCardService.getBusinessCard(userId , customerId);
        Assert.assertTrue( businessCard1!=null);
        Assert.assertEquals( text , businessCard1.getCompanyName() );

        type = BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_JOB;
        text="职位";
        businessCard = businessCardService.updateBusinessCard( customerId , userId , type , text);
        businessCard1 = businessCardService.getBusinessCard(userId , customerId);
        Assert.assertTrue( businessCard1!=null);
        Assert.assertEquals( text , businessCard1.getJob() );

        type = BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_TEL;
        text="固定电话";
        businessCard = businessCardService.updateBusinessCard( customerId , userId , type , text);
        businessCard1 = businessCardService.getBusinessCard(userId , customerId);
        Assert.assertTrue( businessCard1!=null);
        Assert.assertEquals( text , businessCard1.getTel() );

        type = BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_QQ;
        text="QQ";
        businessCard = businessCardService.updateBusinessCard( customerId , userId , type , text);
        businessCard1 = businessCardService.getBusinessCard(userId , customerId);
        Assert.assertTrue( businessCard1!=null);
        Assert.assertEquals( text , businessCard1.getQq() );


    }


}
