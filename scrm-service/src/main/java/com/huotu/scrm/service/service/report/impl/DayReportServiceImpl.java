package com.huotu.scrm.service.service.report.impl;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.User;
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
import com.huotu.scrm.service.service.api.ApiService;
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
import java.io.UnsupportedEncodingException;
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
    @Autowired
    private ApiService apiService;

    /**
     * 定时统计昨日信息
     * 每日12:15分统计
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 15 0 * * *")
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
            UserLevel userLevel = userLevelRepository.findByIdAndCustomerId(user.getLevelId(), user.getCustomerId());
            if (userLevel == null) {
                continue;
            }
            //昨日咨询转发量
            int forwardNum = infoBrowseRepository.findForwardNumBySourceUserId(lastBeginTime, todayBeginTime, sourceUserId).size();
            //昨日访客量（uv）
            int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(sourceUserId, lastBeginTime, todayBeginTime);
            //昨日积分
            int dayScore = getEstimateScore(user, lastBeginTime, todayBeginTime);
            DayReport dayReport = new DayReport();
            dayReport.setSalesman(userLevel.isSalesman());
            //昨日被关注量(销售员特有,即总的关注量)
            int followNum = dayReport.isSalesman() ? businessCardRecordRepository.countByUserId(sourceUserId) : 0;
            dayReport.setFollowNum(followNum);
            dayReport.setUserId(sourceUserId);
            dayReport.setCustomerId(user.getCustomerId());
            dayReport.setLevelId(user.getLevelId());
            dayReport.setForwardNum(forwardNum);
            dayReport.setVisitorNum(visitorNum);
            dayReport.setExtensionScore(dayScore);
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
     * 统计积分(只限当天的积分统计)
     *
     * @param user      用户
     * @param beginTime 开始时间
     * @param endTime   结束时间
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
        //获取本月之前的用户的积分
        List<MonthReport> monthReportList = monthReportRepository.findByUserId(user.getId());
        int historyScore = monthReportList.stream().mapToInt(MonthReport::getExtensionScore).sum();
        int monthScore = getMonthEstimateScore(user);
        return historyScore + monthScore;
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
     * @param user      用户
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    private int getForwardScore(User user, LocalDateTime beginTime, LocalDateTime endTime) {
        Integer totalForwardScore = userFormalIntegralRepository.sumByScore(user.getId(), user.getCustomerId(), user.getLevelId().intValue(), user.getUserType().ordinal(), IntegralTypeEnum.TURN_INFO, beginTime, endTime);
        return totalForwardScore == null ? 0 : totalForwardScore;
    }

    /**
     * 计算用户的浏览咨询奖励积分
     *
     * @param user      用户
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    private int getVisitorScore(User user, LocalDateTime beginTime, LocalDateTime endTime) {
        int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(user.getId(), beginTime, endTime);
        //获取访客量奖励积分
        InfoConfigure infoConfigure = infoConfigureRepository.findOne(user.getCustomerId());
        if (infoConfigure == null) {
            return 0;
        }
        int visitorScore = 0;
        int exchangeRate = infoConfigure.getExchangeRate();
        //判断是否开启访客量积分奖励
        if (infoConfigure.uvIsBuddyAndIsReward()) {
            visitorScore = infoConfigure.isDayExchangeLimitSwitch() ? Math.min(infoConfigure.getDayExchangeLimit(), visitorNum / exchangeRate) : (visitorNum / exchangeRate);
        }
        return visitorScore;
    }

    @Override
    public int getMonthVisitorNum(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime beginTime = today.atStartOfDay();
        LocalDate monthBegin = today.withDayOfMonth(1);
        List<DayReport> dayReportList = dayReportRepository.findByUserIdAndReportDayBetween(user.getId(), monthBegin, today);
        int monthVisitorNum = dayReportList.stream().mapToInt(DayReport::getVisitorNum).sum();
        //获取今日访客量
        int dayVisitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(user.getId(), beginTime, now);
        return monthVisitorNum + dayVisitorNum;
    }

    @Override
    public int getMonthEstimateScore(User user) {
        LocalDate today = LocalDate.now();
        LocalDate monthBegin = today.withDayOfMonth(1);
        List<DayReport> dayReportList = dayReportRepository.findByUserIdAndReportDayBetween(user.getId(), monthBegin, today);
        int todayForwardScore = getTodayForwardScore(user);
        return dayReportList.stream().mapToInt(DayReport::getExtensionScore).sum() + todayForwardScore;
    }

    /**
     * 获取今日转发积分
     *
     * @param user 用户
     * @return
     */
    private int getTodayForwardScore(User user) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        Integer score = userFormalIntegralRepository.sumByScore(user.getId(), user.getCustomerId(), user.getLevelId().intValue(), user.getUserType().ordinal(), IntegralTypeEnum.TURN_INFO, localDate.atStartOfDay(), now);
        return score == null ? 0 : score;
    }

    /**
     * 每日12:05分统计
     */
    @Override
    @Scheduled(cron = "0 5 0 * * *")
    public void saveDayVisitorScore() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayBeginTime = today.atStartOfDay();
        LocalDateTime lastBeginTime = todayBeginTime.minusDays(1);
        List<Long> userIdList = infoBrowseRepository.findSourceUserIdList(lastBeginTime, todayBeginTime);
        userIdList.forEach((Long userId) -> {
            User user = userRepository.findOne(userId);
            int visitorScore = getVisitorScore(user, lastBeginTime, todayBeginTime);
            try {
                if (visitorScore > 0) {
                    apiService.rechargePoint(user.getCustomerId(), user.getId(), (long) visitorScore, IntegralTypeEnum.BROWSE_INFO);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }
}
