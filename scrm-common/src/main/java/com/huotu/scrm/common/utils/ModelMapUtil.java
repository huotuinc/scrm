/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.common.utils;

import org.springframework.ui.ModelMap;

/**
 * model返回值工具
 * Created by slt on 2017/2/22.
 */
public class ModelMapUtil {

    public static final String RESULT_CODE="resultCode";
    public static final String RESULT_MSG="resultMsg";
    public static final String DATA="data";
    public static final int SUCCESS_CODE=200;
    public static final String SUCCESS_MSG="ok";

    /**
     * 创建modelMap，并传入指定参数
     * @param code      返回码
     * @param msg       返回消息
     * @param data      返回数据
     * @return modelMap
     */
    public static ModelMap createModelMap(int code, String msg, Object data){
        ModelMap modelMap=new ModelMap();
        modelMap.addAttribute(RESULT_CODE,code);
        modelMap.addAttribute(RESULT_MSG,msg);
        modelMap.addAttribute(DATA,data);
        return modelMap;
    }

    /**
     * 根据枚举来生成一个modelMap
     * @param resultEnum    result枚举
     * @param data          数据
     * @return              modelMap
     */
    public static ModelMap createModelMap(ResultCodeEnum resultEnum, Object data){
        return createModelMap(resultEnum.getResultCode(),resultEnum.getResultMsg(),data);
    }

    /**
     * 创建一个默认成功的modelMap
     * @return modelMap
     */
    public static ModelMap createModelMap(){
        return createModelMap(SUCCESS_CODE,SUCCESS_MSG,null);
    }
}
