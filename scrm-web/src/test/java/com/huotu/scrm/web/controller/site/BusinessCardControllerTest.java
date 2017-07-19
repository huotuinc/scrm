package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.service.businessCard.BusinessCardService;
import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.controller.page.site.TestEditBusinessCardPage;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * Created by Administrator on 2017/7/17.
 */
public class BusinessCardControllerTest extends CommonTestBase {
    String editUrl = "http://localhost/site/businessCard/editBusinessCard";
    @Autowired
    BusinessCardService businessCardService;

    @Test
    public void editBusinessCard(){
        Random random = new Random();
        Long customerId = Long.valueOf(String.valueOf( random.nextInt()));
        Long userId= Long.valueOf(String.valueOf( random.nextInt()));
        BusinessCard businessCard = businessCardService.updateBusinessCard(customerId, userId , BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR, "a.jpg");

        //BusinessCard businessCard = businessCardService.getBusinessCard( customerId , userId );

        editUrl +="?customerId="+customerId+"&userId="+userId;
        webDriver.get(editUrl);
        TestEditBusinessCardPage editBusinessCardPage = this.initPage(TestEditBusinessCardPage.class);
        editBusinessCardPage.setBusinessCard(businessCard);
        WebElement ele = webDriver.findElement(By.id("div_job"));
        editBusinessCardPage.validate();

    }

    @Test
    public void seeBusinessCard(){
        String url = "https://www.baidu.com/";
        webDriver.get(url);
        WebElement ele = webDriver.findElement(By.id("kw"));
        ele.sendKeys("okkkk");


    }
}
