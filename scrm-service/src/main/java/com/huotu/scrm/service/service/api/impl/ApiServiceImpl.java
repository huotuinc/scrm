package com.huotu.scrm.service.service.api.impl;

import com.alibaba.fastjson.JSON;
import com.huotu.scrm.common.SysConstant;
import com.huotu.scrm.common.httputil.HttpClientUtil;
import com.huotu.scrm.common.httputil.HttpResult;
import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.common.utils.*;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.service.api.ApiService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by helloztt on 2017-07-12.
 */
@Service
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

    @Override
    public ApiResult rechargePoint(Long customerId, Long userId, Long integral, IntegralTypeEnum integralType) throws UnsupportedEncodingException {
        Map<String,Object> requestMap = new TreeMap<>();
        requestMap.put("customerid", customerId);
        requestMap.put("userid", userId);
        requestMap.put("trandno", SerialNo.create());
        requestMap.put("integral", integral);
        requestMap.put("integraltype", integralType.getCode());
        requestMap.put("appid",SysConstant.HUOBANMALL_PUSH_APPID);
        String sign = SignBuilder.buildSignIgnoreEmpty(requestMap, null, SysConstant.HUOBANMALL_PUSH_APP_SECRET);
        requestMap.put("sign",sign);
        ApiResult apiResult;
        HttpResult httpResult = HttpClientUtil.getInstance().post(SysConstant.HUOBANMALL_PUSH_URL + "/Account/rechargePoints",requestMap);
        if (httpResult.getHttpStatus() == HttpStatus.SC_OK) {
            apiResult = JSON.parseObject(httpResult.getHttpContent(), ApiResult.class);
        }else{
            apiResult = ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST);
        }
        return apiResult;
    }
}
