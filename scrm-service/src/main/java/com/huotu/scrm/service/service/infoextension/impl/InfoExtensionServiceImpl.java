package com.huotu.scrm.service.service.infoextension.impl;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.model.DayFollowNumInfo;
import com.huotu.scrm.service.model.DayScoreInfo;
import com.huotu.scrm.service.model.DayScoreRankingInfo;
import com.huotu.scrm.service.model.DayVisitorNumInfo;
import com.huotu.scrm.service.model.InfoModel;
import com.huotu.scrm.service.model.StatisticalInformation;
import com.huotu.scrm.service.repository.InfoBrowseRepository;
import com.huotu.scrm.service.repository.InfoConfigureRepository;
import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.service.infoextension.InfoExtensionService;
import com.huotu.scrm.service.service.report.DayReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    @Autowired
    private InfoConfigureRepository infoConfigureRepository;


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
        //设置今日预计积分(浏览+转发)
        int dayForwardScore = dayReportService.getEstimateScore(userId, beginTime, now);
        //设置每日访客量奖励积分
        int countBySourceUserId = infoBrowseRepository.countBySourceUserIdAndBrowseTime(userId, beginTime, now);
        int dayScore = 0;
        int visitorScore = 0;
        //获取访客量转换比例
        InfoConfigure infoConfigure = infoConfigureRepository.findOne(customerId);
        int exchangeRate = infoConfigure.getExchangeRate();
        //判断是否开启访客量积分奖励
        if (infoConfigure.isExchange()) {
            //判断小伙伴是否开启访客量奖励
            int exchangeUserType = infoConfigure.getExchangeUserType();
            //exchangeUserType 1：小伙伴 2：小伙伴 + 会员
            if (exchangeUserType == 1 || exchangeUserType == 2) {
                visitorScore = (countBySourceUserId / exchangeRate);
            }
        }
        dayScore = dayForwardScore + visitorScore;
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
        //获取访客量排名
        List<Long> sourceUserIdList = infoBrowseRepository.findBySourceUserId();
        Map<Long, Integer> map = new TreeMap<>();
        for (long sourceUserId : sourceUserIdList
                ) {
            dayScore = dayReportService.getEstimateScore(userId, beginTime, now);
            map.put(sourceUserId, dayScore);
        }
        List<Map.Entry<Long, Integer>> list = new ArrayList<Map.Entry<Long, Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<Long, Integer>>() {
            @Override
            public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int visitorRanking = 0;
        for (Map.Entry<Long, Integer> mapping : list) {
            visitorRanking++;
            if (userId.equals(mapping.getKey())) {
                break;
            }
        }
        statisticalInformation.setDayVisitorRanking(visitorRanking);
        return statisticalInformation;
    }

    @Override
    public DayScoreRankingInfo getScoreRankingInfo(Long userId) {

        return null;
    }

    @Override
    public DayScoreInfo getScoreInfo(Long userId) {
        return null;
    }

    @Override
    public DayVisitorNumInfo getVisitorNumInfo(Long userId) {
        return null;
    }

    @Override
    public DayFollowNumInfo getFollowNumInfo(Long userId) {
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
        LocalDateTime createTime = infoRepository.findOne(infoId).getCreateTime();
        Date now = new Date();
        long releaseTime = (now.getTime() - localDateTimeToDate(createTime).getTime()) / (60 * 60 * 1000);
        return (int) releaseTime;
    }


    public Date localDateTimeToDate(LocalDateTime time){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = time.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

}
