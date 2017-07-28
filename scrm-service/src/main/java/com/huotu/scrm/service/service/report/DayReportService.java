package com.huotu.scrm.service.service.report;

import java.time.LocalDateTime;

/**
 * Created by hxh on 2017-07-11.
 */
public interface DayReportService {

    /**
     * 保存今日统计信息
     */
    void saveDayReport();

    /**
     * 统计某段时间预计积分
     *
     * @param userId  用户ID
     * @param minDate 统计起始日期
     * @param maxDate 统计最后日期
     */
    int getEstimateScore(Long userId, LocalDateTime minDate, LocalDateTime maxDate);

    /**
     * 统计用户的累积积分（从注册以后开始计算，没有计算本月积分）
     *
     * @param userId 用户ID
     * @return
     */
    int getCumulativeScore(Long userId);

    /**
     * 定时保存每日统计信息
     */
    void saveDayReportScheduled();
}
