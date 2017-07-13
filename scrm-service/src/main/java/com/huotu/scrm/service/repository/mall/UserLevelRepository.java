package com.huotu.scrm.service.repository.mall;

import com.huotu.scrm.service.entity.mall.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hxh on 2017-07-13.
 */
public interface UserLevelRepository extends JpaRepository<UserLevel, Long> {

    //根据等级和商户ID查询等级
    UserLevel findByLevelAndCustomerId(int level, Long customerId);
}
