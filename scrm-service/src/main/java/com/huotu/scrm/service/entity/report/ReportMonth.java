package com.huotu.scrm.service.entity.report;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by hxh on 2017-07-11.
 */
@Entity
@Getter
@Setter
@Table(name = "SCRM_MonthReport")
public class ReportMonth {

    /**
     * 自增编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;

    /**
     * 用户ID
     */
    @Column(name = "User_Id")
    private int userId;

    /**
     * 商户号
     */
    @Column(name = "Customer_Id")
    private int customerId;

    /**
     * 等级
     */
    @Column(name = "Level_Id")
    private int levelId;

    /**
     * 是否是销售员（1：是 0：不是）
     */
    @Column(name="Is_Salers")
    private boolean isSale;

    /**
     * 每月资讯转发量
     */
    @Column(name = "Forward_Num")
    private int forwardNum;

    /**
     * 每月访客量（转发页面的今月访问量）
     */
    @Column(name = "Visitor_Num")
    private int visitorNum;

    /**
     * 每月推广积分
     */
    @Column(name = "Extension_Score")
    private int extensionScore;

    /**
     * 每月积分排名
     */
    @Column(name = "Score_Ranking")
    private int scoreRanking;

    /**
     * 每月被关注量（销售员特有）
     */
    @Column(name = "Follow_Num")
    private int followNum;

    /**
     * 每月关注排名（销售员特有）
     */
    @Column(name = "Follow_Ranking")
    private int followRanking;

    /**
     * 统计月份
     */
    @Column(name = "Report_Month")
    @Temporal(TemporalType.DATE)
    private Date reportMonth;
}
