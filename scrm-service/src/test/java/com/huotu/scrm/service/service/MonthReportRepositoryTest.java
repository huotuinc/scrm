package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import com.huotu.scrm.service.service.report.MonthReportService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hxh on 2017-07-12.
 */
public class MonthReportRepositoryTest extends CommonTestBase {

    @Autowired
    private MonthReportService monthReportService;
    @Autowired
    private MonthReportRepository monthReportRepository;


    @Test
    // TODO: 2017-08-12
    public void testMonthReportService() {
    }
}
