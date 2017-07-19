package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by helloztt on 2017-07-05.
 */
public class CustomerRepositoryTest extends CommonTestBase {
    @Autowired
    private CustomerRepository customerRepository;
    @Test
    public void findById() throws Exception {
        Long id = 3447L;
        Customer customer = customerRepository.findOne(id);
        System.out.println(customer.getLoginName());
    }

}