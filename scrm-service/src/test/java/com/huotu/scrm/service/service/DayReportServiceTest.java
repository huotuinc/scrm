package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hxh on 2017-07-14.
 */
public class DayReportServiceTest extends CommonTestBase {

    @Autowired
    private DayReportService dayReportService;

    /**
     * 测试保存每日统计信息
     */
    @Test
//    @Rollback(false)
    public void testDayReportService() {
//        dayReportService.saveDayReport();
        int cumulativeScore = dayReportService.getCumulativeScore(687500L);
        System.out.println(cumulativeScore);
    }

}
