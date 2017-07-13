package com.huotu.scrm.service.service;

/**
 * Created by hxh on 2017-07-11.
 */
public interface DayReportService {

    /**
     * 保存今日统计信息
     *
     * @param userId 用户ID
     */
    void saveReportDay(Long userId);
}
