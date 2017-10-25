package com.huotu.scrm.service.service.mall.impl;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.mall.UserBanner;
import com.huotu.scrm.service.repository.mall.UserBannerRepository;
import com.huotu.scrm.service.service.mall.UserBannerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by xyr on 2017/10/25.
 */
public class UserBannerServiceImplTest extends CommonTestBase {

    @Autowired
    private UserBannerService userBannerService;
    @Autowired
    private UserBannerRepository userBannerRepository;

    @Test
    public void findUserBanner() throws Exception {
        Long customerId = Long.valueOf(random.nextInt(10000));
        UserBanner userBanner1 = new UserBanner();
        userBanner1.setCustomerId(customerId);
        userBanner1.setImage(RandomStringUtils.random(6));
        userBanner1.setLinkUrl(RandomStringUtils.random(6));
        userBanner1.setTime(LocalDateTime.now());
        userBanner1.setType(4);
        userBanner1 = userBannerRepository.saveAndFlush(userBanner1);
        UserBanner userBanner2 = new UserBanner();
        userBanner2.setCustomerId(customerId);
        userBanner2.setImage(RandomStringUtils.random(6));
        userBanner2.setLinkUrl(RandomStringUtils.random(6));
        userBanner2.setTime(LocalDateTime.now().plusDays(-5));
        userBanner2.setType(4);
        userBanner2 = userBannerRepository.saveAndFlush(userBanner2);

        UserBanner result = userBannerService.findUserBanner(customerId);
        assertEquals(result.getId(), userBanner1.getId());
    }

}