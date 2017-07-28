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
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoRepository;
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
    private MonthReportRepository monthReportRepository;
    @Autowired
    private MonthReportService monthReportService;
    @Autowired
    private BusinessCardRecordRepository businessCardRecordRepository;


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
        List<Info> infoList = new ArrayList<>();
        if (userType == 0) {//普通会员
            status = true;
            infoList = infoRepository.findByCustomerIdAndIsStatusAndIsDisableFalse(customerId, status);
        } else {//小伙伴
            extendStatus = true;
            infoList = infoRepository.findByCustomerIdAndIsExtendAndAndIsDisableFalse(customerId, extendStatus);
        }
        for (Info info : infoList
                ) {
            InfoModel infoModel = new InfoModel();
            infoModel.setTitle(info.getTitle());
            infoModel.setIntroduce(info.getIntroduce());
            infoModel.setThumbnailImageUrl(info.getThumbnailImageUrl());
            infoModel.setForwardNum(getInfoForwardNum(info.getId()));
            infoModel.setVisitorNum(getVisitorNum(info.getId()));
            infoModel.setReleaseTime(getReleaseTime(info.getId()));
            infoModels.add(infoModel);

        }
        return infoModels;
    }

    @Override
    public StatisticalInformation getInformation(Long userId) {
        StatisticalInformation statisticalInformation = new StatisticalInformation();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        //获取今天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        //设置今日访客量
        int visitorNum = (int) infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(userId, beginTime, now);
        statisticalInformation.setDayVisitorNum(visitorNum);
        //设置今日预计积分(浏览+转发)
        int dayScore = dayReportService.getEstimateScore(userId, beginTime, now);
        statisticalInformation.setDayScore(dayScore);
        //获取累积积分
        int accumulateScore = dayReportService.getCumulativeScore(userId);
        //获取本月预计积分
        int monthScore = monthReportService.getExtensionScore(userId, localDate.withDayOfMonth(1), localDate);
        statisticalInformation.setAccumulateScore(monthScore + accumulateScore);
        //获取关注人数（销售员特有）
        User user = userRepository.findOne(userId);
        UserLevel userLevel = userLevelRepository.findByLevelAndCustomerId(user.getLevelId(), user.getCustomerId());
        int followNum = 0;
//        if (userLevel != null) {
//            if (userLevel.isSalesman()) {
//                List<DayReport> followNumList = dayReportRepository.findByUserIdAndReportDayLessThanEqual(userId, localDate);
//                for (DayReport dayReport : followNumList
//                        ) {
//                    followNum += dayReport.getFollowNum();
//                }
//                //获取今日关注人数
//                int dayFollowNum = (int) businessCardRecordRepository.countByUserIdAndFollowDateBetween(userId, beginTime, now);
//                statisticalInformation.setFollowNum(followNum + dayFollowNum);
//            }
//        } else {
//            statisticalInformation.setFollowNum(0);
//        }
        int dayFollowNum = (int) businessCardRecordRepository.countByUserIdAndFollowDateBetween(userId, beginTime, now);
        statisticalInformation.setFollowNum(followNum + dayFollowNum);
        //获取访客量排名
        Map<Long, Integer> map = new TreeMap<>();
        List<Long> sourceUserIdList = infoBrowseRepository.findByCustomerId(user.getCustomerId());
        sourceUserIdList.forEach(id -> {
            if (getUserType(id) == 1) {
                int dayVisitorNum = (int) infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(id, beginTime, now);
                map.put(id, dayVisitorNum);
            }
        });
        int visitorRanking = getRanking(map, userId);
        statisticalInformation.setDayVisitorRanking(visitorRanking);
        return statisticalInformation;
    }

    /**
     * 统计今日积分排名页面信息
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public DayScoreRankingInfo getScoreRankingInfo(Long userId) {
        DayScoreRankingInfo dayScoreRankingInfo = new DayScoreRankingInfo();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        //本月第一天（时分秒默认为零）
        LocalDateTime firstDay = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
        //获取今日预计积分排名
        Map<Long, Integer> map = new TreeMap<>();
        //设置本月积分排名
        Map<Long, Integer> mapMonth = new TreeMap<>();
        User user = userRepository.findOne(userId);
        List<Long> sourceUserIdList = infoBrowseRepository.findByCustomerId(user.getCustomerId());
        sourceUserIdList.forEach(id -> {
            if (getUserType(id) == 1) {
                int dayScore = dayReportService.getEstimateScore(id, beginTime, now);
                map.put(id, dayScore);
                int dayMonthScore = dayReportService.getEstimateScore(id, firstDay, now);
                mapMonth.put(id, dayMonthScore);
            }
        });
        int dayScoreRanking = getRanking(map, userId);
        dayScoreRankingInfo.setDayScoreRanking(dayScoreRanking);
        int monthRanking = getRanking(mapMonth, userId);
        dayScoreRankingInfo.setMonthScoreRanking(monthRanking);
        //设置最高月积分排名
        int highestMonthScoreRanking = monthReportRepository.findMaxScoreRanking(userId);
        dayScoreRankingInfo.setHighestMonthScoreRanking(highestMonthScoreRanking);
        //近几个月积分排名
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth("本月");
        monthInfo.setData(dayScoreRankingInfo.getMonthScoreRanking());
        monthStatisticInfoList.add(monthInfo);
        for (int i = 1; i < 5; i++) {
            MonthStatisticInfo monthStatisticInfo = getMonthStatisticInfo(userId, 0, i);
            monthStatisticInfoList.add(monthStatisticInfo);
        }
        dayScoreRankingInfo.setMonthScoreRankingList(monthStatisticInfoList);
        return dayScoreRankingInfo;
    }

    /**
     * 统计今日积分页面信息
     *
     * @param userId 用户ID
     * @return
     */
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
        List<DayReport> dayReportList = dayReportRepository.findByUserIdAndReportDay(userId, localDate.minusDays(1));
        if (dayReportList.isEmpty() || dayReportList == null) {
            dayScoreInfo.setLastDayScore(0);
        } else {
            dayScoreInfo.setLastDayScore(dayReportList.get(0).getExtensionScore());
        }
        //设置历史累积积分
        int cumulativeScore = dayReportService.getCumulativeScore(userId);
        //获取本月预计积分
        int monthScore = monthReportService.getExtensionScore(userId, localDate.withDayOfMonth(1), localDate);
        dayScoreInfo.setAccumulateScore(cumulativeScore + monthScore);
        //设置近几个月积分信息
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        MonthStatisticInfo monInfo = new MonthStatisticInfo();
        monInfo.setData(monthScore);
        monInfo.setMonth("本月");
        monthStatisticInfoList.add(monInfo);
        for (int i = 1; i < 5; i++) {
            MonthStatisticInfo monthStatisticInfo = getMonthStatisticInfo(userId, 1, i);
            monthStatisticInfoList.add(monthStatisticInfo);
        }
        dayScoreInfo.setMonthScoreList(monthStatisticInfoList);
        return dayScoreInfo;
    }

    /**
     * 统计今日访客量页面信息
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public DayVisitorNumInfo getVisitorNumInfo(Long userId) {
        DayVisitorNumInfo dayVisitorNumInfo = new DayVisitorNumInfo();
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDate localDate = LocalDate.now();
        //设置今日访客量
        int dayVisitorNum = (int) infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(userId, beginTime, now);
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
        monthInfo.setMonth("本月");
        monthInfo.setData(dayVisitorNumInfo.getMonthVisitorNum());
        monthStatisticInfoList.add(monthInfo);
        for (int i = 1; i < 5; i++) {
            MonthStatisticInfo monthStatisticInfo = getMonthStatisticInfo(userId, 2, i);
            monthStatisticInfoList.add(monthStatisticInfo);
        }
        dayVisitorNumInfo.setMonthVisitorNumList(monthStatisticInfoList);
        return dayVisitorNumInfo;
    }

    /**
     * 统计今日关注页面信息
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public DayFollowNumInfo getFollowNumInfo(Long userId) {
        DayFollowNumInfo dayFollowNumInfo = new DayFollowNumInfo();
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDate localDate = LocalDate.now();
        //今日关注人数
        int dayFollowNum = (int) businessCardRecordRepository.countByUserIdAndFollowDateBetween(userId, beginTime, now);
        dayFollowNumInfo.setDayFollowNum(dayFollowNum);
        //当前排名
        User user = userRepository.findOne(userId);
        List<Long> sourceUserIdList = infoBrowseRepository.findSourceUserIdListByCustomerId(beginTime, now, user.getCustomerId());
        Map<Long, Integer> map = new TreeMap<>();
        for (long sourceUserId : sourceUserIdList
                ) {
            if (getUserType(sourceUserId) == 1) {
                int followNum = (int) businessCardRecordRepository.countByUserIdAndFollowDateBetween(sourceUserId, beginTime, now);
                map.put(sourceUserId, followNum);
            }
        }
        int visitorRanking = getRanking(map, userId);
        dayFollowNumInfo.setFollowRanking(visitorRanking);
        //最高月排名
        int highestFollowRanking = monthReportRepository.findMaxMonthFollowNumRanking(userId);
        dayFollowNumInfo.setHighestFollowRanking(highestFollowRanking);
        //近几个月排名
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth("本月");
        //本月关注量
        int followNum = monthReportService.getFollowNum(userId, localDate.withDayOfMonth(1), localDate);
        monthInfo.setData(followNum + dayFollowNum);
        monthStatisticInfoList.add(monthInfo);
        for (int i = 1; i < 5; i++) {
            MonthStatisticInfo monthStatisticInfo = getMonthStatisticInfo(userId, 3, i);
            monthStatisticInfoList.add(monthStatisticInfo);
        }
        dayFollowNumInfo.setMonthFollowRankingList(monthStatisticInfoList);
        return dayFollowNumInfo;
    }

    @Override
    public boolean checkIsSalesman(Long userId) {
        return true;
//        User user = userRepository.findOne(userId);
//        if (user == null) {
//            return false;
//        }
//        UserLevel userLevel = userLevelRepository.findByLevelAndCustomerId(user.getLevelId(), user.getCustomerId());
//        if (userLevel == null) {
//            return false;
//        }
//        return userLevel.isSalesman();
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
     * 统计访客量（资讯转发浏览量）
     *
     * @param infoId 资讯ID
     * @return
     */
    public int getVisitorNum(Long infoId) {

        return (int) infoBrowseRepository.countByInfoId(infoId);
    }

    /**
     * 获取资讯发布时间距现在多少时间，默认小时数
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

    /**
     * 获取排名
     *
     * @param map
     * @param userId
     * @return
     */
    public int getRanking(Map<Long, Integer> map, Long userId) {
        if (!map.containsKey(userId)) {
            map.put(userId, 0);
        }
        List<Map.Entry<Long, Integer>> list = new ArrayList<>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        int ranking = 0;
        for (Map.Entry<Long, Integer> mapping : list) {
            ranking++;
            if (userId.equals(mapping.getKey())) {
                break;
            }
        }
        return ranking;
    }

    /**
     * 获取月统计信息
     *
     * @param userId 用户ID
     * @param type   0：积分排名 1：今日积分 2：今日访客量 3：今日关注
     * @param i
     * @return
     */
    public MonthStatisticInfo getMonthStatisticInfo(Long userId, int type, int i) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        MonthStatisticInfo monthStatisticInfo = new MonthStatisticInfo();
        List<MonthReport> reportList = monthReportRepository.findByUserIdAndReportMonth(userId, localDate.minusMonths(i).withDayOfMonth(1));
        if (i == 1) {
            monthStatisticInfo.setMonth("上月");
        } else {
            monthStatisticInfo.setMonth(now.minusMonths(i).getMonthValue() + "月");
        }
        if (reportList.isEmpty() || reportList == null) {
            monthStatisticInfo.setData(0);
        } else {
            switch (type) {
                case 0:
                    monthStatisticInfo.setData(reportList.get(0).getScoreRanking());
                    break;
                case 1:
                    monthStatisticInfo.setData(reportList.get(0).getExtensionScore());
                    break;
                case 2:
                    monthStatisticInfo.setData(reportList.get(0).getVisitorNum());
                    break;
                default:
                    monthStatisticInfo.setData(reportList.get(0).getFollowRanking());
                    break;
            }

        }
        return monthStatisticInfo;
    }
}
