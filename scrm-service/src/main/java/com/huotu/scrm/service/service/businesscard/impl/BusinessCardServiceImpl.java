package com.huotu.scrm.service.service.businesscard.impl;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jinxiangdong on 2017/7/11.
 */
@Service
@Transactional
public class BusinessCardServiceImpl implements BusinessCardService {
    @Autowired
    BusinessCardRepository businessCardReposity;
    @Autowired
    BusinessCardRecordRepository businessCardRecordReposity;
    @Autowired
    UserRepository userRepository;

    public BusinessCard getBusinessCard(Long salesmanId, Long customerId) {
        BusinessCard businessCard = businessCardReposity.getByUserIdAndCustomerId(salesmanId, customerId);
        return businessCard;
    }

    public SalesmanBusinessCard getSalesmanBusinessCard(Long customerId, Long salesmanId, Long followerId) {
        BusinessCard businessCard = businessCardReposity.getByUserIdAndCustomerId(salesmanId, customerId);
        User user = userRepository.getByIdAndCustomerId(salesmanId, customerId);
        Integer numberOfFollowers = businessCardRecordReposity.getNumberOfFollowerByCustomerIdAndUserId(customerId, salesmanId);
        Boolean isFollowed = businessCardRecordReposity.existsByCustomerIdAndUserIdAndFollowId(customerId, salesmanId, followerId);
        SalesmanBusinessCard userBusinessCard = new SalesmanBusinessCard();
        userBusinessCard.setBusinessCard(businessCard);
        userBusinessCard.setSalesman(user);
        userBusinessCard.setNumberOfFollowers(numberOfFollowers);
        userBusinessCard.setFollowerId(followerId);
        userBusinessCard.setIsFollowed(isFollowed);

        return userBusinessCard;
    }


    public BusinessCard updateBusinessCard(Long customerId, Long userId, BusinessCardUpdateTypeEnum type, String text) {

        BusinessCard model = businessCardReposity.getByUserIdAndCustomerId(userId, customerId);
        if (model == null) {
            model = new BusinessCard();
            model.setCustomerId(customerId);
            model.setUserId(userId);
        }
        if (type == BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR) {
            model.setAvatar(text);
        } else if (type == BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_COMPANYNAME) {
            model.setCompanyName(text);
        } else if (type == BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_JOB) {
            model.setJob(text);
        } else if (type == BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_TEL) {
            model.setTel(text);
        } else if (type == BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_QQ) {
            model.setQq(text);
        } else if (type == BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_COMPANYADDRESS) {
            model.setCompanyAddress(text);
        } else if (type == BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_EMAIL) {
            model.setEmail(text);
        }

        model = businessCardReposity.saveAndFlush(model);

        return model;
    }

    public List<SalesmanBusinessCard> getMyBusinessCardList(Long customerId, Long userId) {
        List<BusinessCardRecord> list = businessCardRecordReposity.findByCustomerIdAndFollowId(customerId, userId);
        List<SalesmanBusinessCard> myBusinessCards = new ArrayList<>();
        if (list.size() > 0) {
            for (BusinessCardRecord item : list) {
                SalesmanBusinessCard salesman = this.getSalesmanBusinessCard(customerId, item.getUserId(), item.getFollowId());
                myBusinessCards.add(salesman);
            }
        }
        return myBusinessCards;
    }
}