/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.repository.activity;

import com.huotu.scrm.service.entity.activity.ActPrize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by montage on 2017/7/12.
 */

@Repository
public interface ActPrizeRepository extends JpaRepository<ActPrize,Long>,JpaSpecificationExecutor<ActPrize>{

    ActPrize findByPrizeType (boolean prizeType);
}
