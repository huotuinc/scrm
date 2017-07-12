package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.service.ReportMonthService;
import com.huotu.scrm.service.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by hxh on 2017-07-12.
 */
public class ReportMonthServiceTest extends CommonTestBase {

    @Autowired
    private ReportMonthService reportMonthService;

    @Test
    public void testDate() {
        Date lastDay = DateUtil.getLastDay();
        Date lastMonthFirstDay = DateUtil.getLastMonthFirstDay();
        Date lastMonthLastDay = DateUtil.getLastMonthLastDay();
        System.out.println("昨日日期："+lastDay);
        System.out.println("上月第一天："+lastMonthFirstDay);
        System.out.println("上月最后一天："+lastMonthLastDay);
    }
}
