package com.huotu.scrm.service.service.statisticsinfo.impl;

import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.model.statisticinfo.SearchCondition;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.service.report.DayReportService;
import com.huotu.scrm.service.service.statisticsinfo.StatisticsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxh on 2017-08-28.
 */
@Service
public class StatisticsInfoServiceImpl implements StatisticsInfoService{
    @Autowired
    private DayReportRepository dayReportRepository;
    @Autowired
    private InfoBrowseRepository infoBrowseRepository;
    @Autowired
    private DayReportService dayReportService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessCardRecordRepository businessCardRecordRepository;
    @Autowired
    private UserLevelRepository userLevelRepository;
    @Override
    public Page<DayReport> getDayReportList(SearchCondition searchCondition,int pageIndex) {
        Sort sort = new Sort(Sort.Direction.ASC, "reportDay");
        Pageable pageable = new PageRequest(pageIndex-1, Constant.PAGE_SIZE,sort);
        Specification<DayReport> specification = getSpecification(searchCondition);
        return dayReportRepository.findAll(specification,pageable);
    }

    @Override
    public void againStatistic(Long userId ,LocalDate date) {
        User user = userRepository.findOne(userId);
        UserLevel userLevel = userLevelRepository.findByIdAndCustomerId(user.getLevelId(), user.getCustomerId());
        LocalDateTime beginTime = date.atStartOfDay();
        LocalDateTime endTime = beginTime.plusDays(1);
        List<InfoBrowse> infoBrowseList = infoBrowseRepository.findForwardNumBySourceUserId(beginTime, endTime, user.getId());
        int forwardNum = infoBrowseList.size();
        //昨日访客量（uv）
        int visitorNum = infoBrowseRepository.countBySourceUserIdAndBrowseTimeBetween(user.getId(), beginTime, endTime);
        int dayScore = dayReportService.getEstimateScore(user, beginTime, endTime);
        DayReport report = new DayReport();
        report.setSalesman(userLevel.isSalesman());
        int followNum = report.isSalesman() ? businessCardRecordRepository.countByUserId(user.getId()) : 0;
        DayReport dayReport = dayReportRepository.findByUserIdAndReportDay(userId, date);
        report.setFollowNum(followNum);
        report.setUserId(user.getId());
        report.setCustomerId(user.getCustomerId());
        report.setLevelId(user.getLevelId());
        report.setForwardNum(forwardNum);
        report.setVisitorNum(visitorNum);
        report.setExtensionScore(dayScore);
        report.setReportDay(date);
        report.setScoreRanking(dayReport.getScoreRanking());
        report.setVisitorRanking(dayReport.getVisitorRanking());
        report.setFollowRanking(dayReport.getFollowRanking());
        dayReportRepository.delete(dayReport);
        dayReportRepository.save(report);
    }

    private Specification<DayReport> getSpecification(SearchCondition searchCondition) {
        List<Predicate> predicates = new ArrayList<>();
        return ((root, query, cb) -> {
            if(searchCondition.getUserId()!=null){
                predicates.add(cb.equal(root.get("userId").as(Long.class),searchCondition.getUserId()));
            }
            if(searchCondition.getStatisticsStartDate()!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("reportDay").as(LocalDate.class), searchCondition.getStatisticsStartDate()));
            }
            if(searchCondition.getStatisticsEndDate()!=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("reportDay").as(LocalDate.class), searchCondition.getStatisticsEndDate()));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }
}
