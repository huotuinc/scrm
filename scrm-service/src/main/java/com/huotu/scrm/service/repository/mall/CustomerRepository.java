package com.huotu.scrm.service.repository.mall;

import com.huotu.scrm.service.entity.mall.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by helloztt on 2017-07-05.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
