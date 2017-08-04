/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.repository.mall;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by helloztt on 2017-08-02.
 */
public class UserRepositoryTest extends CommonTestBase{
    @Autowired
    private UserRepository userRepository;
    @Test
    public void findUserTypeById() throws Exception {
        Customer customer = mockCustomer();
        User user = mockUser(customer.getId());
        UserType userType = userRepository.findUserTypeById(user.getId());
        assertEquals(user.getUserType().ordinal(),userType.ordinal());

        UserType noUserType = userRepository.findUserTypeById(random.nextLong() + user.getId());
        assertNull(noUserType);
    }

}