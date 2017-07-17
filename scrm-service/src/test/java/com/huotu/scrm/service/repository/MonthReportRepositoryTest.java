package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.service.MonthReportService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

/**
 * Created by hxh on 2017-07-12.
 */
public class MonthReportRepositoryTest extends CommonTestBase {

    @Autowired
    private MonthReportService monthReportService;


    @Test
    @Rollback(false)
    public void testMonthReportService() {
       monthReportService.saveReportMonth();

    }
}
