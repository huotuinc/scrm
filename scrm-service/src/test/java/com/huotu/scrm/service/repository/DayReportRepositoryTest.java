package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
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
    @Autowired
    private BusinessCardRecordRepository businessCardRecordRepository;
    @Autowired
    private InfoRepository infoRepository;

    @Test
    public void testDayReportRepository() {
        LocalDate now = LocalDate.now();
        List<Long> byUserId = dayReportRepository.findByUserId(now.minusDays(3), now);
        byUserId.forEach(p -> {
            System.out.println(".............." + p);
        });
        List<DayReport> list1 = dayReportRepository.findByUserIdAndReportDayLessThanEqual(687500L, now);
        list1.forEach(dayReport -> {
            System.out.println(dayReport.toString());
        });
    }

    @Test
    public void testUserLevelRepository() {
        UserLevel byLevelAndCustomerId = userLevelRepository.findByLevelAndCustomerId(1L, 842L);
        System.out.println(byLevelAndCustomerId.getId());
    }
}
