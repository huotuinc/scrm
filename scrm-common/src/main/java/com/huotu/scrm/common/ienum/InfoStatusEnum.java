package com.huotu.scrm.common.ienum;

/**
 * Created by luohaibo on 2017/7/4.
 * 资讯状态
 */
public enum InfoStatusEnum implements ICommonEnum{

    PUBLISHED(1,"已发布"),
    UNPUBLISHED(0,"未发布"),
    DELETE(-1,"已删除");

    private  Integer code;
    private  String name;

    InfoStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }


    public Object getCode() {
        return this.code;
    }

    public Object getValue() {
        return this.name;
    }



}
