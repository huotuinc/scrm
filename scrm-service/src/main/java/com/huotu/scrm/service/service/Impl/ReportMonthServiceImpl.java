package com.huotu.scrm.service.service.Impl;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.report.ReportDay;
import com.huotu.scrm.service.entity.report.ReportMonth;
import com.huotu.scrm.service.repository.ReportDayRepository;
import com.huotu.scrm.service.service.ReportMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hxh on 2017-07-12.
 */
@Service
public class ReportMonthServiceImpl implements ReportMonthService {

    @Autowired
    private ReportDayRepository reportDayRepository;

    @Override
    @Transactional
    public ApiResult saveReportMonth(int userId) {
        /*得到当前月份*/
        Date month = getMonth(0);
        ReportMonth reportMonth = new ReportMonth();
        /*设置用户ID*/
        reportMonth.setUserId(userId);
        /*设置商户ID*/
        /*设置等级*/
         /*设置是否为销售员*/
        /*设置每月咨询转发量*/
        ApiResult forwardNumResult = getForwardNum(userId, month);
        if (forwardNumResult.getCode() == 2000) {
            reportMonth.setFollowNum((Integer) forwardNumResult.getData());
        } else {
            return forwardNumResult;
        }
        /*设置每月访客量*/
        ApiResult visitorNumResult = getVisitorNum(userId, month);
        if (visitorNumResult.getCode() == 2000) {
            reportMonth.setVisitorNum((Integer) visitorNumResult.getData());
        } else {
            return visitorNumResult;
        }
        /*设置每月推广积分*/

        /*设置每月被关注量(销售员特有)*/
        /*设置统计月份*/
        reportMonth.setReportMonth(month);
        return null;
    }

    /**
     * 统计每月资讯转发量
     *
     * @param userId 用户ID
     * @param date  统计时间
     * @return
     */
    ApiResult getForwardNum(int userId, Date date) {
        try {
            List<ReportDay> forwardNumList = reportDayRepository.findByUserIdAndReportDay(userId, date);
            int num = 0;
            for (ReportDay reportDay : forwardNumList) {
                num += reportDay.getForwardNum();
            }
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS, num);
        } catch (Exception e) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, e.getMessage(), null);
        }
    }

    /**
     *
     * @param userId
     * @param date
     * @return
     */
    ApiResult getVisitorNum(int userId, Date date) {
        try {
            List<ReportDay> forwardNumList = reportDayRepository.findByUserIdAndReportDay(userId, date);
            int num = 0;
            for (ReportDay reportDay : forwardNumList) {
                num += reportDay.getVisitorNum();
            }
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS, num);
        } catch (Exception e) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, e.getMessage(), null);
        }
    }


    /**
     * 获取月份
     *
     * @param n 0：当前月 1：上个月（以此类推）
     * @return
     */
    public Date getMonth(int n) {
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(new Date());//设置当前日期
        calendar.add(Calendar.MONTH, -n);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
