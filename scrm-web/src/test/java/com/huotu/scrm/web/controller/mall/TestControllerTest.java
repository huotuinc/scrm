package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.controller.page.mall.TestIndexPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by helloztt on 2017-06-28.
 */
public class TestControllerTest extends CommonTestBase {
    private String baseUrl = "/mall/test";
    private String indexControllerUrl = baseUrl + "/index";
    private String htmlIndexControllerUrl = baseUrl + "/index/html";
    private Long customerId;

    @Before
    public void init() {
        customerId = Long.valueOf(random.nextInt(10000));
    }

    @Test
    public void index() throws Exception {

        //case 1:post
        //expect:method not allowed
        mockMvc.perform(post(indexControllerUrl)
                .param("customerId", String.valueOf(customerId)))
                .andDo(print())
//                .andExpect(status().isMethodNotAllowed())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("status", HttpStatus.METHOD_NOT_ALLOWED.value()));

        //case 2:no param
        //expect:redirect to login page
        testWithNoParam(indexControllerUrl, 0);

        //case 3: get with param
        //expect: return msg
        String expectResultMsg = "hello scrm,customerId:" + customerId + " !";
        mockMvc.perform(get(indexControllerUrl)
                .param("customerId", String.valueOf(customerId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(expectResultMsg));

        //case 4:404test
        mockMvc.perform(post(baseUrl + "/index/noPageException")
                .param("customerId", String.valueOf(customerId)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testIndex() throws Exception {
        //case 4:404test
        mockMvc.perform(post(baseUrl + "/123")
                .param("customerId", String.valueOf(customerId)))
//                .andDo(print())
                .andExpect(status().isNotFound());;
    }

    @Test
    public void htmlIndex() throws Exception {

        //case 1:post
        //expect:method not allowed
        mockMvc.perform(post(htmlIndexControllerUrl)
                .param("customerId", String.valueOf(customerId)))
//                .andExpect(status().isMethodNotAllowed())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("status", HttpStatus.METHOD_NOT_ALLOWED.value()));

        //case 2:no param
        //expect:redirect to login page
        testWithNoParam(htmlIndexControllerUrl, 0);

        //case 3:
        mockMvc.perform(get(htmlIndexControllerUrl)
                .param("customerId", String.valueOf(customerId)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("title"))
                .andExpect(view().name("test"));

    }

    @Test
    public void wxVerifyCode() throws Exception{
        String randomCode = UUID.randomUUID().toString().replaceAll("-","").substring(0,10);
        MvcResult result = mockMvc.perform(get("/MP_verify_" + randomCode + ".txt").accept("*/*"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        Assert.assertEquals(randomCode,result.getResponse().getContentAsString());
    }

    @Test
    public void htmlIndexPageTest() {
        //test selenium
        webDriver.get("http://localhost" + htmlIndexControllerUrl + "?customerId=" + customerId);
        TestIndexPage textIndex = initPage(TestIndexPage.class);
        textIndex.setCustomerId(customerId);
        textIndex.validate();
    }

}