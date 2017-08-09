package com.huotu.scrm.service.service.report;

/**
 * Created by hxh on 2017-07-12.
 */
public interface MonthReportService {

    /**
     * 保存每月统计信息
     *
     * @return
     */
    void saveMonthReport();

    /**
     * 定时保存每月统计信息
     */
    void saveMonthReportScheduled();
}
