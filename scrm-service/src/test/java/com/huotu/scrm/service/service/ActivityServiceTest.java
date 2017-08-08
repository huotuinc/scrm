/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.service.activity.ActivityService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动service层测试类
 * <p>
 * Created by montage on 2017/7/17.
 */
public class ActivityServiceTest extends CommonTestBase {

    @Autowired
    private ActivityService activityService;
    private Customer customer;
    private List<ActPrize> actPrizeList = new ArrayList<>();
    private List<ActPrize> actPrizeListTwo = new ArrayList<>();

    @Before
    public void setInfo() {
        customer = mockCustomer();
        ActPrize actPrize = new ActPrize();
        actPrizeList.add(actPrize);
        ActPrize actPrizeTwo = new ActPrize();
        actPrizeListTwo.add(actPrizeTwo);
    }

    /**
     * 根据Id 查询活动
     */
    @Test
    public void findByIdTest() {
        Activity activity = mockActivity(customer.getId(), actPrizeList, true);
        Activity byActId = activityService.findByActId(activity.getActId());
        Assert.assertNotNull(byActId);
    }

    /**
     * 保存活动（顺带测试活动的分页查询）
     */
    @Test
    public void saveActTest() throws Exception {
        Activity activity = mockActivityEntity(customer.getId(), actPrizeList, true);
        Activity activity1 = mockActivityEntity(customer.getId(), actPrizeListTwo, true);
        activityService.saveActivity(activity);
        activityService.saveActivity(activity1);
        Page<Activity> allActivity = activityService.findAllActivity(customer.getId(), 1, 2);
        Assert.assertEquals(2, allActivity.getContent().size());
        Page<Activity> allActivityTwo = activityService.findAllActivity(customer.getId(), 1, 1);
        Assert.assertEquals(1, allActivityTwo.getContent().size());
    }

    /**
     * 删除活动，手动删除
     *
     * @throws Exception
     */
    @Test
    public void deleteActPrize() throws Exception {
        Activity activity = mockActivity(customer.getId(), actPrizeList, true);
        Activity byActId = activityService.findByActId(activity.getActId());
        Assert.assertNotNull(byActId);
        activityService.updateActivity(activity.getActId());
        Activity byActId1 = activityService.findByActId(activity.getActId());
        Assert.assertNull(byActId1);

    }

}
