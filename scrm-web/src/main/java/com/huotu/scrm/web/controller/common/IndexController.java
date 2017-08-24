/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.controller.common;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.service.api.ApiService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * Created by helloztt on 2017-08-01.
 */
@Controller
public class IndexController {

    private static final Log log = LogFactory.getLog(IndexController.class);
    @Autowired
    private ApiService apiService;

    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    /**
     * 发送短信验证码
     *
     * @param loginName  手机号码
     * @param customerId 商户ID
     * @return 发送结果
     */
    @RequestMapping(value = "/sendAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult sendAuthCode(@RequestParam String loginName
            , @RequestParam Long customerId) {
        ApiResult result;
        try {
            result = apiService.sendCode(customerId, loginName);
        } catch (UnsupportedEncodingException e) {
            // 对于未知情况需要进行记录
            log.debug("unknown error", e);
            //验证码发送失败
            return ApiResult.resultWith(ResultCodeEnum.SEND_FAIL);
        }
        return result;
    }
}
