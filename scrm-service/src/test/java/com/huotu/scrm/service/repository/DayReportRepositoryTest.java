package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by hxh on 2017-07-13.
 */
public class DayReportRepositoryTest extends CommonTestBase {

    @Autowired
    private DayReportRepository dayReportRepository;
    @Autowired
    private InfoBrowseRepository infoBrowseRepository;
    @Autowired
    private UserLevelRepository userLevelRepository;

    /**
     * 测试日期工具类
     */
    @Test
    public void testDateUtil() {
        System.out.println("");
    }


    //
    @Test
    @Rollback(false)
    public void testDayReportRepository() {
       LocalDate now = LocalDate.now();
//        DayReport dayReport = new DayReport();
//        dayReport.setReportDay(now);
//        System.out.println(now.minusDays(1));
//        System.out.println(now.plusDays(1));
//        List<DayReport> orderByExtensionScore = dayReportRepository.findOrderByExtensionScore(now.minusDays(2));
//        System.out.println(orderByExtensionScore.size());
//        for (DayReport d : orderByExtensionScore
//                ) {
//            System.out.println(d.getId());
//        }
        List<DayReport> li = dayReportRepository.findByReportDayOrderByVisitorNumDesc(now.minusDays(1));
        li.forEach(dayReport -> {
            System.out.println(dayReport.getVisitorNum());
        });

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

    @Test
    public void testUserLevelRepository() {
        UserLevel byLevelAndCustomerId = userLevelRepository.findByLevelAndCustomerId(1, 842L);
        System.out.println(byLevelAndCustomerId.getId());
    }
}
