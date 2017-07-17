package com.huotu.scrm.service.service.impl;

import com.huotu.scrm.common.ienum.ActEnum;
import com.huotu.scrm.common.ienum.EnumHelper;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.repository.ActivityRepository;
import com.huotu.scrm.service.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动Service层实现类
 * Created by montage on 2017/7/12.
 */

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {


    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Page<Activity> findAllActivity(int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
        return activityRepository.findByIsDelete(false,pageable);
    }

    @Override
    public Activity findByActId(Long actId) {
        return activityRepository.findOne(actId);
    }

    @Override
    public Activity saveActivity(Activity activity) {
        Activity newActivity;
        if (activity.getActId() !=null && activity.getActId()!= 0){
            newActivity = activityRepository.findOne(activity.getActId());
        }else {
            newActivity =new Activity();
        }
        newActivity.setCustomerId(activity.getCustomerId());
        newActivity.setActTitle(activity.getActTitle());
        newActivity.setActType(EnumHelper.getEnumType(ActEnum.Activity.class,activity.getActType()));
        newActivity.setStartDate(activity.getStartDate());
        newActivity.setEndDate(activity.getEndDate());
        newActivity.setOpenStatus(activity.isOpenStatus());
        newActivity.setGameCostlyScore(activity.getGameCostlyScore());
        newActivity.setRuleDesc(activity.getRuleDesc());
        newActivity.setRateDesc(activity.getRateDesc());
        newActivity.setDelete(false);
        return activityRepository.save(newActivity);
    }

    @Override
    public void updateActivity(Long actId,boolean isDelete) {
          activityRepository.updateActivityByActId(isDelete,actId);
    }




}
