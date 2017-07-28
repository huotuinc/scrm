package com.huotu.scrm.service.service.businesscard;

import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;

/**
 * Created by Jinxiangdong on 2017/7/12.
 */
public interface BusinessCardRecordService {

    /***
     * 获得指定销售员的关注者人数
     * @param customerId
     * @param userId
     * @return
     */
    int countNumberOfFollowerByCustomerIdAndUserId(Long customerId, Long userId);

    /***
     * 删除关注信息
     * @param customerId
     * @param userId
     */
    void deleteByCustomerIdAndUserIdAndFollowId(Long customerId, Long userId, Long followId);

    /***
     * 是否关注了指定的销售员名片
     * @param customerId
     * @param userId
     * @param followId
     * @return
     */
    boolean existsByCustomerIdAndUserIdAndFollowId(Long customerId, Long userId, Long followId);

    /***
     * 判断用户是否关注过了除了指定的销售员id的其他销售员
     * @param customerId
     * @param followerId
     * @return
     */
    boolean existsByCustomerIdAndFollowerIdNotInSalesmanId(Long customerId, Long followerId, Long salesmanId);

    /***
     * 新增关注记录
     * @param businessCardRecord
     * @return
     */
    BusinessCardRecord insert(BusinessCardRecord businessCardRecord);
}
