package com.huotu.scrm.service.service;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.model.InfoModel;
import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxh on 2017-07-17.
 */
@Service
public class InfoExtensionServiceImpl implements InfoExtensionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InfoRepository infoRepository;

    @Override
    public int getUserType(Long userId) {
        return userRepository.findOne(userId).getUserType().ordinal();
    }

    @Override
    public List<InfoModel> findInfo(Long userId, int userType) {
        //获取用户的customerId
        Long customerId = userRepository.findOne(userId).getCustomerId();
        List<InfoModel> infoModels = new ArrayList<>();
        boolean status = false;
        boolean extendStatus = false;
        if (userType == 0) {//普通会员
            status = true;
        } else {//小伙伴
            extendStatus = true;
        }
        List<Info> infoList = infoRepository.findByCustomerIdAndStatusAndExtendAndDisable(customerId, status, extendStatus, false);
        infoList.forEach(info -> {
            InfoModel infoModel = new InfoModel();
            infoModel.setTitle(info.getTitle());
            infoModel.setIntroduce(info.getIntroduce());
            infoModel.setThumbnailImageUrl(info.getThumbnailImageUrl());
//            infoModel.setForwardNum();
//            infoModel.setBrowseNum();
//            infoModel.setReleaseTime();
        });
        return null;
    }

    @Override
    public int getInfoForwardNum(Long infoId) {
        return 0;
    }


}
