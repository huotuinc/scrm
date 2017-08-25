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
        LocalDate dayOfMonth = LocalDate.now().withDayOfMonth(1);
        mockMonthReport(userBuddy.getId(), customer.getId(), false, 1, 1, 1, 1, dayOfMonth.minusMonths(1));
        mockMonthReport(userBuddy.getId(), customer.getId(), false, 2, 2, 2, 2, dayOfMonth.minusMonths(2));
        mockMonthReport(userBuddy.getId(), customer.getId(), false, 3, 3, 3, 3, dayOfMonth.minusMonths(3));
        mockMonthReport(userBuddy.getId(), customer.getId(), false, 4, 4, 4, 4, dayOfMonth.minusMonths(4));
        mockMonthReport(userBuddyIsSales.getId(), customer.getId(), false, 1, 1, 1, 1, dayOfMonth.minusMonths(1));
        mockMonthReport(userBuddyIsSales.getId(), customer.getId(), false, 2, 2, 2, 2, dayOfMonth.minusMonths(2));
        mockMonthReport(userBuddyIsSales.getId(), customer.getId(), false, 3, 3, 3, 3, dayOfMonth.minusMonths(3));
        mockMonthReport(userBuddyIsSales.getId(), customer.getId(), false, 4, 4, 4, 4, dayOfMonth.minusMonths(4));
    }

    /**
     * 测试积分详情
     * 今日积分：2 昨日积分：2 （本月积分不包含今日预计积分）
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
        List<WebElement> weui_cell_ft = webDriver.findElements(By.className("weui_cell_ft"));
        //本月(昨日2，不包含今日预计积分)
        Assert.assertEquals("2分", weui_cell_ft.get(10).getText());
        //上月（以此类推）
        Assert.assertEquals("1分", weui_cell_ft.get(11).getText());
        Assert.assertEquals("2分", weui_cell_ft.get(12).getText());
        Assert.assertEquals("3分", weui_cell_ft.get(13).getText());
        Assert.assertEquals("4分", weui_cell_ft.get(14).getText());
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
        webDriver.get(url);
        webDriver.findElements(By.className("weui_navbar_item")).get(3).click();
        WebElement follow = webDriver.findElement(By.id("follow"));
        //关注排名 （因为定时任务统计的原因，显示的是200名以外）
        Assert.assertEquals(2, Integer.parseInt(follow.findElements(By.tagName("p")).get(1).getText()));
        List<WebElement> sj = follow.findElements(By.className("sj"));
        Assert.assertEquals("200名以外", sj.get(0).getText());
        List<WebElement> weui_cell_ft = webDriver.findElements(By.className("weui_cell_ft"));
        //本月排名
        Assert.assertEquals("200名以外", weui_cell_ft.get(15).getText());
        //上月（以此类推）
        Assert.assertEquals("第1位", weui_cell_ft.get(16).getText());
        Assert.assertEquals("第2位", weui_cell_ft.get(17).getText());
        Assert.assertEquals("第3位", weui_cell_ft.get(18).getText());
        Assert.assertEquals("第4位", weui_cell_ft.get(19).getText());
    }

    /**
     * 测试今日uv详情页面
     *
     * @throws Exception
     */
    @Test
    public void testUv() throws Exception {
        Info info = mockInfo(customer.getId());
        mockInfoBrowse(info.getId(), userBuddy.getId(), customer.getId());
        mockInfoBrowse(info.getId(), userBuddy.getId(), customer.getId());
        mockDayReport(userBuddy.getId(), customer.getId(), 1, 1, LocalDate.now().minusDays(1));
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + url + "?customerId=" + customer.getId());
        webDriver.findElements(By.className("weui_navbar_item")).get(1).click();
        WebElement uv = webDriver.findElement(By.id("uv"));
        //今日uv
        Assert.assertEquals(2, Integer.parseInt(uv.findElements(By.tagName("p")).get(1).getText()));
        //本月(今日2 + 昨日1)
        Assert.assertEquals(3, Integer.parseInt(uv.findElements(By.tagName("p")).get(4).getText()));
        List<WebElement> weui_cell_ft = webDriver.findElements(By.className("weui_cell_ft"));
        //本月(今日2 + 昨日1)
        Assert.assertEquals("3个", weui_cell_ft.get(5).getText());
        //上月（以此类推）
        Assert.assertEquals("1个", weui_cell_ft.get(6).getText());
        Assert.assertEquals("2个", weui_cell_ft.get(7).getText());
        Assert.assertEquals("3个", weui_cell_ft.get(8).getText());
        Assert.assertEquals("4个", weui_cell_ft.get(9).getText());
    }

    /**
     * 测试积分排名信息
     */
    @Test
    public void testScoreRanking() throws Exception {
        Info info = mockInfo(customer.getId());
        mockInfoConfigure(customer.getId(), true, 1, 1, false, 2);
        mockInfoBrowse(info.getId(), userBuddy.getId(), customer.getId());
        mockInfoBrowse(info.getId(), userBuddy.getId(), customer.getId());
        mockInfoBrowse(info.getId(), userBuddyIsSales.getId(), customer.getId());
        mockDayReport(userBuddy.getId(), customer.getId(), 2, 2, LocalDate.now().minusDays(1));
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + url + "?customerId=" + customer.getId());
        webDriver.findElements(By.className("weui_navbar_item")).get(0).click();
        WebElement ranking = webDriver.findElement(By.id("ranking"));
        //今日预计积分排名 （定时统计的原因显示200名以外）
        Assert.assertEquals("200名以外", ranking.findElements(By.tagName("p")).get(1).getText());
        //本月积分排名（定时统计的原因显示200名以外）
        Assert.assertEquals("200名以外", ranking.findElements(By.tagName("p")).get(4).getText());
        List<WebElement> weui_cell_ft = webDriver.findElements(By.className("weui_cell_ft"));
        //本月排名
        Assert.assertEquals("200名以外", weui_cell_ft.get(0).getText());
        //上月（以此类推）
        Assert.assertEquals("第1位", weui_cell_ft.get(1).getText());
        Assert.assertEquals("第2位", weui_cell_ft.get(2).getText());
        Assert.assertEquals("第3位", weui_cell_ft.get(3).getText());
        Assert.assertEquals("第4位", weui_cell_ft.get(4).getText());
    }
}
