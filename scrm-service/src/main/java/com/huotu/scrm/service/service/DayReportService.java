package com.huotu.scrm.service.service;

/**
 * Created by hxh on 2017-07-11.
 */
public interface DayReportService {

    /**
     * 保存今日统计信息
     */
    void saveReportDay();

    /**
     * 统计今日预计积分
     * @param userId
     */
    int getEstimateScore(Long userId);
}
