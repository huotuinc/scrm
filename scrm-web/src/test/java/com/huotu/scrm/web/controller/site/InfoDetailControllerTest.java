package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.utils.EncryptUtils;
import com.huotu.scrm.service.service.info.InfoService;
import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.interceptor.UserInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.Cookie;
import java.net.URLEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class InfoDetailControllerTest extends CommonTestBase {
    @Autowired
    InfoService infoService;

    private Long customerId;
    private Long userId;
    private Long infoId;
    private Cookie cookie;

    @Before
    public void init() throws Exception {
        customerId = (long) random.nextInt(10000);
        userId = (long) random.nextInt(10000);
        String cookieName = UserInterceptor.USER_ID_PREFIX + customerId;
        String cookieValue = URLEncoder.encode(EncryptUtils.aesEncrypt(String.valueOf(userId), UserInterceptor.USER_ID_SECRET_KEY),"utf-8");
        cookie = new Cookie(cookieName,cookieValue);
        infoId = (long) random.nextInt(10000);
    }

    @Test
    public void deletePage() throws Exception {
        //2、case get
        //method: method allow
        mockMvc.perform(get("/site/info/infoDetail")
                .param("customerId",String.valueOf(customerId))
                .param("infoId",String.valueOf(infoId))
                .param("type",String.valueOf(0)).cookie(cookie)
        ).andExpect(view().name("info/info_delete"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())  ;
    }




}