package com.huotu.scrm.service.entity.businesscard;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 销售员名片关注表
 * Created by jinxiangdong on 2017/7/12.
 */
@Getter
@Setter
@Entity
@Table(name = "SCRM_BusinessCardRecord")
public class BusinessCardRecord implements Serializable {
    /***
     * 被关注者ID
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
     * 关注者
     */
    @Id
    @Column(name = "Follow_Id")
    private Long followId;
    /***
     * 关注时间
     */
    @Column(name = "Follow_Date", columnDefinition = "datetime")
    private LocalDateTime followDate;

}
