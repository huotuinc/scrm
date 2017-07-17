package com.huotu.scrm.service.repository.report;

import com.huotu.scrm.service.entity.report.DayReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by hxh on 2017-07-11.
 */
public interface DayReportRepository extends JpaRepository<DayReport, Long>, JpaSpecificationExecutor<DayReport> {

    //查询某天所有推广积分（降序）
    @Query("select t from DayReport t where t.reportDay >?1 and t.reportDay<?2 order by t.extensionScore desc ")
    List<DayReport> findOrderByExtensionScore(LocalDate dateMin, LocalDate dateMax);

    //查询某天所有关注人数（降序）
    @Query("select t from DayReport  t where t.reportDay>?1 and t.reportDay<?2 and t.isSalesman = true order by t.followNum desc ")
    List<DayReport> findOrderByFollowNum(LocalDate dateMin, LocalDate dateMax);

    //查询某天所有访客量（降序）
    @Query("select t from DayReport  t where t.reportDay>?1 and t.reportDay<?2 order by t.visitorNum desc ")
    List<DayReport> findOrderByVisitorNum(LocalDate dateMin, LocalDate dateMax);

    List<DayReport> findByReportDay(LocalDate date);

    //查询所有数据（取出重复用户ID）
    @Query("select distinct t.userId from DayReport t ")
    List<Long> findByUserId();


}
