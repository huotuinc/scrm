package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.HotUserBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/7/11.
 */
public interface HotUserBaseInfoReposity extends JpaRepository<HotUserBaseInfo, Integer>{

    HotUserBaseInfo getByUserIdAndCustomerId(Integer userId , Integer customerId);

}
