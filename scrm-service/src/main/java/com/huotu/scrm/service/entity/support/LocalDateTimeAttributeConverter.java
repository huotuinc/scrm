package com.huotu.scrm.service.entity.support;

import com.huotu.scrm.common.utils.LocalDateTimeUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.text.ParseException;
import java.time.LocalDateTime;

/**
 * Created by hxh on 2017-07-18.
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, String> {


    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        return LocalDateTimeUtil.toStr(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        try {
            return LocalDateTimeUtil.toLocalDateTime(dbData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
