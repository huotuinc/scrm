/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service.activity.impl;

import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.repository.activity.ActWinDetailRepository;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by montage on 2017/7/13.
 */

@Service
@Transactional
public class ActWinDetailServiceImpl implements ActWinDetailService {

    @Autowired
    private ActWinDetailRepository actWinDetailRepository;

    @Override
    public Page<ActWinDetail> getPageActWinDetail(int pageNo, int pageSize) {
        return actWinDetailRepository.findAll(new PageRequest(pageNo - 1, pageSize));
    }

    @Override
    public ActWinDetail saveActWinDetail(ActWinDetail actWinDetail) {
        return actWinDetailRepository.save(actWinDetail);
    }

    @Override
    public  List<Map<String, Object>> createExcelRecord() {
        List<ActWinDetail> actWinDetailList = actWinDetailRepository.findAll();
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("sheetName", "sheet1");
        listMap.add(map);
        actWinDetailList.forEach( actWinDetail -> {
            Map<String,Object> mapValue = new HashMap<>();
            mapValue.put("winDetailId",actWinDetail.getWinDetailId());
            mapValue.put("userId",actWinDetail.getUserId());
            mapValue.put("actName",actWinDetail.getActName());
            mapValue.put("prizeName",actWinDetail.getPrize().getPrizeName());
            mapValue.put("winnerName",actWinDetail.getWinnerName());
            mapValue.put("winnerTel",actWinDetail.getWinnerTel());
            mapValue.put("winTime",actWinDetail.getWin_Time());
            mapValue.put("ipAddress",actWinDetail.getIpAddress());
        });
        return listMap;
    }
}