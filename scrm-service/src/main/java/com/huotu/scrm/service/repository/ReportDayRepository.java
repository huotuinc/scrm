package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.report.ReportDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by hxh on 2017-07-11.
 */
public interface ReportDayRepository extends JpaRepository<ReportDay, Long>,JpaSpecificationExecutor<ReportDay> {

    @Query("select t from ReportDay t where t.reportDay = ?1 order by t.extensionScore")
    List<ReportDay> findOrderByExtensionScore(Date date);

    @Query("select t from ReportDay  t where t.reportDay=?1 and t.isSalesman = true order by t.followNum")
    List<ReportDay> findOrderByFollowNum(Date date);

    @Query("select t from ReportDay  t where t.reportDay=?1 order by t.visitorNum")
    List<ReportDay> findOrderByVisitorNum(Date date);


}
