package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.common.utils.EncryptUtils;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.interceptor.UserInterceptor;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by hxh on 2017-07-21.
 */
public class InfoExtensionControllerTest extends CommonTestBase {
    private String baseUrl = "/site/extension";
    private Customer customer;
    private User userNormal;
    private User userBuddy;
    private User userBuddyIsSales;
    private Cookie cookieNormal;
    private Cookie cookieBuddy;
    private Cookie cookieBuddyIsSales;

    @Before
    public void setInfo() throws Exception {
        customer = mockCustomer();
        UserLevel userLevelNormal = mockUserLevel(customer.getId(), UserType.normal, false);
        UserLevel userLevelBuddy = mockUserLevel(customer.getId(), UserType.buddy, false);
        UserLevel userLevelBuddyIsSales = mockUserLevel(customer.getId(), UserType.buddy, true);
        userNormal = mockUser(customer.getId(), userLevelNormal);
        userBuddy = mockUser(customer.getId(), userLevelBuddy);
        userBuddyIsSales = mockUser(customer.getId(), userLevelBuddyIsSales);
        String cookieName = UserInterceptor.USER_ID_PREFIX + customer.getId();
        String cookieValueNormal = EncryptUtils.aesEncrypt(String.valueOf(userNormal.getId()), UserInterceptor.USER_ID_SECRET_KEY);
        String cookieValueBuddy = EncryptUtils.aesEncrypt(String.valueOf(userBuddy.getId()), UserInterceptor.USER_ID_SECRET_KEY);
        String cookieValueBuddyIsSales = EncryptUtils.aesEncrypt(String.valueOf(userBuddyIsSales.getId()), UserInterceptor.USER_ID_SECRET_KEY);
        cookieNormal = new Cookie(cookieName, cookieValueNormal);
        cookieBuddy = new Cookie(cookieName, cookieValueBuddy);
        cookieBuddyIsSales = new Cookie(cookieName, cookieValueBuddyIsSales);
    }

    /**
     * 测试进入资讯状态
     * model中statisticalInformation标签存储统计信息
     * model中status标签存储销售员信息
     *
     * @throws Exception
     */
    @Test
    public void getInfoExtension() throws Exception {
        //普通会员登录，没有排名积分等信息（model中没有statisticalInformation标签，没有status标签：判断是否为销售员）
        mockMvc.perform(get(baseUrl + "/getInfoExtension")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieNormal))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("infoModes"))
                .andExpect(model().attributeDoesNotExist("statisticalInformation"))
                .andExpect(model().attributeDoesNotExist("status"));
        //小伙伴登录，有排名积分等信息（model中有statisticalInformation标签,status标签为false，不是销售员）
        mockMvc.perform(get(baseUrl + "/getInfoExtension")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddy))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("infoModes"))
                .andExpect(model().attributeExists("statisticalInformation"))
                .andExpect(model().attribute("status", false));
        //小伙伴且是销售员登录，有排名积分等信息（model中有statisticalInformation标签,status标签为true，是销售员）
        mockMvc.perform(get(baseUrl + "/getInfoExtension")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddyIsSales))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("infoModes"))
                .andExpect(model().attributeExists("statisticalInformation"))
                .andExpect(model().attribute("status", true));
    }

    /**
     * 测试今日排名信息
     * model中dayScoreRankingInfo标签存储排名信息
     * model中status标签存储销售员信息
     *
     * @throws Exception
     */
    @Test
    public void getScoreRanking() throws Exception {
        //如果普通会员请求，会重定向到进入资讯状态请求,状态为302
        mockMvc.perform(get(baseUrl + "/getScoreRanking")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieNormal))
                .andExpect(status().is(302));
        //小伙伴发送请求，有积分排名信息（model中有dayScoreRankingInfo标签,status标签为false，不是销售员）
        mockMvc.perform(get(baseUrl + "/getScoreRanking")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddy))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayScoreRankingInfo"))
                .andExpect(model().attribute("status", false));
        //小伙伴发送请求，有积分排名信息（model中有dayScoreRankingInfo标签,status标签为true，是销售员）
        mockMvc.perform(get(baseUrl + "/getScoreRanking")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddyIsSales))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayScoreRankingInfo"))
                .andExpect(model().attribute("status", true));
    }

    /**
     * 测试今日积分信息
     * model中dayScoreInfo标签存储排名信息
     * model中status标签存储销售员信息
     *
     * @throws Exception
     */
    @Test
    public void getScoreInfo() throws Exception {
        //如果普通会员请求，会重定向到进入资讯状态请求，状态为302
        mockMvc.perform(get(baseUrl + "/getScoreInfo")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieNormal))
                .andExpect(status().is(302));
        //小伙伴发送请求，有今日积分信息（model中有dayScoreInfo标签,status标签为false，不是销售员）
        mockMvc.perform(get(baseUrl + "/getScoreInfo")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddy))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayScoreInfo"))
                .andExpect(model().attribute("status", false));
        //小伙伴发送请求，有今日积分信息（model中有dayScoreInfo标签,status标签为true，是销售员）
        mockMvc.perform(get(baseUrl + "/getScoreInfo")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddyIsSales))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayScoreInfo"))
                .andExpect(model().attribute("status", true));
    }

    /**
     * 测试今日关注信息（销售员特有）
     * model中dayFollowNumInfo标签存储排名信息
     *
     * @throws Exception
     */
    @Test
    public void getFollowInfo() throws Exception {
        //如果普通会员请求，会重定向到进入资讯状态请求，状态为302
        mockMvc.perform(get(baseUrl + "/getScoreInfo")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieNormal))
                .andExpect(status().is(302));
        //小伙伴但不是销售员发送请求，也会重定向到资讯状态请求，状态为302
        mockMvc.perform(get(baseUrl + "/getFollowInfo")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddy))
                .andExpect(status().is(302));
        //小伙伴发送请求，有今日关注信息（model中有dayFollowNumInfo标签,是销售员）
        mockMvc.perform(get(baseUrl + "/getFollowInfo")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddyIsSales))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayFollowNumInfo"));
    }

    /**
     * 测试今日访问量信息
     *
     * @throws Exception
     */
    @Test
    public void getVisitorInfo() throws Exception {
        //如果普通会员请求，会重定向到进入资讯状态请求，状态为302
        mockMvc.perform(get(baseUrl + "/getVisitorInfo")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieNormal))
                .andExpect(status().is(302));
        //小伙伴发送请求，有今日uv信息（model中有dayVisitorNumInfo标签,status标签为false，不是销售员）
        mockMvc.perform(get(baseUrl + "/getVisitorInfo")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddy))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayVisitorNumInfo"))
                .andExpect(model().attribute("status", false));
        //小伙伴发送请求，有今日uv信息（model中有dayVisitorNumInfo标签,status标签为true，是销售员）
        mockMvc.perform(get(baseUrl + "/getVisitorInfo")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddyIsSales))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayVisitorNumInfo"))
                .andExpect(model().attribute("status", true));
    }
}
