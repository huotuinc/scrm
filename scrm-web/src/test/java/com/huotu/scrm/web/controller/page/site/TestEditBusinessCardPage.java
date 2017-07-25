package com.huotu.scrm.web.controller.page.site;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.web.controller.page.AbstractPage;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.util.StringUtils;

/**
 * Created by Jinxiangdong on 2017/7/18.
 */
public class TestEditBusinessCardPage extends AbstractPage {

    @FindBy( id = "img_avatar")
    WebElement img_avatar;
    @FindBy(id="div_companyname")
    WebElement div_companyname;
    @FindBy(id="div_job")
    WebElement div_job;
    @FindBy(id="div_tel")
    WebElement div_tel;
    @FindBy(id="div_qq")
    WebElement div_qq;
    @FindBy(id="div_email")
    WebElement div_email;
    @FindBy(id="div_companyaddress")
    WebElement div_companyaddress;
    @FindBy(id="customerId")
    WebElement customerId;
    @FindBy(id="userId")
    WebElement userId;
    @FindBy(id="btnInput")
    WebElement btnFile;

    @FindBy(id="weuishow_qq")
    WebElement weuishow_qq;

    BusinessCard businessCard;

    public BusinessCard getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }

    public TestEditBusinessCardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validate() {
        //this.webDriver.findElement()
        //WebElement ele = this.webDriver.findElement(By.id("div_job"));

        Assert.assertTrue(img_avatar.getAttribute("src").contains(businessCard.getAvatar())  );
        Assert.assertTrue( StringUtils.isEmpty(businessCard.getCompanyName()) && div_companyname.getText().isEmpty()
        || !StringUtils.isEmpty(businessCard.getCompanyName()) && div_companyname.getText().equals( businessCard.getCompanyName() )
        );

        Assert.assertTrue( StringUtils.isEmpty(businessCard.getJob()) && div_job.getText().isEmpty()
        || !StringUtils.isEmpty(businessCard.getJob()) && div_job.getText().equals( businessCard.getJob() ));

        Assert.assertTrue( StringUtils.isEmpty(businessCard.getTel()) && div_tel.getText().isEmpty()
        || !StringUtils.isEmpty(businessCard.getTel()) && div_tel.getText().equals( businessCard.getTel() ));

        Assert.assertTrue( StringUtils.isEmpty(businessCard.getCompanyAddress()) && div_companyaddress.getText().isEmpty()
        || !StringUtils.isEmpty(businessCard.getCompanyAddress()) && div_companyaddress.getText().equals( businessCard.getCompanyAddress() ));


        Assert.assertTrue( StringUtils.isEmpty(businessCard.getQq()) && div_qq.getText().isEmpty()
        ||!StringUtils.isEmpty(businessCard.getQq()) && div_qq.getText().equals( businessCard.getQq() ));


        Assert.assertTrue( StringUtils.isEmpty(businessCard.getEmail()) && div_email.getText().isEmpty()
        ||!StringUtils.isEmpty(businessCard.getEmail()) && div_email.getText().equals( businessCard.getEmail() ));

        Assert.assertEquals( String.valueOf(businessCard.getCustomerId()) , customerId.getAttribute("value"));
        Assert.assertEquals( String.valueOf(businessCard.getUserId()), userId.getAttribute("value"));


        //btnFile.sendKeys("C:\\Users\\Administrator\\Desktop\\法人证件正反面.jpg");

        //String url= img_avatar.getAttribute("src");

        weuishow_qq.click();
        WebElement ele = webDriver.findElement( By.id("weui-prompt-input"));
        ele.sendKeys("51818549");
        
        ele = webDriver.findElement(By.className("weui_btn_dialog"));
        ele.click();

        //Assert.assertEquals( "51818549" , div_qq.getText());

    }
}
