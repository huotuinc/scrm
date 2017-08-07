/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service.activity.impl;

import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.repository.activity.ActWinDetailRepository;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        Sort sort = new Sort(Sort.Direction.DESC, "winTime");
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, sort);
        return actWinDetailRepository.findAll(pageable);
    }

    @Override
    public ActWinDetail saveActWinDetail(ActWinDetail actWinDetail) {
        return actWinDetailRepository.save(actWinDetail);
    }

    @Override
    public List<Map<String, Object>> createExcelRecord(int startPage, int endPage) {
        List<ActWinDetail> actWinDetailList = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.DESC, "winTime");
        for (int i = startPage; i <= endPage; i++) {
            Pageable pageable = new PageRequest(startPage - 1, Constant.PAGE_SIZE, sort);
            List<ActWinDetail> winDetailList = actWinDetailRepository.findAll(pageable).getContent();
            winDetailList.forEach(actWinDetail -> {
                actWinDetailList.add(actWinDetail);
            });
        }
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("sheetName", "sheet1");
        listMap.add(map);
        actWinDetailList.forEach(actWinDetail -> {
            Map<String, Object> mapValue = new LinkedHashMap<>();
            mapValue.put("userId", actWinDetail.getUserId());
            mapValue.put("actName", actWinDetail.getPrize().getActivity().getActTitle());
            mapValue.put("prizeName", actWinDetail.getPrize().getPrizeName());
            mapValue.put("winnerName", actWinDetail.getWinnerName());
            mapValue.put("winnerTel", actWinDetail.getWinnerTel());
            mapValue.put("winTime", actWinDetail.getWinTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            mapValue.put("ipAddress", actWinDetail.getIpAddress());
            listMap.add(mapValue);
        });
        return listMap;
    }
}
