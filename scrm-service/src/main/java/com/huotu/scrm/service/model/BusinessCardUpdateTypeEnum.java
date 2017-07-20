package com.huotu.scrm.service.model;

import com.huotu.scrm.common.ienum.ICommonEnum;

/**
 * 销售员名片实体更新类型
 * Created by Jinxiangdong on 2017/7/13.
 */
public enum BusinessCardUpdateTypeEnum implements ICommonEnum {

    BUSINESS_CARD_UPDATE_TYPE_AVATAR(1, "名片头像"),
    BUSINESS_CARD_UPDATE_TYPE_COMPANYNAME(2, "企业名"),
    BUSINESS_CARD_UPDATE_TYPE_JOB(3, "职位"),
    BUSINESS_CARD_UPDATE_TYPE_TEL(4, "固定电话"),
    BUSINESS_CARD_UPDATE_TYPE_QQ(5, "QQ"),
    BUSINESS_CARD_UPDATE_TYPE_COMPANYADDRESS(6, "企业地址"),
    BUSINESS_CARD_UPDATE_TYPE_EMAIL(7, "邮箱");

    private int code;
    private String value;

    public Object getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }

    BusinessCardUpdateTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
