package com.huotu.scrm.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huotu.scrm.common.ienum.ICommonEnum;

/**
 * Created by luohaibo on 2017/8/4.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ActivityStatus implements ICommonEnum{

    ACTIVITY_STAUS_TYPE_UNBEGIN(-1, "活动未开始"),
    ACTIVITY_STAUS_TYPE_END(-2, "活动已结束"),
    ACTIVITY_STAUS_TIMEOUT(-3, "抽奖次数已用完"),
    ACTIVITY_STAUS_TYPE_PRIZEOVER(0,"该奖品已领完");

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
    
    ActivityStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String toString() {
        return "ActivityStatus{" +
                "code=" + code +
                ", value='" + value + '\'' +
                '}';
    }
}
