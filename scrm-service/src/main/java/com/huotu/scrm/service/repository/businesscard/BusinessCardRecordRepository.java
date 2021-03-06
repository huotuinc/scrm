package com.huotu.scrm.service.repository.businesscard;

import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecordPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 名片关注记录表
 * Created by Jinxiangdong on 2017/7/12.
 */
public interface BusinessCardRecordRepository extends JpaRepository<BusinessCardRecord, BusinessCardRecordPK> {
    /***
     * 根据商户Id和销售员Id和关注人id删除关注记录
     * @param customerId
     * @param salesmanId
     * @param followId
     */
    int deleteByCustomerIdAndUserIdAndFollowId(Long customerId, Long salesmanId, Long followId);

    /***
     * 获得指定商户id下的指定销售员Id的关注人数
     * @param customerId
     * @param salesmanId
     * @return
     */
    int countNumberOfFollowerByCustomerIdAndUserId(Long customerId, Long salesmanId);

    /***
     * 检测是否关注了指定的销售员名片
     * @param customerId
     * @param salesmanId
     * @param followId
     * @return
     */
    int countByCustomerIdAndUserIdAndFollowId(Long customerId, Long salesmanId, Long followId);

    /***
     * 检测是否存在关注了除了指定的销售员名片的其他名片
     * @param customerId
     * @param followId
     * @param userId
     * @return
     */
    int countByCustomerIdAndFollowIdAndUserIdNot(Long customerId, Long followId, Long userId);

    /**
     * 查询用户被关注关注人数
     *
     * @param userId
     * @return
     */
    int countByUserId(Long userId);

    /**
     * 查询某个时间之前的所有关注人数
     *
     * @param userId
     * @param date
     * @return
     */
    int countByUserIdAndFollowDateLessThanEqual(Long userId, LocalDateTime date);

    /***
     * 查询我关注的名片列表
     * @param customerId
     * @param followId
     * @return
     */
    List<BusinessCardRecord> findByCustomerIdAndFollowId(Long customerId, Long followId);

    /**
     * 查询所有商户
     * @return
     */
    @Query("select distinct (t.customerId) from BusinessCardRecord t ")
    List<Long> findCustomerList();

    /**
     * 查询某个商户下所有用户
     * @param customerId
     * @return
     */
    @Query("select distinct (t.userId) from BusinessCardRecord t where t.customerId=?1")
    List<Long> findUserIdList(Long customerId);


}
