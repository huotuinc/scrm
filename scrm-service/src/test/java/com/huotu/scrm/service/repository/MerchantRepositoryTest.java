package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.Merchant;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by helloztt on 2017-07-05.
 */
public class MerchantRepositoryTest extends CommonTestBase {
    @Autowired
    private MerchantRepository merchantRepository;
    @Test
    public void findById() throws Exception {
        Long id = 3447L;
        Merchant merchant = merchantRepository.findById(id);
        System.out.println(merchant.getLoginName());
    }

}