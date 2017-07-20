package com.huotu.scrm.service.service.businesscard;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;

import java.util.List;

/**
 * Created by Jinxiangdong on 2017/7/11.
 */
public interface BusinessCardService {

    /***
     * 获得销售员名片信息
     * @param salesmanId
     * @param customerId
     * @return
     */
    BusinessCard getBusinessCard(Long salesmanId, Long customerId);

    /***
     * 通过salesmanId和customerId和关注者d获得名片信息和用户信息
     * @param salesmanId 销售员Id
     * @param customerId 商户Id
     * @param followerId 关注者Id
     * @return
     */
    SalesmanBusinessCard getSalesmanBusinessCard(Long customerId, Long salesmanId, Long followerId);

    /***
     * 按照类型更新名片信息
     * @param customerId
     * @param userId
     * @param type
     * @param text
     * @return
     */
    BusinessCard updateBusinessCard(Long customerId, Long userId, BusinessCardUpdateTypeEnum type, String text);

    /**
     * 获得我的名片夹
     *
     * @param customerId
     * @param userId
     * @return
     */
    List<SalesmanBusinessCard> getMyBusinessCardList(Long customerId, Long userId);
}
