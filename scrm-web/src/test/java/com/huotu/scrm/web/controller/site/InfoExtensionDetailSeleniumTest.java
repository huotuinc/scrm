package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by hxh on 2017-08-21.
 */
public class InfoExtensionDetailSeleniumTest extends CommonTestBase {
    private String baseUrl = "/site/extension";
    private String url = "/site/extension/getInfoDetail";
    private Customer customer;
    private User userBuddy;
    private User userBuddyIsSales;

    @Before
    public void setInfo() throws Exception {
        customer = mockCustomer();
        UserLevel userLevelBuddy = mockUserLevel(customer.getId(), UserType.buddy, false);
        UserLevel userLevelBuddyIsSales = mockUserLevel(customer.getId(), UserType.buddy, true);
        userBuddy = mockUser(customer.getId(), userLevelBuddy);
        userBuddyIsSales = mockUser(customer.getId(), userLevelBuddyIsSales);
    }

    /**
     * 测试积分详情
     * 今日积分：2 昨日积分：2
     * 历史累计（推广中心有测）
     */
    @Test
    public void testScoreInfo() throws Exception {
        Info info = mockInfo(customer.getId());
        mockInfoBrowse(info.getId(), userBuddy.getId(), customer.getId());
        mockInfoBrowse(info.getId(), userBuddy.getId(), customer.getId());
        //积分配置（默认设置一个uv一个积分，没有上限限制）
        mockInfoConfigure(customer.getId(), true, 1, 1, false, 2);
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + url + "?customerId=" + customer.getId());
        webDriver.findElements(By.className("weui_navbar_item")).get(2).click();
        WebElement score = webDriver.findElement(By.id("score"));
        Assert.assertEquals(2, Integer.parseInt(score.findElements(By.tagName("p")).get(1).getText()));
        //昨日积分
        mockDayReport(userBuddy.getId(), customer.getId(), 1, 2, LocalDate.now().minusDays(1));
        webDriver.get("http://localhost" + url + "?customerId=" + customer.getId());
        webDriver.findElements(By.className("weui_navbar_item")).get(2).click();
        WebElement score1 = webDriver.findElement(By.id("score"));
        Assert.assertEquals(2, Integer.parseInt(score1.findElements(By.tagName("p")).get(4).getText()));
        // TODO: 2017-08-21 近几个月积分信息 

    }

    /**
     * 测试关注详情(默认是销售员登录)
     * 关注量：2
     */
    @Test
    public void testFollowInfo() throws Exception {
        mockBusinessCardRecord(userBuddyIsSales.getId(), customer.getId());
        mockBusinessCardRecord(userBuddyIsSales.getId(), customer.getId());
        mockUserLogin(userBuddyIsSales.getId(), customer.getId());
        //关注人数
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_navbar_item = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item.get(1).click();
        WebElement weui_cell = webDriver.findElement(By.className("weui_cell"));
        String url = weui_cell.getAttribute("href");
        webDriver.get(url + "?customerId=" + customer.getId());
        webDriver.findElements(By.className("weui_navbar_item")).get(3).click();
        WebElement follow = webDriver.findElement(By.id("follow"));
        //关注排名 （因为定时任务统计的原因，显示的是200名以外）
        Assert.assertEquals(2, Integer.parseInt(follow.findElements(By.tagName("p")).get(1).getText()));
        List<WebElement> sj = follow.findElements(By.className("sj"));
        Assert.assertEquals("200名以外", sj.get(0).getText());
        // TODO: 2017-08-21 近几个月排名信息以及最高月的排名
    }
}
