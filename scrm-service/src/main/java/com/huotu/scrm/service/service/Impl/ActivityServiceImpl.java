package com.huotu.scrm.service.service.impl;

import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.repository.ActPrizeRepository;
import com.huotu.scrm.service.repository.ActWinDetailRepository;
import com.huotu.scrm.service.repository.ActivityRepository;
import com.huotu.scrm.service.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ActPrizeRepository actPrizeRepository;

    @Autowired
    private ActWinDetailRepository actWinDetailRepository;


    @Override
    public Page<Activity> findAllActivity(int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
        return activityRepository.findByIsDelete(1,pageable);
    }

    @Override
    public void addActivity(Activity activity) {
        try {
            if (activity != null) {
                activityRepository.save(activity);
            }
        }catch (Exception e){
            e.getMessage();
        }
    }


    @Override
    public void updateActivity(long actId, Activity newActivity) {
        Activity oldActivity = activityRepository.findOne(actId);
        oldActivity.setActTitle(newActivity.getActTitle());
        /*oldActivity.setActType(Integer.valueOf(newActivity.getActType().getCode()));*/
        oldActivity.setStartDate(newActivity.getStartDate());
        oldActivity.setEndDate(newActivity.getEndDate());
        /*oldActivity.setOpenStatus(newActivity.getOpenStatus());*/
        oldActivity.setGameCostlyScore(newActivity.getGameCostlyScore());
        oldActivity.setRuleDesc(newActivity.getRuleDesc());
        oldActivity.setRateDesc(newActivity.getRateDesc());
        activityRepository.save(oldActivity);
    }

    @Override
    public void deleteActivity(long actId,int isDelete) {
          activityRepository.deleteActivityByActId(isDelete,actId);
    }

    @Override
    public Page<ActWinDetail> getPageActWinDetail(int pageNo, int pageSize) {
        return actWinDetailRepository.findAll(new PageRequest(pageNo-1,pageSize));
    }

    @Override
    public Page<ActPrize> getPageActPrize(int pageNo, int pageSize) {
        return actPrizeRepository.findAll(new PageRequest(pageNo-1,pageSize,new Sort(Sort.Direction.DESC,"sort")));
    }

    @Override
    public void addActPrize(ActPrize actPrize) {
        try{
            if (actPrize != null){
                actPrizeRepository.save(actPrize);
            }
        }catch (Exception e){
            e.getMessage();
        }

    }

    @Override
    public void updateActPrize(long prizeId ,ActPrize newActPrize) {
        ActPrize oldActPrize = actPrizeRepository.findOne(prizeId);
        oldActPrize.setPrizeName(newActPrize.getPrizeName());
        oldActPrize.setPrizeImageUrl(newActPrize.getPrizeImageUrl());
        oldActPrize.setWinRate(newActPrize.getWinRate());
        oldActPrize.setSort(newActPrize.getSort());
        actPrizeRepository.save(oldActPrize);
    }

    @Override
    public void deleteActPrize(long prizeId) {
        actPrizeRepository.delete(prizeId);
    }
}
