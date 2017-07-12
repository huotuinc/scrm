package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.service.ReportMonthService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by hxh on 2017-07-12.
 */
public class ReportMonthServiceTest extends CommonTestBase {

    @Autowired
    private ReportMonthService reportMonthService;

    @Test
    public void testDate() {
        Date month = reportMonthService.getMonth();
        System.out.println(month);
    }
}
