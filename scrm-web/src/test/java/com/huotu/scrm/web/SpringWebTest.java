package com.huotu.scrm.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.huotu.scrm.web.controller.filter.ParamFilter;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by helloztt on 2017-06-28.
 */
public class SpringWebTest {
    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;
    protected WebClient webClient;
    protected WebDriver webDriver;
    protected ParamFilter paramFilter = new ParamFilter();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilters(paramFilter)
                .build();
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(this.mockMvc)
                .build();
        this.webDriver = MockMvcHtmlUnitDriverBuilder
                .mockMvcSetup(this.mockMvc)
                .build();
    }

    @After
    public void afterTest() {
        if (webDriver != null) {
            webDriver.close();
        }
    }
}
