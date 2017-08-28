package com.huotu.scrm.service.service.statisticsinfo.impl;

import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.model.statisticinfo.SearchCondition;
import com.huotu.scrm.service.repository.report.DayReportRepository;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxh on 2017-08-28.
 */
@Service
public class StatisticsInfoServiceImpl implements StatisticsInfoService{
    @Autowired
    private DayReportRepository dayReportRepository;
    @Override
    public Page<DayReport> getDayReportList(SearchCondition searchCondition,int pageIndex) {
        Sort sort = new Sort(Sort.Direction.ASC, "reportDay");
        Pageable pageable = new PageRequest(pageIndex-1, Constant.PAGE_SIZE,sort);
        Specification<DayReport> specification = getSpecification(searchCondition);
        return dayReportRepository.findAll(specification,pageable);
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
