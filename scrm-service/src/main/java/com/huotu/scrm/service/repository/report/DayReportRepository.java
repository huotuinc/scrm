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

    /**
     * 查询某段时间内所有统计用户（去除重复用户）
     *
     * @param minDate 起始时间
     * @param maxDate 结束时间
     * @return
     */
    @Query("select distinct t.userId from DayReport t where t.reportDay>=?1 and t.reportDay<=?2")
    List<Long> findByUserId(LocalDate minDate, LocalDate maxDate);

    /**
     * 根据用户和时间查询
     *
     * @param userId 用户ID
     * @param date   统计日期
     * @return
     */
    DayReport findByUserIdAndReportDay(Long userId, LocalDate date);

    /**
     * 查询某天日期下的所有商户
     *
     * @param date 统计日期
     * @return
     */
    @Query("select distinct t.customerId from DayReport  t where t.reportDay=?1")
    List<Long> findCustomerByReportDay(LocalDate date);

}
