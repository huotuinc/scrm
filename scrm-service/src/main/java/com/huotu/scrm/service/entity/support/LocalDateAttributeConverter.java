package com.huotu.scrm.service.entity.support;


import com.huotu.scrm.common.utils.LocalDateUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.text.ParseException;
import java.time.LocalDate;

/**
 * Created by hxh on 2017-07-15.
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        return LocalDateUtil.toStr(attribute);
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        try {
            return LocalDateUtil.toLocalDate(dbData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
