package com.huotu.scrm.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 商城用户信息表
 * Created by Administrator on 2017/7/11.
 */
@Entity
@Table(name = "Hot_UserBaseInfo")
@Getter
@Setter
public class HotUserBaseInfo implements Serializable{
    /***
     * 用户Id
     */
    @Id
    @Column(name = "UB_UserID")
    private Integer userId;
    /***
     * 商户
     */
    @Column(name="UB_CustomerID")
    private Integer customerId;
    /***
     * 性别
     */
    @Column(name="UB_UserGender",length = 10)
    private String userGender;
    /***
     * 手机
     */
    @Column(name="UB_UserMobile",length = 50)
    private String userMobile;
    /***
     * 邮箱
     */
    @Column(name="UB_UserEmail",length = 50)
    private String userEmail;

}
