package com.huotu.scrm.service.entity.Information;

import com.huotu.scrm.service.Enum.InfoStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Entity
@Data
@Table(name = "Scrm_Info")
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    /**
     * 商户号
     */
    private int CustomId;

    /**
     * 资讯标题
     */
    private String InfoTitle;


    /**
     * 资讯简介
     */
    private String InfoIntro;


    /**
     * 资讯图
     */
    private String InfoImageUrl;

    /**
     * 资讯创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date InfoCreateDate;


    /**
     * 资讯状态
     */
    private InfoStatusEnum InfoStatus;


    /**
     * 资讯推广状态
     */
    private boolean  InfoExtend = false;


    /**
     * 资讯外部链接
     */
    private String InfoOutLink;


    /**
     * 资讯转发记录
     */
    private String InfoTurnUrl;



    /**
     * 资讯访问记录
     */
    private String InfoVisitUrl;


}
