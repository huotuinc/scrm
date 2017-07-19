package com.huotu.scrm.service.service.api.impl;

import com.huotu.scrm.common.SysConstant;
import com.huotu.scrm.common.httputil.HttpClientUtil;
import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.service.api.ApiService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by helloztt on 2017-07-12.
 */
@Service
@DependsOn("sysConstant")
public class ApiServiceImpl implements ApiService {
    private static final Log log = LogFactory.getLog(ApiServiceImpl.class);
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void userLogin(Long customerId, String redirectUrl) {
        Customer customer = customerRepository.findOne(customerId);
        if(customer == null){
            return;
        }
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("customerid", String.valueOf(customerId));
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, Constant.UTF8);
        } catch (UnsupportedEncodingException e) {
            log.error("url encode error",e);
        }
        requestMap.put("redirecturl", redirectUrl);
        HttpClientUtil.getInstance().get(customer.getSubDomain() + SysConstant.COOKIE_DOMAIN + "/OAuth2/XiangzhangAuthorize.aspx",requestMap);
    }
}
