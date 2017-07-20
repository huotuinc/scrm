package com.huotu.scrm.service.service.report.impl;

import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.service.report.DayReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


/**
 * Created by hxh on 2017-07-11.
 */
@Service
public class DayReportServiceImpl implements DayReportService {

    @Autowired
    private DayReportRepository dayReportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLevelRepository userLevelRepository;
    @Autowired
    private InfoBrowseRepository infoBrowseRepository;
    @Autowired
    private InfoConfigureRepository infoConfigureRepository;
    @Autowired
    private BusinessCardRecordRepository businessCardRecordReposity;

    @Override
    @Transactional
    public void saveDayReport() {
        //获取今日日期
        LocalDate today = LocalDate.now();
        //获取昨日日期
        LocalDate lastDay = today.minusDays(1);
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime lastTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.minusDays(1).getDayOfMonth(), 0, 0, 0);
        List<Long> bySourceUserIdList = infoBrowseRepository.findBySourceUserId();
        for (long sourceUserId : bySourceUserIdList) {
            DayReport dayReport = new DayReport();
            User user = userRepository.findOne(sourceUserId);
            //设置用户ID
            dayReport.setUserId(sourceUserId);
            //设置商户号
            dayReport.setCustomerId(user.getCustomerId());
            //设置等级
            dayReport.setLevelId(user.getLevelId());
            //设置是否为销售员
            UserLevel userLevel = userLevelRepository.findByLevelAndCustomerId(user.getLevelId(), user.getCustomerId());
            dayReport.setSalesman(userLevel.isSalesman());
            //设置每日咨询转发量
            int forwardNumBySourceUserId = infoBrowseRepository.findForwardNumBySourceUserId(lastTime, now, sourceUserId);
            dayReport.setForwardNum(forwardNumBySourceUserId);
            //设置每日访客量
            int countBySourceUserId = infoBrowseRepository.countBySourceUserIdAndBrowseTime(sourceUserId, lastTime, now);
            dayReport.setVisitorNum(countBySourceUserId);
            //设置每日推广积分（咨询转发奖励和转发咨询浏览奖励）
            InfoConfigure infoConfigure = infoConfigureRepository.findOne(user.getCustomerId());
            //获取转发资讯奖励积分
            int forwardScore = getEstimateScore(sourceUserId, lastTime, now);
            //获取每日访客量奖励积分
            int visitorScore = 0;
            //获取访客量转换比例
            int exchangeRate = infoConfigure.getExchangeRate();
            //判断是否开启访客量积分奖励
            if (infoConfigure.isExchangeSwitch()) {
                //判断小伙伴是否开启访客量奖励
                int exchangeUserType = infoConfigure.getExchangeUserType();
                //exchangeUserType 1：小伙伴 2：小伙伴 + 会员
                if (exchangeUserType == 1 || exchangeUserType == 2) {
                    visitorScore = (countBySourceUserId / exchangeRate);
                }
            }
            dayReport.setExtensionScore(visitorScore + forwardScore);
            //设置每日被关注量(销售员特有)
            if (dayReport.isSalesman()) {
                int countByUserId = businessCardRecordReposity.countByUserId(sourceUserId, lastTime, now);
                dayReport.setFollowNum(countByUserId);
            } else {
                dayReport.setFollowNum(0);
            }
            //设置统计日期
            dayReport.setReportDay(lastDay);
            //保存数据
            dayReportRepository.save(dayReport);
            //设置每日访客排名
        }
        for (long userId : bySourceUserIdList) {
            DayReport dayReport = dayReportRepository.findByUserIdAndReportDay(userId, lastDay);
            int visitorRanking = visitorRanking(userId, lastDay);
            dayReport.setVisitorRanking(visitorRanking);
            //设置每日积分排名
            int scoreRanking = scoreRanking(userId, lastDay);
            dayReport.setScoreRanking(scoreRanking);
            //设置每日关注排名（销售员特有）
            if (dayReport.isSalesman()) {
                int followRanking = followRanking(userId, lastDay);
                dayReport.setFollowRanking(followRanking);
            } else {
                dayReport.setFollowRanking(-1);
            }
            dayReportRepository.save(dayReport);
        }
    }

    /**
     * 统计预计积分
     *
     * @param userId  用户ID
     * @param minDate 统计起始日期
     * @param maxDate 统计最后日期
     * @return
     */
    @Override
    public int getEstimateScore(Long userId, LocalDateTime minDate, LocalDateTime maxDate) {
        User user = userRepository.findOne(userId);
        int forwardNumBySourceUserId = infoBrowseRepository.findForwardNumBySourceUserId(minDate, maxDate,userId);
        InfoConfigure infoConfigure = infoConfigureRepository.findOne(user.getCustomerId());
        //获取转发咨询浏览量奖励积分
        int forwardScore = 0;
        //获取咨询转发转换比例
        int rewardScore = infoConfigure.getRewardScore();
        //判断是否开启咨询转发积分奖励
        if (infoConfigure.isRewardSwitch()) {
            //判断小伙伴是否开启咨询转发奖励
            int rewardUserType = infoConfigure.getRewardUserType();
            //rewardUserType 1：小伙伴 2：小伙伴 + 会员
            if (rewardUserType == 1 || rewardUserType == 2) {
                //判断是否开启奖励次数限制
                if (infoConfigure.isRewardLimitSwitch()) {
                    int rewardLimitNum = infoConfigure.getRewardLimitNum();
                    if (rewardLimitNum < forwardNumBySourceUserId) {
                        forwardScore = rewardLimitNum * rewardScore;
                    } else {
                        forwardScore = forwardNumBySourceUserId * rewardScore;
                    }
                } else {
                    forwardScore = forwardNumBySourceUserId * rewardScore;
                }
            }
        }
        return forwardScore;
    }

    @Override
    public int getCumulativeScore(Long userId) {
        User user = userRepository.findOne(userId);
        //获取注册时间
        Date regTime = user.getRegTime();
        Instant instant = regTime.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate regTimeDate = instant.atZone(zoneId).toLocalDate();
        List<DayReport> dayReports = dayReportRepository.findByReportDay(userId, regTimeDate);
        int cumulativeScore = 0;
        for (DayReport dayReport :
                dayReports) {
            cumulativeScore += dayReport.getExtensionScore();
        }
        return cumulativeScore;
    }

    /**
     * 统计访客排名
     *
     * @param userId 用户ID
     * @param date   统计日期
     * @return
     */
    public int visitorRanking(Long userId, LocalDate date) {
        List<DayReport> sortAll = dayReportRepository.findByReportDayOrderByVisitorNumDesc(date);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            System.out.println(userId);
            System.out.println(sortAll.get(i).getUserId());
            if (userId.equals(sortAll.get(i).getUserId())) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }

    /**
     * 统计推广积分排名
     *
     * @param userId 用户ID
     * @param date   统计日期
     * @return
     */
    public int scoreRanking(Long userId, LocalDate date) {
        List<DayReport> sortAll = dayReportRepository.findByReportDayOrderByExtensionScoreDesc(date);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (userId.equals(sortAll.get(i).getUserId())) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }

    /**
     * 统计关注排名
     *
     * @param userId 用户ID
     * @param date   统计日期
     * @return
     */
    public int followRanking(Long userId, LocalDate date) {
        List<DayReport> sortAll = dayReportRepository.findByReportDayOrderByFollowNumDesc(date);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (userId.equals(sortAll.get(i).getUserId())) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }
}
