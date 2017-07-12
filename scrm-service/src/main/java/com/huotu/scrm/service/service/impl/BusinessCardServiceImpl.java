package com.huotu.scrm.service.service.impl;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.User;
import com.huotu.scrm.service.model.UserBusinessCard;
import com.huotu.scrm.service.repository.BusinessCardReposity;
import com.huotu.scrm.service.repository.UserReposity;
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
    UserReposity hotUserBaseInfoReposity;

    public UserBusinessCard getUserBusinessCard(long userId, long customerId) {

        BusinessCard businessCard = businessCardReposity.getByUserIdAndCustomerId(userId , customerId);
        User user = hotUserBaseInfoReposity.getByIdAndCustomerId(userId , customerId);
        UserBusinessCard userBusinessCard = new UserBusinessCard();
        userBusinessCard.setBusinessCard(businessCard);
        userBusinessCard.setUser(user);

        return userBusinessCard;
    }

    public UserBusinessCard updateBusinessCard(UserBusinessCard userBusinessCard) {

        userBusinessCard.setBusinessCard( businessCardReposity.saveAndFlush(userBusinessCard.getBusinessCard()));
        return userBusinessCard;
    }

    @Override
    public BusinessCard updateBusinessCard(long customerId , long userId , int type , String text) {

        BusinessCard model = businessCardReposity.getByUserIdAndCustomerId( userId , customerId );
        if(model==null){
            model = new BusinessCard();
        }
        if(type==1){
            model.setAvatar(text);
        }else if(type == 2){
            model.setCompanyName(text);
        }else if(type==3){
            model.setJob(text);
        }else if(type==4){
            model.setCompanyName(text);
        }else if(type==5){
            model.setTel(text);
        }else if(type==6){
            model.setQq(text);
        }else if(type==7){
            model.setCompanyAddress(text);
        }

        model = businessCardReposity.saveAndFlush(model);

        return model;
    }
}
