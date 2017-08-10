/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.entity.mall;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户正式积分表
 * Created by slt on 2016/5/6.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable(value = false)
@Table(name = "Hot_UserIntegral_History")
public class UserFormalIntegral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UIH_ID")
    private Long id;

    @Column(name = "UIH_CustomerID")
    private Long merchantId;

    @Column(name = "UIH_UserID")
    private Long userId;

    /**
     * 返利积分
     */
    @Column(name = "UIH_Integral")
    private Integer score;

    /**
     * 状态(1.购买商品获得，还有其他的)
     * 121:积分提现
     */
    @Column(name = "UIH_Type")
    private IntegralTypeEnum status;

    @Column(name = "UIH_NewType")
    private Integer newStatus;

    /**
     * 生成时间
     */
    @Column(name = "UIH_AddTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    /**
     * 描述
     */
    @Column(name = "UIH_Desc")
    private String desc;

    /**
     * 积分转为金额
     */
    @Column(name = "UIH_Integral_Money")
    private Double integralMoney;

    /**
     * 用户等级
     */
    @Column(name = "UIH_UserLevelId")
    private Integer userLevelId;

    /**
     * 用户类型
     */
    @Column(name = "UIH_UserType")
    private Integer userType;

    @Column(name = "UIH_RebateAssignMode")
    private Integer rebateAssignMode;
}
