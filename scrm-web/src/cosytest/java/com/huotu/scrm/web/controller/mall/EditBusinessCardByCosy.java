package com.huotu.scrm.web.controller.mall;



import com.huotu.scrm.web.controller.WebTestOfMock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import org.openqa.selenium.By;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;


/**
 * Created by cosy on 2017/7/21.
 */


@ActiveProfiles(value = {"development"})
public class EditBusinessCardByCosy extends WebTestOfMock {


    /**
     *  编辑时检验置头像，企业名称，职位，固话，qq，邮箱，公司地址，验证数据是否正确
      * @throws Exception
     */
    public WebDriverWait wait;




    @Before
    public void first() throws Exception
    {
        createBusinessCardMock();
        wait = new WebDriverWait(webDriver, 10);
        String customerID = getCustomerMock().getId().toString();
        String userID = getUserMock().getId().toString();
        webDriver.get("http://localhost/site/businessCard/editBusinessCard?customerId=" + customerID + "&userId=" + userID);

    }

    /**
     *    //验证企业名称
     * @throws Exception
     */
    @Test
    public  void  editCompanyName() throws Exception {

        String firstName = webDriver.findElement(By.id("div_companyname")).getText();

        webDriver.findElement(By.xpath(".//*[@id='weuishow_companyname']/div[1]/p")).click();


        WebElement companyName = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.id("weui-prompt-input"));
            }
        });


        Assert.assertEquals(firstName, companyName.getAttribute("value").toString());

        String changeCompanyName = UUID.randomUUID().toString();

        companyName.clear();
        companyName.sendKeys(changeCompanyName);

        webDriver.findElement(By.xpath("html/body/div[3]/div[3]/a[2]")).click();



        Thread.sleep(2000);

        WebElement afterEditCompanyName = webDriver.findElement(By.id("div_companyname"));
        String editName = afterEditCompanyName.getText();

        Assert.assertEquals(changeCompanyName, editName);
    }


    /**
     * 验证职位
     * @throws Exception
     */
    @Test
    public void editJob()throws Exception{
        String jobTest=webDriver.findElement(By.id("div_job")).getText();
        webDriver.findElement(By.xpath(".//*[@id='weuishow_job']/div[1]/p")).click();

        WebElement job = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.id("weui-prompt-input"));
            }
        });

        Assert.assertEquals(jobTest, job.getAttribute("value").toString());

        String changeJob = UUID.randomUUID().toString();
        job.clear();
        job.sendKeys(changeJob);

        webDriver.findElement(By.xpath("html/body/div[3]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        String editJob=webDriver.findElement(By.id("div_job")).getText();
        Assert.assertEquals(editJob,changeJob);
    }


    /**
     * 验证固定电话
     * @throws Exception
     */
    @Test
    public void editTel()throws Exception{


        String telTest=webDriver.findElement(By.id("div_tel")).getText();
        webDriver.findElement(By.xpath(".//*[@id='weuishow_tel']/div[1]/p")).click();

        WebElement tel = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.xpath(".//*[@id='weui-prompt-input']"));
            }
        });
        Assert.assertEquals(telTest,tel.getAttribute("value").toString());

        String changeTel = UUID.randomUUID().toString();
        tel.clear();
        tel.sendKeys(changeTel);
        webDriver.findElement(By.xpath("html/body/div[3]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        String editTel=webDriver.findElement(By.id("div_tel")).getText();
        Assert.assertEquals(editTel,changeTel);

    }

    /**
     * 验证QQ
     * @throws Exception
     */
    @Test
    public void editQQ()throws Exception{
        String qqTest=webDriver.findElement(By.id("div_qq")).getText();
        webDriver.findElement(By.xpath(".//*[@id='weuishow_qq']/div[1]/p")).click();


        WebElement qq = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.xpath(".//*[@id='weui-prompt-input']"));
            }
        });

        Assert.assertEquals(qqTest,qq.getAttribute("value").toString());
        String changeQQ = UUID.randomUUID().toString();
        qq.clear();
        qq.sendKeys(changeQQ);
        webDriver.findElement(By.xpath("html/body/div[3]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        String editQQ=webDriver.findElement(By.id("div_qq")).getText();
        Assert.assertEquals(editQQ,changeQQ);
    }


    /**
     * 验证邮箱
     * @throws Exception
     */
    @Test
    public void editEmail()throws Exception{
        String emailTest=webDriver.findElement(By.id("div_email")).getText();
        webDriver.findElement(By.xpath(".//*[@id='weuishow_email']/div[1]/p")).click();
        WebElement email = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.xpath(".//*[@id='weui-prompt-input']"));
            }
        });

        Assert.assertEquals(emailTest,email.getAttribute("value").toString());

        String changeTel = UUID.randomUUID().toString();
        email.clear();
        email.sendKeys(changeTel);
        webDriver.findElement(By.xpath("html/body/div[3]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        String editEmail=webDriver.findElement(By.id("div_email")).getText();
        Assert.assertEquals(editEmail,changeTel);
    }

    @Test
    public void  editCompanyAddress()throws Exception{
        String companyAddressTest=webDriver.findElement(By.id("div_companyaddress")).getText();
        webDriver.findElement(By.xpath(".//*[@id='weuishow_companyaddress']/div[1]/p")).click();
        WebElement companyAddress = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.xpath(".//*[@id='weui-prompt-input']"));
            }
        });

        Assert.assertEquals(companyAddressTest,companyAddress.getAttribute("value").toString());

        String changeTel = UUID.randomUUID().toString();
        companyAddress.clear();
        companyAddress.sendKeys(changeTel);
        webDriver.findElement(By.xpath("html/body/div[3]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        String editEmail=webDriver.findElement(By.id("div_companyaddress")).getText();
        Assert.assertEquals(editEmail,changeTel);
    }
}
