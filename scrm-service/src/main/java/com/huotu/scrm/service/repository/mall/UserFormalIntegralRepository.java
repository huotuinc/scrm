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
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

/**
 * Created by helloztt on 2017-08-04.
 */
public interface UserFormalIntegralRepository extends JpaRepository<UserFormalIntegral, Long> {

    @Query("SELECT SUM(u.score) FROM UserFormalIntegral u WHERE u.userId = ?1 AND u.merchantId = ?2 AND u.userLevelId = ?3 and u.userType = ?4 and u.status = ?5 AND u.time >= ?6 AND u.time <= ?7")
    Integer sumByScore(Long userId, Long customerId, Integer userLevelId, Integer userType, IntegralTypeEnum status, LocalDateTime beginTime, LocalDateTime endTime);
}
