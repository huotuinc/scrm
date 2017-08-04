package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
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
    @Autowired
    private MonthReportRepository monthReportRepository;
    @Autowired
    private BusinessCardRecordRepository businessCardRecordRepository;
    @Autowired
    private InfoRepository infoRepository;

    @Test
    public void testDayReportRepository() {
        LocalDateTime now = LocalDateTime.now(),threeDayBefore = now.minusDays(3);
        List<InfoBrowse> list = infoBrowseRepository.findForwardNumBySourceUserId(threeDayBefore,now, 1L);
        System.out.println("............"+list.size());
        list.forEach(infoBrowse -> {
            System.out.println(infoBrowse.toString());
        });
    }

    @Test
    public void testUserLevelRepository() {
        // TODO: 2017-08-04 需要重写
        UserLevel byLevelAndCustomerId = userLevelRepository.findByIdAndCustomerId(1L, 842L);
        if(byLevelAndCustomerId != null){
            System.out.println(byLevelAndCustomerId.getId());
        }
    }
}
