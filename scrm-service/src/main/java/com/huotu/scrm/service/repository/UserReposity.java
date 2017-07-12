package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/7/11.
 */
public interface UserReposity extends JpaRepository<User, Integer>{
    /***
     * 通过userid和customerid获得用户信息
     * @param userId
     * @param customerId
     * @return
     */
    User getByIdAndCustomerId(Long userId , Long customerId);

}
