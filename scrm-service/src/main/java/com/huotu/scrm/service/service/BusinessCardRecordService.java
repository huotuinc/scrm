package com.huotu.scrm.service.service;

/**
 * Created by Administrator on 2017/7/12.
 */
public interface BusinessCardRecordService {

    /***
     * 获得指定销售员的关注者人数
     * @param customerId
     * @param userId
     * @return
     */
    int getFollowCountByCustomerIdAndUserId(Long customerId , Long userId);

    /***
     * 删除关注信息
     * @param customerId
     * @param userId
     */
    void deleteByCustomerIdAndUserIdAndFollowId(Long customerId , Long userId , Long followId );

    /***
     * 是否关注了指定的销售员名片
     * @param customerId
     * @param userId
     * @param followId
     * @return
     */
    boolean existsByCustomerIdAndUserIdAndFollowId(Long customerId , Long userId , Long followId);
}
