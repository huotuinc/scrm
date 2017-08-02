/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.service.service.activity.impl;

import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.repository.activity.ActPrizeRepository;
import com.huotu.scrm.service.service.activity.ActPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 奖品service实现类
 * Created by montage on 2017/7/13.
 */

@Service
public class ActPrizeServiceImpl implements ActPrizeService {

    @Autowired
    private ActPrizeRepository actPrizeRepository;

    @Override
    public Page<ActPrize> getPageActPrize(int pageNo, int pageSize) {
        return actPrizeRepository.findAll(new PageRequest(pageNo - 1, pageSize, new Sort(Sort.Direction.DESC, "sort")));
    }

    @Override
    @Transactional
    public ActPrize saveActPrize(ActPrize actPrize) {
        return actPrizeRepository.save(actPrize);
    }

    @Override
    public void deleteActPrize(Long prizeId) {
        actPrizeRepository.delete(prizeId);
    }

    @Override
    public ActPrize findByPrizeId(Long prizeId) {
        return actPrizeRepository.findOne(prizeId);
    }

    @Override
    public List<ActPrize> findAll() {
        return actPrizeRepository.findAll();
    }

    @Override
    public ActPrize findByPrizeType(boolean prizeType) {
        return actPrizeRepository.findByPrizeType(prizeType);
    }
}
