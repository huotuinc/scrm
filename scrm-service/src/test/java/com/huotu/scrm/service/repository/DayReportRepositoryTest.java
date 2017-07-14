package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

/**
 * Created by hxh on 2017-07-13.
 */
public class DayReportRepositoryTest extends CommonTestBase {

    @Autowired
    private DayReportRepository dayReportRepository;

    @Autowired
    private InfoBrowseRepository infoBrowseRepository;

    /**
     * 测试日期工具类
     */
    @Test
    public void testDateUtil() {
        System.out.println("前天日期(时分秒默认为最大):" + DateUtil.getBeforeLastDay());
        System.out.println("昨天日期(时分秒默认最大):" + DateUtil.getLastDayMax());
    }

    /**
     * 测试保存每日统计信息
     */
    @Test
    @Rollback(false)
    public void testDayReportRepository() {
        //保存一天数据到每日统计表
        Date lastDay = DateUtil.getLastDay();
        DayReport dayReport = new DayReport();
//        dayReport.setId(1L);
        dayReport.setLevelId(1);
        dayReport.setUserId(1L);
        dayReport.setSalesman(true);
        dayReport.setFollowNum(1);
        dayReport.setExtensionScore(1);
        dayReport.setReportDay(lastDay);
        dayReportRepository.save(dayReport);
        List<DayReport> all1 = dayReportRepository.findAll();
        for (DayReport dayReport1 : all1) {
            System.out.println(dayReport1.toString());
        }

        //测试统计排名访客数据
        List<DayReport> orderByVisitorNum = dayReportRepository.findOrderByVisitorNum(DateUtil.getBeforeLastDay(), DateUtil.getLastDayMax());
        System.out.println("。。。。。。。。。。。。。。。。。。：" + orderByVisitorNum.size());
        for (DayReport d : orderByVisitorNum
                ) {
            System.out.println(d.getUserId().equals(687500L));
        }
    }

    /**
     * 测试查询咨询转发来源用户ID（去掉重复）
     */
    @Test
    public void testInfoBrowseRepository() {
        List<Long> bySourceUserId = infoBrowseRepository.findBySourceUserId();
        for (long infoBrowse : bySourceUserId) {
            System.out.println(infoBrowse);
        }
    }
}
