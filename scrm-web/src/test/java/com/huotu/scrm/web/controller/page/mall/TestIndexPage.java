package com.huotu.scrm.web.controller.page.mall;

import com.huotu.scrm.web.controller.page.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.util.StringUtils;

import static org.junit.Assert.*;

/**
 * Created by helloztt on 2017-06-28.
 */
public class TestIndexPage extends AbstractPage {

    @FindBy(id = "title")
    private WebElement title;

    private Long merchantId;

    public TestIndexPage(WebDriver webDriver) {
        super(webDriver);
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public void validate() {
        //验证title有值
        assertTrue(!StringUtils.isEmpty(title.getText()));
        String expectResultMsg = "hello scrm,merchantId:"+ merchantId+" !";
        assertTrue(expectResultMsg.equals(title.getText()));
    }
}
