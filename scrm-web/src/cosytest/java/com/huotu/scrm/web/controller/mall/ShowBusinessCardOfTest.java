package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.config.ServiceConfig;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.web.controller.WebTestOfMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

/**
 * Created by cosy on 2017/7/21.
 * 1.验证已关注人数
 * 2.取消关注后，验证已关注人数
 * 3.验证显示的各项值是否正确
 * (value = "spring4.1-htmlunit/src/main/webapp")
 */

@ActiveProfiles(value = {"development"})
public class ShowBusinessCardOfTest extends WebTestOfMock {

    public BusinessCard businessCard;
    public WebDriverWait wait;
    public  User user;

   @Before
   public void  first()throws Exception{
       //创建数据
       Customer customer= mockCustomer();
       UserLevel userLevel= mockUserLevel(customer.getId(), UserType.buddy,true);
       user= mockUser(customer.getId(),userLevel);

       businessCard=createBusinessCardMock(customer,user);

       wait = new WebDriverWait(webDriver, 10);

       //用户登入
       mockUserLogin(user.getId(),customer.getId());
       webDriver.get("http://localhost/site/businessCard/showBusinessCard?customerId="+customer.getId()+"&salesmanId="+user.getId());
   }



    /**
     * 验证已关注人数
     * @throws Exception
     */
    @Test
    public void personUnmber() throws Exception{
        Assert.assertEquals("已关注1人", webDriver.findElement(By.xpath("html/body/div[1]/div[1]/p[1]")).getText());
    }



    /**
     * 验证显示的各项值是否正确
     * @throws Exception
     */

    @Test
    public void allMessage()throws Exception{
        Assert.assertEquals(user.getUserMobile(),webDriver.findElement(By.xpath("html/body/div[2]/a[1]/div/p/span")).getText());
        Assert.assertEquals("13600541783",webDriver.findElement(By.xpath("html/body/div[2]/a[2]/div/p/span")).getText());
        Assert.assertEquals("826449783",webDriver.findElement(By.xpath("html/body/div[2]/a[3]/div/p/span")).getText());
        Assert.assertEquals("826449783@qq.com",webDriver.findElement(By.xpath("html/body/div[2]/a[4]/div/p/span")).getText());
        Assert.assertEquals("杭州火图科技有限公司",webDriver.findElement(By.xpath("html/body/div[2]/a[5]/div/p/span")).getText());
        Assert.assertEquals("浙江省阡陌路智慧E谷",webDriver.findElement(By.xpath("html/body/div[2]/a[6]/div/p/span")).getText());

        //Assert.assertEquals(user.getLoginName(),webDriver.findElement(By.xpath("html/body/div[1]/div[2]/div/p[1]/i")).getText());
        Assert.assertEquals("测试狗",webDriver.findElement(By.xpath("html/body/div[1]/div[2]/div/p[2]/b")).getText());
    }
}
