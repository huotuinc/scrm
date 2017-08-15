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
 * Created by hxh on 2017-07-21.
 */
public class InfoExtensionControllerSeleniumTest extends CommonTestBase {
    private String baseUrl = "/site/extension";
    private Customer customer;
    private User userNormal;
    private User userBuddy;
    private User userBuddyIsSales;

    @Before
    public void setInfo() throws Exception {
        customer = mockCustomer();
        UserLevel userLevelNormal = mockUserLevel(customer.getId(), UserType.normal, false);
        UserLevel userLevelBuddy = mockUserLevel(customer.getId(), UserType.buddy, false);
        UserLevel userLevelBuddyIsSales = mockUserLevel(customer.getId(), UserType.buddy, true);
        userNormal = mockUser(customer.getId(), userLevelNormal);
        userBuddy = mockUser(customer.getId(), userLevelBuddy);
        userBuddyIsSales = mockUser(customer.getId(), userLevelBuddyIsSales);
    }

    /**
     * 测试进入资讯状态（普通会员和小伙伴进入不同的页面）
     * 普通会员：资讯推广
     * 小伙伴：推广中心
     *
     * @throws Exception
     */
    @Test
    public void getInfoExtension() throws Exception {
        //普通会员请求，转到资讯推广页面
        mockUserLogin(userNormal.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        Assert.assertEquals("资讯推广", webDriver.getTitle());
        //小伙伴请求，转到推广中心页面
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        Assert.assertEquals("推广中心", webDriver.getTitle());
    }

    /**
     * 测试资讯推广和推广中心页面
     * 判断普通会员和小伙伴资讯显示情况
     * isStatus:判断普通会员
     * isExtend:判断小伙伴
     */
    @Test
    public void testInfoStatus() throws Exception {
        mockInfo(customer.getId(), true, true);
        mockInfo(customer.getId(), true, true);
        //普通会员请求
        mockUserLogin(userNormal.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> elements = webDriver.findElements(By.className("weui_media_title"));
        Assert.assertEquals(2, elements.size());
        //小伙伴请求
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> elements1 = webDriver.findElements(By.className("weui_media_title"));
        Assert.assertEquals(2, elements1.size());
        //改变资讯的状态
        mockInfo(customer.getId(), false, false);
        mockUserLogin(userNormal.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> elements2 = webDriver.findElements(By.className("weui_media_title"));
        Assert.assertEquals(2, elements2.size());
        //小伙伴请求
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> elements3 = webDriver.findElements(By.className("weui_media_title"));
        Assert.assertEquals(2, elements3.size());
    }


    /**
     * 若是小伙伴请求判断是否为销售员
     * 销售员有关注tab
     *
     * @throws Exception
     */
    @Test
    public void getFollowTag() throws Exception {
        //小伙伴（不是销售员）请求
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> elementList = webDriver.findElements(By.className("sj"));
        Assert.assertEquals(4, elementList.size());
        //小伙伴（销售员）请求，有关注tab
        mockUserLogin(userBuddyIsSales.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> elementList1 = webDriver.findElements(By.className("sj"));
        Assert.assertEquals(5, elementList1.size());
    }

    /**
     * 测试今日访客量（uv）信息（顺便测试资讯的浏览和转发量）
     * 用户uv：2
     * 资讯转发量：1
     * 资讯uv：2
     *
     * @throws Exception
     */
    @Test
    public void getVisitorInfo() throws Exception {
        Info info = mockInfo(customer.getId(), true, true);
        mockInfoBrowse(info.getId(), userBuddy.getId(), customer.getId());
        mockInfoBrowse(info.getId(), userBuddy.getId(), customer.getId());
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_media_title = webDriver.findElements(By.className("weui_media_title"));
        //我的推广默认也还有一条
        Assert.assertEquals(2, weui_media_title.size());
        List<WebElement> elements = webDriver.findElements(By.className("weui_media_info_meta"));
        //资讯转发量
        Assert.assertEquals(1, Integer.parseInt(elements.get(0).getText()));
        //资讯浏览量
        Assert.assertEquals(2, Integer.parseInt(elements.get(1).getText()));
        List<WebElement> weui_navbar_item = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item.get(1).click();
        //用户uv
        List<WebElement> sj = webDriver.findElements(By.className("sj"));
        Assert.assertEquals(2, Integer.parseInt(sj.get(1).getText()));
        // TODO: 2017-08-12 我的推广页面资讯的转发和浏览
    }

    /**
     * 测试今日预计积分（转发积分不测，只测试浏览奖励积分）
     * 用户uv：5 积分配置：是否开启奖励，奖励对象，奖励比例，每日最高限额
     * 浏览积分计算直接取整
     */
    @Test
    public void getScoreInfo() throws Exception {
        mockInfoBrowse(1L, userBuddy.getId(), customer.getId());
        mockInfoBrowse(1L, userBuddy.getId(), customer.getId());
        mockInfoBrowse(2L, userBuddy.getId(), customer.getId());
        mockInfoBrowse(2L, userBuddy.getId(), customer.getId());
        mockInfoBrowse(2L, userBuddy.getId(), customer.getId());
        //没有开启浏览积分奖励
        mockInfoConfigure(customer.getId(), false);
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_navbar_item = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item.get(1).click();
        List<WebElement> elements = webDriver.findElements(By.className("sj"));
        String dayScore = elements.get(2).getText();
        Assert.assertEquals(0, Integer.parseInt(dayScore));
        //开启浏览积分奖励,奖励对象为普通会员
        mockInfoConfigure(customer.getId(), true, 0, 2, false, 1);
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_navbar_item2 = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item2.get(1).click();
        List<WebElement> elements1 = webDriver.findElements(By.className("sj"));
        String dayScore1 = elements1.get(2).getText();
        Assert.assertEquals(0, Integer.parseInt(dayScore1));
        //开启浏览积分奖励,奖励对象为小伙伴，比例：2,没有每日最高限额，奖励积分2（直接取整）
        mockInfoConfigure(customer.getId(), true, 1, 2, false, 1);
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_navbar_item3 = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item3.get(1).click();
        List<WebElement> elements2 = webDriver.findElements(By.className("sj"));
        String dayScore2 = elements2.get(2).getText();
        Assert.assertEquals(2, Integer.parseInt(dayScore2));
        //开启浏览积分奖励,奖励对象为小伙伴，比例：2,有每日最高限额：1  奖励积分1
        mockInfoConfigure(customer.getId(), true, 1, 2, true, 1);
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_navbar_item4 = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item4.get(1).click();
        List<WebElement> elements3 = webDriver.findElements(By.className("sj"));
        String dayScore3 = elements3.get(2).getText();
        Assert.assertEquals(1, Integer.parseInt(dayScore3));
        //开启浏览积分奖励,奖励对象为小伙伴，比例：2,有每日最高限额：3  奖励积分2
        mockInfoConfigure(customer.getId(), true, 1, 2, true, 3);
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_navbar_item5 = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item5.get(1).click();
        List<WebElement> elements4 = webDriver.findElements(By.className("sj"));
        String dayScore4 = elements4.get(2).getText();
        Assert.assertEquals(2, Integer.parseInt(dayScore4));
    }

    /**
     * 测试历史累积积分（）
     * 累积积分 = monthReport(查询以前积分记录)+本月统计
     * 本月统计 = 本月1日-昨日读取 dayReport（不包含今日的预计积分）
     * monthReport：2条记录积分共：70 = 30 + 40
     * dayReport：2条记录积分共：30 = 10 + 20
     * 合计：100
     */
    @Test
    public void getAccumulate() throws Exception {
        LocalDate date = LocalDate.now().withDayOfMonth(1).minusMonths(1);
        LocalDate lastDay = LocalDate.now().minusDays(1);
        mockMonthReport(userBuddyIsSales.getId(), customer.getId(), true, 20, 30, 1, 2, date);
        mockMonthReport(userBuddyIsSales.getId(), customer.getId(), true, 10, 40, 2, 2, date.minusMonths(1));
        mockDayReport(userBuddyIsSales.getId(), customer.getId(), 10, 10, lastDay);
        mockDayReport(userBuddyIsSales.getId(), customer.getId(), 10, 20, lastDay.minusDays(1));
        mockUserLogin(userBuddyIsSales.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_navbar_item = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item.get(1).click();
        List<WebElement> elements = webDriver.findElements(By.className("sj"));
        String accumulateScore = elements.get(3).getText();
        Assert.assertEquals(100, Integer.parseInt(accumulateScore));
    }

    /**
     * 测试关注人数（销售员）
     * 关注人数：2
     */
    @Test
    public void getFollowNum() throws Exception {
        mockBusinessCardRecord(userBuddyIsSales.getId(), customer.getId());
        mockBusinessCardRecord(userBuddyIsSales.getId(), customer.getId());
        mockUserLogin(userBuddyIsSales.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_navbar_item = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item.get(1).click();
        List<WebElement> elements = webDriver.findElements(By.className("sj"));
        String accumulateScore = elements.get(4).getText();
        Assert.assertEquals(2, Integer.parseInt(accumulateScore));
    }

    /**
     * 测试今日uv排名
     * 若今日uv为0不参与排名，显示：--
     * 若今日uv相同，不会出现两个相同的排名，两者排名的顺序随机
     */
    @Test
    public void getVisitorRanking() throws Exception {
        //没有uv,显示：--
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> weui_navbar_item = webDriver.findElements(By.className("weui_navbar_item"));
        weui_navbar_item.get(1).click();
        List<WebElement> elements = webDriver.findElements(By.className("sj"));
        String accumulateScore = elements.get(0).getText();
        Assert.assertEquals("--", accumulateScore);
        // TODO: 2017-08-12 有uv,排名显示 
    }
}
