package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.controller.page.site.TestEditBusinessCardPage;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Random;

/**
 * Created by Administrator on 2017/7/17.
 */
@ActiveProfiles(value = {"development"})
public class BusinessCardControllerTest extends CommonTestBase {
    String editUrl = "http://localhost/site/businessCard/editBusinessCard";
    @Autowired
    BusinessCardService businessCardService;
//    @Autowired
//    Environment environment;

    private Long customerId =0L;


    @Before
    public void init() {
        customerId = Long.valueOf(random.nextInt(10000));
//        boolean s = environment.acceptsProfiles("development");
//        boolean s2 = environment.acceptsProfiles("test");
//        boolean s3 = environment.acceptsProfiles("unit_test");
    }


    @Test
    public void editBusinessCard(){
        Random random = new Random();
        Long customerId = Long.valueOf(String.valueOf( random.nextInt()));
        Long userId=  Long.valueOf(String.valueOf( random.nextInt()));
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
    public void seeBusinessCard(){
        String url = "https://www.baidu.com/";
        //webDriver.get(url);
        //WebElement ele = webDriver.findElement(By.id("kw"));
        //ele.sendKeys("okkkk");

        url="http://localhost/mall/test/index/html?customerId=4421";
        webDriver.get(url);
        WebElement ele1 = webDriver.findElement(By.id("title"));
        String text = ele1.getText();

        url="http://localhost/site/businessCard/editBusinessCard?customerId="+customerId;
        webDriver.get(url);
        WebElement ele2 = webDriver.findElement(By.id("customerId"));
        text = ele2.getText();
    }
}
