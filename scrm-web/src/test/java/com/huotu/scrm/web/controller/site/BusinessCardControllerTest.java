package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import com.huotu.scrm.service.service.mall.UserLevelService;
import com.huotu.scrm.service.service.mall.UserService;
import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.controller.page.site.ShowBusinessCardPage;
import com.huotu.scrm.web.controller.page.site.TestEditBusinessCardPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/17.
 */
@ActiveProfiles(value = {"development"})
public class BusinessCardControllerTest extends CommonTestBase {
    String editUrl = "http://localhost/site/businessCard/editBusinessCard";
    @Autowired
    BusinessCardService businessCardService;
    @Autowired
    UserLevelService userLevelService;
    @Autowired
    UserService userService;
    @Autowired
    CustomerRepository customerRepository;


    private Long customerId =0L;


    @Before
    public void init() {
        customerId = Long.valueOf(random.nextInt(10000));
    }


    @Test
    public void editBusinessCard(){
        Random random = new Random();
        Customer customer = new Customer();
        customer.setLoginName(UUID.randomUUID().toString().replace("-", ""));
        customer.setEnabled(true);
        customer.setLoginPassword(UUID.randomUUID().toString().replace("-", ""));
        customer.setNickName(UUID.randomUUID().toString().replace("-", ""));
        customer.setMobile(String.valueOf(random.nextInt()));
        customer.setNickName(UUID.randomUUID().toString().replace("-", ""));
        customer.setSubDomain(UUID.randomUUID().toString().replace("-", ""));
        customer.setId(Long.valueOf(String.valueOf(random.nextInt())));
        customer = customerRepository.save(customer);

        Long customerId = customer.getId();

        UserLevel userLevel = new UserLevel();
        userLevel.setCustomerId(customerId);
        userLevel.setLevel(0);
        userLevel.setLevelName(UUID.randomUUID().toString());
        userLevel.setSalesman(true);
        userLevel.setType(UserType.buddy);
        userLevelService.save(userLevel);

        List<UserLevel> userLevelList = userLevelService.findByCustomerIdAndIsSalesman(customer.getId(), true);
        Assert.assertTrue( userLevelList.size() == 1 );

        User user = new User();
        user.setId(Long.valueOf(String.valueOf(random.nextInt())));
        user.setCustomerId(customerId);
        user.setLoginName(UUID.randomUUID().toString());
        user.setUserGender("男");
        user.setNickName(UUID.randomUUID().toString());
        user.setLevelId( userLevelList.get(0).getId() );
        user.setLockedBalance(random.nextDouble());
        user.setLockedIntegral(random.nextDouble());
        user.setRegTime(new Date());
        user.setUserMobile(String.valueOf(random.nextInt()));
        user.setUserBalance(random.nextDouble());
        user.setUserType(UserType.buddy);
        user.setUserTempIntegral(random.nextInt());
        user.setWeixinImageUrl(UUID.randomUUID().toString());
        user.setWxNickName(UUID.randomUUID().toString().replace("-", ""));
        user = userService.save(user);

        Long userId= user.getId();
        BusinessCard businessCard = businessCardService.updateBusinessCard(customerId, userId , BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR, "a.jpg");

        //BusinessCard businessCard = businessCardService.getBusinessCard( customerId , userId );

        editUrl +="?customerId="+customerId+"&userId="+userId;
        webDriver.get(editUrl);
        TestEditBusinessCardPage editBusinessCardPage = this.initPage(TestEditBusinessCardPage.class);
        editBusinessCardPage.setBusinessCard(businessCard);
        //WebElement ele = webDriver.findElement(By.id("div_job"));
        editBusinessCardPage.validate();

    }

    @Test
    public void showBusinessCard(){

        UserLevel userLevel = new UserLevel();
        userLevel.setType(UserType.buddy);
        userLevel.setSalesman(true);
        userLevel.setLevelName(UUID.randomUUID().toString());
        userLevel.setLevel(1);
        userLevel.setCustomerId(customerId);
        userLevel = userLevelService.save(userLevel);

        User user = new User();
        user.setId(Long.valueOf(String.valueOf(random.nextInt())));
        user.setCustomerId(customerId);
        user.setLoginName(UUID.randomUUID().toString());
        user.setUserGender("男");
        user.setNickName(UUID.randomUUID().toString());
        user.setLevelId( userLevel.getId() );
        user.setLockedBalance(random.nextDouble());
        user.setLockedIntegral(random.nextDouble());
        user.setRegTime(new Date());
        user.setUserMobile(String.valueOf(random.nextInt()));
        user.setUserBalance(random.nextDouble());
        user.setUserType(userLevel.getType());
        user.setUserTempIntegral(random.nextInt());
        user.setWeixinImageUrl(UUID.randomUUID().toString());
        user.setWxNickName(UUID.randomUUID().toString().replace("-", ""));
       user = userService.save(user);

        businessCardService.updateBusinessCard( customerId , user.getId() , BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_JOB , "job" );

        User follower =  new User();
        user.setId(Long.valueOf(String.valueOf(random.nextInt())));
        user.setCustomerId(customerId);
        user.setLoginName(UUID.randomUUID().toString());
        user.setUserGender("男");
        user.setNickName(UUID.randomUUID().toString());
        user.setLevelId( userLevel.getId() );
        user.setLockedBalance(random.nextDouble());
        user.setLockedIntegral(random.nextDouble());
        user.setRegTime(new Date());
        user.setUserMobile(String.valueOf(random.nextInt()));
        user.setUserBalance(random.nextDouble());
        user.setUserType(userLevel.getType());
        user.setUserTempIntegral(random.nextInt());
        user.setWeixinImageUrl(UUID.randomUUID().toString());
        user.setWxNickName(UUID.randomUUID().toString().replace("-", ""));
        user = userService.save(user);


        SalesmanBusinessCard salesmanBusinessCard= businessCardService.getSalesmanBusinessCard(customerId , user.getId() , follower.getId() );

        String url="http://localhost/site/businessCard/seeBusinessCard?customerId="+customerId+"&salesmanId="+ user.getId()+"&userId="+follower.getId();
        webDriver.get(url);
        ShowBusinessCardPage page = this.initPage(ShowBusinessCardPage.class);
        page.setSalesmanBusinessCard(salesmanBusinessCard);
        page.validate();

    }
}
