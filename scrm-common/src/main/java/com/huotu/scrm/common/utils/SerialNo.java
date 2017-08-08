/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by WenbinChen on 2015/10/12 20:09.
 */
public class SerialNo {

    /**
     * 以时间戳为基础生成20位随机序列号
     * @return String
     */
    public static  String create() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        Random random = new Random();
        String code="";
        //随机产生6位数字的字符串
        for (int i=0;i<6;i++){
            String rand=String.valueOf(random.nextInt(10));
            code+=rand;
        }
        return date+code;
    }
}
