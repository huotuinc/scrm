package com.huotu.scrm.service.entity.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "SCRM_Info")
@Cacheable(value = false)
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Info_Id")
    private Long id;

    /**
     * 商户号
     */
    @Column(name = "Custom_Id")
    private Long customerId;

    /**
     * 资讯标题
     */
    @Column(name = "Title", length = 60)
    private String title;


    /**
     * 资讯简介
     */
    @Column(name = "Introduce", length = 300)
    private String introduce;


    /**
     * 资讯详情
     */
    @Column(name = "Content", length = 500)
    private String content;


    /**
     * 资讯图
     */
    @Column(name = "Image")
    private String imageUrl;

    @Transient
    private String mallImageUrl;


    /**
     * 资讯缩略图
     */
    @Column(name = "Thumbnail_Image")
    private String thumbnailImageUrl;

    /**
     * 资讯创建时间
     */
    @Column(name = "Create_Time" , columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     * 资讯状态     1、 已发布  0、未发布 (判断普通会员)
     */
    @Column(name = "Status")
    private boolean isStatus;


    /**
     * 资讯推广状态  1、 已推广  0、未推广（判断小伙伴）
     */
    @Column(name = "Extend")
    private boolean isExtend = false;


    /**
     * 资讯是否删除  1、已删除  0、正常
     */
    @Column(name = "Disable")
    private boolean isDisable = false;


//    /**
//     * 资讯点赞数量
//     */
//    @Column(name = "Good_Num")
//    private int goodNum;


}
