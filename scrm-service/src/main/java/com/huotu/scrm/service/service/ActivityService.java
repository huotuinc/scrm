package com.huotu.scrm.service.service;

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
    Page<Activity> findAllActivity(int pageNo, int pageSize);

    /**
     * 根据Id查询
     * @param actId 活动Id
     * @return
     */
    Activity findByActId(Long actId);

    /**
     * 保存活动
     */
    Activity saveActivity(Activity activity);

    /**
     * 逻辑删除活动
     * @param actId 活动Id
     * @param isDelete 删除
     */
    void deleteActivity(Long actId, boolean isDelete);



}
