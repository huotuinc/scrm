package com.huotu.scrm.service.repository.report;

import com.huotu.scrm.service.entity.report.DayReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by hxh on 2017-07-11.
 */
@Repository
public interface DayReportRepository extends JpaRepository<DayReport, Long>, JpaSpecificationExecutor<DayReport> {


    /**
     * 查询某天某个商户下所有推广积分（降序）
     *
     * @param date 查询日期
     * @return
     */
    List<DayReport> findByReportDayAndCustomerIdOrderByExtensionScoreDesc(LocalDate date, Long customerId);


    /**
     * 查询某天某个商户所有关注人数（降序）
     *
     * @param date 查询日期
     * @return
     */
    List<DayReport> findByReportDayAndCustomerIdAndIsSalesmanTrueOrderByFollowNumDesc(LocalDate date, Long customerId);

    /**
     * 查询某天某个商户所有访客量（降序）
     *
     * @param date 查询日期
     * @return
     */
    List<DayReport> findByReportDayAndCustomerIdOrderByVisitorNumDesc(LocalDate date, Long customerId);

    /**
     * 查询某段时间内所有统计用户（去除重复用户）
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    @Query("select distinct t.userId from DayReport t where t.reportDay>=?1 and t.reportDay<=?2")
    List<Long> findByUserId(LocalDate minDate, LocalDate maxDate);

    /**
     * 根据用户和时间查询
     *
     * @param userId
     * @param date
     * @return
     */
    List<DayReport> findByUserIdAndReportDay(Long userId, LocalDate date);

    /**
     * 查询用户在某个时间点之前的所有记录
     *
     * @param userId
     * @param date
     * @return
     */
    List<DayReport> findByUserIdAndReportDayLessThanEqual(Long userId, LocalDate date);

}
