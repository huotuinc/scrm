package com.huotu.scrm.common.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by hxh on 2017-07-18.
 */
public class LocalDateTimeUtil {
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static final DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");


    /**
     * 字符串转换成LocalDateTime
     *
     * @param text yyyy-MM-dd hh:mm:ss
     * @return
     * @throws ParseException 转换异常
     */
    public static LocalDateTime toLocalDateTime(String text) throws ParseException {
        if (StringUtils.isEmpty(text))
            return null;

        return LocalDateTime.from(dateTimeFormatter.parse(text));
    }

    /**
     * 日期转换为字符串
     *
     * @param dateTime
     * @return yyyy-MM-dd hh:mm:ss 格式字符
     */
    public static String toStr(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTimeFormatter.format(dateTime);
    }

    /**
     * 日期转换为字符串
     *
     * @param dateTime
     * @return yyyyMMddhhmmssSSS 格式字符
     */
    public static String toStr2(LocalDateTime dateTime) {
        return dateTimeFormatter2.format(dateTime);
    }

}
