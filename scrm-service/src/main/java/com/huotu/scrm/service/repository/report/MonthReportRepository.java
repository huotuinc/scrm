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

    /**
     * 根据某个商户下某个月份的积分排名（倒序）
     *
     * @param date
     * @param customerId
     * @return
     */
    List<MonthReport> findByReportMonthAndCustomerIdOrderByExtensionScoreDesc(LocalDate date, Long customerId);

    /**
     * 根据某个商户下某个月份销售员的关注排名（倒序）
     *
     * @param date
     * @param customerId
     * @return
     */

    List<MonthReport> findByReportMonthAndCustomerIdAndIsSalesmanTrueOrderByFollowNumDesc(LocalDate date, Long customerId);

    /**
     * 根据用户和某个月份查询
     *
     * @param userId
     * @param lastFirstDay
     * @return
     */
    List<MonthReport> findByUserIdAndReportMonth(long userId, LocalDate lastFirstDay);

    /**
     * 查询用户最高月的访客量排名
     *
     * @param userId
     * @return
     */
    @Query("select max(t.visitorNum) from MonthReport t where t.userId = ?1")
    int findMaxMonthVisitorNum(Long userId);

    /**
     * 查询用户最高月关注排名（销售员特有）
     *
     * @param userId
     * @return
     */
    @Query("select min(t.followRanking) from MonthReport t where t.userId = ?1")
    int findMaxMonthFollowNumRanking(Long userId);

    /**
     * 查询用户最高月的积分排名
     *
     * @param userId
     * @return
     */
    @Query("select min(t.scoreRanking) from MonthReport t where t.userId=?1")
    int findMaxScoreRanking(Long userId);

    /**
     * 查询用户某个月份之前的所有数据
     *
     * @param userId
     * @param monthReport
     * @return
     */
    List<MonthReport> findByUserIdAndReportMonthGreaterThanEqual(Long userId, LocalDate monthReport);

}
