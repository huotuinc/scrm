package com.huotu.scrm.web.controller.site;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by helloztt on 2017-07-06.
 */
@RequestMapping("/site")
public class SiteBaseController {

    @ModelAttribute("userId")
    public long getUserId(HttpServletRequest request) {
        return Long.parseLong(request.getAttribute("userId").toString());
    }
}
