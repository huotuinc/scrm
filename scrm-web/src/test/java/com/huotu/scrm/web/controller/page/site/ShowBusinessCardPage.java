package com.huotu.scrm.web.controller.page.site;

import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.web.controller.page.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ShowBusinessCardPage extends AbstractPage {
    private SalesmanBusinessCard salesmanBusinessCard;

    WebElement js_data_numberOfFollower;


    public ShowBusinessCardPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void setSalesmanBusinessCard(SalesmanBusinessCard salesmanBusinessCard){
        this.salesmanBusinessCard=salesmanBusinessCard;
    }


    @Override
    public void validate() {

    }
}
