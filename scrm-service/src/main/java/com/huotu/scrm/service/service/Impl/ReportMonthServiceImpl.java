package com.huotu.scrm.service.service.impl;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.report.ReportDay;
import com.huotu.scrm.service.entity.report.ReportMonth;
import com.huotu.scrm.service.repository.ReportDayRepository;
import com.huotu.scrm.service.repository.ReportMonthRepository;
import com.huotu.scrm.service.service.ReportMonthService;
import com.huotu.scrm.service.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hxh on 2017-07-12.
 */
@Service
public class ReportMonthServiceImpl implements ReportMonthService {

    @Autowired
    private ReportDayRepository reportDayRepository;

    @Autowired
    private ReportMonthRepository reportMonthRepository;

    @Override
    @Transactional
    public ApiResult saveReportMonth(long userId) {
        /*得到上月最后一天*/
        Date month = DateUtil.getLastMonthLastDay();
        /*得到上月第一天*/
        Date monthDay = DateUtil.getLastMonthFirstDay();
        ReportMonth reportMonth = new ReportMonth();
        /*设置用户ID*/
        reportMonth.setUserId(userId);
        /*设置商户ID*/
        /*设置等级*/
         /*设置是否为销售员*/
        /*设置每月咨询转发量*/
        int forwardNum = getForwardNum(userId, month, monthDay);
        reportMonth.setFollowNum(forwardNum);
        /*设置每月访客量*/
        int visitorNum = getVisitorNum(userId, month, monthDay);
        reportMonth.setVisitorNum(visitorNum);
        /*设置每月推广积分*/
        int extensionScore = getExtensionScore(userId, month, monthDay);
        reportMonth.setExtensionScore(extensionScore);
        /*设置每月被关注量(销售员特有)*/
        if (reportMonth.isSalesman()) {
            int followNum = getFollowNum(userId, month, monthDay);
            reportMonth.setFollowNum(followNum);
        } else {
            reportMonth.setFollowNum(-1);
        }
        /*设置统计月份*/
        reportMonth.setReportMonth(monthDay);
         /*保存数据*/
        reportMonthRepository.save(reportMonth);
        /*设置每日积分排名*/
        int scoreRanking = getScoreRanking(userId, monthDay);
        reportMonth.setScoreRanking(scoreRanking);
        reportMonthRepository.save(reportMonth);
        /*设置每月关注量排名*/
        int followRanking = getFollowRanking(userId, monthDay);
        reportMonth.setFollowRanking(followRanking);
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS, "每月统计信息成功", null);
    }

    /**
     * 统计每月资讯转发量
     *
     * @param userId   用户ID
     * @param month    当前月份
     * @param monthDay 当前月份第一天
     * @return
     */
    int getForwardNum(long userId, Date month, Date monthDay) {
        Specification<ReportDay> specification = getSpecification(userId, month, monthDay);
        List<ReportDay> visitorNumList = reportDayRepository.findAll(specification);
        int num = 0;
        for (ReportDay reportDay : visitorNumList) {
            num += reportDay.getForwardNum();
        }
        return num;
    }

    /**
     * 统计每月访客量
     *
     * @param userId   用户ID
     * @param month    当前月份
     * @param monthDay 当前月份第一天
     * @return
     */
    int getVisitorNum(long userId, Date month, Date monthDay) {
        Specification<ReportDay> specification = getSpecification(userId, month, monthDay);
        List<ReportDay> visitorNumList = reportDayRepository.findAll(specification);
        int num = 0;
        for (ReportDay reportDay : visitorNumList) {
            num += reportDay.getVisitorNum();
        }
        return num;
    }

    /**
     * 统计每月推广积分
     *
     * @param userId   用户ID
     * @param month    当前月份
     * @param monthDay 上个月份第一天
     * @return
     */
    int getExtensionScore(long userId, Date month, Date monthDay) {
        Specification<ReportDay> specification = getSpecification(userId, month, monthDay);
        List<ReportDay> visitorNumList = reportDayRepository.findAll(specification);
        int num = 0;
        for (ReportDay reportDay : visitorNumList) {
            num += reportDay.getExtensionScore();
        }
        return num;
    }

    /**
     * 统计每月别关注量（销售员特有）
     *
     * @param userId   用户ID
     * @param month    当前月份
     * @param monthDay 上个月份第一天
     * @return
     */
    int getFollowNum(long userId, Date month, Date monthDay) {
        Specification<ReportDay> specification = ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId").as(Long.class), userId));
            predicates.add(cb.lessThanOrEqualTo(root.get("reportDay").as(Date.class), month));
            predicates.add(cb.greaterThanOrEqualTo(root.get("reportDay").as(Date.class), monthDay));
            predicates.add(cb.equal(root.get("isSalesman").as(boolean.class), true));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        });
        List<ReportDay> visitorNumList = reportDayRepository.findAll(specification);
        int num = 0;
        for (ReportDay reportDay : visitorNumList) {
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
    public int getScoreRanking(long userId, Date month) {
        List<ReportMonth> sortAll = reportMonthRepository.findOrderByExtensionScore(month);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
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
    public int getFollowRanking(long userId, Date month) {
        List<ReportMonth> sortAll = reportMonthRepository.findOrderByFollowNum(month);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }

    public Specification<ReportDay> getSpecification(long userId, Date month, Date monthDay) {
        Specification<ReportDay> specification = ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId").as(Long.class), userId));
            predicates.add(cb.lessThanOrEqualTo(root.get("reportDay").as(Date.class), month));
            predicates.add(cb.greaterThanOrEqualTo(root.get("reportDay").as(Date.class), monthDay));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        });
        return specification;
    }
}
