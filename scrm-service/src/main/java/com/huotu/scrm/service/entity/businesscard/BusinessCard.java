package com.huotu.scrm.service.entity.businesscard;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 销售员名片信息表
 * Created by jinxiangdong on 2017/7/11.
 */
@Table(name = "SCRM_BusinessCard")
@Getter
@Setter
@Entity
public class BusinessCard implements Serializable {
    /***
     * UserId
     */
    @Id
    @Column(name = "User_Id")
    private Long userId;
    /***
     * 商户Id
     */
    @Id
    @Column(name = "Customer_Id")
    private Long customerId;
    /***
     * 名片头像地址
     */
    @Column(name = "Avatar", length = 512)
    private String avatar;
    /***
     * 职位
     */
    @Column(name = "Job", length = 100)
    private String job;
    /***
     * 企业名
     */
    @Column(name = "CompanyName")
    private String companyName;
    /***
     * 固定电话
     */
    @Column(name = "Tel")
    private String tel;
    /***
     * QQ
     */
    @Column(name = "QQ", length = 20)
    private String qq;
    /***
     * 企业地址
     */
    @Column(name = "CompanyAddress", length = 512)
    private String companyAddress;
    /***
     * 邮箱
     */
    @Column(name = "Email", length = 100)
    private String email;

}
