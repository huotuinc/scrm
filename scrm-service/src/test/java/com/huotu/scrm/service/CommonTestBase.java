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
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.model.PrizeType;
import com.huotu.scrm.service.repository.activity.ActPrizeRepository;
import com.huotu.scrm.service.repository.activity.ActivityRepository;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
    @Autowired
    protected CustomerRepository customerRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserLevelRepository userLevelRepository;
    @Autowired
    protected InfoBrowseRepository infoBrowseRepository;
    @Autowired
    protected ActPrizeRepository actPrizeRepository;
    @Autowired
    protected ActivityRepository activityRepository;
    @Autowired
    protected InfoConfigureRepository infoConfigureRepository;

    protected Random random = new Random();

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
        return customerRepository.saveAndFlush(customer);
    }

    protected User mockUser(Long customerId, UserType userType, Long userLevelId) {
        //新增用户
        User user = new User();
        user.setId(Long.valueOf(String.valueOf(random.nextInt())));
        user.setCustomerId(customerId);
        user.setLoginName(UUID.randomUUID().toString());
        user.setUserGender("男");
        user.setNickName(UUID.randomUUID().toString());
        user.setLevelId(userLevelId);
        user.setLockedBalance(random.nextDouble());
        user.setLockedIntegral(random.nextDouble());
        user.setRegTime(new Date());
        user.setUserMobile(String.valueOf(random.nextInt()));
        user.setUserBalance(random.nextDouble());
        user.setUserType(userType);
        user.setUserTempIntegral(random.nextInt());
        user.setWeixinImageUrl(UUID.randomUUID().toString());
        user.setWxNickName(UUID.randomUUID().toString().replace("-", ""));
        return userRepository.saveAndFlush(user);
    }

    protected User mockUser(Long customerId) {
        return mockUser(customerId, UserType.normal, 0L);
    }

    protected User mockUser(Long customerId, UserType userType) {
        return mockUser(customerId, userType, 0L);
    }

    @SuppressWarnings("Duplicates")
    protected UserLevel mockUserLevel(Long customerId, UserType userType, boolean isSalesman) {
        UserLevel userLevel = new UserLevel();
        userLevel.setCustomerId(customerId);
        userLevel.setLevel(random.nextInt());
        userLevel.setLevelName(UUID.randomUUID().toString());
        userLevel.setType(userType);
        userLevel.setSalesman(isSalesman);
        return userLevelRepository.saveAndFlush(userLevel);
    }

    protected InfoBrowse mockInfoBrowse(Long infoId, Long sourceUserId, Long readUserId, Long customerId) {
        InfoBrowse infoBrowse = new InfoBrowse();
        infoBrowse.setInfoId(infoId);
        infoBrowse.setSourceUserId(sourceUserId);
        infoBrowse.setReadUserId(readUserId);
        infoBrowse.setCustomerId(customerId);
        infoBrowse.setBrowseTime(LocalDateTime.now().minusHours(1));
        return infoBrowseRepository.saveAndFlush(infoBrowse);
    }

    protected ActPrize mockActPrize(Activity activity, PrizeType typeEnum) {
        ActPrize actPrize = new ActPrize();
        actPrize.setActivity(activity);
        actPrize.setPrizeName(UUID.randomUUID().toString());
        actPrize.setPrizeType(typeEnum);
        actPrize.setPrizeImageUrl(UUID.randomUUID().toString());
        actPrize.setSort(random.nextInt());
        actPrize.setWinRate(random.nextInt());
        actPrize.setPrizeCount(random.nextInt());
        actPrize.setRemainCount(random.nextInt());
        return actPrizeRepository.saveAndFlush(actPrize);
    }

    protected Activity mockActivity(Long customerId, List<ActPrize> actPrizeList, boolean openStatus) {
        Activity activity = new Activity();
        activity.setCustomerId(customerId);
        activity.setActTitle(UUID.randomUUID().toString());
        activity.setDelete(false);
        activity.setRateDesc(UUID.randomUUID().toString());
        activity.setRuleDesc(UUID.randomUUID().toString());
        activity.setGameCostlyScore(random.nextInt());
        activity.setOpenStatus(openStatus);
        activity.setActPrizes(actPrizeList);
        activity.setStartDate(LocalDateTime.now().minusDays(3));
        activity.setEndDate(LocalDateTime.now().plusDays(3));
        return activityRepository.saveAndFlush(activity);
    }
    protected Activity mockActivityEntity(Long customerId, List<ActPrize> actPrizeList, boolean openStatus) {
        Activity activity = new Activity();
        activity.setActId(Long.valueOf(String.valueOf(random.nextInt())));
        activity.setCustomerId(customerId);
        activity.setActTitle(UUID.randomUUID().toString());
        activity.setDelete(false);
        activity.setRateDesc(UUID.randomUUID().toString());
        activity.setRuleDesc(UUID.randomUUID().toString());
        activity.setGameCostlyScore(random.nextInt());
        activity.setOpenStatus(openStatus);
        activity.setActPrizes(actPrizeList);
        activity.setStartDate(LocalDateTime.now().minusDays(3));
        activity.setEndDate(LocalDateTime.now().plusDays(3));
        return activity;
    }
    protected InfoBrowse mockInfoBrowseLastDay(Long infoId, Long sourceUserId, Long readUserId, Long customerId) {
        InfoBrowse infoBrowse = new InfoBrowse();
        infoBrowse.setInfoId(infoId);
        infoBrowse.setSourceUserId(sourceUserId);
        infoBrowse.setReadUserId(readUserId);
        infoBrowse.setCustomerId(customerId);
        infoBrowse.setBrowseTime(LocalDateTime.now().minusDays(1));
        return infoBrowseRepository.saveAndFlush(infoBrowse);
    }
    protected InfoConfigure mockInfoConfigure(Long customerId, boolean exchangeSwitch, int type, int rate, boolean exchangeLimitSwitch, int dayExchangeLimit) {
        InfoConfigure infoConfigure = new InfoConfigure();
        infoConfigure.setCustomerId(customerId);
        infoConfigure.setExchangeSwitch(exchangeSwitch);
        infoConfigure.setExchangeUserType(type);
        infoConfigure.setDayExchangeLimit(dayExchangeLimit);
        infoConfigure.setExchangeRate(rate);
        infoConfigure.setDayExchangeLimitSwitch(exchangeLimitSwitch);
        return infoConfigureRepository.saveAndFlush(infoConfigure);
    }
}
