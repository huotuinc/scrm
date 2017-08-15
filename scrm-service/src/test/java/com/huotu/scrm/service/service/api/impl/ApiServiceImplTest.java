/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service.api.impl;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.service.api.ApiService;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by helloztt on 2017-08-09.
 */
public class ApiServiceImplTest extends CommonTestBase{
    @Autowired
    private ApiService apiService;

    /**
     * 调用接口时请指定 host: 192.168.1.13   mallapi.xiangzhang.com
     * @throws Exception
     */
    @Test
    @Ignore
    public void rechargePoint() throws Exception {
        Long customerId = 4421L;
        Long userId = 1058823L;
        Long integral = 100L;
        ApiResult result = apiService.rechargePoint(customerId,userId,integral, IntegralTypeEnum.TURN_INFO);
        assertNotNull(result);
        assertEquals(HttpStatus.SC_OK,result.getCode());
    }

}