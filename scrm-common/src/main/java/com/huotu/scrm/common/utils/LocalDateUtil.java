package com.huotu.scrm.common.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by hxh on 2017-07-15.
 */
public class LocalDateUtil {
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy.MM .dd HH:mm");
    static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 字符串转换成LocalDateTime
     *
     * @param text yyyy-MM-dd
     * @return
     * @throws ParseException 转换异常
     */
    public static LocalDate toLocalDate(String text) throws ParseException {
        if (StringUtils.isEmpty(text))
            return null;

        return LocalDate.from(dateTimeFormatter.parse(text));
    }

    /**
     * 字符串转换成LocalDate
     *
     * @param text yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static LocalDate convertLocalDate(String text) throws ParseException {
        if (StringUtils.isEmpty(text))
            return null;

        return LocalDate.from(localDateFormatter.parse(text));
    }

    /**
     * 日期转换为字符串
     *
     * @param dateTime
     * @return yyyy-MM-dd  格式字符
     */
    public static String toStr(LocalDate dateTime) {
        return dateTimeFormatter.format(dateTime);
    }

    /**
     * 字符串转换成LocalDate
     *
     * @param text "yyyy.MM
     *             <p>
     *             .dd HH:mm
     * @return
     */
    public static LocalDate getLocalDate(String text) {
        if (StringUtils.isEmpty(text))
            return null;
        return LocalDate.from(dateTimeFormatter1.parse(text));
    }


}
