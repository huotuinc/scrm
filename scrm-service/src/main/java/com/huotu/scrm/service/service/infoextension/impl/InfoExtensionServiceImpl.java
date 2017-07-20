package com.huotu.scrm.service.service.infoextension.impl;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.model.DayFollowNumInfo;
import com.huotu.scrm.service.model.DayScoreInfo;
import com.huotu.scrm.service.model.DayScoreRankingInfo;
import com.huotu.scrm.service.model.DayVisitorNumInfo;
import com.huotu.scrm.service.model.InfoModel;
import com.huotu.scrm.service.model.MonthStatisticInfo;
import com.huotu.scrm.service.model.StatisticalInformation;
import com.huotu.scrm.service.repository.BusinessCardRecordReposity;
import com.huotu.scrm.service.repository.InfoBrowseRepository;
import com.huotu.scrm.service.repository.InfoConfigureRepository;
import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import com.huotu.scrm.service.service.infoextension.InfoExtensionService;
import com.huotu.scrm.service.service.report.DayReportService;
import com.huotu.scrm.service.service.report.MonthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
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
    @Autowired
    private MonthReportRepository monthReportRepository;
    @Autowired
    private MonthReportService monthReportService;
    @Autowired
    private BusinessCardRecordReposity businessCardRecordReposity;


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
        Long customerId = userRepository.findOne(userId).getCustomerId();
        StatisticalInformation statisticalInformation = new StatisticalInformation();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        //设置今日访客量
        int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTime(userId, beginTime, now);
        statisticalInformation.setDayVisitorNum(visitorNum);
        //设置今日预计积分(浏览+转发)
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
            //获取今日关注人数
            int dayFollowNum = businessCardRecordReposity.countByUserId(userId, beginTime, now);
            statisticalInformation.setFollowNum(followNum + dayFollowNum);
        }
        //获取访客量排名
        List<Long> sourceUserIdList = infoBrowseRepository.findBySourceUserId();
        Map<Long, Integer> map = new TreeMap<>();
        for (long sourceUserId : sourceUserIdList
                ) {
            int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTime(userId, beginTime, now);
            map.put(sourceUserId, dayVisitorNum);
        }
        int visitorRanking = getRanking(map, userId);
        statisticalInformation.setDayVisitorRanking(visitorRanking);
        return statisticalInformation;
    }

    @Override
    public DayScoreRankingInfo getScoreRankingInfo(Long userId) {
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        //获取预计积分
        int dayForwardScore = dayReportService.getEstimateScore(userId, beginTime, now);
        return null;
    }

    @Override
    public DayScoreInfo getScoreInfo(Long userId) {
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDate localDate = LocalDate.now();
        DayScoreInfo dayScoreInfo = new DayScoreInfo();
        int extensionScore = dayReportService.getEstimateScore(userId, beginTime, now);
        dayScoreInfo.setDayScore(extensionScore);
        //设置昨日积分
        DayReport dayReport = dayReportRepository.findByUserIdAndReportDay(userId, localDate.minusDays(1));
        dayScoreInfo.setLastDayScore(dayReport.getExtensionScore());
        //设置历史累积积分
        int cumulativeScore = dayReportService.getCumulativeScore(userId);
        dayScoreInfo.setAccumulateScore(cumulativeScore);
        //设置近几个月积分信息
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MonthStatisticInfo monthStatisticInfo = new MonthStatisticInfo();
            if (i == 0) {
                int monthScore = monthReportService.getExtensionScore(userId, localDate.withDayOfMonth(1), localDate);
                monthStatisticInfo.setData(monthScore);
            } else {
                MonthReport monthReport = monthReportRepository.findByUserIdAndReportMonth(userId, localDate.minusMonths(i));
                monthStatisticInfo.setData(monthReport.getExtensionScore());
            }
            monthStatisticInfo.setMonth(now.minusMonths(i).getMonthValue());
            monthStatisticInfoList.add(monthStatisticInfo);
        }
        dayScoreInfo.setMonthScoreList(monthStatisticInfoList);
        return dayScoreInfo;
    }

    @Override
    public DayVisitorNumInfo getVisitorNumInfo(Long userId) {
        DayVisitorNumInfo dayVisitorNumInfo = new DayVisitorNumInfo();
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDate localDate = LocalDate.now();
        //设置今日访客量
        int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTime(userId, beginTime, now);
        dayVisitorNumInfo.setDayVisitorBum(dayVisitorNum);
        //设置本月访问量
        int monthVisitorNum = monthReportService.getVisitorNum(userId, localDate.withDayOfMonth(1), localDate);
        dayVisitorNumInfo.setMonthVisitorNum(monthVisitorNum + dayVisitorNum);
        //设置历史最高月访问量
        int highestMonthVisitorNum = monthReportRepository.findMaxMonthVisitorNum(userId);
        dayVisitorNumInfo.setHighestMonthVisitorNum(highestMonthVisitorNum);
        //设置近几个月访问量
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth(now.getMonthValue());
        monthInfo.setData(dayVisitorNumInfo.getMonthVisitorNum());
        monthStatisticInfoList.add(monthInfo);
        for (int i = 1; i < 5; i++) {
            MonthStatisticInfo monthStatisticInfo = new MonthStatisticInfo();
            MonthReport monthReport = monthReportRepository.findByUserIdAndReportMonth(userId, localDate.minusMonths(i));
            monthStatisticInfo.setData(monthReport.getVisitorNum());
            monthStatisticInfo.setMonth(now.minusMonths(i).getMonthValue());
            monthStatisticInfoList.add(monthStatisticInfo);
        }
        dayVisitorNumInfo.setMonthVisitorNumList(monthStatisticInfoList);
        return dayVisitorNumInfo;
    }

    @Override
    public DayFollowNumInfo getFollowNumInfo(Long userId) {
        DayFollowNumInfo dayFollowNumInfo = new DayFollowNumInfo();
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDate localDate = LocalDate.now();
        //今日关注人数
        int dayFollowNum = businessCardRecordReposity.countByUserId(userId, beginTime, now);
        dayFollowNumInfo.setDayFollowNum(dayFollowNum);
        //当前排名
        List<Long> sourceUserIdList = infoBrowseRepository.findBySourceUserId();
        Map<Long, Integer> map = new TreeMap<>();
        for (long sourceUserId : sourceUserIdList
                ) {
            int followNum = businessCardRecordReposity.countByUserId(sourceUserId, beginTime, now);
            map.put(sourceUserId, followNum);
        }
        int visitorRanking = getRanking(map, userId);
        dayFollowNumInfo.setFollowRanking(visitorRanking);
        //最高月排名
        int highestFollowRanking = monthReportRepository.findByMinMonthFollowNumRanking(userId);
        dayFollowNumInfo.setHighestFollowRanking(highestFollowRanking);
        //近几个月排名
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth(now.getMonthValue());
        //本月关注量
        int followNum = monthReportService.getFollowNum(userId, localDate.withDayOfMonth(1), localDate);
        monthInfo.setData(followNum + dayFollowNum);
        monthStatisticInfoList.add(monthInfo);
        for (int i = 1; i < 5; i++) {
            MonthStatisticInfo monthStatisticInfo = new MonthStatisticInfo();
            MonthReport monthReport = monthReportRepository.findByUserIdAndReportMonth(userId, localDate.minusMonths(i));
            monthStatisticInfo.setData(monthReport.getFollowRanking());
            monthStatisticInfo.setMonth(now.minusMonths(i).getMonthValue());
            monthStatisticInfoList.add(monthStatisticInfo);
        }
        dayFollowNumInfo.setMonthFollowRankingList(monthStatisticInfoList);
        return dayFollowNumInfo;
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


    public Date localDateTimeToDate(LocalDateTime time) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = time.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public int getRanking(Map<Long, Integer> map, Long userId) {
        List<Map.Entry<Long, Integer>> list = new ArrayList<Map.Entry<Long, Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<Long, Integer>>() {
            @Override
            public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int ranking = 0;
        for (Map.Entry<Long, Integer> mapping : list) {
            ranking++;
            if (userId.equals(mapping.getKey())) {
                break;
            }
        }
        return ranking;
    }
}
