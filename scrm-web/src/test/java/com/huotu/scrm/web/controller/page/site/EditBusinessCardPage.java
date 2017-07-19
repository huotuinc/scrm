package com.huotu.scrm.web.controller.page.site;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.web.controller.page.AbstractPage;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by Administrator on 2017/7/18.
 */
public class EditBusinessCardPage extends AbstractPage {

    @FindBy( id = "img_avatar")
    WebElement img_avatar;
    @FindBy(id="div_companyname")
    WebElement div_companyname;
    @FindBy(id="div_job")
    WebElement div_job;
    @FindBy(id="div_tel")
    WebElement div_tel;

    BusinessCard businessCard;

    public BusinessCard getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }

    public EditBusinessCardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validate() {
        //this.webDriver.findElement()


        Assert.assertEquals( businessCard.getAvatar() , img_avatar.getText()  );
        Assert.assertEquals( businessCard.getCompanyName() , div_companyname.getText() );
        Assert.assertEquals(businessCard.getJob() , div_job.getText());
        Assert.assertEquals(businessCard.getTel() , div_tel.getText());
    }
}
