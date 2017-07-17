package com.huotu.scrm.service.entity.report;

import com.huotu.scrm.service.entity.support.LocalDateAttributeConverter;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.persistence.annotations.Converter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;


/**
 * Created by hxh on 2017-07-11.
 */
@Entity
@Getter
@Setter
@Table(name = "SCRM_DayReport")
public class DayReport {

    /**
     * 自增编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "User_Id")
    private Long userId;

    /**
     * 商户号
     */
    @Column(name = "Customer_Id")
    private Long customerId;

    /**
     * 等级
     */
    @Column(name = "Level_Id")
    private int levelId;

    /**
     * 是否是销售员（1：是 0：不是）
     */
    @Column(name = "Is_Salesman")
    private boolean isSalesman;

    /**
     * 每日资讯转发量
     */
    @Column(name = "Forward_Num")
    private int forwardNum;

    /**
     * 每日访客量（转发页面的今日访问量）
     */
    @Column(name = "Visitor_Num")
    private int visitorNum;

    /**
     * 每日访客排名
     */
    @Column(name = "Visitor_Ranking")
    private int visitorRanking;

    /**
     * 每日推广积分
     */
    @Column(name = "Extension_Score")
    private int extensionScore;

    /**
     * 每日积分排名
     */
    @Column(name = "Score_Ranking")
    private int scoreRanking;

    /**
     * 每日被关注量（销售员特有）
     */
    @Column(name = "Follow_Num")
    private int followNum;

    /**
     * 每日关注排名（销售员特有）
     */
    @Column(name = "Follow_Ranking")
    private int followRanking;

    /**
     * 统计日期
     */
    @Column(name = "Report_Day", columnDefinition = "date")
    @Converter(name = "",converterClass = LocalDateAttributeConverter.class)
    private LocalDate reportDay;

}
