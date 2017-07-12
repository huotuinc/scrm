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
    int getFollowCountByCustomerIdAndUserId(long customerId , long userId);

    /***
     * 删除关注信息
     * @param customerId
     * @param userId
     */
    void deleteByCustomerIdAndUserIdAndFollowId(long customerId , long userId , long followId );

}
