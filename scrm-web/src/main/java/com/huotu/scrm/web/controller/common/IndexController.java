/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.controller.common;

import com.huotu.scrm.common.utils.ModelMapUtil;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.web.service.VerifyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by helloztt on 2017-08-01.
 */
@Controller
public class IndexController {

    private static final Log log = LogFactory.getLog(IndexController.class);
    private final Map<String, Integer> map = new HashMap<>();
    @Autowired
    private VerifyService verifyService;

    @RequestMapping({"/","/index"})
    public String index(){
        return "index";
    }

    /**
     * 发送短信验证码
     *
     * @param loginName 手机号码
     * @param request
     * @param imageCode 图片验证码可为空
     * @param session
     * @return
     */
    @RequestMapping(value = "/sendAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap sendAuthCode(@RequestParam String loginName, HttpServletRequest request
            , String imageCode, HttpSession session) {
        String ip;
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = request.getHeader("x-forwarded-for");
        }
        if (StringUtils.isNotBlank(imageCode)) {
            String imageCode_session = (String) session.getAttribute("imageCode");
            if (imageCode.equalsIgnoreCase(imageCode_session)) {
                session.removeAttribute("imageCode");
                map.put(ip, 0);
                map.put(loginName, 0);
            } else {
                return ModelMapUtil.createModelMap(ResultCodeEnum.IMAGE_CODE_ERROR, null);
            }
        }
        Boolean flag_1 = verifyService.verifyCount(ip, map);
        Boolean flag_2 = verifyService.verifyCount(loginName, map);
        if (!(flag_1 && flag_2)) {
            //限制发送短信验证码
            return ModelMapUtil.createModelMap(ResultCodeEnum.SEND_LIMIT, null);
        }

        try {
            verifyService.sendVerificationCode(loginName);
        } catch (IOException e) {
            // 对于未知情况需要进行记录
            log.debug("unknown error", e);
            //验证码发送失败
            return ModelMapUtil.createModelMap(ResultCodeEnum.SEND_FAIL, null);
        } catch (IllegalStateException e) {
            //请勿重复点击发送按钮
            return ModelMapUtil.createModelMap(ResultCodeEnum.NO_REPEAT_SEND, null);
        }
        return ModelMapUtil.createModelMap(ResultCodeEnum.SUCCESS, null);
    }
}
