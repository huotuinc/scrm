/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * Created by montage on 2017/7/13.
 */
public class DateUtil {
    private static final long HOURS_PER_DAY = 24;

    /**
     * 字符串转日期
     *
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String compareLocalDateTime(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        Duration duration = Duration.between(localDateTime1, localDateTime2);
        System.out.println(duration.toHours());
        long totalDays = duration.toDays();
        long totalHours = duration.toHours();
        long totalMin = duration.toMinutes();
        StringBuilder sb = new StringBuilder();
        //显示了天就不用显示分了
        boolean isDay = false;
        if (totalDays > 0) {
            sb.append(totalDays).append("天");
            isDay = true;
        }
        if (totalHours > 0) {
            sb.append(totalHours).append("时");
        }
        if(totalMin > 0 && !isDay){
            sb.append(totalMin).append("分");
        }
        sb.append("前");
        return sb.toString();
    }
}
