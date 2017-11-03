package com.huotu.scrm.service.model.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hxh on 2017-07-17.
 */
@Getter
@Setter
@ToString
public class InfoModel implements Serializable {

    /**
     * 咨询编号
     */
    private Long infoId;

    /**
     * 商户编号
     */
    private Long customerId;

    /**
     * 资讯标题
     */
    private String title;


    /**
     * 资讯简介
     */
    private String introduce;

    /**
     * 资讯缩略图
     */
    private String thumbnailImageUrl;

    /**
     * 资讯转发量
     */
    private int forwardNum;

    /**
     * 资讯浏览量
     */
    private int visitorNum;

    /**
     * 发布时间距现在多少时间（默认小时数，超过24小时提示天数）
     */
    private String releaseTime;


}
