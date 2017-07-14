package com.huotu.scrm.service.service.impl;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.repository.BusinessCardRecordReposity;
import com.huotu.scrm.service.repository.BusinessCardReposity;
import com.huotu.scrm.service.repository.UserReposity;
import com.huotu.scrm.service.service.BusinessCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jinxiangdong on 2017/7/11.
 */
@Service
public class BusinessCardServiceImpl implements BusinessCardService{
    @Autowired
    BusinessCardReposity businessCardReposity;
    @Autowired
    BusinessCardRecordReposity businessCardRecordReposity;
    @Autowired
    UserReposity userReposity;

    public BusinessCard getBusinessCard(Long salesmanId, Long customerId) {
        BusinessCard businessCard = businessCardReposity.getByUserIdAndCustomerId(salesmanId , customerId);
        return businessCard;
    }

    public SalesmanBusinessCard getSalesmanBusinessCard(Long salesmanId , Long customerId , Long followerId ) {
        BusinessCard businessCard = businessCardReposity.getByUserIdAndCustomerId(salesmanId , customerId);
        User user = userReposity.getByIdAndCustomerId(salesmanId , customerId);
        Integer numberOfFollowers = businessCardRecordReposity.getNumberOfFollowerByCustomerIdAndUserId(customerId, salesmanId);
        Boolean isFollowed = businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId(customerId,salesmanId,followerId);
        SalesmanBusinessCard userBusinessCard = new SalesmanBusinessCard();
        userBusinessCard.setBusinessCard(businessCard);
        userBusinessCard.setSalesman(user);
        userBusinessCard.setNumberOfFollowers( numberOfFollowers);
        userBusinessCard.setFollowerId(followerId);
        userBusinessCard.setIsFollowed(isFollowed);

        return userBusinessCard;
    }


    public BusinessCard updateBusinessCard(Long customerId , Long userId , BusinessCardUpdateTypeEnum type , String text) {

        BusinessCard model = businessCardReposity.getByUserIdAndCustomerId( userId , customerId );
        if(model==null){
            model = new BusinessCard();
            model.setCustomerId( customerId);
            model.setUserId(userId);
        }
        if(type== BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR){
            model.setAvatar(text);
        }else if(type == BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_COMPANYNAME){
            model.setCompanyName(text);
        }else if(type== BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_JOB){
            model.setJob(text);
        }else if(type== BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_TEL){
            model.setTel(text);
        }else if(type== BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_QQ){
            model.setQq(text);
        }else if(type== BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_COMPANYADDRESS){
            model.setCompanyAddress(text);
        }else if(type == BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_EMAIL){
            model.setEmail(text);
        }

        model = businessCardReposity.saveAndFlush(model);

        return model;
    }
}
