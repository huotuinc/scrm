package com.huotu.scrm.service.service.report.impl;

import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import com.huotu.scrm.service.service.report.MonthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
    private DayReportRepository reportDayRepository;
    @Autowired
    private MonthReportRepository monthReportRepository;
    @Autowired
    private UserLevelRepository userLevelRepository;
    @Autowired
    private UserRepository userRepository;

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
        List<Long> userIdList = reportDayRepository.findByUserId(lastFirstDay, lastEndDay);
        for (long userId : userIdList) {
            User user = userRepository.findOne(userId);
            MonthReport monthReport = new MonthReport();
            //设置用户ID
            monthReport.setUserId(userId);
            //设置商户号
            monthReport.setCustomerId(user.getCustomerId());
            //设置等级
            monthReport.setLevelId(user.getLevelId());
            //设置是否为销售员
            UserLevel userLevel = userLevelRepository.findByLevelAndCustomerId(user.getLevelId(), user.getCustomerId());
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
            if (monthReport.isSalesman()) {
                int followNum = getFollowNum(userId, lastFirstDay, lastEndDay);
                monthReport.setFollowNum(followNum);
            } else {
                monthReport.setFollowNum(-1);
            }
            //设置统计月份
            monthReport.setReportMonth(lastFirstDay);
            //保存数据
            //先删除数据
            List<MonthReport> monthReportList = monthReportRepository.findByUserIdAndReportMonth(userId, lastFirstDay);
            monthReportList.forEach(p -> {
                monthReportRepository.delete(p.getId());
            });
            monthReportRepository.save(monthReport);
        }
        //设置排名
        for (long userId : userIdList) {
            MonthReport monthReport = monthReportRepository.findByUserIdAndReportMonth(userId, lastFirstDay).get(0);
            //设置每月积分排名
            int scoreRanking = getScoreRanking(userId, lastFirstDay);
            monthReport.setScoreRanking(scoreRanking);
            //设置每月关注量排名
            int followRanking = getFollowRanking(userId, lastFirstDay);
            monthReport.setFollowRanking(followRanking);
            monthReportRepository.save(monthReport);
        }
    }

    /**
     * 统计每月资讯转发量
     *
     * @param userId  用户ID
     * @param minDate 统计起始时间
     * @param maxDate 统计结束时间
     * @return
     */
    public int getForwardNum(Long userId, LocalDate minDate, LocalDate maxDate) {
        Specification<DayReport> specification = getSpecification(userId, minDate, maxDate);
        List<DayReport> sortAll = reportDayRepository.findAll(specification);
        int num = 0;
        for (DayReport reportDay : sortAll) {
            num += reportDay.getForwardNum();
        }
        return num;
    }

    /**
     * 统计每月访客量
     *
     * @param userId  用户ID
     * @param minDate 统计起始时间
     * @param maxDate 统计结束时间
     * @return
     */
    public int getVisitorNum(Long userId, LocalDate minDate, LocalDate maxDate) {
        Specification<DayReport> specification = getSpecification(userId, minDate, maxDate);
        List<DayReport> sortAll = reportDayRepository.findAll(specification);
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
    public int getExtensionScore(Long userId, LocalDate minDate, LocalDate maxDate) {
        Specification<DayReport> specification = getSpecification(userId, minDate, maxDate);
        List<DayReport> sortAll = reportDayRepository.findAll(specification);
        int num = 0;
        for (DayReport reportDay : sortAll) {
            num += reportDay.getExtensionScore();
        }
        return num;
    }

    /**
     * 统计每月关注量（销售员特有）
     *
     * @param userId  用户ID
     * @param minDate 统计起始时间
     * @param maxDate 统计起始时间
     * @return
     */
    public int getFollowNum(Long userId, LocalDate minDate, LocalDate maxDate) {
        Specification<DayReport> specification = ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId").as(Long.class), userId));
            predicates.add(cb.greaterThanOrEqualTo(root.get("reportDay").as(LocalDate.class), minDate));
            predicates.add(cb.lessThanOrEqualTo(root.get("reportDay").as(LocalDate.class), maxDate));
            predicates.add(cb.equal(root.get("isSalesman").as(boolean.class), true));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        });
        List<DayReport> sortAll = reportDayRepository.findAll(specification);
        int num = 0;
        for (DayReport reportDay : sortAll) {
            num += reportDay.getFollowNum();
        }
        return num;
    }

    /**
     * 统计每月推广积分排名
     *
     * @param userId 用户ID
     * @param month  统计月份
     * @return
     */
    public int getScoreRanking(Long userId, LocalDate month) {
        List<MonthReport> sortAll = monthReportRepository.findByReportMonthOrderByExtensionScoreDesc(month);
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
     * 统计关注量排名（销售员特有）
     *
     * @param userId 用户ID
     * @param month  统计月份
     * @return
     */
    public int getFollowRanking(Long userId, LocalDate month) {
        List<MonthReport> sortAll = monthReportRepository.findOrderByFollowNum(month);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (userId.equals(sortAll.get(i).getUserId())) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }

    public Specification<DayReport> getSpecification(Long userId, LocalDate lastFirstDay, LocalDate lastEndDay) {
        Specification<DayReport> specification = ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId").as(Long.class), userId));
            predicates.add(cb.greaterThanOrEqualTo(root.get("reportDay").as(LocalDate.class), lastFirstDay));
            predicates.add(cb.lessThanOrEqualTo(root.get("reportDay").as(LocalDate.class), lastEndDay));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        });
        return specification;
    }
}
