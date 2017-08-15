/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.repository.activity;

import com.huotu.scrm.service.entity.activity.ActWinDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by montage on 2017/7/12.
 */

@Repository
public interface ActWinDetailRepository extends JpaRepository<ActWinDetail, Long>, JpaSpecificationExecutor<ActWinDetail> {


    /**
     * 获取某个人某个活动的所以中奖记录
     * @return
     */
    List<ActWinDetail> findAllByActIdAndUserId(Long actId, Long userId);

    Page<ActWinDetail> findAllByActId(Long actId, Pageable pageable);



}
