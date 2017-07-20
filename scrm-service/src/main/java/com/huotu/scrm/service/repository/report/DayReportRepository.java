package com.huotu.scrm.service.repository.report;

import com.huotu.scrm.service.entity.report.DayReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by hxh on 2017-07-11.
 */
@Repository
public interface DayReportRepository extends JpaRepository<DayReport, Long>, JpaSpecificationExecutor<DayReport> {


    /**
     * 查询某天所有推广积分（降序）
     *
     * @param date 查询日期
     * @return
     */
    List<DayReport> findByReportDayOrderByExtensionScoreDesc(LocalDate date);


    /**
     * 查询某天所有关注人数（降序）
     *
     * @param date 查询日期
     * @return
     */
    List<DayReport> findByReportDayOrderByFollowNumDesc(LocalDate date);

    /**
     * 查询某天所有访客量（降序）
     *
     * @param date 查询日期
     * @return
     */
    List<DayReport> findByReportDayOrderByVisitorNumDesc(LocalDate date);

    //查询所有数据（取出重复用户ID）
    @Query("select distinct t.userId from DayReport t where t.reportDay>=?1 and t.reportDay<=?2")
    List<Long> findByUserId(LocalDate minDate, LocalDate maxDate);

    DayReport findByUserIdAndReportDay(Long userId, LocalDate date);

    @Query("select t from DayReport t where t.userId=?1 and t.reportDay>=?2")
    List<DayReport> findByReportDay(Long userId, LocalDate date);

    @Query("select t from DayReport t where t.userId = ?1 and t.reportDay<=?2")
    List<DayReport> findAllFollowNum(Long userId, LocalDateTime date);

}
