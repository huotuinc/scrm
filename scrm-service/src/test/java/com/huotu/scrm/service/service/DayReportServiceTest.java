package com.huotu.scrm.service.service;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.service.api.ApiService;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hxh on 2017-07-14.
 */
public class DayReportServiceTest extends CommonTestBase {

    @Autowired
    private ApiService apiService;

    /**
     * 测试保存每日资讯浏览奖励积分测试
     */
    @Test
    public void testDaySaveVisitorScore() throws Exception {
        Long customerId = 4421L;
        Long userId = 1058823L;
        Long integral = 100L;
        ApiResult result = apiService.rechargePoint(customerId, userId, integral, IntegralTypeEnum.BROWSE_INFO);
        Assert.assertNotNull(result);
        Assert.assertEquals(HttpStatus.SC_OK, result.getCode());
    }
}
