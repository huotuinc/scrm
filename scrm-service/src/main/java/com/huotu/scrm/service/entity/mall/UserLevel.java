/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.scrm.service.entity.mall;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.huotu.scrm.common.ienum.UserType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Description;

import javax.persistence.*;

/**
 * @author CJ
 */
@Entity
@Setter
@Getter
@Cacheable(value = false)
@Table(name = "Mall_UserLevel")
@Description("会员等级")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UL_ID")
    private Long id;

    /**
     * 等级
     */
    @Column(name = "UL_Level")
    private int level;
    /**
     * 等级描述
     */
    @Column(name = "UL_LevelName", length = 50)
    private String levelName;

    /**
     * 等级类型
     */
    @Column(name = "UL_Type")
    private UserType type;
    /**
     * 商家
     */
    @Column(name = "UL_CustomerID")
    private Long customerId;

    @Column(name = "Is_Salesman")
    private boolean isSalesman;
}
