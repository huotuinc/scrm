package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.service.report.DayReportService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Created by hxh on 2017-07-14.
 */
public class DayReportServiceTest extends CommonTestBase {

    @Autowired
    private DayReportService dayReportService;
    @Autowired
    private DayReportRepository dayReportRepository;

    /**
     * 测试保存每日统计信息
     */
    @Test
    public void testDayReportService() {
        dayReportService.saveDayReport();
        List<DayReport> all = dayReportRepository.findAll();
        all.forEach(p -> {
            System.out.println(p.toString());
        });
    }

    /**
     * 测试定时任务
     *
     * @throws IOException
     */
    @Test
    public void testSaveDayReportScheduled() throws IOException {
        System.in.read();
    }

}
