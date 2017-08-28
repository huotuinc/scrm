package com.huotu.scrm.service.service.statisticsinfo;

import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.model.statisticinfo.SearchCondition;
import org.springframework.data.domain.Page;

/**
 * Created by hxh on 2017-08-28.
 */
public interface StatisticsInfoService {
    /**
     * 获取每日统计数据
     * @param searchCondition 查询条件
     * @param pageNo 页数
     * @return
     */
    Page<DayReport> getDayReportList(SearchCondition searchCondition, int pageNo);
}
