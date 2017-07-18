package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.repository.InfoBrowseRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.service.report.DayReportService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by hxh on 2017-07-14.
 */
public class DayReportServiceTest extends CommonTestBase {

    @Autowired
    private DayReportService dayReportService;
    @Autowired
    private DayReportRepository dayReportRepository;
    @Autowired
    private InfoBrowseRepository infoBrowseRepository;

    /**
     * 测试保存每日统计信息
     */
    @Test
    public void testDayReportService() {
//        dayReportService.saveDayReport();
        int cumulativeScore = dayReportService.getCumulativeScore(687500L);
      dayReportService.saveDayReport();
        System.out.println(cumulativeScore);
        LocalDateTime now = LocalDateTime.now();
//        now.minusDays(1);
        System.out.println(now.minusDays(1));
        LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        infoBrowseRepository.findForwardNumBySourceUserId(now.minusDays(2), now, 687500L);
        List<DayReport> all = dayReportRepository.findAll();
        all.forEach(p->{
            System.out.println(p.getExtensionScore());
        });
    }

}
