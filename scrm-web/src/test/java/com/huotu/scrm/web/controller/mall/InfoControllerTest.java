package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    }

    @Test
    public void infoEditPage() throws Exception {

        Info info = mockInfo(customerId);
    }



    @Test
    public void deleteInfo() throws Exception {

    }

}