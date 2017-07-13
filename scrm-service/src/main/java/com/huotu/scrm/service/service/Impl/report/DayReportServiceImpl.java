package com.huotu.scrm.service.service.Impl.report;

import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.repository.InfoBrowseRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.ReportDayRepository;
import com.huotu.scrm.service.service.DayReportService;
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
public class DayReportServiceImpl implements DayReportService {

    @Autowired
    private ReportDayRepository reportDayRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private InfoBrowseRepository infoBrowseRepository;

    @Override
    @Transactional
    public void saveReportDay(Long userId) {
        User user = userRepository.findOne(userId);
        //获取昨日日期
        Date date = DateUtil.getLastDay();
        DayReport reportDay = new DayReport();
        //设置用户ID
        reportDay.setUserId(userId);
        //设置商户号
        reportDay.setCustomerId(user.getCustomerId());
        //设置等级
        reportDay.setLevelId(user.getLevelId());
        //设置是否为销售员
        UserLevel userLevel = userLevelRepository.findByLevelAndCustomerId(user.getLevelId(), user.getCustomerId());
        reportDay.setSalesman(userLevel.isSalesman());
        //设置每日咨询转发量
        int forwardNumBySourceUserId = infoBrowseRepository.findForwardNumBySourceUserId(userId);
        reportDay.setForwardNum(forwardNumBySourceUserId);
        //设置每日访客量
        long countBySourceUserId = infoBrowseRepository.countBySourceUserId(userId);
        reportDay.setVisitorNum((int) countBySourceUserId);
        //设置每日预计积分()

        //设置每日被关注量(销售员特有)
        //设置统计日期
        reportDay.setReportDay(date);
        //保存数据
        reportDayRepository.save(reportDay);
        //设置每日访客排名
        int visitorRanking = visitorRanking(userId, date);
        reportDay.setVisitorRanking(visitorRanking);
        //设置每日积分排名
        int scoreRanking = scoreRanking(userId, date);
        reportDay.setScoreRanking(scoreRanking);
        //设置每日关注排名（销售员特有）
        if (reportDay.isSalesman()) {
            int followRanking = followRanking(userId, date);
            reportDay.setFollowRanking(followRanking);
        } else {
            reportDay.setFollowRanking(-1);
        }
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
    public int visitorRanking(Long userId, Date date) {
        List<DayReport> sortAll = reportDayRepository.findOrderByVisitorNum(date);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }

    public int scoreRanking(Long userId, Date date) {
        List<DayReport> sortAll = reportDayRepository.findOrderByExtensionScore(date);
        int ranking = 0;
        for (int i = 0; i < sortAll.size(); i++) {
            if (sortAll.get(i).getUserId() == userId) {
                ranking = i + 1;
                break;
            }
        }
        return ranking;
    }

    public int followRanking(Long userId, Date date) {
        List<DayReport> sortAll = reportDayRepository.findOrderByFollowNum(date);
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
