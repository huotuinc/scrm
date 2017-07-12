package com.huotu.scrm.service.entity.activity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 活动中奖记录
 *
 * Created by montage on 2017/7/11.
 */

@Entity
@Getter
@Setter
@Table(name = "SCRM_WinDetail")
public class ActWinDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long winDetailId;

    @Column(name = "User_Id")
    private long userId;

    @Column(name = "Winner_Tel")
    private String winnerTel;

    @Column(name = "Winner_Name")
    private String winnerName;

    @Column(name = "Win_Time")
    private Date win_Time;

    @Column(name = "Ip_Address")
    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "Prize_Id",referencedColumnName = "Prize_Id")
    private ActPrize prizeId;
}
