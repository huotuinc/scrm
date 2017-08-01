/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service.activity;

import com.huotu.scrm.service.entity.activity.Activity;
import org.springframework.data.domain.Page;

/**
 * 活动service层接口
 * <p>
 * Created by montage on 2017/7/12.
 */
public interface ActivityService {

    /**
     * 分页查询所有活动
     *
     * @return 活动集合
     */
    Page<Activity> findAllActivity(int pageNo, int pageSize);

    /**
     * 根据Id查询
     *
     * @param actId 活动Id
     * @return
     */
    Activity findByActId(Long actId);

    /**d
     * 保存活动
     */
    void saveActivity(Activity activity);

    /**
     * 逻辑删除活动
     *
     * @param actId 活动Id
     */
    void updateActivity(Long actId);


}
