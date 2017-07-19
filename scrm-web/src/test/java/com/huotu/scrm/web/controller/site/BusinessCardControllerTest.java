package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.service.BusinessCardRecordService;
import com.huotu.scrm.service.service.BusinessCardService;
import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.controller.page.site.EditBusinessCardPage;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * Created by Administrator on 2017/7/17.
 */
public class BusinessCardControllerTest extends CommonTestBase {
    String editUrl = "http://localhost/site/editBusinessCard";
    @Autowired
    BusinessCardService businessCardService;

    @Test
    public void editBusinessCard(){
        Random random = new Random();
        Long customerId = random.nextLong();
        Long userId= random.nextLong();
//        BusinessCard businessCard = new BusinessCard();
//        businessCard.setAvatar("a.jpg");
//        businessCard.setCustomerId(customerId);
//        businessCard.setUserId(userId);
//        businessCard.setEmail("a@qq.com");
//        businessCard.setCompanyAddress("aaaaaadddress");
//        businessCard.setCompanyName("commmpany");
//        businessCard.setJob("job");
//        businessCard.setQq("2342343");
//        businessCard.setTel("146646554545");
        BusinessCard businessCard = businessCardService.updateBusinessCard(customerId, userId , BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR, "a.jpg");

        //BusinessCard businessCard = businessCardService.getBusinessCard( customerId , userId );

        editUrl +="?customerId="+customerId+"&userId="+userId;
        webDriver.get(editUrl);

        EditBusinessCardPage editBusinessCardPage = this.initPage(EditBusinessCardPage.class);
        editBusinessCardPage.setBusinessCard(businessCard);
        editBusinessCardPage.validate();

    }

    @Test
    public void seeBusinessCard(){

    }
}
