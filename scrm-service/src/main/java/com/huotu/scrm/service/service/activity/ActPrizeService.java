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
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 奖品Service层接口
 * <p>
 * Created by montage on 2017/7/13.
 */
public interface ActPrizeService {
    /**
     * 分页查询所有奖品
     *
     * @return
     */
    Page<ActPrize> getPageActPrize(int pageNo, int pageSize);

    /**
     * 保存奖品
     *
     * @param actPrize 奖品实体类
     */
    ActPrize saveActPrize(ActPrize actPrize);

    /**
     * 删除奖品
     *
     * @param prizeId 奖品Id
     */
    void deleteActPrize(Long prizeId);

    /**
     * 根据Id查询奖品
     *
     * @param prizeId
     * @return
     */
    ActPrize findByPrizeId(Long prizeId);

    /**
     * 查询所有奖品
     *
     * @return
     */
    List<ActPrize> findAll();

    /**
     * 根据奖品类型查询奖品
     *
     * @return
     */
    ActPrize findByPrizeType(boolean prizeType);
}
