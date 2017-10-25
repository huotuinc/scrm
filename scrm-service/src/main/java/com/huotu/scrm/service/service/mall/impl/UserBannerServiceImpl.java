package com.huotu.scrm.service.service.mall.impl;

import com.huotu.scrm.service.entity.mall.UserBanner;
import com.huotu.scrm.service.repository.mall.UserBannerRepository;
import com.huotu.scrm.service.service.mall.UserBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xyr on 2017/10/25.
 */
@Service
public class UserBannerServiceImpl implements UserBannerService {

    @Autowired
    private UserBannerRepository userBannerRepository;

    @Override
    public UserBanner findUserBanner(long customerId) {
        List<UserBanner> userBannerList = userBannerRepository.findAll(((root, query, cb) -> {
            Predicate predicate = cb.isTrue(cb.literal(true));

            predicate = cb.and(predicate, cb.equal(root.get("customerId").as(Long.class), customerId));
            predicate = cb.and(predicate, cb.equal(root.get("type").as(Integer.class), 4));

            return predicate;
        }));

        UserBanner userBanner = userBannerList.stream()
                .sorted(Comparator.comparing(UserBanner::getTime).reversed())
                .findFirst()
                .orElse(null);
        return userBanner;
    }
}
