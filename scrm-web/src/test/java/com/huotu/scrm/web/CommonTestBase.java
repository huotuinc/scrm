package com.huotu.scrm.web;

import com.huotu.scrm.common.SysConstant;
import com.huotu.scrm.web.config.MVCConfig;
import com.huotu.scrm.web.controller.page.AbstractPage;
import org.junit.runner.RunWith;
import org.openqa.selenium.support.PageFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 这里放一些公用的方法，参数之类的
 * Created by helloztt on 2017-06-28.
 */
@WebAppConfiguration
@ActiveProfiles({"test", "unit_test"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MVCConfig.class})
@Transactional
public abstract class CommonTestBase extends SpringWebTest{

    protected Random random = new Random();

    /**
     * 初始化逻辑页面
     *
     * @param clazz 该页面相对应的逻辑页面的类
     * @param <T>   该页面相对应的逻辑页面
     * @return 页面实例
     */
    public <T extends AbstractPage> T initPage(Class<T> clazz) {
        T page = PageFactory.initElements(webDriver, clazz);
        return page;
    }



    /**
     * 测试嵌入到伙伴商城的页面，不带参数 customerId 的情况
     *
     * @param controllerUrl 请求地址
     * @param method        请求方法
     *                      0：get
     *                      1：post
     */
    protected void testWithNoParam(String controllerUrl, Integer method) throws Exception {
        switch (method) {
            case 0:
            default:
                mockMvc.perform(get(controllerUrl))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl("http://login." + SysConstant.COOKIE_DOMAIN));
                break;
            case 1:
                mockMvc.perform(post(controllerUrl))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl("http://login." + SysConstant.COOKIE_DOMAIN));
                break;
        }
    }


}
