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
import com.huotu.scrm.service.model.PrizeType;
import com.huotu.scrm.service.repository.activity.ActWinDetailRepository;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
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
    public Page<ActWinDetail> getPageActWinDetail(Long actId, int type, int pageNo, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "winTime");
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, sort);
        if (type > 0) {
            Specification<ActWinDetail> specification = getSpecification(actId, type);
            return actWinDetailRepository.findAll(specification, pageable);
        } else {
            return actWinDetailRepository.findAllByActId(actId, pageable);
        }
    }

    @Override
    public ActWinDetail saveActWinDetail(ActWinDetail actWinDetail) {
        return actWinDetailRepository.saveAndFlush(actWinDetail);
    }

    @Override
    public List<Map<String, Object>> createExcelRecord(Long actId, int type, int startPage, int endPage) {
        List<ActWinDetail> actWinDetailList = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.DESC, "winTime");
        Specification<ActWinDetail> specification = null;
        if (type > 0) {
            specification = getSpecification(actId, type);
        }
        for (int i = startPage; i <= endPage; i++) {
            Pageable pageable = new PageRequest(startPage - 1, Constant.PAGE_SIZE, sort);
            List<ActWinDetail> winDetailList = type > 0 ? actWinDetailRepository.findAll(specification, pageable).getContent() : actWinDetailRepository.findAllByActId(actId, pageable).getContent();
            actWinDetailRepository.findAll(specification, pageable).getContent();
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
            mapValue.put("prizeName", actWinDetail.getPrizeName());
            mapValue.put("winnerName", actWinDetail.getWinnerName());
            mapValue.put("winnerTel", actWinDetail.getWinnerTel());
            mapValue.put("winTime", actWinDetail.getWinTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            mapValue.put("ipAddress", actWinDetail.getIpAddress());
            listMap.add(mapValue);
        });
        return listMap;
    }

    @Override
    public ActWinDetail updateActWinDetail(Long winDetailID, String name, String mobile) {

        ActWinDetail actWinDetail = actWinDetailRepository.findOne(winDetailID);
        if (actWinDetail != null) {
            actWinDetail.setWinnerName(name);
            actWinDetail.setWinnerTel(mobile);
            actWinDetail.setGetReward(true);
            return actWinDetailRepository.save(actWinDetail);

        }
        return null;
    }

    @Override
    public List<ActWinDetail> getActWinDetailRecordByActIdAndUserId(Long actId, Long userId) {
        return actWinDetailRepository.findAllByActIdAndUserId(actId, userId);
    }

    /**
     * @param actId
     * @param type  1：查询未领取 2：查询已领取
     * @return
     */
    private Specification<ActWinDetail> getSpecification(Long actId, int type) {
        List<Predicate> predicates = new ArrayList<>();
        return ((root, query, cb) -> {
            PrizeType prizeType = (type == 1) ? PrizeType.PRIZE_TYPE_THANKS : PrizeType.PRIZE_TYPE_ENTITY_PRIZE;
            predicates.add(cb.equal(root.get("actId").as(Long.class), actId));
            predicates.add(cb.equal(root.join("prize", JoinType.LEFT).get("prizeType").as(PrizeType.class), prizeType));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }
}
