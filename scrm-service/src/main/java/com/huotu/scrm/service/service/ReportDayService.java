package com.huotu.scrm.service.service;

import com.huotu.scrm.common.utils.ApiResult;

/**
 * Created by hxh on 2017-07-11.
 */
public interface ReportDayService {

    /**
     * 保存今日统计信息
     *
     * @param userId 用户ID
     */
    ApiResult saveReportDay(long userId);
}
