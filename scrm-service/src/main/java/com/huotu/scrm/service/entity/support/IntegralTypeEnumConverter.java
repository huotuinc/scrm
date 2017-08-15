/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.entity.support;

import com.huotu.scrm.common.ienum.EnumHelper;
import com.huotu.scrm.common.ienum.IntegralTypeEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by helloztt on 2017-08-15.
 */
@Converter(autoApply = true)
public class IntegralTypeEnumConverter implements AttributeConverter<IntegralTypeEnum,Integer> {
    @Override
    public Integer convertToDatabaseColumn(IntegralTypeEnum attribute) {
        return attribute.getCode();
    }

    @Override
    public IntegralTypeEnum convertToEntityAttribute(Integer dbData) {
        if(dbData == null){
            return null;
        }
        return EnumHelper.getEnumType(IntegralTypeEnum.class,dbData);
    }
}
