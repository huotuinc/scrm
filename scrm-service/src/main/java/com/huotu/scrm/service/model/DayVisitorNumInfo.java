package com.huotu.scrm.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 今日访客信息
 * Created by hxh on 2017-07-19.
 */
@Getter
@Setter
@ToString
public class DayVisitorNumInfo implements Serializable {
    /**
     * 今日访客量
     */
    private int dayVisitorBum;
    /**
     * 本月访客量
     */
    private int monthVisitorNum;
    /**
     * 历史最高月访问量
     */
    private int highestMonthVisitorNum;
    /**
     * 近几个月访客量（默认统计5个月包含本月）
     */
    private List<MonthStatisticInfo> monthVisitorNumList;
}
