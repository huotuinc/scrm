package com.huotu.scrm.service.converter;

import com.huotu.scrm.common.ienum.EnumHelper;
import com.huotu.scrm.service.model.prizeTypeEnum;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


/**
 * Created by luohaibo on 2017/8/3.
 */
@Converter(autoApply = true)
public class prizeTypeConverter implements AttributeConverter<prizeTypeEnum,Integer> {


    @Override
    public Integer convertToDatabaseColumn(prizeTypeEnum attribute) {
        return attribute.getCode();
    }

    @Override
    public prizeTypeEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return EnumHelper.getEnumType(prizeTypeEnum.class, dbData);
    }
}
