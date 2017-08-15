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

    @Override
    public UserLevel save(UserLevel userLevel) {
        return userLevelRepository.save(userLevel);
    }
}
