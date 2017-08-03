package com.huotu.scrm.service.service.mall.impl;

import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.service.mall.UserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLevelServiceImpl implements UserLevelService {
    @Autowired
    private UserLevelRepository userLevelRepository;

    @Override
    public UserLevel findByLevelAndCustomerId(Long level, Long customerId) {
        return userLevelRepository.findByIdAndCustomerId(level,customerId);
    }

    @Override
    public List<UserLevel> findByCustomerIdAndIsSalesman(Long customerId, boolean isSalesman) {
        return userLevelRepository.findByCustomerIdAndIsSalesman(customerId,isSalesman);
    }

    /**
     * 根据商户id和Id获得用户等级信息
     * @param customerId
     * @param id
     * @return
     */
    @Override
    public UserLevel findByCustomerIdAndId(Long customerId , Long id)
    {
        return userLevelRepository.findByCustomerIdAndId(customerId,id);
    }

    @Override
    public UserLevel save(UserLevel userLevel) {
        return userLevelRepository.save(userLevel);
    }
}
