package com.huotu.scrm.service.repository.report;

import com.huotu.scrm.service.entity.report.DayReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by hxh on 2017-07-11.
 */
public interface ReportDayRepository extends JpaRepository<DayReport, Long>, JpaSpecificationExecutor<DayReport> {

    //查询某天所有推广积分（降序）
    @Query("select t from DayReport t where t.reportDay = ?1 order by t.extensionScore")
    List<DayReport> findOrderByExtensionScore(Date date);

    //查询某天所有关注人数（降序）
    @Query("select t from DayReport  t where t.reportDay=?1 and t.isSalesman = true order by t.followNum")
    List<DayReport> findOrderByFollowNum(Date date);

    //查询某天所有访客量（降序）
    @Query("select t from DayReport  t where t.reportDay=?1 order by t.visitorNum")
    List<DayReport> findOrderByVisitorNum(Date date);


}
