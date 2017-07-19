package com.huotu.scrm.service.service.infoextension.impl;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.model.InfoModel;
import com.huotu.scrm.service.model.StatisticalInformation;
import com.huotu.scrm.service.repository.InfoBrowseRepository;
import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.service.infoextension.InfoExtensionService;
import com.huotu.scrm.service.service.report.DayReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private DayReportService dayReportService;
    @Autowired
    private UserLevelRepository userLevelRepository;
    @Autowired
    private DayReportRepository dayReportRepository;


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
            infoModels.add(infoModel);
        });
        return infoModels;
    }

    @Override
    public StatisticalInformation getInformation(Long userId) {
        StatisticalInformation statisticalInformation = new StatisticalInformation();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        //设置今日访客量
        int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTime(userId, beginTime, now);
        statisticalInformation.setDayVisitorNum(visitorNum);
        //设置今日预计积分
        int dayScore = dayReportService.getEstimateScore(userId, beginTime, now);
        statisticalInformation.setDayScore(dayScore);
        //获取累积积分
        int accumulateScore = dayReportService.getCumulativeScore(userId);
        statisticalInformation.setAccumulateScore(accumulateScore);
        //获取关注人数（销售员特有）
        User user = userRepository.findOne(userId);
        UserLevel userLevel = userLevelRepository.findByLevelAndCustomerId(user.getLevelId(), user.getCustomerId());
        int followNum = 0;
        if (userLevel.isSalesman()) {
            List<DayReport> followNumList = dayReportRepository.findAllFollowNum(userId, now);
            for (DayReport dayReport : followNumList
                    ) {
                followNum += dayReport.getFollowNum();
            }
            statisticalInformation.setFollowNum(followNum);
        }
        //获取积分排名
        return statisticalInformation;
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
