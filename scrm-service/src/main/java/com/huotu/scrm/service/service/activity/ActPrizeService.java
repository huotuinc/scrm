/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service.activity;

import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import org.springframework.transaction.annotation.Transactional;

/**
 * 奖品Service层接口
 * <p>
 * Created by montage on 2017/7/13.
 */
public interface ActPrizeService {

    /**
     * 保存奖品
     *
     * @param activity
     */
    @Transactional
    void saveActPrize(Activity activity);

    @Transactional
    void saveActPrice(ActPrize actPrize);

    /**
     * 删除奖品
     *
     * @param prizeId 奖品Id
     */
    @Transactional
    void deleteActPrize(Long prizeId);

    /**
     * 根据Id查询奖品
     *
     * @param prizeId
     * @return
     */
    ActPrize findByPrizeId(Long prizeId);

}
