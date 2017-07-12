package com.huotu.scrm.service.service;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.model.UserBusinessCard;

/**
 * Created by Administrator on 2017/7/11.
 */
public interface BusinessCardService {
    /***
     * 通过UserId和CustomerId获得名片信息和用户信息
     * @param userId
     * @param customerId
     * @return
     */
    UserBusinessCard getUserBusinessCard( long userId , long customerId);

    /***
     * 更新名片信息
     * @param userBusinessCard
     * @return
     */
    UserBusinessCard updateBusinessCard(UserBusinessCard userBusinessCard);

    /***
     *
     * @param customerId
     * @param userId
     * @param type
     * @param text
     * @return
     */
    BusinessCard updateBusinessCard(long customerId , long userId , int type , String text);

}
