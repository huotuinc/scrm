package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.web.CommonTestBase;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by hxh on 2017-07-21.
 */
public class InfoExtensionControllerSeleniumTest extends CommonTestBase {
    private String baseUrl = "/site/extension";

    /**
     * 测试进入资讯状态
     *
     * @throws Exception
     */
    @Test
    public void getInfoExtension() throws Exception {
        webDriver.get("http://localhost" + baseUrl + "/getInfoExtension");
        String title = webDriver.getTitle();
        if ("推广中心".equals(title)) {//小伙伴
            WebElement data = webDriver.findElement(By.className("sj"));
            //今日排名
            Assert.assertNotNull(data.getText());
        }
        if ("资讯推广".equals(title)) {//普通会员
            WebElement data = webDriver.findElement(By.className("weui_media_title"));
            //第一条咨询标题
            Assert.assertNotNull(data.getText());
        }

    }

    /**
     * 测试今日积分排名信息
     *
     * @throws Exception
     */
    @Test
    public void getScoreRanking() throws Exception {
        webDriver.get("http://localhost" + baseUrl + "/getScoreRanking");
        WebElement data = webDriver.findElement(By.className("sj"));
        //本月积分排名
        Assert.assertNotNull(data.getText());
    }

    /**
     * 测试今日积分信息
     *
     * @throws Exception
     */
    @Test
    public void getScoreInfo() throws Exception {
        webDriver.get("http://localhost" + baseUrl + "/getScoreInfo");
        WebElement data = webDriver.findElement(By.className("sj"));
        //昨日积分
        Assert.assertNotNull(data.getText());
    }

    /**
     * 测试今日关注信息
     *
     * @throws Exception
     */
    @Test
    public void getFollowInfo() throws Exception {
        webDriver.get("http://localhost" + baseUrl + "/getFollowInfo");
        WebElement data = webDriver.findElement(By.className("sj"));
        //当前排名
        Assert.assertNotNull(data.getText());    }

    /**
     * 测试今日访客量信息
     *
     * @throws Exception
     */
    @Test
    public void getVisitorInfo() throws Exception {
        webDriver.get("http://localhost" + baseUrl + "/getVisitorInfo");
        WebElement data = webDriver.findElement(By.className("sj"));
        //本月UV（人）
        Assert.assertNotNull(data.getText());    }
}
