package com.huotu.scrm.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huotu.scrm.common.ienum.ICommonEnum;

/**
 * 活动的奖品类型
 * Created by luohaibo on 2017/8/3.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum prizeTypeEnum implements ICommonEnum {

    PRIZE_TYPE_THANKS(0, "谢谢参与"),
    PRIZE_TYPE_ENTITY_PRIZE(1, "实物"),
    PRIZE_TYPE_CASH_VOUCHER(2, "消费券"),
    PRIZE_TYPE_CASH(3, "现金"),
    PRIZE_TYPE_MALl_SCORE(4,"商城积分");

    private int code;
    private String value;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    prizeTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
