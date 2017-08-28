/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service.activity;

import com.huotu.scrm.service.entity.activity.ActWinDetail;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 活动中奖记录Service层接口
 * <p>
 * Created by montage on 2017/7/13.
 */
public interface ActWinDetailService {

    /**
     * 分页查询活动的中奖记录
     *
     * @param actId
     * @param type
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<ActWinDetail> getPageActWinDetail(Long actId, int type, int pageNo, int pageSize);

    /**
     * 添加中奖记录
     *
     * @param actWinDetail 中奖记录实体
     * @return
     */
    @Transactional
    ActWinDetail saveActWinDetail(ActWinDetail actWinDetail);

    /**
     * 创建中奖记录的excel表格
     *
     * @return
     */
    List<Map<String, Object>> createExcelRecord(Long actId, int type,int startPage, int endPage);


    @Transactional
    ActWinDetail updateActWinDetail(Long winDetailId, String name, String mobile);


    /**
     * 获取某人某个活动的中奖记录
     *
     * @param actId
     * @param userId
     * @return
     */
    List<ActWinDetail> getActWinDetailRecordByActIdAndUserId(Long actId, Long userId);

    /**
     * 修改记录的领取状态
     * @param winDetailId
     */
    @Transactional
    void updateRewardStatus(Long winDetailId);

}
