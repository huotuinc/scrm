/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.scrm.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 商户
 * <p>只允许获取，不允许插入</p>
 */
@Entity
@Setter
@Getter
@Cacheable(value = false)
@Table(name = "Swt_CustomerManage")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Merchant implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SC_UserID")
    private Long id;


    /**
     * 登录名
     */
    @Column(name = "SC_UserLoginName", nullable = false, length = 50)
    private String loginName;


    /**
     * 密码
     * 保存的是一次md5以后的小写hex
     */
    @Column(name = "SC_UserLoginPassword", length = 32)
    @JsonIgnore
    private String loginPassword;


    /**
     * 昵称
     */
    @Column(name = "SC_UserNickName", length = 200)
    private String nickName;


    /**
     * 手机号
     */
    @Column(name = "SC_PhoneNumber")
    private String mobile;

    /**
     * 经过核查，数据库内存有为null的SC_MallStatus
     * 为null暂时没有语义。
     */
    @Column(name = "SC_MallStatus")
    private Boolean enabled;

    /**
     * 二级域名 如huotu
     *
     * @since 1.3
     */
    @Column(name = "SC_SubDomain")
    private String subDomain;
}