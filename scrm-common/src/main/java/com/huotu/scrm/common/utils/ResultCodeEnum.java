package com.huotu.scrm.common.utils;

/**
 * Created by liual on 2015-09-21.
 */
public enum ResultCodeEnum {
    SUCCESS(200, "请求成功"),
    PARAM_ERROR(300, "参数错误,"),
    NO_SIGN(301, "签名参数未传"),
    SEND_FAIL(302, "发送失败"),
    SAVE_DATA_ERROR(400, "数据保存出错"),
    APP_KEY_ERROR(401, "没有授权"),
    SIGN_ERROR(402, "签名无效"),
    SYSTEM_BAD_REQUEST(500, "系统请求失败"),
    NO_REPEAT_SEND(501, "请勿重复点击发送按钮！"),
    DATA_BAD_PARSER(600,"数据解析失败"),
    SEND_LIMIT(714, "请验证图片验证码后再获取"),
    IMAGE_CODE_ERROR(715, "图片验证码校验错误"),;

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

    public static final ResultCodeEnum getByResultCode(int resultCode){
        for(ResultCodeEnum item:ResultCodeEnum.values()){
            if(item.getResultCode() == resultCode){
                return item;
            }
        }
        return null;
    }
}
