package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by helloztt on 2017-07-05.
 */
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findById(Long id);
}
