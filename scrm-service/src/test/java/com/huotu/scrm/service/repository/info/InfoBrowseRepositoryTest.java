/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.repository.info;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by helloztt on 2017-08-02.
 */
public class InfoBrowseRepositoryTest extends CommonTestBase{

    @Test
    public void testFindFollowNum(){
        Customer customer = mockCustomer();
        UserLevel salesmanLevel = mockUserLevel(customer.getId(),UserType.buddy,true);
        User buddyUser = mockUser(customer.getId(), UserType.buddy,salesmanLevel.getId());

        LocalDateTime now = LocalDateTime.now(),yesterday = now.minusDays(1);

        User readUser1 = mockUser(customer.getId());
        User readUser2 = mockUser(customer.getId());
        Long infoId = random.nextLong();
        InfoBrowse user1ReadInfo = mockInfoBrowse(infoId,buddyUser.getId(),readUser1.getId(),customer.getId());
        InfoBrowse user2ReadInfo = mockInfoBrowse(infoId,buddyUser.getId(),readUser2.getId(),customer.getId());

        List<InfoBrowse> infoBrowseList = infoBrowseRepository.findForwardNumBySourceUserId(yesterday,now,buddyUser.getId());
        assertEquals(1,infoBrowseList.size());


    }

}