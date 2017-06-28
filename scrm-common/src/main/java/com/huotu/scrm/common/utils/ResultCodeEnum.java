package com.huotu.scrm.common.utils;

/**
 * Created by liual on 2015-09-21.
 */
public enum ResultCodeEnum {
    SUCCESS(200, "请求成功"),
    SYSTEM_BAD_REQUEST(500,"系统请求失败"),
    DATA_BAD_PARSER(600,"数据解析失败"),
    SIGN_ERROR(300,"签名错误"),
    NO_SIGN(301,"签名参数未传"),
    SAVE_DATA_ERROR(400,"数据保存出错");

    //todo 其他状态
    private int resultCode;
    private String resultMsg;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    ResultCodeEnum(int resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
