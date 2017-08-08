package com.huotu.scrm.web.controller.mall;



import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
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


//@ActiveProfiles(value = {"development"})
public class EditBusinessCardOfTest extends WebTestOfMock {

    /**
     *  编辑时检验置头像，企业名称，职位，固话，qq，邮箱，公司地址，验证数据是否正确
      * @throws Exception
     */
    public WebDriverWait wait;
    public BusinessCard businessCard;

    @Before
    public void first() throws Exception
    {
        //创建数据

        Customer customer= mockCustomer();
        UserLevel userLevel= mockUserLevel(customer.getId(), UserType.buddy,true);
        User user= mockUser(customer.getId(),userLevel);
        businessCard=createBusinessCardMock(customer,user);

        wait = new WebDriverWait(webDriver, 10);

        //用户登入
        mockUserLogin(user.getId(),customer.getId());
        webDriver.get("http://localhost/site/businessCard/editBusinessCard?customerId="+customer.getId()+"&salesmanId="+user.getId());
    }


    /**
     *    //验证企业名称
     * @throws Exception
     */
    @Test
    public  void  editCompanyName() throws Exception {

        //获取页面上显示的企业名称并与数据库中的数据比对
        String firstName = webDriver.findElement(By.id("div_companyname")).getText();
        Assert.assertEquals(firstName, businessCard.getCompanyName());


        //点击 显示弹框，并获取输入控件
        webDriver.findElement(By.xpath("./*//*[@id='weuishow_companyname']/div[1]/p")).click();
        WebElement companyName = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.id("weui-prompt-input"));
            }
        });

        //随机生成数据
        String changeCompanyName = UUID.randomUUID().toString();

        //清空控件原有值，设置随机值，点击保存按钮
        companyName.clear();
        companyName.sendKeys(changeCompanyName);
        webDriver.findElement(By.xpath("html/body/div[4]/div[3]/a[2]")).click();

        Thread.sleep(2000);

        //再次页面上显示的企业名称与设置的值是否一致
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
        //获取页面上显示的职位并与数据库中的数据比对
        String jobTest=webDriver.findElement(By.id("div_job")).getText();
        Assert.assertEquals(jobTest, businessCard.getJob());

        //点击 显示弹框，并获取输入控件
        webDriver.findElement(By.xpath(".//*[@id='weuishow_job']/div[1]/p")).click();
        WebElement job = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.id("weui-prompt-input"));
            }
        });


        //随机生成数据
        String changeJob = UUID.randomUUID().toString();

        //清空控件原有值，设置随机值，点击保存按钮
        job.clear();
        job.sendKeys(changeJob);
        webDriver.findElement(By.xpath("html/body/div[4]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        //再次页面上显示的job与设置的值是否一致
        String editJob=webDriver.findElement(By.id("div_job")).getText();
        Assert.assertEquals(editJob,changeJob);
    }


    /**
     * 验证固定电话
     * @throws Exception
     */
    @Test
    public void editTel()throws Exception{

       //获取页面上显示的固定电话并与数据库中的数据比对
        String telTest=webDriver.findElement(By.id("div_tel")).getText();
        Assert.assertEquals(telTest,businessCard.getTel());

        //点击 显示弹框，并获取输入控件
        webDriver.findElement(By.xpath(".//*[@id='weuishow_tel']/div[1]/p")).click();
        WebElement tel = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.xpath(".//*[@id='weui-prompt-input']"));
            }
        });

        //随机生成数据
        String changeTel = UUID.randomUUID().toString();

        //清空控件原有值，设置随机值，点击保存按钮
        tel.clear();
        tel.sendKeys(changeTel);
        webDriver.findElement(By.xpath("html/body/div[4]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        //再次页面上显示的job与设置的值是否一致
        String editTel=webDriver.findElement(By.id("div_tel")).getText();
        Assert.assertEquals(editTel,changeTel);

    }

    /**
     * 验证QQ  没有通过测试
     * @throws Exception
     */
    @Test
    public void editQq()throws Exception{

        //获取页面上显示的固定电话并与数据库中的数据比对
        String qqTest=webDriver.findElement(By.id("div_qq")).getText();
        Assert.assertEquals(qqTest,businessCard.getQq());

        //点击 显示弹框，并获取输入控件
        webDriver.findElement(By.xpath(".//*[@id='weuishow_qq']/div[1]/p")).click();
        WebElement qq = wait.until((ExpectedCondition<WebElement>) webdriver -> webdriver.findElement(By.xpath(".//*[@id='weui-prompt-input']")));

        //随机生成数据
        String changeQQ = UUID.randomUUID().toString();
        String subChangeQQ=changeQQ.substring(1,5);

        //清空控件原有值，设置随机值，点击保存按钮
        qq.clear();
        qq.sendKeys(subChangeQQ);
        webDriver.findElement(By.xpath("html/body/div[4]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        //再次页面上显示的qq与设置的值是否一致
        String editQQ=webDriver.findElement(By.id("div_qq")).getText();
        Assert.assertEquals(editQQ,subChangeQQ);
    }


    /**
     * 验证邮箱
     * @throws Exception
     */
    @Test
    public void editEmail()throws Exception{
        //获取页面上显示的固定电话并与数据库中的数据比对
        String emailTest=webDriver.findElement(By.id("div_email")).getText();
        Assert.assertEquals(emailTest,businessCard.getEmail());

        //点击 显示弹框，并获取输入控件
        webDriver.findElement(By.xpath(".//*[@id='weuishow_email']/div[1]/p")).click();
        WebElement email = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.xpath(".//*[@id='weui-prompt-input']"));
            }
        });


        //随机生成数据
        String changeTel = UUID.randomUUID().toString();

        //清空控件原有值，设置随机值，点击保存按钮
        email.clear();
        email.sendKeys(changeTel);
        webDriver.findElement(By.xpath("html/body/div[4]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        //再次页面上显示的job与设置的值是否一致
        String editEmail=webDriver.findElement(By.id("div_email")).getText();
        Assert.assertEquals(editEmail,changeTel);
    }

    /**
     * 验证公司地址
     * @throws Exception
     */
    @Test
    public void  editCompanyAddress()throws Exception{

        //获取页面上显示的固定电话并与数据库中的数据比对
        String companyAddressTest=webDriver.findElement(By.id("div_companyaddress")).getText();
        Assert.assertEquals(companyAddressTest,businessCard.getCompanyAddress());

        //点击 显示弹框，并获取输入控件
        webDriver.findElement(By.xpath(".//*[@id='weuishow_companyaddress']/div[1]/p")).click();
        WebElement companyAddress = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver webdriver) {
                return webdriver.findElement(By.xpath(".//*[@id='weui-prompt-input']"));
            }
        });

        //随机生成数据
        String changeTel = UUID.randomUUID().toString();

        //清空控件原有值，设置随机值，点击保存按钮
        companyAddress.clear();
        companyAddress.sendKeys(changeTel);
        webDriver.findElement(By.xpath("html/body/div[4]/div[3]/a[2]")).click();
        Thread.sleep(2000);

        //再次页面上显示的job与设置的值是否一致
        String editEmail=webDriver.findElement(By.id("div_companyaddress")).getText();
        Assert.assertEquals(editEmail,changeTel);
    }
}
