package com.huotu.scrm.service.entity.info;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
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
    @Column(name = "Introduce", length = 100)
    private String introduce;


    /**
     * 资讯详情
     */
    @Column(name = "Content", length = 400)
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
    @Column(name = "Create_Time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     * 资讯状态     1、 已发布  0、未发布 (判断普通会员)
     */
    @Column(name = "Status")
    private boolean status;


    /**
     * 资讯推广状态  1、 已推广  0、未推广（判断小伙伴）
     */
    @Column(name = "Extend")
    private boolean extend = false;


    /**
     * 资讯是否删除  1、已删除  0、正常
     */
    @Column(name = "Disable")
    private boolean disable = false;


    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", title='" + title + '\'' +
                ", introduce='" + introduce + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", thumbnailImageUrl='" + thumbnailImageUrl + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", extend=" + extend +
                ", disable=" + disable +
                '}';
    }

}
