package com.huotu.scrm.service.repository.mall;

import com.huotu.scrm.service.entity.mall.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hxh on 2017-07-13.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /***
     * 通过userid和customerid获得用户信息
     * @param id
     * @param customerId
     * @return
     */
    User getByIdAndCustomerId(Long id , Long customerId);
}
