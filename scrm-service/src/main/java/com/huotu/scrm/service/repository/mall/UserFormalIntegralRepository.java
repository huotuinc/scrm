/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.repository.mall;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.service.entity.mall.UserFormalIntegral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by helloztt on 2017-08-04.
 */
public interface UserFormalIntegralRepository extends JpaRepository<UserFormalIntegral, Long> {

    List<UserFormalIntegral> findByUserIdAndMerchantIdAndStatusAndTimeBetween(Long userId, Long customerId, IntegralTypeEnum status, LocalDateTime beginTime, LocalDateTime endTime);
    List<UserFormalIntegral> findByUserIdAndMerchantIdAndStatus(Long userId,Long customerId,IntegralTypeEnum status);
}
