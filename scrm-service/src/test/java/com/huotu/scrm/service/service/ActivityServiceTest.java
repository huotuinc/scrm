/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service;

import com.huotu.scrm.common.ienum.ActEnum;
import com.huotu.scrm.common.ienum.EnumHelper;
import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.service.activity.ActivityService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 活动service层测试类
 *
 * Created by montage on 2017/7/17.
 */
public class ActivityServiceTest extends CommonTestBase {

    @Autowired
    private ActivityService activityService;

    /**
     * 测试 分页查询所有活动
     */
    @Test
    @Rollback(false)
    public void findAllActivityTest(){
        Page<Activity> allActivity = activityService.findAllActivity(1, 2);
        System.out.println(allActivity.getTotalElements());
        System.out.println(allActivity.getTotalPages());
        List<Activity> activityList = allActivity.getContent();
        activityList.stream()
                .filter(s -> s.isDelete())
                .forEach(s -> System.out.println(s.getActId()));
    }

    /**
     * 根据Id 查询活动
     */
    @Test
    @Rollback(false)
    public void findByIdTest(){
        long ActId = 2;
        Activity activity = activityService.findByActId(ActId);
        System.out.println(activity.toString());
    }


    /**
     * 保存活动
     */
    @Test
    @Rollback(false)
    public void saveActTest() throws Exception{
        Date now = new Date();
        ActEnum.Activity enumType = EnumHelper.getEnumType(ActEnum.Activity.class, 0);
        Date newNow = new Date();
        Activity activity = new Activity();
        activity.setActId(7L);
        activity.setCustomerId(3L);
        activity.setActTitle("agsghsdhdh");
        activity.setActType(enumType);
        activity.setOpenStatus(false);
        activity.setStartDate(LocalDateTime.now());
        activity.setEndDate(LocalDateTime.now());
        activity.setGameCostlyScore(40);
        activity.setRuleDesc("gsghhdhgdshedjtyjdt");
        activity.setRateDesc("agtrshdhdsfhasegwyhqehjrjhtr");
        activity.setDelete(false);
        activity = activityService.saveActivity(activity);
        System.out.println(activity.toString());
    }

    @Test
    @Rollback(false)
    public void updateActTest() throws Exception{
        activityService.updateActivity(7L,true);
    }
}
