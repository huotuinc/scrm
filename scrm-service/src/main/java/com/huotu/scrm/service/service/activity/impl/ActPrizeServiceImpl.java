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
import com.huotu.scrm.service.repository.activity.ActPrizeRepository;
import com.huotu.scrm.service.repository.activity.ActivityRepository;
import com.huotu.scrm.service.service.activity.ActPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 奖品service实现类
 * Created by montage on 2017/7/13.
 */

@Service
public class ActPrizeServiceImpl implements ActPrizeService {

    @Autowired
    private ActPrizeRepository actPrizeRepository;
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public void saveActPrize(Activity activity) {
        activityRepository.save(activity);
    }

    @Override
    public void saveActPrice(ActPrize actPrize) {
        actPrizeRepository.saveAndFlush(actPrize);
    }


    @Override
    public void deleteActPrize(Long prizeId) {
        ActPrize actPrize = actPrizeRepository.findOne(prizeId);
        Activity activity = actPrize.getActivity();
        activity.getActPrizes().remove(actPrize);
        activityRepository.save(activity);
    }

    @Override
    public ActPrize findByPrizeId(Long prizeId) {
        return actPrizeRepository.findOne(prizeId);
    }

}
