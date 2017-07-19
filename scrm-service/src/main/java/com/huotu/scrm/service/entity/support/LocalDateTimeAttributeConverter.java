package com.huotu.scrm.service.entity.support;

import org.springframework.data.convert.Jsr310Converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by hxh on 2017-07-18.
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Date> {


    @Override
    public Date convertToDatabaseColumn(LocalDateTime attribute) {
        return Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date dbData) {
        return Jsr310Converters.DateToLocalDateTimeConverter.INSTANCE.convert(dbData);
    }
}
