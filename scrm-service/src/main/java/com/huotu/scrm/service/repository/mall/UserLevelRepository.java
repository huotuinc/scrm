package com.huotu.scrm.service.repository.mall;

import com.huotu.scrm.service.entity.mall.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by hxh on 2017-07-13.
 */
public interface UserLevelRepository extends JpaRepository<UserLevel, Long> {

  @Query("select t from UserLevel t where t.level=?1 and t.customerId=?2")
    UserLevel findByLevelAndCustomerId(int level, Long customerId);
}
