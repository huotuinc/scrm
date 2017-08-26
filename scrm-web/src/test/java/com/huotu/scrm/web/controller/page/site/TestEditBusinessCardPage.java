package com.huotu.scrm.web.controller.page.site;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.web.controller.page.AbstractPage;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Created by Jinxiangdong on 2017/7/18.
 */
public class TestEditBusinessCardPage extends AbstractPage {

    @FindBy(id = "img_avatar")
    WebElement img_avatar;
    @FindBy(id = "div_companyname")
    WebElement div_companyname;
    @FindBy(id = "div_job")
    WebElement div_job;
    @FindBy(id = "div_tel")
    WebElement div_tel;
    @FindBy(id = "div_qq")
    WebElement div_qq;
    @FindBy(id = "div_email")
    WebElement div_email;
    @FindBy(id = "div_companyaddress")
    WebElement div_companyaddress;
    @FindBy(id = "customerId")
    WebElement customerId;
    @FindBy(id = "userId")
    WebElement userId;
    @FindBy(id = "btnInput")
    WebElement btnFile;

    @FindBy(id = "weuishow_qq")
    WebElement weuishow_qq;


    BusinessCard businessCard;
    UserRepository userRepository;

    public BusinessCard getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TestEditBusinessCardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validate() {
        //this.webDriver.findElement()
        //WebElement ele = this.webDriver.findElement(By.id("div_job"));

        Assert.assertTrue(img_avatar.getAttribute("src").contains(businessCard.getAvatar()));
        Assert.assertTrue(StringUtils.isEmpty(businessCard.getCompanyName()) && div_companyname.getText().isEmpty()
                || !StringUtils.isEmpty(businessCard.getCompanyName()) && div_companyname.getText().equals(businessCard.getCompanyName())
        );

        Assert.assertTrue(StringUtils.isEmpty(businessCard.getJob()) && div_job.getText().isEmpty()
                || !StringUtils.isEmpty(businessCard.getJob()) && div_job.getText().equals(businessCard.getJob()));

        Assert.assertTrue(StringUtils.isEmpty(businessCard.getTel()) && div_tel.getText().isEmpty()
                || !StringUtils.isEmpty(businessCard.getTel()) && div_tel.getText().equals(businessCard.getTel()));

        Assert.assertTrue(StringUtils.isEmpty(businessCard.getCompanyAddress()) && div_companyaddress.getText().isEmpty()
                || !StringUtils.isEmpty(businessCard.getCompanyAddress()) && div_companyaddress.getText().equals(businessCard.getCompanyAddress()));


        Assert.assertTrue(StringUtils.isEmpty(businessCard.getQq()) && div_qq.getText().isEmpty()
                || !StringUtils.isEmpty(businessCard.getQq()) && div_qq.getText().equals(businessCard.getQq()));


        Assert.assertTrue(StringUtils.isEmpty(businessCard.getEmail()) && div_email.getText().isEmpty()
                || !StringUtils.isEmpty(businessCard.getEmail()) && div_email.getText().equals(businessCard.getEmail()));

        Assert.assertEquals(String.valueOf(businessCard.getCustomerId()), customerId.getAttribute("value"));
        Assert.assertEquals(String.valueOf(businessCard.getUserId()), userId.getAttribute("value"));


        //btnFile.sendKeys("C:\\Users\\Administrator\\Desktop\\法人证件正反面.jpg");

        //String url= img_avatar.getAttribute("src");

        //修改QQ字段
        weuishow_qq.click();
        WebElement ele = webDriver.findElement(By.id("weui-prompt-input"));

        ele.clear();
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
        webDriverWait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                WebElement temp = webDriver.findElement(By.id("weui-prompt-input"));
                if (temp != null && temp.getAttribute("value").isEmpty()) {
                    return true;
                }
                return false;
            }
        });

        String qq = UUID.randomUUID().toString().substring(0 ,20);
        ele.sendKeys(qq );

        webDriverWait = new WebDriverWait(webDriver, 10);
        webDriverWait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                WebElement temp = webDriver.findElement(By.id("weui-prompt-input"));
                if (temp != null && !temp.getAttribute("value").isEmpty()) {
                    Boolean isShow = temp.isDisplayed();
                    return true;
                }
                return false;
            }
        });

        Assert.assertEquals(ele.getAttribute("value"), qq );


        WebElement ele2 = webDriver.findElement(By.linkText("确定"));
        WebElement eleParent = ele2.findElement(By.xpath(".."));
        eleParent.click();
        ele2.click();

        webDriverWait = new WebDriverWait(webDriver , 30);
        webDriverWait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                WebElement temp = webDriver.findElement(By.id("weui-prompt-input"));
                Boolean isShow = temp.isDisplayed();
                if( !isShow ) return true;
                return false;
            }
        });

//        ele = webDriver.findElement(By.id("weui-prompt-input"));
//        Assert.assertEquals(null , ele);


        webDriverWait = new WebDriverWait(webDriver, 40);
        webDriverWait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                WebElement temp = webDriver.findElement(By.id("div_qq"));
                if (temp != null && !temp.getText().isEmpty() && temp.getText().equals(qq)) {
                    return true;
                }
                return false;
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(qq , div_qq.getText());

        // TODO: 2017-08-01 这里报错，还需要再调试
       WebElement inputFileEle = webDriver.findElement(By.id("btnInput"));
       String js = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';arguments[0].style.display='block';";
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor)webDriver;
        javascriptExecutor.executeScript(js , inputFileEle);

        webDriverWait = new WebDriverWait(webDriver,10);
        webDriverWait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                WebElement inputFileEle = webDriver.findElement(By.id("btnInput"));
                return  inputFileEle.isDisplayed();
            }
        });

        inputFileEle.sendKeys("e:\\1.png");

        webDriverWait = new WebDriverWait(webDriver, 10);
        webDriverWait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                WebElement temp  = webDriver.findElement(By.id("img_avatar"));
                if (temp != null && !temp.getAttribute("src").isEmpty()) {
                    return true;
                }
                return false;
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement temp  = webDriver.findElement(By.id("img_avatar"));
        String url = temp.getAttribute("src");

        Assert.assertFalse( url.isEmpty() );
    }
}
