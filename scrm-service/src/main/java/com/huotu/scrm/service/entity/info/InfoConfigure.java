package com.huotu.scrm.service.entity.info;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 资讯配置表
 * <p>
 * Created by montage on 2017/7/11.
 */
@Entity
@Getter
@Setter
@Table(name = "SCRM_InfoConfigure")
public class InfoConfigure {

    /**
     * 商户Id
     */
    @Id
    @Column(name = "Customer_Id")
    private Long customerId;

    /**
     * 是否开启转发奖励
     */
    @Column(name = "Reward_Switch")
    private boolean isRewardSwitch;

    /**
     * 转发咨询奖励积分
     */
    @Column(name = "Reward_Score")
    private int rewardScore;

    /**
     * 是否开启转发奖励限制
     */
    @Column(name = "Reward_Limit_Switch")
    private boolean isRewardLimitSwitch;

    /**
     * 每日奖励限制次数
     */
    @Column(name = "Reward_LimitNum")
    private int rewardLimitNum;

    /**
     * 转发奖励获取对象 0 会员  1 小伙伴
     */
    @Column(name = "Reward_UserType")
    private int rewardUserType;

    /**
     * 是否开启UV转换积分
     */
    @Column(name = "Exchange_Switch")
    private boolean isExchangeSwitch;

    /**
     * UV转换积分的比例
     */
    @Column(name = "Exchange_Rate")
    private int exchangeRate;

    /**
     * UV转换积分获取对象
     * 0 会员  1 小伙伴
     */
    @Column(name = "Exchange_UserType")
    private int exchangeUserType;


    /**
     * UV转换开关s
     */
    @Column(name = "Day_Exchange_Limit_Switch")
    private boolean isDayExchangeLimitSwitch;

    /**
     * UV转换积分每日上限
     */
    @Column(name = "Day_Exchange_Limit")
    private int dayExchangeLimit;

    /**
     * 资讯配置链接
     */
    @Column(name = "Info_IntroduceUrl")
    private String infoIntroduceUrl;

    public boolean extensionIsBuddyAndIsReward() {
        return isRewardLimitSwitch && rewardUserType == 1;
    }

    public boolean uvIsBuddyAndIsReward() {
        return isExchangeSwitch && exchangeUserType == 1;
    }
}
