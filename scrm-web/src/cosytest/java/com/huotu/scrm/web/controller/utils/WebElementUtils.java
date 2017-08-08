package com.huotu.scrm.web.controller.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

/**
 * Created by cosy on 2017/8/2.
 */
public class WebElementUtils {

    /**
     * 验证某控件是否存在
     * @param driver
     * @param selector
     * @return
     */
    public static boolean doesWebElementExist(WebDriver driver, By selector)
    {
        try {
            driver.findElement(selector);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
