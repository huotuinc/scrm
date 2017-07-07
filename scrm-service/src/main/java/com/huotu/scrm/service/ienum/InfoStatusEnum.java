package com.huotu.scrm.service.ienum;

/**
 * Created by luohaibo on 2017/7/4.
 * 资讯状态
 */
public enum InfoStatusEnum {

    published(1, "已发布"),
    unpublished(0, "未发布"),
    delete(-1, "已删除");

    private final int value;
    private final String description;

    InfoStatusEnum (Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * @return 资讯状态值
     */
    public int getValue() {
        return this.value;
    }

    /**
     * @return 资讯状态值描述
     */
    public String getDescription() {
        return this.description;
    }

}
