package com.huotu.scrm.service.repository.mall;

import com.huotu.scrm.service.entity.mall.UserBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by xyr on 2017/10/25.
 */
public interface UserBannerRepository extends JpaRepository<UserBanner, Integer>, JpaSpecificationExecutor<UserBanner> {
}
