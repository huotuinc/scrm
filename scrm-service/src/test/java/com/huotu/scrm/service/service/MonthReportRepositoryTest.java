package com.huotu.scrm.service.service;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import com.huotu.scrm.service.service.report.MonthReportService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by hxh on 2017-07-12.
 */
public class MonthReportRepositoryTest extends CommonTestBase {

    @Autowired
    private MonthReportService monthReportService;
    @Autowired
    private MonthReportRepository monthReportRepository;

    private Customer customer;
    private User userBuddyIsSales;

    @Before
    public void setBefore() throws Exception {
        customer = mockCustomer();
        UserLevel userLevelBuddyIsSales = mockUserLevel(customer.getId(), UserType.buddy, true);
        userBuddyIsSales = mockUser(customer.getId(), userLevelBuddyIsSales);
    }

    /**
     * 测试每月统计信息
     */
    @Test
    public void testMonthReportService() throws Exception {
        LocalDate now = LocalDate.now();
        mockDayReport(userBuddyIsSales, 1, 1, 1, now.minusMonths(1));
        mockDayReport(userBuddyIsSales, 2, 2, 2, now.minusDays(1).minusMonths(1));
        mockBusinessCardRecord(userBuddyIsSales.getId(), customer.getId(), LocalDateTime.now().minusMonths(1));
        monthReportService.saveMonthReport();
        List<MonthReport> monthReportList = monthReportRepository.findByUserId(userBuddyIsSales.getId());
        Assert.assertEquals(1, monthReportList.size());
        Assert.assertEquals(3, monthReportList.get(0).getExtensionScore());
        Assert.assertEquals(3, monthReportList.get(0).getForwardNum());
        Assert.assertEquals(1, monthReportList.get(0).getFollowNum());
        Assert.assertEquals(1, monthReportList.get(0).getScoreRanking());
        Assert.assertEquals(1, monthReportList.get(0).getFollowRanking());
    }
}
