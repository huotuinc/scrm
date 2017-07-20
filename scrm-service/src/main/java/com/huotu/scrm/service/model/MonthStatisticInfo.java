package com.huotu.scrm.service.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 保存某个月统计信息（积分排名等）
 * Created by hxh on 2017-07-19.
 */
@Getter
@Setter
public class MonthStatisticInfo implements Serializable {
    /**
     * 统计月份
     */
    private int month;
    /**
     * 统计数据（积分排名等）
     */
    private int data;
}
