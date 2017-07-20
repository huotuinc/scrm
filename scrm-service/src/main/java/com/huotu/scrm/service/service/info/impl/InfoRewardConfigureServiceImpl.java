package com.huotu.scrm.service.service.info.impl;

import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
import com.huotu.scrm.service.service.info.InfoRewardConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;

/**
 * Created by luohaibo on 2017/7/19.
 */
@Service
public class InfoRewardConfigureServiceImpl implements InfoRewardConfigureService {


    @Autowired
    InfoConfigureRepository infoConfigureRepository;

    @Override
    public InfoConfigure saveRewardConfigure(InfoConfigure infoConfigure) {
        InfoConfigure newInfoConfigure;
        if (infoConfigure.getCustomerId() != null && infoConfigure.getCustomerId() != 0){
            newInfoConfigure = infoConfigureRepository.findOne(infoConfigure.getCustomerId());
        }else {
            newInfoConfigure = new InfoConfigure();
            newInfoConfigure.setCustomerId(infoConfigure.getCustomerId());
        }
        newInfoConfigure.setRewardSwitch(infoConfigure.isRewardSwitch());
        newInfoConfigure.setRewardScore(infoConfigure.getRewardScore());
        newInfoConfigure.setRewardLimitSwitch(infoConfigure.isRewardLimitSwitch());
        newInfoConfigure.setRewardLimitNum(infoConfigure.getRewardLimitNum());
        newInfoConfigure.setRewardUserType(infoConfigure.getRewardUserType());
        newInfoConfigure.setExchangeSwitch(infoConfigure.isExchangeSwitch());
        newInfoConfigure.setExchangeRate(infoConfigure.getExchangeRate());
        newInfoConfigure.setExchangeUserType(infoConfigure.getExchangeUserType());
        newInfoConfigure.setDayExchangeLimit(infoConfigure.getDayExchangeLimit());
        newInfoConfigure.setInfoIntroduceUrl(infoConfigure.getInfoIntroduceUrl());
        return infoConfigureRepository.save(newInfoConfigure);
    }

    @Override
    public InfoConfigure readRewardConfigure(Long customerId) {
        return infoConfigureRepository.findOne(customerId);
    }
}
