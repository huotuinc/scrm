package com.huotu.scrm.service.service;

import java.time.LocalDate;

/**
 * Created by hxh on 2017-07-11.
 */
public interface DayReportService {

    /**
     * 保存今日统计信息
     */
    void saveDayReport();

    /**
     * 统计今日预计积分
     *
     * @param userId 用户ID
     * @param date   统计日期
     */
    int getEstimateScore(Long userId, LocalDate date);

    /**
     * 统计用户的累积积分（从注册以后开始计算）
     *
     * @param userId 用户ID
     * @return
     */
    int getCumulativeScore(Long userId);
}
