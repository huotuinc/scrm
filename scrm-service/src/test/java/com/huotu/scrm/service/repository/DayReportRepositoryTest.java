package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * Created by hxh on 2017-07-13.
 */
public class DayReportRepositoryTest extends CommonTestBase {

    @Autowired
    private DayReportRepository dayReportRepository;
    @Autowired
    private InfoBrowseRepository infoBrowseRepository;
    @Autowired
    private UserLevelRepository userLevelRepository;
    @Autowired
    private MonthReportRepository monthReportRepository;

    @Test
    public void testDayReportRepository() {
        LocalDate now = LocalDate.now();
        DayReport dayReport = new DayReport();
        dayReport.setReportDay(now);
        System.out.println(now.minusDays(1));
        System.out.println(now.plusDays(1));
        LocalDate date = now.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1);
        System.out.println(date);
        List<MonthReport> list = monthReportRepository.findByReportMonthOrderByExtensionScoreDesc(date);
        System.out.println(list.size());
        list.forEach(p -> {
            System.out.println(p.getExtensionScore());
        });
    }

    @Test
    public void testUserLevelRepository() {
        UserLevel byLevelAndCustomerId = userLevelRepository.findByLevelAndCustomerId(1, 842L);
        System.out.println(byLevelAndCustomerId.getId());
    }
}
