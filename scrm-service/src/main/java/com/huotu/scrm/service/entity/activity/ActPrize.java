package com.huotu.scrm.service.entity.activity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 活动奖品表
 *
 * Created by montage on 2017/7/11.
 */
@Entity
@Getter
@Setter
@Table(name = "SCRM_ActPrize")
public class ActPrize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Prize_Id")
    private long prizeId;

    @Column(name ="Prize_Name")
    private String prizeName;

    @Column(name = "Prize_Image_Url")
    private String prizeImageUrl;

    @Column(name = "Win_Rate")
    private int winRate;

    @Column(name = "Sort")
    private int sort;
}
