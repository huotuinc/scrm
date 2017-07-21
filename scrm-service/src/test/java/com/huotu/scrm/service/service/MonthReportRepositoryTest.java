package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import com.huotu.scrm.service.service.report.MonthReportService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hxh on 2017-07-12.
 */
public class MonthReportRepositoryTest extends CommonTestBase {

    @Autowired
    private MonthReportService monthReportService;
    @Autowired
    private MonthReportRepository monthReportRepository;


    @Test
    public void testMonthReportService() {
        monthReportService.saveMonthReport();
        List<MonthReport> all = monthReportRepository.findAll();
        System.out.println(all.size());
        all.forEach(p -> {
            System.out.println(p.toString());
        });
    }
}
