package com.huotu.scrm.common.utils;

/**
 * 接口返回实体
 * Created by liual on 2015-09-21.
 */
public class ApiResult<T> {

    /**
     * 返回结果描述
     */
    private String msg;

    private int code;

    /**
     * 返回数据
     */
    private T data;

    public ApiResult() {
        super();
    }

    public ApiResult(String msg) {
        this.msg = msg;
    }

    public ApiResult(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public static ApiResult resultWith(ResultCodeEnum resultCodeEnum, Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.msg = resultCodeEnum.getResultMsg();
        apiResult.code = resultCodeEnum.getResultCode();
        apiResult.data = data;
        return apiResult;
    }

    public static ApiResult resultWith(ResultCodeEnum resultCodeEnum) {
        ApiResult apiResult = new ApiResult();
        apiResult.msg = resultCodeEnum.getResultMsg();
        apiResult.code = resultCodeEnum.getResultCode();
        return apiResult;
    }

    public static ApiResult resultWith(ResultCodeEnum resultCodeEnum, String msg, Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.code = resultCodeEnum.getResultCode();
        apiResult.msg = msg;
        apiResult.data = data;
        return apiResult;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
