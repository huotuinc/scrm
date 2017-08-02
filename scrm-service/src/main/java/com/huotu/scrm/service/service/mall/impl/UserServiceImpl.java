package com.huotu.scrm.service.service.mall.impl;

import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.mall.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User getByIdAndCustomerId(Long id, Long customerId) {
        return userRepository.findOneByIdAndCustomerId(id,customerId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
