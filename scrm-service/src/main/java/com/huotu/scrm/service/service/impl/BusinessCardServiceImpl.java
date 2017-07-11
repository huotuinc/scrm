package com.huotu.scrm.service.service.impl;

import com.huotu.scrm.service.entity.BusinessCard;
import com.huotu.scrm.service.entity.HotUserBaseInfo;
import com.huotu.scrm.service.model.UserBusinessCard;
import com.huotu.scrm.service.repository.BusinessCardReposity;
import com.huotu.scrm.service.repository.HotUserBaseInfoReposity;
import com.huotu.scrm.service.service.BusinessCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/11.
 */
@Service
public class BusinessCardServiceImpl implements BusinessCardService{
    @Autowired
    BusinessCardReposity businessCardReposity;
    @Autowired
    HotUserBaseInfoReposity hotUserBaseInfoReposity;

    public UserBusinessCard getUserBusinessCard(int userId, int customerId) {

        BusinessCard businessCard = businessCardReposity.getByUserIdAndCustomerId(userId , customerId);
        HotUserBaseInfo hotUserBaseInfo = hotUserBaseInfoReposity.getByUserIdAndCustomerId(userId , customerId);
        UserBusinessCard userBusinessCard = new UserBusinessCard();
        userBusinessCard.setBusinessCard(businessCard);
        userBusinessCard.setHotUserBaseInfo(hotUserBaseInfo);

        return userBusinessCard;
    }

    public UserBusinessCard update(UserBusinessCard userBusinessCard) {

        userBusinessCard.setBusinessCard( businessCardReposity.saveAndFlush(userBusinessCard.getBusinessCard()));
        return userBusinessCard;
    }
}
