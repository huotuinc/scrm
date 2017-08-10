package com.huotu.scrm.service.service.report.impl;

import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import com.huotu.scrm.service.service.report.MonthReportService;
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
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxh on 2017-07-12.
 */
@Service
public class MonthReportServiceImpl implements MonthReportService {

    @Autowired
    private DayReportRepository dayReportRepository;
    @Autowired
    private MonthReportRepository monthReportRepository;
    @Autowired
    private UserLevelRepository userLevelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessCardRecordRepository businessCardRecordRepository;
    private int pageSize = 200;

    @Override
    @Transactional
    public void saveMonthReport() {
        LocalDate today = LocalDate.now();
        //获取本月第一天
        LocalDate firstDay = today.with(TemporalAdjusters.firstDayOfMonth());
        //得到上月第一天
        LocalDate lastFirstDay = firstDay.minusMonths(1);
        //得到上月最后一天
        LocalDate lastEndDay = today.with(TemporalAdjusters.lastDayOfMonth()).minusMonths(1);
        List<Long> userIdList = dayReportRepository.findByUserId(lastFirstDay, lastEndDay);
        for (long userId : userIdList) {
            User user = userRepository.findOne(userId);
            if (user == null) {
                continue;
            }
            MonthReport monthReport = new MonthReport();
            //设置用户ID
            monthReport.setUserId(userId);
            //设置商户号
            monthReport.setCustomerId(user.getCustomerId());
            //设置等级
            monthReport.setLevelId(user.getLevelId());
            //设置是否为销售员
            UserLevel userLevel = userLevelRepository.findByIdAndCustomerId(user.getLevelId(), user.getCustomerId());
            if (userLevel == null) {
                continue;
            }
            monthReport.setSalesman(userLevel.isSalesman());
            //设置每月咨询转发量
            int forwardNum = getForwardNum(userId, lastFirstDay, lastEndDay);
            monthReport.setForwardNum(forwardNum);
            //设置每月访客量
            int visitorNum = getVisitorNum(userId, lastFirstDay, lastEndDay);
            monthReport.setVisitorNum(visitorNum);
            //设置每月推广积分
            int extensionScore = getExtensionScore(userId, lastFirstDay, lastEndDay);
            monthReport.setExtensionScore(extensionScore);
            //设置每月被关注量(销售员特有)
            int followNum = monthReport.isSalesman() ? businessCardRecordRepository.countByUserId(userId) : 0;
            monthReport.setFollowNum(followNum);
            //设置统计月份
            monthReport.setReportMonth(lastFirstDay);
            //保存数据
            monthReportRepository.save(monthReport);
        }
        //获取商户编号列表
        List<Long> customerIdList = monthReportRepository.findCustomerByReportDay(lastFirstDay);
        //设置每月积分排名（默认设置前200名）
        setRanking(0, customerIdList, lastFirstDay);
        //设置每月关注排名（默认设置前200名）
        setRanking(1, customerIdList, lastFirstDay);
    }

    /**
     * 统计每月资讯转发量
     *
     * @param userId  用户ID
     * @param minDate 统计起始时间
     * @param maxDate 统计结束时间
     * @return
     */
    private int getForwardNum(Long userId, LocalDate minDate, LocalDate maxDate) {
        Specification<DayReport> specification = getSpecification(userId, minDate, maxDate);
        List<DayReport> sortAll = dayReportRepository.findAll(specification);
        int num = 0;
        for (DayReport reportDay : sortAll) {
            num += reportDay.getForwardNum();
        }
        return num;
    }

    /**
     * 统计每月访客量(如果统计本月不含当天数据)
     *
     * @param userId  用户ID
     * @param minDate 统计起始时间
     * @param maxDate 统计结束时间
     * @return
     */
    private int getVisitorNum(Long userId, LocalDate minDate, LocalDate maxDate) {
        Specification<DayReport> specification = getSpecification(userId, minDate, maxDate);
        List<DayReport> sortAll = dayReportRepository.findAll(specification);
        int num = 0;
        for (DayReport reportDay : sortAll) {
            num += reportDay.getVisitorNum();
        }
        return num;
    }

    /**
     * 统计每月推广积分
     *
     * @param userId  用户ID
     * @param minDate 统计起始时间
     * @param maxDate 统计结束时间
     * @return
     */
    private int getExtensionScore(Long userId, LocalDate minDate, LocalDate maxDate) {
        Specification<DayReport> specification = getSpecification(userId, minDate, maxDate);
        List<DayReport> sortAll = dayReportRepository.findAll(specification);
        int num = 0;
        for (DayReport reportDay : sortAll) {
            num += reportDay.getExtensionScore();
        }
        return num;
    }

    /**
     * 设置排名
     *
     * @param rankingType    排名类型 0：积分 1：关注（销售员特有）
     * @param customerIdList 商户编号列表
     * @param month          排名月份
     */
    private void setRanking(int rankingType, List<Long> customerIdList, LocalDate month) {
        String str;
        if (rankingType == 0) {
            str = "extensionScore";
        } else {
            str = "followNum";
        }
        String rankingAttribute = str;
        customerIdList.forEach((Long customerId) -> {
            Specification<MonthReport> specification = ((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("customerId").as(Long.class), customerId));
                predicates.add(cb.equal(root.get("reportMonth").as(LocalDate.class), month));
                predicates.add(cb.greaterThan(root.get(rankingAttribute).as(Integer.class), 0));
                if (rankingType == 1) {
                    predicates.add(cb.equal(root.get("isSalesman").as(Boolean.class), true));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            });
            Sort sort = new Sort(Sort.Direction.DESC, rankingAttribute);
            Page<MonthReport> monthReportPage = monthReportRepository.findAll(specification, new PageRequest(0, pageSize, sort));
            List<MonthReport> monthReportList = monthReportPage.getContent();
            for (int i = 0; i < monthReportList.size(); i++) {
                MonthReport monthReport = monthReportList.get(i);
                if (rankingType == 0) {
                    monthReport.setScoreRanking(i + 1);
                } else {
                    monthReport.setFollowRanking(i + 1);
                }
                monthReportRepository.save(monthReport);
            }
        });
    }

    /**
     * 定时统计每月信息
     */
    @Override
    @Scheduled(cron = "0 45 0 1 * *")
    public void saveMonthReportScheduled() {
        saveMonthReport();
    }

    private Specification<DayReport> getSpecification(Long userId, LocalDate lastFirstDay, LocalDate lastEndDay) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId").as(Long.class), userId));
            predicates.add(cb.greaterThanOrEqualTo(root.get("reportDay").as(LocalDate.class), lastFirstDay));
            predicates.add(cb.lessThanOrEqualTo(root.get("reportDay").as(LocalDate.class), lastEndDay));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }
}
