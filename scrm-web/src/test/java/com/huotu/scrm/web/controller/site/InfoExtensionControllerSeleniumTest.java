package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
     * 判断普通会员和小伙伴资讯查看情况（isStatus:普通会员，isExtend:小伙伴）
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
     * 若是小伙伴请求判断是否为销售员，销售员有关注tab
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
        //小伙伴（销售员）请求，有官职tab
        mockUserLogin(userBuddyIsSales.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> elementList1 = webDriver.findElements(By.className("sj"));
        Assert.assertEquals(5, elementList1.size());
    }

    /**
     * 测试今日访客量（uv）信息（顺便测试资讯的浏览和转发量）
     * 用户uv：2  资讯转发：1  资讯uv：2
     *
     * @throws Exception
     */
    @Test
    public void getVisitorInfo() throws Exception {
        mockInfo(customer.getId(), true, true);
        mockInfoBrowse(1L, userBuddy.getId(), customer.getId());
        mockInfoBrowse(1L, userBuddy.getId(), customer.getId());
        mockUserLogin(userBuddy.getId(), customer.getId());
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension?customerId=" + customer.getId());
        List<WebElement> elements = webDriver.findElements(By.className("sj"));
        String visitorNum = elements.get(1).getText();
        Assert.assertEquals(2, Integer.parseInt(visitorNum));
        List<WebElement> elements1 = webDriver.findElements(By.className("weui_media_info"));
        List<WebElement> infoMeta = elements1.get(0).findElements(By.className("weui_media_info_meta"));
        //转发量
        String forwardNum = infoMeta.get(0).getText();
        //浏览量
        String uv = infoMeta.get(1).getText();
        Assert.assertEquals(1, Integer.parseInt(forwardNum));
        Assert.assertEquals(2, Integer.parseInt(uv));
    }
}
