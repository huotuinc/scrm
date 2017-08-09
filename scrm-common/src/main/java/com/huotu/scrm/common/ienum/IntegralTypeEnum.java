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
 * 积分类型
 * Created by helloztt on 2017-08-08.
 */
public enum  IntegralTypeEnum implements ICommonEnum{
    TURN_INFO(1000,"资讯转发奖励积分"),
    BROWSE_INFO(1001,"资讯浏览奖励积分");
    private Integer code;
    private String value;

    IntegralTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
