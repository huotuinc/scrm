package com.huotu.scrm.service.service.report;

import java.time.LocalDate;

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
     * 统计推广积分
     *
     * @param userId       用户ID
     * @param lastFirstDay 统计起始时间
     * @param lastEndDay   统计结束时间
     * @return
     */
    int getExtensionScore(Long userId, LocalDate lastFirstDay, LocalDate lastEndDay);

    /**
     * 统计访客量
     *
     * @param userId       用户ID
     * @param lastFirstDay 统计起始时间
     * @param lastEndDay   统计结束时间
     * @return
     */
    int getVisitorNum(Long userId, LocalDate lastFirstDay, LocalDate lastEndDay);
}
