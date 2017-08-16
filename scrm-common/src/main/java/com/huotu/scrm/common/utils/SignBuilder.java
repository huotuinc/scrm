/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liual on 2015-10-19.
 */
public class SignBuilder {
    /**
     * 创建一个sign签名
     * 忽略值为空的
     *
     * @param params 代签名参数，key排序的map
     * @param prefix 签名前缀
     * @param suffix 签名后缀
     * @return 返回鉴权信息字符串
     */
    public static String buildSignIgnoreEmpty(Map<String, Object> params, String prefix, String suffix) throws UnsupportedEncodingException {
        if (prefix == null)
            prefix = "";
        if (suffix == null)
            suffix = "";
        StringBuilder stringBuilder = new StringBuilder(prefix);
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            if (!StringUtils.isEmpty(next.getValue())) {
                if(stringBuilder.length()>0){
                    stringBuilder.append("&");
                }
                stringBuilder.append(next.getKey().toLowerCase()).append("=").append(String.valueOf(next.getValue()));
            }
        }
        stringBuilder.append(suffix);
        return DigestUtils.md5Hex(stringBuilder.toString().getBytes("utf-8"));
    }

    /**
     * 创建一个sign签名
     * 不忽略值为空的
     *
     * @param params 代签名参数，key排序的map
     * @param prefix 签名前缀
     * @param suffix 签名后缀
     * @return 返回鉴权信息字符串
     */
    public static String buildSign(Map<String, String> params, String prefix, String suffix) throws UnsupportedEncodingException {
        if (prefix == null)
            prefix = "";
        if (suffix == null)
            suffix = "";
        StringBuilder stringBuilder = new StringBuilder(prefix);
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            stringBuilder.append(next.getKey()).append(String.valueOf(next.getValue()));
        }
        stringBuilder.append(suffix);
        return DigestUtils.md5Hex(stringBuilder.toString().getBytes("utf-8"));
    }
}
