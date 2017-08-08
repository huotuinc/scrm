package com.huotu.scrm.service.repository;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by hxh on 2017-07-13.
 */
public class DayReportRepositoryTest extends CommonTestBase {

    @Autowired
    private InfoBrowseRepository infoBrowseRepository;
    @Autowired
    private UserLevelRepository userLevelRepository;


    /**
     * 测试咨询转发量查询
     */
    @Test
    public void testDayReportRepository() {
         mockInfoBrowse(1L, 1L, 2L, 3344L);
        mockInfoBrowse(2L, 1L, 4L, 3344L);
        LocalDateTime now = LocalDateTime.now(), threeDayBefore = now.minusDays(3);
        List<InfoBrowse> list = infoBrowseRepository.findForwardNumBySourceUserId(threeDayBefore, now, 1L);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testUserLevelRepository() {
        Customer customer = mockCustomer();
        UserLevel userLevel = mockUserLevel(customer.getId(), UserType.buddy, false);
        User user = mockUser(customer.getId(), userLevel.getType(), userLevel.getId());
        UserLevel byLevelAndCustomerId = userLevelRepository.findByIdAndCustomerId(user.getLevelId(), user.getCustomerId());
        Assert.assertNotNull(byLevelAndCustomerId);
    }
}
