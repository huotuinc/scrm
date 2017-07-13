package com.huotu.scrm.service.service.Impl;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.report.ReportDay;
import com.huotu.scrm.service.repository.ReportDayRepository;
import com.huotu.scrm.service.service.ReportDayService;
import com.huotu.scrm.service.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * Created by hxh on 2017-07-11.
 */
@Service
public class ReportDayServiceImpl implements ReportDayService {

    @Autowired
    private ReportDayRepository reportDayRepository;

    private

    @Override
    @Transactional
    public ApiResult saveReportDay(long userId) {
        /*获取昨日日期*/
        Date date = DateUtil.getLastDay();
        ReportDay reportDay = new ReportDay();
        /*设置用户ID*/
        reportDay.setUserId(userId);
        /*设置商户号*/
        /*设置等级*/
        /*设置是否为销售员*/
        /*设置每日咨询转发量*/
        /*设置每日访客量*/
        /*设置每日预计积分*/
        /*设置每日被关注量(销售员特有)*/
        /*设置统计日期*/
        reportDay.setReportDay(date);
        /*保存数据*/
        reportDayRepository.save(reportDay);
        /*设置每日访客排名*/
        int visitorRanking = visitorRanking(userId, date);
        reportDay.setVisitorRanking(visitorRanking);
        /*设置每日积分排名*/
        int scoreRanking = scoreRanking(userId, date);
        reportDay.setScoreRanking(scoreRanking);
        /*设置每日关注排名（销售员特有）*/
        if (reportDay.isSalesman()) {
            int followRanking = followRanking(userId, date);
            reportDay.setFollowRanking(followRanking);
        } else {
            reportDay.setFollowRanking(-1);
        }
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS, "保存每日记录信息成功!", null);
    }

    //    public int saveVisitorNum(int userId) {
//        return 0;
//    }
//
    //    public int saveFollowNum(int userId) {
//        return 0;
//    }
//
    //    public int saveExtensionScore(int userId) {
//        return 0;
//    }
//
    public int visitorRanking(long userId, Date date) {
        List<ReportDay> sortAll = reportDayRepository.findOrderByVisitorNum(date);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }

    public int scoreRanking(long userId, Date date) {
        List<ReportDay> sortAll = reportDayRepository.findOrderByExtensionScore(date);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }

    public int followRanking(long userId, Date date) {
        List<ReportDay> sortAll = reportDayRepository.findOrderByFollowNum(date);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }
}
