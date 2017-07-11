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
 * 用户类型，枚举值是按顺序排列的（0,1,2），所以不需要 implements ICommonEnum
 * @author CJ
 */
public enum UserType {

    normal,
    /**
     * 小伙伴
     */
    buddy,
    /**
     * 默认小伙伴
     */
    defaultBuddy

}
