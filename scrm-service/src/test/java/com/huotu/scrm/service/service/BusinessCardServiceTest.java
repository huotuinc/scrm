package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.model.UserBusinessCard;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/7/11.
 */
public class BusinessCardServiceTest extends CommonTestBase {
    @Autowired
    private BusinessCardService businessCardService;

    @Test
    public void getUserBusinessCard(){
        //测试不存在的信息
        Integer userId=22222222;
        Integer customerId = 2323;
        UserBusinessCard userBusinessCard = businessCardService.getUserBusinessCard(userId, customerId);
        Assert.assertEquals(null , userBusinessCard.getBusinessCard());
        Assert.assertEquals(null , userBusinessCard.getUser());
        //测试存在的信息
        userId=124;
        customerId=742;
        userBusinessCard = businessCardService.getUserBusinessCard(userId,customerId);
        Assert.assertTrue( userBusinessCard !=null );
        Assert.assertTrue( userBusinessCard.getUser() !=null);
        Assert.assertTrue( userBusinessCard.getBusinessCard() !=null );
        Assert.assertEquals( userId ,userBusinessCard.getUser().getId() );
        Assert.assertEquals( customerId , userBusinessCard.getUser().getCustomerId());

    }

}
