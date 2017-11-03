package com.huotu.scrm.service.service;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.service.api.ApiService;
import com.huotu.scrm.service.service.report.DayReportService;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by hxh on 2017-07-14.
 */
public class DayReportServiceTest extends CommonTestBase {

    @Autowired
    private ApiService apiService;
    @Autowired
    private DayReportService dayReportService;
    private Customer customer;
    private User userBuddy;

    @Before
    public void setInfo() throws Exception {
        customer = mockCustomer();
        UserLevel userLevelBuddy = mockUserLevel(customer.getId(), UserType.buddy, false);
        userBuddy = mockUser(customer.getId(), userLevelBuddy);
    }

    /**
     * 测试保存每日资讯浏览奖励积分测试
     */
    @Test
    @Ignore
    public void testDaySaveVisitorScore() throws Exception {
        Long customerId = 4421L;
        Long userId = 1058823L;
        Long integral = 100L;
        ApiResult result = apiService.rechargePoint(customerId, userId, integral, IntegralTypeEnum.BROWSE_INFO);
        Assert.assertNotNull(result);
        Assert.assertEquals(HttpStatus.SC_OK, result.getCode());
    }

    /**
     * 测试统计积分信息（只限当天积分）
     */
    @Test
    public void testGetEstimateScore() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        Info info = mockInfo(customer.getId());
        mockInfoBrowse(info.getId(), userBuddy.getId(), userBuddy.getId(), customer.getId());
        mockInfoConfigure(customer.getId());
        mockUserFormalIntegral(userBuddy, 1, now);
        int score = dayReportService.getEstimateScore(userBuddy, localDate.atStartOfDay(), now);
        Assert.assertEquals(2, score);
    }

    /**
     * 测试本月访客量（uv）统计信息
     */
    @Test
    public void testGetMonthVisitorNum() {
        LocalDate now = LocalDate.now();
        Info info = mockInfo(customer.getId());
        mockDayReport(userBuddy, 1, 1, 1, now.withDayOfMonth(random.nextInt(LocalDate.now().getDayOfMonth())));
        mockDayReport(userBuddy, 2, 2, 2, now.minusDays(random.nextInt(LocalDate.now().getDayOfMonth())));
        mockInfoBrowse(info.getId(), userBuddy.getId(), userBuddy.getId(), customer.getId());
        int monthVisitorNum = dayReportService.getMonthVisitorNum(userBuddy);
        Assert.assertEquals(4, monthVisitorNum);
    }

    /**
     * 测试本月积分统计信息
     */
    @Test
    public void testGetMonthEstimateScore() {
        LocalDate now = LocalDate.now();
        mockDayReport(userBuddy, 1, 1, 1, now.minusDays(random.nextInt(LocalDate.now().getDayOfMonth())));
        mockDayReport(userBuddy, 1, 1, 2, now.minusDays(random.nextInt(LocalDate.now().getDayOfMonth())));
        mockDayReport(userBuddy, 1, 1, 3, now.minusDays(random.nextInt(LocalDate.now().getDayOfMonth())));
        int monthEstimateScore = dayReportService.getMonthEstimateScore(userBuddy);
        Assert.assertEquals(6, monthEstimateScore);
    }

    @Test
    public void testDayReportService() {
        Info info = mockInfo(customer.getId());
        Info info1 = mockInfo(customer.getId());
        mockInfoConfigure(customer.getId());
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        mockUserFormalIntegral(userBuddy, 1, time);
        mockInfoBrowse(info.getId(), userBuddy.getId(), userBuddy.getId(), customer.getId(),time);
        mockInfoBrowse(info1.getId(), userBuddy.getId(), userBuddy.getId(), customer.getId(),time);
        dayReportService.saveDayReport();
        DayReport dayReport = dayReportRepository.findByUserIdAndReportDay(userBuddy.getId(), LocalDate.now().minusDays(1));
        Assert.assertNotNull(dayReport);
        Assert.assertEquals(3, dayReport.getExtensionScore());
        Assert.assertEquals(2, dayReport.getForwardNum());
        Assert.assertEquals(2, dayReport.getVisitorNum());
        Assert.assertEquals(0, dayReport.getFollowNum());
        Assert.assertEquals(3, dayReport.getExtensionScore());
        Assert.assertEquals(0, dayReport.getFollowRanking());
        Assert.assertEquals(1, dayReport.getVisitorRanking());
    }
}
