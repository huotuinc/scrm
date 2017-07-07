package com.huotu.scrm.service.entity.Information;

import com.huotu.scrm.service.ienum.InfoStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Entity
@Data
@Table(name = "SCRM_Info")
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 商户号
     */
    @Column(name = "CustomId")
    private int customId;

    /**
     * 资讯标题
     */
    @Column(name = "Title")
    private String infoTitle;


    /**
     * 资讯简介
     */
    @Column(name = "Introduce")
    private String infoIntro;


    /**
     * 资讯详情
     */
    @Column(name = "Content")
    private String infoContent;


    /**
     * 资讯图
     */
    @Column(name = "Image")
    private String infoImageUrl;


    /**
     * 资讯缩略图
     */
    @Column(name = "Thumbnail_image")
    private String thumbnailImage;

    /**
     * 资讯创建时间
     */
    @Column(name = "Create_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date infoCreateDate;


    /**
     * 资讯状态
     */
    @Column(name = "Status")
    private InfoStatusEnum infoStatus;


    /**
     * 资讯推广状态
     */
    @Column(name = "Extend")
    private boolean  infoExtend = false;


    /**
     * 资讯分享标题
     */
    @Column(name = "Share_Title")
    private String  shareTitle;


    /**
     * 资讯分享详情
     */
    @Column(name = "Share_Description")
    private String  shareDescription;


}
