package com.huotu.scrm.service.service;

import com.huotu.scrm.common.utils.ApiResult;

/**
 * Created by hxh on 2017-07-12.
 */
public interface ReportMonthService {

    /**
     * 保存每月统计信息
     * @param userId 用户ID
     * @return
     */
    ApiResult saveReportMonth(int userId);
}
