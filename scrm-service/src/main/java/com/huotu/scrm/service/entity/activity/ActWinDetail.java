package com.huotu.scrm.service.entity.activity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

/**
 * 活动中奖记录
 * <p>
 * Created by montage on 2017/7/11.
 */

@Entity
@Getter
@Setter
@ToString
@Table(name = "SCRM_WinDetail")
public class ActWinDetail {

    /**
     * 中奖记录Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long winDetailId;

    /**
     * 用户Id
     */
    @Column(name = "User_Id")
    private Long userId;

    /**
     * 中奖人电话
     */
    @Column(name = "Winner_Tel")
    private String winnerTel;

    /**
     * 中奖人姓名
     */
    @Column(name = "Winner_Name")
    private String winnerName;

    /**
     * 中奖日期
     */
    @Column(name = "Win_Time")
    private LocalDateTime winTime;

    /**
     * 中奖Ip地址
     */
    @Column(name = "Ip_Address")
    private String ipAddress;

    /**
     * 奖品Id
     */
    @ManyToOne
    @JoinColumn(name = "Prize_Id", referencedColumnName = "Prize_Id")
    private ActPrize prize;

    /**
     * 活动名称
     */
    @Transient
    private String actName;
}
