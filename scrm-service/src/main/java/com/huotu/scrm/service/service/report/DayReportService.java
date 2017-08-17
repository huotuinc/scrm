package com.huotu.scrm.service.service.report;

import com.huotu.scrm.service.entity.mall.User;

import java.time.LocalDateTime;

/**
 * Created by hxh on 2017-07-11.
 */
public interface DayReportService {

    void saveDayReport();

    /**
     * 统计某段时间预计积分(只限当天的预计积分查询)
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
     * 统计某用户某个时间段的转发咨询奖励积分
     *
     * @param user
     * @param beginTime
     * @param endTime
     * @return
     */
    int getForwardScore(User user, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 统计某商户的浏览咨询奖励积分
     *
     * @param user
     * @return
     */
    int getVisitorScore(User user, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 统计本月的访客量（浏览量）
     *
     * @param user
     * @return
     */
    int getMonthVisitorNum(User user);

    /**
     * 统计本月的转发量
     *
     * @param user
     * @return
     */
    int getMonthForwardNum(User user);

    /**
     * 获取本月的预计积分
     *
     * @param user
     * @return
     */
    int getMonthEstimateScore(User user);

    void saveDayVisitorScore();

}
