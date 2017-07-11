/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.scrm.common.httputil;


/**
 * Created by liual on 2015-11-11.
 */
public class HttpResult {
    private int httpStatus;
    private String httpContent;

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getHttpContent() {
        return httpContent;
    }

    public void setHttpContent(String httpContent) {
        this.httpContent = httpContent;
    }

    public HttpResult(int httpStatus, String httpContent) {
        this.httpStatus = httpStatus;
        this.httpContent = httpContent;
    }
}
