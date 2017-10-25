package com.huotu.scrm.service.model.info;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by xyr on 2017/10/25.
 */
@Data
public class InfoExcelModel {

    /**
     * 资讯标题
     */
    private String title;

    /**
     * 资讯简介
     */
    private String introduce;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 资讯状态     1、 已发布  0、未发布 (判断普通会员，1)
     */
    private boolean isStatus;

    /**
     * 资讯推广状态  1、 已推广  0、未推广（判断小伙伴,1）
     */
    private boolean isExtend;

    /**
     * 资讯浏览量
     */
    private int infoBrowseNum;

    public InfoExcelModel(String title, String introduce, LocalDateTime createTime, boolean isStatus, boolean isExtend, int infoBrowseNum) {
        this.title = title;
        this.introduce = introduce;
        this.createTime = createTime;
        this.isStatus = isStatus;
        this.isExtend = isExtend;
        this.infoBrowseNum = infoBrowseNum;
    }
}
