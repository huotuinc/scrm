package com.huotu.scrm.service.repository.report;

import com.huotu.scrm.service.entity.report.MonthReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by hxh on 2017-07-12.
 */
public interface MonthReportRepository extends JpaRepository<MonthReport, Long> {

    List<MonthReport> findByReportMonthOrderByExtensionScoreDesc(LocalDate date);

    @Query("select t from MonthReport  t where t.reportMonth=?1 and t.isSalesman = true order by t.followNum desc ")
    List<MonthReport> findOrderByFollowNum(LocalDate date);
    
    MonthReport findByUserIdAndReportMonth(long userId, LocalDate lastFirstDay);


}
