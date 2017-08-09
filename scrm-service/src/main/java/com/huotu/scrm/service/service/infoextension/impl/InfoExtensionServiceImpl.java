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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private BusinessCardRecordRepository businessCardRecordRepository;
    Map<Long, Integer> mapRankingVisitor = new TreeMap<>();
    Map<Long, Integer> mapDayScoreRanking = new TreeMap<>();
    Map<Long, Integer> mapMonthScoreRanking = new TreeMap<>();
    Map<Long, Integer> mapDayFollowRanking = new TreeMap<>();

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
            infoModel.setThumbnailImageUrl(info.getImageUrl());
            infoModel.setForwardNum(getInfoForwardNum(info.getId()));
            infoModel.setVisitorNum(getVisitorNum(info.getId()));
            infoModel.setReleaseTime(DateUtil.compareLocalDateTime(info.getCreateTime(), now));
            infoModels.add(infoModel);
        }
        return infoModels;
    }

    @Override
    public StatisticalInformation getInformation(User user) {
        StatisticalInformation statisticalInformation = new StatisticalInformation();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime beginTime = today.atStartOfDay();
        //设置今日访客量
        int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(user.getId(), beginTime, now);
        //设置今日预计积分(浏览+转发)
        int dayScore = dayReportService.getEstimateScore(user, beginTime, now);
        //获取累积积分
        int accumulateScore = dayReportService.getCumulativeScore(user);
        statisticalInformation.setDayVisitorNum(visitorNum);
        statisticalInformation.setDayScore(dayScore);
        statisticalInformation.setAccumulateScore(accumulateScore);
        //获取关注人数（销售员特有）
        UserLevel userLevel = userLevelRepository.findByIdAndCustomerId(user.getLevelId(), user.getCustomerId());
        int followNum = 0;
        if (userLevel != null) {
            if (userLevel.isSalesman()) {
                followNum = businessCardRecordRepository.countByUserId(user.getId());
            }
        }
        statisticalInformation.setFollowNum(followNum);
        //从map中获取对应信息
        //获取访客量排名
        int visitorRanking = 0;
        if (mapRankingVisitor.containsKey(user.getId())) {
            visitorRanking = mapRankingVisitor.get(user.getId());
        } else {
            //若map中没有信息，实时统计排名
            Map<Long, Integer> map = new TreeMap<>();
            List<Long> sourceUserIdList = infoBrowseRepository.findSourceIdByCustomerId(user.getCustomerId(), beginTime, now);
            sourceUserIdList.forEach(id -> {
                int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(id, beginTime, now);
                map.put(id, dayVisitorNum);
            });
            visitorRanking = getRanking(map, user.getId());
        }
        //判断用户是否有访客量（浏览量）
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
     * @param user 用户
     * @return
     */
    @Override
    public DayScoreRankingInfo getScoreRankingInfo(User user) {
        DayScoreRankingInfo dayScoreRankingInfo = new DayScoreRankingInfo();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        LocalDateTime beginTime = localDate.atStartOfDay();
        LocalDateTime firstDay = localDate.withDayOfMonth(1).atStartOfDay();
        int dayScoreRanking = 0;
        int monthRanking = 0;
        if (mapDayScoreRanking.containsKey(user.getId())) {
            dayScoreRanking = mapDayScoreRanking.get(user.getId());
        } else {
            List<Long> sourceUserIdList = infoBrowseRepository.findSourceIdByCustomerId(user.getCustomerId(), beginTime, now);
            List<Map<Long, Integer>> mapList = getMapList(sourceUserIdList);
            dayScoreRanking = getRanking(mapList.get(0), user.getId());
            monthRanking = getRanking(mapList.get(1), user.getId());
        }
        //判断用户是否有预计积分
        if (dayScoreRanking == 0) {
            int dayScore = dayReportService.getEstimateScore(user, beginTime, now);
            if (dayScore > 0) {
                dayScoreRanking = 1;
            }
        }
        dayScoreRankingInfo.setDayScoreRanking(dayScoreRanking);
        if (monthRanking == 0) {
            int monthScore = dayReportService.getEstimateScore(user, firstDay, now);
            if (monthScore > 0) {
                monthRanking = 1;
            }
        }
        dayScoreRankingInfo.setMonthScoreRanking(monthRanking);
        //设置最高月积分排名
        int highestMonthScoreRanking = monthReportRepository.findMaxScoreRanking(user.getId());
        dayScoreRankingInfo.setHighestMonthScoreRanking(highestMonthScoreRanking);
        //近几个月积分排名
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth("本月");
        monthInfo.setData(dayScoreRankingInfo.getMonthScoreRanking());
        monthStatisticInfoList.add(monthInfo);
        monthStatisticInfoList = getMonthStatisticInfo(user.getId(), 0);
        dayScoreRankingInfo.setMonthScoreRankingList(monthStatisticInfoList);
        return dayScoreRankingInfo;
    }

    /**
     * 统计今日积分页面信息
     *
     * @param user 用户
     * @return
     */
    @Override
    public DayScoreInfo getScoreInfo(User user) {
        DayScoreInfo dayScoreInfo = new DayScoreInfo();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime todayBegin = today.atStartOfDay();
        int extensionScore = dayReportService.getEstimateScore(user, todayBegin, now);
        dayScoreInfo.setDayScore(extensionScore);
        //设置昨日积分
        DayReport dayReport = dayReportRepository.findByUserIdAndReportDay(user.getId(), today.minusDays(1));
        if (dayReport == null) {
            dayScoreInfo.setLastDayScore(0);
        } else {
            dayScoreInfo.setLastDayScore(dayReport.getExtensionScore());
        }
        //设置历史累积积分
        int cumulativeScore = dayReportService.getCumulativeScore(user);
        //获取本月预计积分
        int monthScore = dayReportService.getMonthEstimateScore(user);
        dayScoreInfo.setAccumulateScore(cumulativeScore);
        //设置近几个月积分信息
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        MonthStatisticInfo monInfo = new MonthStatisticInfo();
        monInfo.setData(monthScore);
        monInfo.setMonth("本月");
        monthStatisticInfoList.add(monInfo);
        monthStatisticInfoList = getMonthStatisticInfo(user.getId(), 1);
        dayScoreInfo.setMonthScoreList(monthStatisticInfoList);
        return dayScoreInfo;
    }

    /**
     * 统计今日访客量页面信息
     *
     * @param user 用户
     * @return
     */
    @Override
    public DayVisitorNumInfo getVisitorNumInfo(User user) {
        DayVisitorNumInfo dayVisitorNumInfo = new DayVisitorNumInfo();
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime beginTime = today.atStartOfDay();
        int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(user.getId(), beginTime, now);
        dayVisitorNumInfo.setDayVisitorBum(dayVisitorNum);
        int monthVisitorNum = dayReportService.getMonthVisitorNum(user);
        dayVisitorNumInfo.setMonthVisitorNum(monthVisitorNum + dayVisitorNum);
        //设置历史最高月访问量
        int highestMonthVisitorNum = monthReportRepository.findMaxMonthVisitorNum(user.getId());
        dayVisitorNumInfo.setHighestMonthVisitorNum(highestMonthVisitorNum);
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth("本月");
        monthInfo.setData(dayVisitorNumInfo.getMonthVisitorNum());
        monthStatisticInfoList.add(monthInfo);
        monthStatisticInfoList = getMonthStatisticInfo(user.getId(), 2);
        dayVisitorNumInfo.setMonthVisitorNumList(monthStatisticInfoList);
        return dayVisitorNumInfo;
    }

    /**
     * 统计今日关注页面信息
     *
     * @param user 用户
     * @return
     */
    @Override
    public DayFollowNumInfo getFollowNumInfo(User user) {
        DayFollowNumInfo dayFollowNumInfo = new DayFollowNumInfo();
        //获取昨天时间（时分秒默认为零）
        LocalDate localDate = LocalDate.now();
        LocalDateTime beginTime = localDate.atStartOfDay();
        LocalDateTime endTime = LocalDateTime.now();
        //今日关注人数
        int dayFollowNum = businessCardRecordRepository.countByUserId(user.getId());
        dayFollowNumInfo.setDayFollowNum(dayFollowNum);
        //当前排名
        int followRanking = 0;
        if (dayFollowNum > 0) {
            if (mapDayFollowRanking.containsKey(user.getId())) {
                followRanking = mapDayFollowRanking.get(user.getId());
            } else {
                List<Long> sourceUserIdList = businessCardRecordRepository.findByCustomerIdAndFollowDate(user.getCustomerId(), beginTime, endTime);
                Map<Long, Integer> map = new TreeMap<>();
                for (long sourceUserId : sourceUserIdList
                        ) {
                    int followNum = businessCardRecordRepository.countByUserId(sourceUserId);
                    map.put(sourceUserId, followNum);
                }
                followRanking = getRanking(map, user.getId());
            }
        }
        dayFollowNumInfo.setFollowRanking(followRanking);
        //最高月排名
        int highestFollowRanking = monthReportRepository.findMaxMonthFollowNumRanking(user.getId());
        dayFollowNumInfo.setHighestFollowRanking(highestFollowRanking);
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth("本月");
        //本月关注量排名
        monthInfo.setData(followRanking);
        monthStatisticInfoList.add(monthInfo);
        monthStatisticInfoList = getMonthStatisticInfo(user.getId(), 3);
        dayFollowNumInfo.setMonthFollowRankingList(monthStatisticInfoList);
        return dayFollowNumInfo;
    }

    @Override
    public boolean checkIsSalesman(User user) {
        UserLevel userLevel = userLevelRepository.findByIdAndCustomerId(user.getLevelId(), user.getCustomerId());
        return userLevel != null && userLevel.isSalesman();
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
     * @return
     */
    private List<MonthStatisticInfo> getMonthStatisticInfo(Long userId, int type) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        List<MonthStatisticInfo> monthStatisticInfoList = new ArrayList<>();
        for (int i = 1; i < Constant.MONTH_NUM; i++) {
            MonthStatisticInfo monthStatisticInfo = new MonthStatisticInfo();
            MonthReport monthReport = monthReportRepository.findByUserIdAndReportMonth(userId, localDate.minusMonths(i).withDayOfMonth(1));
            if (i == 1) {
                monthStatisticInfo.setMonth("上月");
            } else {
                monthStatisticInfo.setMonth(now.minusMonths(i).getMonthValue() + "月");
            }
            if (monthReport == null) {
                monthStatisticInfo.setData(0);
            } else {
                switch (type) {
                    case 0:
                        monthStatisticInfo.setData(monthReport.getScoreRanking());
                        break;
                    case 1:
                        monthStatisticInfo.setData(monthReport.getExtensionScore());
                        break;
                    case 2:
                        monthStatisticInfo.setData(monthReport.getVisitorNum());
                        break;
                    default:
                        monthStatisticInfo.setData(monthReport.getFollowRanking());
                }

            }
            monthStatisticInfoList.add(monthStatisticInfo);
        }
        return monthStatisticInfoList;
    }

    /**
     * 设置排名
     *
     * @param map  统计数据
     * @param type 统计类型 0：访客量（浏览量） 1：今日积分 2：本月积分 3：关注量排名
     */
    private void setRanking(Map<Long, Integer> map, int type) {
        List<Map.Entry<Long, Integer>> list = new ArrayList<>(map.entrySet());
        //然后通过比较器来实现排序
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        int ranking = 0;
        for (Map.Entry<Long, Integer> mapping : list) {
            ranking++;
            switch (type) {
                case 0:
                    mapRankingVisitor.put(mapping.getKey(), ranking);
                    break;
                case 1:
                    mapDayScoreRanking.put(mapping.getKey(), ranking);
                    break;
                case 2:
                    mapMonthScoreRanking.put(mapping.getKey(), ranking);
                default:
                    mapDayFollowRanking.put(mapping.getKey(), ranking);
            }
        }
    }

    /**
     * 定时统计当日每个用户的访客量（浏览量）排名
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void setMapVisitorNumRanking() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayBegin = today.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        Map<Long, Integer> map = new TreeMap<>();
        List<Long> customerIdList = infoBrowseRepository.findCustomerIdList(todayBegin, now);
        customerIdList.forEach((Long customerId) -> {
            List<Long> sourceIdList = infoBrowseRepository.findSourceIdByCustomerId(customerId, todayBegin, now);
            sourceIdList.forEach((Long userId) -> {
                int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(userId, todayBegin, now);
                map.put(userId, dayVisitorNum);
            });
            setRanking(map, 0);
        });
    }

    /**
     * 统计每日和每月积分排名
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void setMapDayScoreRanking() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayBegin = today.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        List<Long> customerIdList = infoBrowseRepository.findCustomerIdList(todayBegin, now);
        customerIdList.forEach((Long customerId) -> {
            List<Long> sourceIdList = infoBrowseRepository.findSourceIdByCustomerId(customerId, todayBegin, now);
            List<Map<Long, Integer>> mapList = getMapList(sourceIdList);
            setRanking(mapList.get(0), 1);
            setRanking(mapList.get(0), 2);
        });
    }

    /**
     * 获取用户每日和每月积分map
     *
     * @param userIdList 用户列表
     * @return
     */
    private List<Map<Long, Integer>> getMapList(List<Long> userIdList) {
        LocalDate today = LocalDate.now();
        LocalDateTime todayBegin = today.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        List<Map<Long, Integer>> mapList = new ArrayList<>();
        Map<Long, Integer> mapDay = new TreeMap<>();
        Map<Long, Integer> mapMonth = new TreeMap<>();
        userIdList.forEach((Long userId) -> {
            //设置今日和本月预计积分(浏览+转发)
            User user = userRepository.findOne(userId);
            int dayScore = dayReportService.getEstimateScore(user, todayBegin, now);
            int monthScore = dayReportService.getMonthEstimateScore(user);
            mapDay.put(userId, dayScore);
            mapMonth.put(userId, monthScore);
        });
        mapList.add(mapDay);
        mapList.add(mapMonth);
        return mapList;
    }

    /**
     * 统计关注排名
     */

    @Scheduled(cron = "0 0/5 * * * *")
    public void setMapFollowRanking() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayBegin = today.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        Map<Long, Integer> map = new TreeMap<>();
        List<Long> customerIdList = infoBrowseRepository.findCustomerIdList(todayBegin, now);
        customerIdList.forEach((Long customerId) -> {
            List<Long> sourceIdList = infoBrowseRepository.findSourceIdByCustomerId(customerId, todayBegin, now);
            sourceIdList.forEach((Long userId) -> {
                int followNum = businessCardRecordRepository.countByUserId(userId);
                map.put(userId, followNum);
            });
            setRanking(map, 3);
        });
    }
}
