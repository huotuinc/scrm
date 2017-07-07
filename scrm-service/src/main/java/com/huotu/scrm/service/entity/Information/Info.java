package com.huotu.scrm.service.entity.Information;

import com.huotu.scrm.service.ienum.InfoStatusEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Entity
@Getter
@Setter
@Table(name = "SCRM_Info")
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 商户号
     */
    @Column(name = "Custom_Id")
    private int customId;

    /**
     * 资讯标题
     */
    @Column(name = "Title")
    private String title;


    /**
     * 资讯简介
     */
    @Column(name = "Introduce")
    private String introduce;


    /**
     * 资讯详情
     */
    @Column(name = "Content")
    private String content;


    /**
     * 资讯图
     */
    @Column(name = "Image")
    private String imageUrl;


    /**
     * 资讯缩略图
     */
    @Column(name = "Thumbnail_Image")
    private String thumbnailImageUrl;

    /**
     * 资讯创建时间
     */
    @Column(name = "Create_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;


    /**
     * 资讯状态
     */
    @Column(name = "Status")
    private InfoStatusEnum status;


    /**
     * 资讯推广状态
     */
    @Column(name = "Extend")
    private boolean  extend = false;


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
