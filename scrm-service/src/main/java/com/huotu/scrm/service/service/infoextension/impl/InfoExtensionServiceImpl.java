package com.huotu.scrm.service.service.infoextension.impl;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.common.utils.DateUtil;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.model.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public UserType getUserType(Long userId) {
        return userRepository.findUserTypeById(userId);
    }

    @Override
    public List<InfoModel> findInfo(User user) {
        List<InfoModel> infoModels = new ArrayList<>();
        List<Info> infoList;
        if (user.getUserType() == UserType.normal) {//普通会员
            infoList = infoRepository.findByCustomerIdAndIsStatusTrueAndIsDisableFalse(user.getCustomerId());
        } else {//小伙伴
            infoList = infoRepository.findByCustomerIdAndIsExtendTrueAndIsDisableFalse(user.getCustomerId());
        }
        LocalDateTime now = LocalDateTime.now();
        for (Info info : infoList) {
            InfoModel infoModel = new InfoModel();
            infoModel.setInfoId(info.getId());
            infoModel.setCustomerId(info.getCustomerId());
            infoModel.setTitle(info.getTitle());
            infoModel.setIntroduce(info.getIntroduce());
            infoModel.setThumbnailImageUrl(info.getThumbnailImageUrl());
            infoModel.setForwardNum(getInfoForwardNum(info.getId()));
            infoModel.setVisitorNum(getVisitorNum(info.getId()));
            infoModel.setReleaseTime(DateUtil.compareLocalDateTime(info.getCreateTime(),now));
            infoModels.add(infoModel);
        }
        return infoModels;
    }

    @Override
    public StatisticalInformation getInformation(User user) {
        StatisticalInformation statisticalInformation = new StatisticalInformation();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //今天和本月第一天
        LocalDate today = LocalDate.now(),firstDayOfMonth = today.withDayOfMonth(1);
        //获取今天时间（时分秒默认为零）
        LocalDateTime beginTime = today.atStartOfDay();
        //设置今日访客量
        int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(user.getId(), beginTime, now);
        //设置今日预计积分(浏览+转发)
        int dayScore = dayReportService.getEstimateScore(user.getId(),user.getCustomerId(), beginTime, now);
        //获取累积积分
        int accumulateScore = dayReportService.getCumulativeScore(user.getId());
        //获取本月预计积分
        int monthScore = monthReportService.getExtensionScore(user.getId(), today.withDayOfMonth(1), today);
        statisticalInformation.setDayVisitorNum(visitorNum);
        statisticalInformation.setDayScore(dayScore);
        statisticalInformation.setAccumulateScore(monthScore + accumulateScore);
        //获取关注人数（销售员特有）
        UserLevel userLevel = userLevelRepository.findByLevelAndCustomerId(user.getLevelId(), user.getCustomerId());

        // TODO: 2017-08-04 关注人数并不是累计的，而是随时变化的，所以不用累计每月统计数，需要你自己改掉！！！
        int followNum = 0;
        List<MonthReport> monthReportList = monthReportRepository.findByUserId(user.getId());
        for (MonthReport monthReport : monthReportList
                ) {
            followNum += monthReport.getFollowNum();
        }
        //获取本月关注人数
        int monthFollowNum = businessCardRecordRepository.countByUserIdAndFollowDateBetween(user.getId(), firstDayOfMonth.atStartOfDay(), now);
        statisticalInformation.setFollowNum(followNum + monthFollowNum);

        // TODO: 2017-08-04 访客量排名通过schedule放到map里面，否则太消耗了
        //获取访客量排名
        Map<Long, Integer> map = new TreeMap<>();
        List<Long> sourceUserIdList = infoBrowseRepository.findByCustomerId(user.getCustomerId());
        sourceUserIdList.forEach(id -> {
            if (UserType.buddy.equals(getUserType(id))) {
                int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(id, beginTime, now);
                map.put(id, dayVisitorNum);
            }
        });
        int visitorRanking = getRanking(map, user.getId());
        if (visitorRanking == 0) {
            if (visitorNum > 0) {
                visitorRanking = 1;
            }
        }
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
    public DayScoreRankingInfo getScoreRankingInfo(Long userId,Long customerId) {
        DayScoreRankingInfo dayScoreRankingInfo = new DayScoreRankingInfo();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        //获取今天时间（时分秒默认为零）
        LocalDateTime beginTime = localDate.atStartOfDay();
        //本月第一天（时分秒默认为零）
        LocalDateTime firstDay = localDate.withDayOfMonth(1).atStartOfDay();
        //获取今日预计积分排名
        Map<Long, Integer> map = new TreeMap<>();
        //设置本月积分排名
        Map<Long, Integer> mapMonth = new TreeMap<>();
        User user = userRepository.findOne(userId);
        List<Long> sourceUserIdList = infoBrowseRepository.findByCustomerId(user.getCustomerId());
        sourceUserIdList.forEach(id -> {
            if (UserType.buddy.equals(getUserType(id))) {
                int dayScore = dayReportService.getEstimateScore(id,customerId, beginTime, now);
                map.put(id, dayScore);
                int dayMonthScore = dayReportService.getEstimateScore(id,customerId, firstDay, now);
                mapMonth.put(id, dayMonthScore);
            }
        });
        int dayScoreRanking = getRanking(map, userId);
        if (dayScoreRanking == 0) {
            int dayScore = dayReportService.getEstimateScore(userId,customerId, beginTime, now);
            if (dayScore > 0) {
                dayScoreRanking = 1;
            }
        }
        dayScoreRankingInfo.setDayScoreRanking(dayScoreRanking);
        int monthRanking = getRanking(mapMonth, userId);
        if (monthRanking == 0) {
            int monthScore = dayReportService.getEstimateScore(userId,customerId, firstDay, now);
            if (monthScore > 0) {
                monthRanking = 1;
            }
        }
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
        for (int i = 1; i < Constant.MONTH_NUM; i++) {
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
    public DayScoreInfo getScoreInfo(Long userId,Long customerId) {
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime beginTime = localDate.atStartOfDay();
        DayScoreInfo dayScoreInfo = new DayScoreInfo();
        int extensionScore = dayReportService.getEstimateScore(userId,customerId, beginTime, now);
        dayScoreInfo.setDayScore(extensionScore);
        //设置昨日积分
        DayReport dayReport = dayReportRepository.findByUserIdAndReportDay(userId, localDate.minusDays(1));
        if (dayReport == null) {
            dayScoreInfo.setLastDayScore(0);
        } else {
            dayScoreInfo.setLastDayScore(dayReport.getExtensionScore());
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
        for (int i = 1; i < Constant.MONTH_NUM; i++) {
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
        LocalDate localDate = LocalDate.now();
        //获取今天时间（时分秒默认为零）
        LocalDateTime beginTime = localDate.atStartOfDay();
        //设置今日访客量
        int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(userId, beginTime, now);
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
        for (int i = 1; i < Constant.MONTH_NUM; i++) {
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
        //获取昨天时间（时分秒默认为零）
        LocalDate localDate = LocalDate.now();
        LocalDateTime beginTime = localDate.atStartOfDay();
        LocalDateTime endTime = LocalDateTime.now();
        //今日关注人数
        int dayFollowNum = businessCardRecordRepository.countByUserIdAndFollowDateBetween(userId, beginTime, endTime);
        dayFollowNumInfo.setDayFollowNum(dayFollowNum);
        //当前排名
        User user = userRepository.findOne(userId);
        List<Long> sourceUserIdList = businessCardRecordRepository.findByCustomerIdAndFollowDate(user.getCustomerId(), beginTime, endTime);
        Map<Long, Integer> map = new TreeMap<>();
        for (long sourceUserId : sourceUserIdList
                ) {
            if (UserType.buddy.equals(getUserType(sourceUserId))) {
                int followNum = businessCardRecordRepository.countByUserIdAndFollowDateBetween(sourceUserId, beginTime, endTime);
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
        for (int i = 1; i < Constant.MONTH_NUM; i++) {
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
    private int getInfoForwardNum(Long infoId) {
        return infoBrowseRepository.findInfoForwardNum(infoId).size();
    }

    /**
     * 统计访客量（资讯转发浏览量）
     *
     * @param infoId 资讯ID
     * @return
     */
    private int getVisitorNum(Long infoId) {
        return infoBrowseRepository.countByInfoId(infoId);
    }

    /**
     * 获取排名
     *
     * @param map    所有用户
     * @param userId 用户ID
     * @return
     */
    private int getRanking(Map<Long, Integer> map, Long userId) {
        if (!map.containsKey(userId)) {
            return 0;
        }
        List<Map.Entry<Long, Integer>> list = new ArrayList<>(map.entrySet());
        //然后通过比较器来实现排序
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
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
     * @param i      判断月份
     * @return
     */
    private MonthStatisticInfo getMonthStatisticInfo(Long userId, int type, int i) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        MonthStatisticInfo monthStatisticInfo = new MonthStatisticInfo();
        List<MonthReport> reportList = monthReportRepository.findByUserIdAndReportMonth(userId, localDate.minusMonths(i).withDayOfMonth(1));
        if (i == 1) {
            monthStatisticInfo.setMonth("上月");
        } else {
            monthStatisticInfo.setMonth(now.minusMonths(i).getMonthValue() + "月");
        }
        if (reportList.isEmpty()) {
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
            }

        }
        return monthStatisticInfo;
    }
}
