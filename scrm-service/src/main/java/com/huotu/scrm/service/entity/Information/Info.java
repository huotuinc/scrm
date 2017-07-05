package com.huotu.scrm.service.entity.Information;

import com.huotu.scrm.service.Enum.InfoStatusEnum;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Entity
@Data
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

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
    private String InfoCreateDate;


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
    @Temporal(TemporalType.TIMESTAMP)
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
