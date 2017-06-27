/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.scrm.common.ienum;

/**
 * 枚举处理类
 *
 * @author Administrator
 */
public class EnumHelper {

    public static Object getEnumName(Class<? extends ICommonEnum> cls, Object code) {
        ICommonEnum ice = getEnumType(cls, code);
        if (ice != null) {
            return ice.getValue();
//            try {
//                return new String(ice.getName().getBytes("UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                return "";
//            }
        }
        return "";
    }

    public static <T extends ICommonEnum> T getEnumType(Class<T> cls, Object code) {
        for (T item : cls.getEnumConstants()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
