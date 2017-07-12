package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 名片
 * Created by Administrator on 2017/7/11.
 */
public interface BusinessCardReposity extends JpaRepository<BusinessCard , Long>{

    BusinessCard getByUserIdAndCustomerId( Long userId , Long customerId);


}
