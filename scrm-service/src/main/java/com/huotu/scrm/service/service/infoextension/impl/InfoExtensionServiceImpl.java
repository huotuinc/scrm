package com.huotu.scrm.service.service.infoextension.impl;

import com.huotu.scrm.common.SysConstant;
import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.common.utils.DateUtil;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.model.info.InfoModel;
import com.huotu.scrm.service.model.statisticinfo.*;
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
import java.util.*;
import java.util.stream.Collectors;

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
    private Map<Long, Integer> mapRankingVisitor = new HashMap<>();
    private Map<Long, Integer> mapDayScoreRanking = new HashMap<>();
    private Map<Long, Integer> mapMonthScoreRanking = new HashMap<>();
    private Map<Long, Integer> mapDayFollowRanking = new HashMap<>();


    @Override
    public List<InfoModel> findInfo(User user) {
        List<Info> infoList;
        if (user.getUserType() == UserType.normal) {//普通会员
            infoList = infoRepository.findByCustomerIdAndIsStatusTrueAndIsDisableFalseOrderByCreateTimeDesc(user.getCustomerId());
        } else {//小伙伴
            infoList = infoRepository.findByCustomerIdAndIsExtendTrueAndIsDisableFalseOrderByCreateTimeDesc(user.getCustomerId());
        }
        return getInfoModes(infoList, user, false);
    }

    @Override
    public List<InfoModel> findForwardInfo(User user) {
        List<InfoBrowse> infoBrowseList = infoBrowseRepository.findUserForwardInfo(user.getId());
        if (infoBrowseList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> infoIdList = infoBrowseList.stream().map(InfoBrowse::getInfoId).collect(Collectors.toList());
        List<Info> infoList = infoRepository.findInfoList(infoIdList);
        List<Info> resultList = new ArrayList<>();
        for (Long infoId : infoIdList) {
            for (Info info : infoList) {
                if (infoId.equals(info.getId())) {
                    resultList.add(info);
                }
            }
        }
        return getInfoModes(resultList, user, true);
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
        //获取关注人数（销售员特有）
        int followNum = 0;
        if (checkIsSalesman(user)) {
            followNum = businessCardRecordRepository.countByUserId(user.getId());
        }
        //从map中获取对应信息
        //获取访客量排名
        int visitorRanking = 0;
        if (mapRankingVisitor.containsKey(user.getId())) {
            visitorRanking = mapRankingVisitor.get(user.getId());
        }
        if (visitorRanking == 0 && visitorNum > 0) {
            //表示200名以外
            visitorRanking = -1;
        }
        statisticalInformation.setFollowNum(followNum);
        statisticalInformation.setDayVisitorNum(visitorNum);
        statisticalInformation.setDayScore(dayScore);
        statisticalInformation.setAccumulateScore(accumulateScore);
        statisticalInformation.setDayVisitorRanking(visitorRanking);
        return statisticalInformation;
    }

    /**
     * 统计今日积分排名页面信息:今日积分排名；本月积分排名；最高月积分排名
     *
     * @param user 用户
     * @return
     */
    @Override
    public DayScoreRankingInfo getScoreRankingInfo(User user) {
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        LocalDateTime beginTime = localDate.atStartOfDay();
        DayScoreRankingInfo dayScoreRankingInfo = new DayScoreRankingInfo();
        int dayScoreRanking = 0;
        int monthRanking = 0;
        if (mapDayScoreRanking.containsKey(user.getId())) {
            dayScoreRanking = mapDayScoreRanking.get(user.getId());
        }
        if (mapMonthScoreRanking.containsKey(user.getId())) {
            monthRanking = mapMonthScoreRanking.get(user.getId());
        }
        //判断用户今日是否有预计积分
        if (dayScoreRanking == 0 && dayReportService.getEstimateScore(user, beginTime, now) > 0) {
            //表示200名以外
            dayScoreRanking = -1;
        }
        //判断用户本月是否有预计积分
        if (monthRanking == 0 && dayReportService.getMonthEstimateScore(user) > 0) {
            //表示200名以外
            monthRanking = -1;
        }
        //近几个月积分排名
        List<MonthStatisticInfo> monthStatisticInfoList;
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth("本月");
        monthInfo.setData(monthRanking);
        monthStatisticInfoList = getMonthStatisticInfo(user.getId(), 0);
        monthStatisticInfoList.add(0, monthInfo);
        //设置最高月排名（默认在近5个月排名）
        int highestMonthScoreRanking = setHighestMonthRanking(monthStatisticInfoList);
        dayScoreRankingInfo.setHighestMonthScoreRanking(highestMonthScoreRanking);
        dayScoreRankingInfo.setMonthScoreRankingList(monthStatisticInfoList);
        dayScoreRankingInfo.setDayScoreRanking(dayScoreRanking);
        dayScoreRankingInfo.setMonthScoreRanking(monthRanking);
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
        //设置昨日积分
        DayReport dayReport = dayReportRepository.findByUserIdAndReportDay(user.getId(), today.minusDays(1));
        int lastScore = (dayReport == null) ? 0 : dayReport.getExtensionScore();
        //设置历史累积积分
        int cumulativeScore = dayReportService.getCumulativeScore(user);
        //获取本月积分
        int monthScore = dayReportService.getMonthEstimateScore(user);
        //设置近几个月积分信息
        List<MonthStatisticInfo> monthStatisticInfoList;
        MonthStatisticInfo monInfo = new MonthStatisticInfo();
        monInfo.setMonth("本月");
        monInfo.setData(monthScore);
        monthStatisticInfoList = getMonthStatisticInfo(user.getId(), 1);
        monthStatisticInfoList.add(0, monInfo);
        dayScoreInfo.setLastDayScore(lastScore);
        dayScoreInfo.setMonthScoreList(monthStatisticInfoList);
        dayScoreInfo.setDayScore(extensionScore);
        dayScoreInfo.setAccumulateScore(cumulativeScore);
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
        int monthVisitorNum = dayReportService.getMonthVisitorNum(user);
        List<MonthStatisticInfo> monthStatisticInfoList;
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth("本月");
        monthInfo.setData(monthVisitorNum);
        monthStatisticInfoList = getMonthStatisticInfo(user.getId(), 2);
        monthStatisticInfoList.add(0, monthInfo);
        //设置历史最高月访问量
        int highestMonthVisitorNum = setHighestMonthNum(monthStatisticInfoList);
        dayVisitorNumInfo.setHighestMonthVisitorNum(highestMonthVisitorNum);
        dayVisitorNumInfo.setDayVisitorBum(dayVisitorNum);
        dayVisitorNumInfo.setMonthVisitorNum(monthVisitorNum);
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
        //今日关注人数
        int dayFollowNum = businessCardRecordRepository.countByUserId(user.getId());
        //当前排名
        int followRanking = 0;
        if (mapDayFollowRanking.containsKey(user.getId())) {
            followRanking = mapDayFollowRanking.get(user.getId());
        }
        if (followRanking == 0 && dayFollowNum > 0) {
            //表示200名以外
            followRanking = -1;
        }
        List<MonthStatisticInfo> monthStatisticInfoList;
        MonthStatisticInfo monthInfo = new MonthStatisticInfo();
        monthInfo.setMonth("本月");
        //本月关注量排名
        monthInfo.setData(followRanking);
        monthStatisticInfoList = getMonthStatisticInfo(user.getId(), 3);
        monthStatisticInfoList.add(0, monthInfo);
        //最高月排名（默认排近5个月）
        int highestFollowRanking = setHighestMonthRanking(monthStatisticInfoList);
        dayFollowNumInfo.setHighestFollowRanking(highestFollowRanking);
        dayFollowNumInfo.setMonthFollowRankingList(monthStatisticInfoList);
        dayFollowNumInfo.setDayFollowNum(dayFollowNum);
        dayFollowNumInfo.setFollowRanking(followRanking);
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
     * @param user
     * @param status
     * @return
     */
    private int getVisitorNum(Long infoId, User user, boolean status) {
        if (status) {
            return infoBrowseRepository.countByInfoIdAndSourceUserId(infoId, user.getId());
        }
        return infoBrowseRepository.countByInfoId(infoId);
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
            String monthInfo = (i == 1) ? "上月" : now.minusMonths(i).getMonthValue() + "月";
            monthStatisticInfo.setMonth(monthInfo);
            if (monthReport == null) {
                monthStatisticInfo.setData(0);
            } else {
                switch (type) {
                    case 0:
                        int scoreRanking = (monthReport.getScoreRanking() == 0) ? -1 : monthReport.getScoreRanking();
                        monthStatisticInfo.setData(scoreRanking);
                        break;
                    case 1:
                        monthStatisticInfo.setData(monthReport.getExtensionScore());
                        break;
                    case 2:
                        monthStatisticInfo.setData(monthReport.getVisitorNum());
                        break;
                    default:
                        int followRanking = (monthReport.getFollowRanking() == 0) ? -1 : monthReport.getFollowRanking();
                        monthStatisticInfo.setData(followRanking);
                }
            }
            monthStatisticInfoList.add(monthStatisticInfo);
        }
        return monthStatisticInfoList;
    }

    /**
     * 设置排名（默认只排前20名）
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
            if (ranking > Integer.parseInt(SysConstant.DAY_SORT_NUM)) {
                break;
            }
            switch (type) {
                case 0:
                    mapRankingVisitor.put(mapping.getKey(), ranking);
                    break;
                case 1:
                    mapDayScoreRanking.put(mapping.getKey(), ranking);
                    break;
                case 2:
                    mapMonthScoreRanking.put(mapping.getKey(), ranking);
                    break;
                default:
                    mapDayFollowRanking.put(mapping.getKey(), ranking);
            }
        }
    }

    /**
     * 获取资讯model
     *
     * @param infoList 资讯列表
     * @param user     用户
     * @return
     */
    private List<InfoModel> getInfoModes(List<Info> infoList, User user, boolean status) {
        List<InfoModel> infoModels = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        infoList.forEach(info -> {
            InfoModel infoModel = new InfoModel();
            infoModel.setInfoId(info.getId());
            infoModel.setCustomerId(info.getCustomerId());
            infoModel.setTitle(info.getTitle());
            infoModel.setIntroduce(info.getIntroduce());
            infoModel.setThumbnailImageUrl(info.getImageUrl());
            infoModel.setForwardNum(getInfoForwardNum(info.getId()));
            infoModel.setVisitorNum(getVisitorNum(info.getId(), user, status));
            infoModel.setReleaseTime(DateUtil.compareLocalDateTime(info.getCreateTime(), now));
            infoModels.add(infoModel);
        });
        return infoModels;
    }

    /**
     * 定时统计当日每个用户的访客量（uv）排名,每日和每月积分排名
     * 每隔3分钟统计一次
     */
    @Scheduled(cron = "0 */3 * * * *")
    public void setMapVisitorNumRanking() {
        mapRankingVisitor.clear();
        mapDayScoreRanking.clear();
        mapMonthScoreRanking.clear();
        LocalDate today = LocalDate.now();
        LocalDateTime todayBegin = today.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        List<Long> customerIdList = infoBrowseRepository.findCustomerIdList(todayBegin, now);
        customerIdList.forEach((Long customerId) -> {
            List<Long> sourceIdList = infoBrowseRepository.findSourceIdByCustomerId(customerId, todayBegin, now);
            List<Map<Long, Integer>> mapList = getMapList(sourceIdList);
            setRanking(mapList.get(0), 0);
            setRanking(mapList.get(1), 1);
            setRanking(mapList.get(2), 2);
        });
    }

    /**
     * 统计关注排名
     */
    @Scheduled(cron = "0 */3 * * * *")
    public void setMapFollowRanking() {
        mapDayFollowRanking.clear();
        Map<Long, Integer> map = new HashMap<>();
        List<Long> customerIdList = businessCardRecordRepository.findCustomerList();
        customerIdList.forEach((Long customerId) -> {
            List<Long> sourceIdList = businessCardRecordRepository.findUserIdList(customerId);
            sourceIdList.forEach((Long userId) -> {
                int followNum = businessCardRecordRepository.countByUserId(userId);
                if (followNum > 0) {
                    map.put(userId, followNum);
                }
            });
            setRanking(map, 3);
            map.clear();
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
        Map<Long, Integer> mapDayScore = new HashMap<>();
        Map<Long, Integer> mapMonthScore = new HashMap<>();
        Map<Long, Integer> mapDayVisitorNum = new HashMap<>();
        userIdList.forEach((Long userId) -> {
            //设置今日和本月预计积分(浏览+转发)
            User user = userRepository.findOne(userId);
            if(user != null){
                int dayScore = dayReportService.getEstimateScore(user, todayBegin, now);
                int monthScore = dayReportService.getMonthEstimateScore(user);
                int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(userId, todayBegin, now);
                if (dayVisitorNum > 0) {
                    mapDayVisitorNum.put(userId, dayVisitorNum);
                }
                if (dayScore > 0) {
                    mapDayScore.put(userId, dayScore);
                }
                if (monthScore > 0) {
                    mapMonthScore.put(userId, monthScore);
                }
            }
        });
        mapList.add(mapDayVisitorNum);
        mapList.add(mapDayScore);
        mapList.add(mapMonthScore);
        return mapList;
    }

    /**
     * 获取最高月排名信息
     *
     * @param monthStatisticInfoList 每月信息
     * @return
     */
    private int setHighestMonthRanking(List<MonthStatisticInfo> monthStatisticInfoList) {
        //先排序200名以内（升序）
        List<MonthStatisticInfo> monthStatisticList = monthStatisticInfoList.stream().filter(monthStatisticInfo -> monthStatisticInfo.getData() > 0).sorted(Comparator.comparing(MonthStatisticInfo::getData))
                .collect(Collectors.toList());
        if (monthStatisticList.isEmpty()) {
            return monthStatisticInfoList.stream().sorted(Comparator.comparing(MonthStatisticInfo::getData))
                    .collect(Collectors.toList()).get(0).getData();
        } else {
            return monthStatisticInfoList.get(0).getData();
        }
    }

    /**
     * 获取最高月数量信息
     *
     * @param monthStatisticInfoList 每月信息
     * @return
     */
    private int setHighestMonthNum(List<MonthStatisticInfo> monthStatisticInfoList) {
        //先排序200名以内（降序）
        return monthStatisticInfoList.stream().sorted(Comparator.comparing(MonthStatisticInfo::getData).reversed())
                .collect(Collectors.toList()).get(0).getData();
    }
}
