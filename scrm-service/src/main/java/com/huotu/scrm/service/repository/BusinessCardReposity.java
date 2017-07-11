package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.BusinessCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 名片
 * Created by Administrator on 2017/7/11.
 */
public interface BusinessCardReposity extends JpaRepository<BusinessCard , Integer>{

    BusinessCard getByUserIdAndCustomerId( Integer userId , int customerId);

}
