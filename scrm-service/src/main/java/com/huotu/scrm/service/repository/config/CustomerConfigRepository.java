/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.repository.config;

import com.huotu.scrm.service.entity.config.CustomerConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by helloztt on 2017-09-05.
 */
public interface CustomerConfigRepository extends JpaRepository<CustomerConfig,Long> {
}
