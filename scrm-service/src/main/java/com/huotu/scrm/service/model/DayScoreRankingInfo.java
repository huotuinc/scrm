package com.huotu.scrm.service.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 今日积分排名信息
 * Created by hxh on 2017-07-19.
 */
@Getter
@Setter
public class DayScoreRankingInfo implements Serializable {
    /**
     * 今日预计积分排名
     */
    private int dayScoreRanking;
    /**
     * 本月积分排名
     */
    private int monthScoreRanking;
    /**
     * 最高月积分排名
     */
    private int HighestMonthScoreRanking;
    /**
     * 近几个月积分排名（默认统计5个月包含本月）
     */
    private List<MonthStatisticInfo> monthScoreRankingList;
}
