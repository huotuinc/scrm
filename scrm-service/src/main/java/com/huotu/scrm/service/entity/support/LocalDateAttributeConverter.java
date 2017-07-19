package com.huotu.scrm.service.entity.support;


import org.springframework.data.convert.Jsr310Converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by hxh on 2017-07-15.
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {


    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        return Jsr310Converters.LocalDateToDateConverter.INSTANCE.convert(attribute);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        return Jsr310Converters.DateToLocalDateConverter.INSTANCE.convert(dbData);
    }
}
