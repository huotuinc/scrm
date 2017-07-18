package com.huotu.scrm.web.controller.mall;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 所有需要嵌入伙伴商城的页面Controller需要继承该Controller
 * 从request中获取attribute
 * Created by helloztt on 2017-06-27.
 */
@RequestMapping("/mall")
public class MallBaseController {
    @ModelAttribute("customerId")
    public long getCustomerId(HttpServletRequest request) {
        return Long.parseLong(request.getAttribute("customerId").toString());
    }

    @ModelAttribute("loginType")
    public int getLoginType(HttpServletRequest request) {
        return Integer.parseInt(request.getAttribute("loginType").toString());
    }

    @ModelAttribute("funAuthorize")
    public String getFunAuthorize(HttpServletRequest request) {
        return request.getAttribute("funAuthorize") != null ? request.getAttribute("funAuthorize").toString() : "";
    }
}
