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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * 测试资讯进入状态，具体页面数据测试在webDriver测试
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
     * 普通会员：infoextension/info_extension页面
     * 小伙伴：infoextension/info_center页面
     * model中statisticalInformation标签存储统计信息
     * model中status标签存储销售员信息
     *
     * @throws Exception
     */
    @Test
    public void getInfoExtension() throws Exception {

        //小伙伴登录，有排名积分等信息（model中有statisticalInformation标签,status标签为false，不是销售员）
        mockMvc.perform(get(baseUrl + "/getInfoExtension")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddy))
                .andExpect(status().isOk())
                .andExpect(view().name("infoextension/info_center"))
                .andExpect(model().attributeExists("infoModes"))
                .andExpect(model().attributeExists("statisticalInformation"))
                .andExpect(model().attribute("status", false));
        //小伙伴且是销售员登录，有排名积分等信息（model中有statisticalInformation标签,status标签为true，是销售员）
        mockMvc.perform(get(baseUrl + "/getInfoExtension")
                .param("customerId", String.valueOf(customer.getId()))
                .cookie(cookieBuddyIsSales))
                .andExpect(status().isOk())
                .andExpect(view().name("infoextension/info_center"))
                .andExpect(model().attributeExists("infoModes"))
                .andExpect(model().attributeExists("statisticalInformation"))
                .andExpect(model().attribute("status", true));
    }
}
