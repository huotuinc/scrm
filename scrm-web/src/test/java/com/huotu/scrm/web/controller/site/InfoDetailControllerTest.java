package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.service.info.InfoService;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class InfoDetailControllerTest extends CommonTestBase {

    private Long customerId;
    private Long infoId;
    @Autowired
    InfoService infoService;

    @Before
    public void init() {
        customerId = Long.valueOf(random.nextInt(10000));
        infoId =  Long.valueOf(random.nextInt(10000));
    }

    @Test
    public void deletePage() throws Exception {
        //2、case get
        //method: method allow
        mockMvc.perform(get("/site/info/infoDetail")
                .param("customerId",String.valueOf(4421))
                .param("infoId",String.valueOf(infoId))
                .param("type",String.valueOf(0))
        ).andExpect(view().name("info/info_delete.html"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())  ;
    }




}