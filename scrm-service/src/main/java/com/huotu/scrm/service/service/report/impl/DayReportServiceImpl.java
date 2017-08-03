package com.huotu.scrm.service.service.report.impl;

import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import com.huotu.scrm.service.service.report.DayReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
    private BusinessCardRecordRepository businessCardRecordRepository;
    @Autowired
    private MonthReportRepository monthReportRepository;

    @Override
    @Transactional
    public void saveDayReport() {
        //获取今日日期
        LocalDate today = LocalDate.now();
        //获取昨日日期
        LocalDate lastDay = today.minusDays(1);
        //获取当前时间（时分秒默认为零）
        LocalDateTime todayBeginTime = today.atStartOfDay();
        //获取昨天时间（时分秒默认为零）
        LocalDateTime lastBeginTime = todayBeginTime.minusDays(1);
        List<Long> bySourceUserIdList = infoBrowseRepository.findSourceUserIdList(lastBeginTime, todayBeginTime);
        for (long sourceUserId : bySourceUserIdList) {
            User user = userRepository.findOne(sourceUserId);
            if (user == null) {
                continue;
            }
            DayReport dayReport = new DayReport();
            //设置用户ID
            dayReport.setUserId(sourceUserId);
            //设置商户号
            dayReport.setCustomerId(user.getCustomerId());
            //设置等级
            dayReport.setLevelId(user.getLevelId());
            //设置是否为销售员
            UserLevel userLevel = userLevelRepository.findByIdAndCustomerId(user.getLevelId(), user.getCustomerId());
            if (userLevel == null) {
                continue;
            }
            dayReport.setSalesman(userLevel.isSalesman());
            dayReport.setSalesman(true);
            //设置每日咨询转发量
            Date lastBeginTimeDate = Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(lastBeginTime);
            Date todayBeginDate = Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(todayBeginTime);
            int forwardNum = infoBrowseRepository.findForwardNumBySourceUserId(lastBeginTimeDate, todayBeginDate, sourceUserId).size();
            dayReport.setForwardNum(forwardNum);
            //设置每日访客量
            int countBySourceUserId = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(sourceUserId, lastBeginTime, todayBeginTime);
            dayReport.setVisitorNum(countBySourceUserId);
            //设置每日推广积分（咨询转发奖励和转发咨询浏览奖励）
            int extensionScore = getEstimateScore(sourceUserId, lastBeginTime, todayBeginTime);
            dayReport.setExtensionScore(extensionScore);
            //设置每日被关注量(销售员特有)
            if (dayReport.isSalesman()) {
                int countByUserId = businessCardRecordRepository.countByUserIdAndFollowDateBetween(sourceUserId, lastBeginTime, todayBeginTime);
                dayReport.setFollowNum(countByUserId);
            } else {
                dayReport.setFollowNum(0);
            }
            //设置统计日期
            dayReport.setReportDay(lastDay);
            //保存数据
            dayReportRepository.save(dayReport);
        }
        //获取所有商户编号
        List<Long> customerIdList = dayReportRepository.findCustomerByReportDay(lastDay);
        //设置每日访客量排名（默认设置前200名）
        setRanking(0, customerIdList, lastDay);
        //设置每日积分排名（默认设置前200名）
        setRanking(1, customerIdList, lastDay);
        //设置每日关注排名（默认设置前200名）
        setRanking(2, customerIdList, lastDay);
    }

    /**
     * 统计推广积分
     *
     * @param userId  用户ID
     * @param minDate 统计起始日期
     * @param maxDate 统计最后日期
     * @return
     */
    @Override
    public int getEstimateScore(Long userId, LocalDateTime minDate, LocalDateTime maxDate) {
        User user = userRepository.findOne(userId);
        Date lastBeginTimeDate = Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(minDate);
        Date todayBeginDate = Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(maxDate);
        int forwardNum = infoBrowseRepository.findForwardNumBySourceUserId(lastBeginTimeDate, todayBeginDate, userId).size();
        InfoConfigure infoConfigure = infoConfigureRepository.findOne(user.getCustomerId());
        if (infoConfigure == null) {
            return 0;
        }

        //获取转发咨询浏览量奖励积分
        int forwardScore = 0;
        //获取咨询转发转换比例
        int rewardScore = infoConfigure.getRewardScore();
        //判断是否开启咨询转发积分奖励
        if (infoConfigure.extensionIsBuddyAndIsReward()) {
            int rewardNum = 0;
            if (infoConfigure.isExchangeSwitch()) {
                rewardNum = Math.min(infoConfigure.getRewardLimitNum(), forwardNum);
            } else {
                rewardNum = forwardNum;
            }
            forwardScore = rewardNum * rewardScore;
        }
        //获取用户访客量
        int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(userId, minDate, maxDate);
        //获取每日访客量奖励积分
        int visitorScore = 0;
        //获取访客量转换比例
        int exchangeRate = infoConfigure.getExchangeRate();
        //判断是否开启访客量积分奖励
        if (infoConfigure.uvIsBuddyAndIsReward()) {
            if (infoConfigure.isDayExchangeLimitSwitch()) {
                visitorScore = Math.min(infoConfigure.getDayExchangeLimit(), visitorNum / exchangeRate);
            } else {
                visitorScore = visitorNum / exchangeRate;
            }
        }
        return forwardScore + visitorScore;
    }

    @Override
    public int getCumulativeScore(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            return 0;
        }
        //获取注册时间
        Date regTime = user.getRegTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(regTime);
        LocalDate date = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        List<MonthReport> monthReports = monthReportRepository.findByUserIdAndReportMonthGreaterThanEqual(userId, date);
        int cumulativeScore = 0;
        for (MonthReport monthReport :
                monthReports) {
            cumulativeScore += monthReport.getExtensionScore();
        }
        return cumulativeScore;
    }

    /**
     * 定时统计每日信息
     */
    @Override
    @Scheduled(cron = "0 15 0 * * *")
    public void saveDayReportScheduled() {
        saveDayReport();
    }

    /**
     * 设置排名
     *
     * @param rankingType    排名类型 0：访客量 1：积分 2：关注量（销售员特有）
     * @param customerIdList 商户ID列表
     * @param date           排名日期
     */
    private void setRanking(int rankingType, List<Long> customerIdList, LocalDate date) {
        String str;
        switch (rankingType) {
            case 0:
                str = "visitorNum";
                break;
            case 1:
                str = "extensionScore";
                break;
            default:
                str = "followNum";
        }
        String rankingAttribute = str;
        customerIdList.forEach((Long customerId) -> {
            Specification<DayReport> specification = ((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("customerId").as(Long.class), customerId));
                predicates.add(cb.equal(root.get("reportDay").as(LocalDate.class), date));
                predicates.add(cb.greaterThan(root.get(rankingAttribute).as(Integer.class), 0));
                if (rankingType == 2) {
                    predicates.add(cb.equal(root.get("isSalesman").as(Boolean.class), true));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            });
            Sort sort = new Sort(Sort.Direction.DESC, rankingAttribute);
            Page<DayReport> dayReportPage = dayReportRepository.findAll(specification, new PageRequest(0, Constant.SORT_NUM, sort));
            List<DayReport> dayReportList = dayReportPage.getContent();
            for (int i = 0; i < dayReportList.size(); i++) {
                DayReport dayReport = dayReportList.get(i);
                switch (rankingType) {
                    case 0:
                        dayReport.setVisitorRanking(i + 1);
                        break;
                    case 1:
                        dayReport.setScoreRanking(i + 1);
                        break;
                    default:
                        dayReport.setFollowRanking(i + 1);
                }
                dayReportRepository.save(dayReport);
            }
        });
    }
}
