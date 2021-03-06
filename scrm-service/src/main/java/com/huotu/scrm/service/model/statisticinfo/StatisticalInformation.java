package com.huotu.scrm.service.model.statisticinfo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统计个人信息
 * <p>
 * Created by hxh on 2017-07-18.
 */
@Getter
@Setter
public class StatisticalInformation implements Serializable {

    /**
     * 今日访问量排名
     */
    private int dayVisitorRanking;
    /**
     * 今日访客量
     */
    private int dayVisitorNum;
    /**
     * 今日预计积分
     */
    private int dayScore;
    /**
     * 累积积分
     */
    private int accumulateScore;
    /**
     * 关注人数（销售员特有 -1: 不是销售员）
     */
    private int followNum = -1;
}
