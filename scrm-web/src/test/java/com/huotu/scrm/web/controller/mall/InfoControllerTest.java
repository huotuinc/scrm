package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by luohaibo on 2017/8/7.
 */
public class InfoControllerTest extends CommonTestBase {

    private Long customerId;

    @Before
    public void init() {
        customerId = Long.valueOf(random.nextInt(10000));
    }

    @Test
    public void infoHomeLists() throws Exception {

        List<Info> mockInfo = new ArrayList();
        mockInfo.add(mockInfo(customerId));
        mockInfo.add(mockInfo(customerId));


        //1、case post
        //method: method allow
        mockMvc.perform(post("/mall/info/infoList")
                .param("customerId",String.valueOf(customerId))
        ).andExpect(model().attributeExists("infoListsPage"))
                .andExpect(view().name("info/info_list"))
                .andExpect(status().isOk());

        //2、case get
        //method: method allow
        mockMvc.perform(get("/mall/info/infoList")
                .param("customerId",String.valueOf(customerId))
        ).andExpect(model().attributeExists("infoListsPage"))
                .andExpect(view().name("info/info_list"))
                .andExpect(status().isOk());

        //3、case get no param
        //method: go to login
        testWithNoParam("/mall/info/infoList",0);

        //4、case post no param
        //method: go to login
        testWithNoParam("/mall/info/infoList",1);

        //5、case
        //method: 验证list数据
        MvcResult result = mockMvc.perform(post("/mall/info/infoList")
                .param("customerId",String.valueOf(customerId))
        ).andExpect(model().attributeExists("infoListsPage"))
                .andExpect(view().name("info/info_list"))
                .andReturn();
        List<Info> list = ((Page)result.getModelAndView().getModel().get("infoListsPage")).getContent();
        Assert.assertEquals(mockInfo.size(),list.size());
        Assert.assertEquals(mockInfo.get(mockInfo.size()-2).getTitle(),list.get(list.size()-2).getTitle());
        Assert.assertEquals(mockInfo.get(mockInfo.size()-1).getTitle(),list.get(list.size()-1).getTitle());
    }

    @Test
    public void infoEditPage() throws Exception {

        Info info = mockInfo(customerId);
    }



    @Test
    public void deleteInfo() throws Exception {

    }

}