/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.controller.api;

import com.huotu.scrm.common.SysConstant;
import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.exception.ApiResultException;
import com.huotu.scrm.service.repository.info.InfoRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by helloztt on 2017-07-28.
 */
@Controller
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private InfoRepository infoRepository;
    @Autowired
    private StaticResourceService staticResourceService;
    @Autowired
    private UserRepository userRepository;

    /**
     * 资讯分享引用脚本
     *
     * @param customerId   商户ID
     * @param infoId       资讯主键
     * @param sourceUserId 资讯来源用户ID
     * @return js文件
     */
    @RequestMapping(value = "/infoShare.js", produces = "application/javascript")
    public ModelAndView infoShare(
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "infoId") Long infoId,
            @RequestParam(value = "sourceUserId") Long sourceUserId) {
        ModelAndView model = new ModelAndView();
        Info info = infoRepository.findOneByIdAndCustomerIdAndIsDisableFalse(infoId, customerId);
        if (info != null) {
            //查询当前用户的类型
            UserType readUserType = userRepository.findUserTypeById(sourceUserId);
            //判断资讯对该类型用户是否启用

            //设置图片
            if (info.getImageUrl() != null && !StringUtils.isEmpty(info.getImageUrl())) {
                URI imgUri = null;
                try {
                    imgUri = staticResourceService.getResource(StaticResourceService.huobanmallMode, info.getImageUrl());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                info.setMallImageUrl(imgUri != null ? imgUri.toString() : null);
            }
            model.setViewName("api/info_share.js");
            model.addObject("customerId", customerId);
            model.addObject("info", info);
            model.addObject("domain", SysConstant.COOKIE_DOMAIN);
            if (!((readUserType == UserType.normal) && info.isStatus() && info.isExtend())) {
                model.addObject("sourceUserId", sourceUserId);
            }

        } else {
            model.setViewName("api/info_empty.js");
        }
        return model;
    }
}
