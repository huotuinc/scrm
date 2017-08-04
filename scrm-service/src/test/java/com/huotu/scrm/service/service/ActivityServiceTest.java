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
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.repository.activity.ActPrizeRepository;
import com.huotu.scrm.service.repository.activity.ActivityRepository;
import com.huotu.scrm.service.service.activity.ActivityService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 活动service层测试类
 *
 * Created by montage on 2017/7/17.
 */
public class ActivityServiceTest extends CommonTestBase {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActPrizeRepository actPrizeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 测试 分页查询所有活动
     */
    @Test
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
    public void findByIdTest(){
        // TODO: 2017-08-04 重写
        long ActId = 2;
        Activity activity = activityService.findByActId(ActId);
        if(activity != null){
            System.out.println(activity.toString());
        }
    }


    /**
     * 保存活动
     */
    @Test
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
//        activity.setStartDate(now);
//        activity.setEndDate(newNow);
        activity.setGameCostlyScore(40);
        activity.setRuleDesc("gsghhdhgdshedjtyjdt");
        activity.setRateDesc("agtrshdhdsfhasegwyhqehjrjhtr");
        activity.setDelete(false);
        activityService.saveActivity(activity);
        System.out.println(activity.toString());
    }

    @Test
    @Ignore
    public void updateActTest() throws Exception{
        // TODO: 2017-08-04 报错，说明存在漏洞
        activityService.updateActivity(7L);
    }

    @Test
    public void deleteActPrize() throws Exception{
        List<ActPrize> actPrizeList = new ArrayList<>();
        Activity activity = new Activity();

        ActPrize actPrize1 = new ActPrize();
        actPrize1.setPrizeName(UUID.randomUUID().toString());
        actPrize1.setActivity(activity);

        ActPrize actPrize2 = new ActPrize();
        actPrize2.setPrizeName(UUID.randomUUID().toString());
        actPrize2.setActivity(activity);

        actPrizeList.add(actPrize1);
        actPrizeList.add(actPrize2);
        activity.setActPrizes(actPrizeList);
        activity = activityRepository.saveAndFlush(activity);
        //查询
        Activity realActivity = activityRepository.findOne(activity.getActId());
        Assert.assertEquals(2,realActivity.getActPrizes().size());
        Assert.assertEquals(actPrize1.getPrizeName(),realActivity.getActPrizes().get(0).getPrizeName());
        Assert.assertEquals(actPrize2.getPrizeName(),realActivity.getActPrizes().get(1).getPrizeName());

        //查询actPrize1
        actPrize1 = actPrizeRepository.findOne(actPrize1.getPrizeId());

        Assert.assertTrue(actPrize1 == realActivity.getActPrizes().get(0));

        //删除actPrize1
        activity.getActPrizes().remove(actPrize1);
        activityRepository.saveAndFlush(activity);
        actPrize1 = actPrizeRepository.findOne(actPrize1.getPrizeId());
        Assert.assertNull(actPrize1);
    }

}
