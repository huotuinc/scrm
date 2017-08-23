package com.huotu.scrm.service.service.report;

import com.huotu.scrm.service.entity.mall.User;

import java.time.LocalDateTime;

/**
 * Created by hxh on 2017-07-11.
 */
public interface DayReportService {

    /**
     * 每日统计
     */
    void saveDayReport();

    /**
     * 统计某段时间预计积分(只限当天的积分统计)
     *
     * @param user      用户
     * @param beginTime 统计起始日期
     * @param endTime   统计最后日期
     */
    int getEstimateScore(User user, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 统计用户的累积积分
     *
     * @param user 用户
     * @return
     */
    int getCumulativeScore(User user);

    /**
     * 统计本月的访客量（浏览量）
     *
     * @param user 用户
     * @return
     */
    int getMonthVisitorNum(User user);


    /**
     * 获取本月的积分（不包含今日的预计积分）
     *
     * @param user 用户
     * @return
     */
    int getMonthEstimateScore(User user);

    /**
     * 保存每日浏览积分到积分表
     */
    void saveDayVisitorScore();

}
