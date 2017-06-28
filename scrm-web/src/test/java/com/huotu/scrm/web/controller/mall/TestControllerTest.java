package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.controller.page.mall.TestIndexPage;
import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by helloztt on 2017-06-28.
 */
public class TestControllerTest extends CommonTestBase {
    private String baseUrl = "/mall/test";
    private String indexControllerUrl = baseUrl + "/index";
    private String htmlIndexControllerUrl = baseUrl + "/index/html";
    private Long merchantId;

    @Before
    public void init() {
        merchantId = Long.valueOf(random.nextInt(10000));
    }

    @Test
    public void index() throws Exception {

        //case 1:post
        //expect:method not allowed
        mockMvc.perform(post(indexControllerUrl)
                .param("customerId", String.valueOf(merchantId)))
                .andExpect(status().isMethodNotAllowed());

        //case 2:no param
        //expect:redirect to login page
        testWithNoParam(indexControllerUrl,0);

        //case 3: get with param
        //expect: return msg
        String expectResultMsg = "hello scrm,merchantId:" + merchantId + " !";
        mockMvc.perform(get(indexControllerUrl)
                .param("customerId", String.valueOf(merchantId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(expectResultMsg));
    }

    @Test
    public void htmlIndex() throws Exception {

        //case 1:post
        //expect:method not allowed
        mockMvc.perform(post(htmlIndexControllerUrl)
                .param("customerId", String.valueOf(merchantId)))
                .andExpect(status().isMethodNotAllowed());

        //case 2:no param
        //expect:redirect to login page
        testWithNoParam(htmlIndexControllerUrl,0);

        //case 3:
        mockMvc.perform(get(htmlIndexControllerUrl)
        .param("customerId", String.valueOf(merchantId)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("title"))
                .andExpect(view().name("test"));

    }

    @Test
    public void htmlIndexPageTest(){
        //test selenium
        webDriver.get("http://localhost" + htmlIndexControllerUrl + "?customerId=" + merchantId);
        TestIndexPage textIndex = initPage(TestIndexPage.class);
        textIndex.setMerchantId(merchantId);
        textIndex.validate();
    }

}