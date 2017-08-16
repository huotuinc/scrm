/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service.activity.impl;

import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.model.PrizeType;
import com.huotu.scrm.service.repository.activity.ActivityRepository;
import com.huotu.scrm.service.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动Service层实现类
 * Created by montage on 2017/7/12.
 */

@Service
public class ActivityServiceImpl implements ActivityService {


    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Page<Activity> findAllActivity(Long customerId, int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
        return activityRepository.findByIsDeleteFalseAndCustomerId(customerId, pageable);
    }

    @Override
    public Activity findByActId(Long actId) {
        return activityRepository.findByActIdAndIsDeleteFalse(actId);
    }

    @Override
    public void saveActivity(Activity activity) {
        if (activity.getActId() != null) {
            Activity one = activityRepository.findOne(activity.getActId());
            if (one != null) {
                activity.setActPrizes(one.getActPrizes());
            }
        } else {
            ActPrize actPrize = new ActPrize();
            actPrize.setPrizeType(PrizeType.PRIZE_TYPE_THANKS);
            actPrize.setActivity(activity);
            actPrize.setPrizeName("谢谢惠顾");
            List<ActPrize> actPrizeList = new ArrayList<>();
            actPrizeList.add(actPrize);
            activity.setActPrizes(actPrizeList);
        }
        activityRepository.save(activity);
    }

    @Override
    public void updateActivity(Long actId) {
        Activity activity = activityRepository.findOne(actId);
        activity.setDelete(true);
        activity.setOpenStatus(false);
        activityRepository.save(activity);
    }
}
