package com.huotu.scrm.service.repository.report;

import com.huotu.scrm.service.entity.report.MonthReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by hxh on 2017-07-12.
 */
public interface MonthReportRepository extends JpaRepository<MonthReport, Long>, JpaSpecificationExecutor<MonthReport> {

    /**
     * 根据用户和某个月份查询
     *
     * @param userId 用户ID
     * @param month  统计月份
     * @return
     */
    MonthReport findByUserIdAndReportMonth(long userId, LocalDate month);

    /**
     * 查询用户最高月的访客量
     *
     * @param userId 用户ID
     * @return
     */
    @Query("select max(t.visitorNum) from MonthReport t where t.userId = ?1")
    int findMaxMonthVisitorNum(Long userId);

    /**
     * 查询用户最高月关注排名（销售员特有）
     *
     * @param userId 用户ID
     * @return
     */
    @Query("select min(t.followRanking) from MonthReport t where t.userId = ?1 and t.followRanking>0")
    int findMaxMonthFollowNumRanking(Long userId);

    /**
     * 查询用户最高月的积分排名
     *
     * @param userId 用户ID
     * @return
     */
    @Query("select min(t.scoreRanking) from MonthReport t where t.userId=?1 and t.scoreRanking>0")
    int findMaxScoreRanking(Long userId);

    /**
     * 查询用户某个月份之前的所有数据
     *
     * @param userId 用户ID
     * @param month  统计月份
     * @return
     */
    List<MonthReport> findByUserIdAndReportMonthGreaterThanEqual(Long userId, LocalDate month);

    /**
     * 查询某个月份下的所有商户
     *
     * @param month 统计月份
     * @return
     */
    @Query("select distinct t.customerId from MonthReport  t where t.reportMonth=?1")
    List<Long> findCustomerByReportDay(LocalDate month);

    /**
     * 查询某个用户所有数据
     *
     * @param userId 用户ID
     * @return
     */
    List<MonthReport> findByUserId(Long userId);
}
