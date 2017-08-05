package com.huotu.scrm.service.service.report.impl;

import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserFormalIntegral;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
import com.huotu.scrm.service.repository.mall.UserFormalIntegralRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import com.huotu.scrm.service.service.report.DayReportService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserFormalIntegralRepository userFormalIntegralRepository;
    @Autowired
    private MonthReportRepository monthReportRepository;


    @Override
    @Transactional
    public void saveDayReport() {
        LocalDate today = LocalDate.now();
        LocalDate lastDay = today.minusDays(1);
        LocalDateTime todayBeginTime = today.atStartOfDay();
        LocalDateTime lastBeginTime = todayBeginTime.minusDays(1);
        //获取昨日转发过资讯的用户
        List<Long> bySourceUserIdList = infoBrowseRepository.findSourceUserIdList(lastBeginTime, todayBeginTime);
        for (long sourceUserId : bySourceUserIdList) {
            User user = userRepository.findOne(sourceUserId);
            if (user == null) {
                continue;
            }
            DayReport dayReport = new DayReport();
            dayReport.setUserId(sourceUserId);
            dayReport.setCustomerId(user.getCustomerId());
            dayReport.setLevelId(user.getLevelId());
            UserLevel userLevel = userLevelRepository.findByIdAndCustomerId(user.getLevelId(), user.getCustomerId());
            if (userLevel == null) {
                continue;
            }
            dayReport.setSalesman(userLevel.isSalesman());
            //设置每日咨询转发量
            int forwardNum = infoBrowseRepository.findForwardNumBySourceUserId(lastBeginTime, todayBeginTime, sourceUserId).size();
            dayReport.setForwardNum(forwardNum);
            //设置每日访客量
            int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(sourceUserId, lastBeginTime, todayBeginTime);
            dayReport.setVisitorNum(visitorNum);
            int dayScore = getEstimateScore(user, lastBeginTime, todayBeginTime);
            dayReport.setExtensionScore(dayScore);
            //设置每日被关注量(销售员特有,即总的关注量)
            if (dayReport.isSalesman()) {
                int followNum = businessCardRecordRepository.countByUserId(sourceUserId);
                dayReport.setFollowNum(followNum);
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
     * @param user
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public int getEstimateScore(User user, LocalDateTime beginTime, LocalDateTime endTime) {
        int forwardScore = getForwardScore(user, beginTime, endTime);
        int visitorScore = getVisitorScore(user, beginTime, endTime);
        return forwardScore + visitorScore;
    }


    @Override
    public int getCumulativeScore(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime monthBegin = today.withDayOfMonth(1).atStartOfDay();
        int historyScore = 0;
        //获取本月之前的用户的积分
        List<MonthReport> monthReportList = monthReportRepository.findByUserId(user.getId());
        for (MonthReport monthReport : monthReportList
                ) {
            historyScore += monthReport.getExtensionScore();
        }
        int monthScore = getEstimateScore(user, monthBegin, now);
        return historyScore + monthScore;
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

    /**
     * 获取某个用户某段时间转发咨询奖励积分
     *
     * @param user
     * @param beginTime
     * @param endTime
     * @return
     */
    public int getForwardScore(User user, LocalDateTime beginTime, LocalDateTime endTime) {
        // TODO: 2017-08-04 status 待定
        List<UserFormalIntegral> formalIntegralList = userFormalIntegralRepository.findByUserIdAndMerchantIdAndStatusAndTimeBetween(user.getId(), user.getCustomerId(), 1, beginTime, endTime);
        int forwardScore = 0;
        for (UserFormalIntegral u : formalIntegralList
                ) {
            forwardScore += u.getScore();
        }
        return forwardScore;
    }

    /**
     * 计算用户的浏览咨询转发积分
     *
     * @param user
     * @param beginTime
     * @param endTime
     * @return
     */
    public int getVisitorScore(User user, LocalDateTime beginTime, LocalDateTime endTime) {
        int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(user.getId(), beginTime, endTime);
        //获取访客量奖励积分
        InfoConfigure infoConfigure = infoConfigureRepository.findOne(user.getCustomerId());
        int visitorScore = 0;
        int exchangeRate = infoConfigure.getExchangeRate();
        //判断是否开启访客量积分奖励
        if (infoConfigure.uvIsBuddyAndIsReward()) {
            if (infoConfigure.isDayExchangeLimitSwitch()) {
                visitorScore = Math.min(infoConfigure.getDayExchangeLimit(), visitorNum / exchangeRate);
            } else {
                visitorScore = visitorNum / exchangeRate;
            }
        }
        return visitorScore;
    }

    @Override
    public int getMonthVisitorNum(User user) {
        int monthVisitorNum = 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime beginTime = today.atStartOfDay();
        LocalDate monthBegin = today.withDayOfMonth(1);
        List<DayReport> dayReportList = dayReportRepository.findByUserIdAndReportDayGreaterThanEqual(user.getId(), monthBegin);
        for (DayReport dayReport :
                dayReportList) {
            monthVisitorNum += dayReport.getVisitorNum();
        }
        //获取今日访客量
        int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(user.getId(), beginTime, now);
        return monthVisitorNum + dayVisitorNum;
    }

    @Override
    public int getMonthForwardNum(User user) {
        int MonthForwardNum = 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime beginTime = today.atStartOfDay();
        LocalDate monthBegin = today.withDayOfMonth(1);
        List<DayReport> dayReportList = dayReportRepository.findByUserIdAndReportDayGreaterThanEqual(user.getId(), monthBegin);
        for (DayReport dayReport :
                dayReportList) {
            MonthForwardNum += dayReport.getForwardNum();
        }
        //获取今日转发量
        int dayForwardNum = infoBrowseRepository.findForwardNumBySourceUserId(beginTime, now, user.getId()).size();
        return MonthForwardNum + dayForwardNum;
    }

    @Override
    public int getMonthEstimateScore(User user) {
        int MonthEstimateScore = 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime beginTime = today.atStartOfDay();
        LocalDate monthBegin = today.withDayOfMonth(1);
        List<DayReport> dayReportList = dayReportRepository.findByUserIdAndReportDayGreaterThanEqual(user.getId(), monthBegin);
        for (DayReport dayReport :
                dayReportList) {
            MonthEstimateScore += dayReport.getExtensionScore();
        }
        //获取今日预计积分
        int dayEstimateScore = getEstimateScore(user, beginTime, now);
        return MonthEstimateScore + dayEstimateScore;
    }
}
