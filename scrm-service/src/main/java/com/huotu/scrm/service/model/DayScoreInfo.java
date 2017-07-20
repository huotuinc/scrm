package com.huotu.scrm.service.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hxh on 2017-07-19.
 */
@Getter
@Setter
public class DayScoreInfo implements Serializable {
    /**
     * 今日预计积分
     */
    private int dayScore;
    /**
     * 昨日积分
     */
    private int lastDayScore;
    /**
     * 历史累计积分
     */
    private int accumulateScore;
    /**
     * 近几个月积分（默认统计5个月包含本月）
     */
    private List<MonthStatisticInfo> monthScoreList;
}
