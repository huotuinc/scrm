package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.service.config.ServiceConfig;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import com.huotu.scrm.web.controller.WebTestOfMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 销售员身份进入该页面，验证能否看到关注信息
 * 非销售员的身份进入该也面。
 * Created by cosy on 2017/7/21.
 */


@ActiveProfiles(value = {"development"})
public class MyBusinessCardByCosy extends WebTestOfMock {
    @Autowired
    BusinessCardService businessCardService;

    @Before

    public void first() throws Exception{
        BusinessCard businessCard=createBusinessCardMock();
        createBusinessCardRecordMock();

        String customerID = getCustomerMock().getId().toString();
        String userID = getUserMock().getId().toString();

        webDriver.get("http://localhost/site/businessCard/myBusinessCard?customerId="+customerID+"&userId="+userID);

    }

    /**
     * 非销售员进入该页面
     * @throws Exception
     */
    @Test
    public void notSalesman()throws Exception{



       /*   BusinessCard businessCard=createBusinessCardMock();
          createBusinessCardRecordMock();

        webDriver.get("http://localhost/site/businessCard/myBusinessCard?customerId="+getCustomerMock().getId()+"&userId="+getUserMock().getId());
        Assert.assertEquals(getUserMock().getLoginName(),webDriver.findElement(By.xpath("html/body/div[1]/div/div/div/div[2]/h4/span")).getText());
        Assert.assertEquals(businessCard.getJob(),webDriver.findElement(By.xpath("html/body/div[1]/div/div/div/div[2]/h4/i")).getText());
        Assert.assertEquals(businessCard.getCompanyName(),webDriver.findElement(By.xpath("html/body/div[1]/div/div/div/div[2]/p[1]")).getText());
        Assert.assertEquals(getUserMock().getUserMobile(),webDriver.findElement(By.xpath("html/body/div[1]/div/div/div/div[2]/p[2]")).getText());
*/
    }


    /**
     * 销售员进入该页面
     */
}
