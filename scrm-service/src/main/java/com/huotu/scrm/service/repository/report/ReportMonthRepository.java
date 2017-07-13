package com.huotu.scrm.service.repository.report;

import com.huotu.scrm.service.entity.report.MonthReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by hxh on 2017-07-12.
 */
public interface ReportMonthRepository extends JpaRepository<MonthReport, Long> {
    @Query("select t from MonthReport t where t.reportMonth = ?1 order by t.extensionScore")
    List<MonthReport> findOrderByExtensionScore(Date date);

    @Query("select t from MonthReport  t where t.reportMonth=?1 and t.isSalesman = true order by t.followNum")
    List<MonthReport> findOrderByFollowNum(Date date);

}
