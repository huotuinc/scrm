package com.huotu.scrm.service.service;

import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.entity.activity.Activity;
import org.springframework.data.domain.Page;

/**
 * 活动service层接口
 *
 * Created by montage on 2017/7/12.
 */
public interface ActivityService {

    /**
     * 分页查询所有活动
     * @return 活动集合
     */
    Page<Activity> findAllActivity(int pageNo,int pageSize);

    /**
     * 添加活动
     * @param activity 活动实体类
     */
    void addActivity(Activity activity);

    /**
     * 编辑活动
     * @param actId 活动Id
     */
    void updateActivity(long actId , Activity newActivity);

    /**
     * 逻辑删除活动
     * @param actId 活动Id
     * @param isDelete 删除
     */
    void deleteActivity(long actId , int isDelete);

    /**
     * 分页查询所有中奖记录
     * @return
     */
    Page<ActWinDetail> getPageActWinDetail(int pageNo ,int pageSize);

    /**
     * 分页查询所有奖品
     * @return
     */
    Page<ActPrize> getPageActPrize(int pageNo , int pageSize);

    /**
     * 添加奖品
     * @param actPrize 奖品实体类
     */
    void addActPrize(ActPrize actPrize);

    /**
     * 编辑奖品
     * @param prizeId 奖品Id
     */
    void updateActPrize(long prizeId,ActPrize newActPrize);

    /**
     * 删除奖品
     * @param prizeId 奖品Id
     */
    void deleteActPrize(long prizeId);
}
