package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

/**
 * 名片关注记录表
 * Created by Administrator on 2017/7/12.
 */
public interface BusinessCardRecordReposity extends JpaRepository<BusinessCardRecord, Long> {

    /***
     * 根据商户Id和销售员Id和关注人id删除关注记录
     * @param customerId
     * @param salesmanId
     * @param followId
     */
    void deleteByCustomerIdAndUserIdAndFollowId(Long customerId, Long salesmanId, Long followId);

    /***
     * 获得指定商户id下的指定销售员Id的关注人数
     * @param customerId
     * @param salesmanId
     * @return
     */
    @Query("select count(u) from BusinessCardRecord u where u.customerId=?1 and u.userId=?2")
    Integer getNumberOfFollowerByCustomerIdAndUserId(Long customerId, Long salesmanId);

    /***
     * 检测是否关注了指定的销售员名片
     * @param customerId
     * @param salesmanId
     * @param followId
     * @return
     */
    Boolean existsByCustomerIdAndUserIdAndFollowId(Long customerId, Long salesmanId, Long followId);

    /***
     *
     * @param customerId
     * @param followId
     * @param userId
     * @return
     */
    Boolean existsByCustomerIdAndFollowIdAndUserIdNot(Long customerId, Long followId, Long userId);

    //根据用户ID查询关注人数(每天)
    @Query("select count (t) from BusinessCardRecord t where t.userId =?1 and t.followDate>=?2 and t.followDate<?3")
    int countByUserId(Long userId, LocalDateTime minDate, LocalDateTime maxDate);

}
