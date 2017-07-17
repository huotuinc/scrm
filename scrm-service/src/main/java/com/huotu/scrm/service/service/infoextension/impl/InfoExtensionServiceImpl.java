package com.huotu.scrm.service.service.infoextension.impl;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.model.InfoModel;
import com.huotu.scrm.service.repository.InfoBrowseRepository;
import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.infoextension.InfoExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private InfoBrowseRepository infoBrowseRepository;

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
            infoModel.setForwardNum(getInfoForwardNum(info.getId()));
            infoModel.setVisitorNum(getVisitorNum(info.getId()));
            infoModel.setReleaseTime(getReleaseTime(info.getId()));
        });
        return null;
    }

    /**
     * 统计资讯转发量
     *
     * @param infoId 资讯ID
     * @return
     */
    public int getInfoForwardNum(Long infoId) {
        return infoBrowseRepository.findInfoForwardNum(infoId);
    }

    /**
     * 统计访客量（咨询转发浏览量）
     *
     * @param infoId 咨询ID
     * @return
     */
    public int getVisitorNum(Long infoId) {
        return (int) infoBrowseRepository.countByInfoId(infoId);
    }

    /**
     * 获取咨询发布时间距现在多少时间，默认小时数
     *
     * @param infoId
     * @return
     */
    public int getReleaseTime(Long infoId) {
        Date createTime = infoRepository.findOne(infoId).getCreateTime();
        Date now = new Date();
        long releaseTime = (now.getTime() - createTime.getTime()) / (60 * 60 * 1000);
        return (int) releaseTime;
    }

}
