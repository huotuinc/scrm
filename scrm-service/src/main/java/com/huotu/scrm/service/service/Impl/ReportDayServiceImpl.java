package com.huotu.scrm.service.service.Impl;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.report.ReportDay;
import com.huotu.scrm.service.repository.ReportDayRepository;
import com.huotu.scrm.service.service.ReportDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by hxh on 2017-07-11.
 */
@Service
public class ReportDayServiceImpl implements ReportDayService {

    @Autowired
    private ReportDayRepository reportDayRepository;

    @Override
    @Transactional
    public ApiResult saveReportDay(int userId) {
        Date date = getDay();
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
        ApiResult visitorRankingResult = visitorRanking(userId, date);
        if (visitorRankingResult.getCode() == 2000) {
            reportDay.setVisitorRanking((Integer) visitorRankingResult.getData());
        }
        /*设置每日积分排名*/
        ApiResult scoreRankingResult = scoreRanking(userId, date);
        if (scoreRankingResult.getCode() == 2000) {
            reportDay.setScoreRanking((Integer) scoreRankingResult.getData());
        } else {
            return scoreRankingResult;
        }
        /*设置每日关注排名（销售员特有）*/
        if (reportDay.isSalesman()) {
            ApiResult followRankingResult = followRanking(userId, date);
            if (followRankingResult.getCode() == 2000) {
                reportDay.setFollowRanking((Integer) followRankingResult.getData());
            } else {
                return followRankingResult;
            }
        } else {
            reportDay.setFollowRanking(0);
        }
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS, "保存每日记录信息成功!",null);
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
    public ApiResult visitorRanking(int userId, Date date) {

        List<ReportDay> sortAll = reportDayRepository.findOrderByVisitorNum(date);
        /*默认只排200名*/
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
                return ApiResult.resultWith(ResultCodeEnum.SUCCESS,i + 1);
            }
        }
        /*排名异常*/
        return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, "访客排名异常", null);
    }

    //

    public ApiResult scoreRanking(int userId, Date date) {
        List<ReportDay> sortAll = reportDayRepository.findOrderByExtensionScoce(date);
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
                return ApiResult.resultWith(ResultCodeEnum.SUCCESS, i + 1);
            }
        }
        /*预计积分排名异常*/
        return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, "推广积分排名异常", null);
    }

    public ApiResult followRanking(int userId, Date date) {
        List<ReportDay> sortAll = reportDayRepository.findOrderByFollowNum(date);
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
                return ApiResult.resultWith(ResultCodeEnum.SUCCESS, i + 1);
            }
        }
        /*关注量排名异常*/
        return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, "关注排名异常", null);
    }

    /**
     * 获取当天时间
     *
     * @return
     */
    public Date getDay() {
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(new Date());//设置当前日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

}
