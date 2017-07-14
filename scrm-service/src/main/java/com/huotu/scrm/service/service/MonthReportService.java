package com.huotu.scrm.service.service;

/**
 * Created by hxh on 2017-07-12.
 */
public interface MonthReportService {

    /**
     * 保存每月统计信息
     * @param userId 用户ID
     * @return
     */
    void saveReportMonth(Long userId);

}
