/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 *
 */

package com.huotu.scrm.service;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.config.ServiceConfig;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by helloztt on 2017-05-03.
 */

@ActiveProfiles("test")
@ContextConfiguration(classes = ServiceConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CommonTestBase {

    protected Customer mockCustomer() {
        Customer customer = new Customer();
        customer.setLoginName(UUID.randomUUID().toString().replace("-", ""));
        customer.setEnabled(true);
        customer.setLoginPassword(UUID.randomUUID().toString().replace("-", ""));
        customer.setNickName(UUID.randomUUID().toString().replace("-", ""));
        Random random = new Random();
        customer.setMobile(String.valueOf(random.nextInt()));
        customer.setNickName(UUID.randomUUID().toString().replace("-", ""));
        customer.setSubDomain(UUID.randomUUID().toString().replace("-", ""));
        customer.setId(Long.valueOf(String.valueOf(random.nextInt())));
        return customer;
    }

    protected User mockUser(Long customerId, UserType userType) {
        //新增用户
        Random random = new Random();
        User user = new User();
        user.setId(Long.valueOf(String.valueOf(random.nextInt())));
        user.setCustomerId(customerId);
        user.setLoginName(UUID.randomUUID().toString());
        user.setUserGender("男");
        user.setNickName(UUID.randomUUID().toString());
        user.setLevelId(0);
        user.setLockedBalance(random.nextDouble());
        user.setLockedIntegral(random.nextDouble());
        user.setRegTime(new Date());
        user.setUserMobile(String.valueOf(random.nextInt()));
        user.setUserBalance(random.nextDouble());
        user.setUserType(userType);
        user.setUserTempIntegral(random.nextInt());
        user.setWeixinImageUrl(UUID.randomUUID().toString());
        user.setWxNickName(UUID.randomUUID().toString().replace("-", ""));
        return user;
    }

    protected User mockUser(Long customerId) {
        return mockUser(customerId, UserType.normal);
    }

}
