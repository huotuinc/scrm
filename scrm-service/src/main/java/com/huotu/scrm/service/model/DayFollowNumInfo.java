package com.huotu.scrm.service.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 关注明细（销售员特有）
 * Created by hxh on 2017-07-19.
 */
@Getter
@Setter
public class DayFollowNumInfo implements Serializable {
    /**
     * 今日关注人数
     */
    private int dayFollowNum;
    /**
     * 当前排名
     */
    private int followRanking;
    /**
     * 最高月排名
     */
    private int highestFollowRanking;
    /**
     * 近几个月关注排名 （默认统计5个月包含本月）
     */
    private List<Integer> monthFollowRankingList;
}
