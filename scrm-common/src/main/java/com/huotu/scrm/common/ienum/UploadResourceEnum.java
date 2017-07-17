/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.common.ienum;

/**
 * 上传图片存放的路径
 * Created by helloztt on 2017-07-13.
 */
public enum UploadResourceEnum implements ICommonEnum {
    USER(0, "user"), //名片相关
    INFO(1, "info"), //资讯相关
    ACTIVE(2, "active"); //活动相关
    private Integer code;
    private String name;

    UploadResourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.name;
    }
}
