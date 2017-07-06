package com.huotu.scrm.service.entity.Information;

import com.huotu.scrm.service.enuma.InfoStatusEnum;
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
    @Column(name = "ID")
    private Long id;

    /**
     * 商户号
     */
    private int customId;

    /**
     * 资讯标题
     */
    private String infoTitle;


    /**
     * 资讯简介
     */
    private String infoIntro;


    /**
     * 资讯图
     */
    private String infoImageUrl;

    /**
     * 资讯创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date infoCreateDate;


    /**
     * 资讯状态
     */
    private InfoStatusEnum infoStatus;


    /**
     * 资讯推广状态
     */
    private boolean  infoExtend = false;


    /**
     * 资讯外部链接
     */
    private String infoOutLink;


    /**
     * 资讯转发记录
     */
    private String infoTurnUrl;



    /**
     * 资讯访问记录
     */
    private String infoVisitUrl;


}
