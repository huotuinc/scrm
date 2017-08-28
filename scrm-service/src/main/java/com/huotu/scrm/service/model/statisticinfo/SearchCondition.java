package com.huotu.scrm.service.model.statisticinfo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by hxh on 2017-08-28.
 */
@Data
public class SearchCondition implements Serializable {
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 统计起始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate statisticsStartDate;
    /**
     * 统计结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate statisticsEndDate;
}
