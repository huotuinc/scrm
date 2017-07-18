package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.service.InfoExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hxh on 2017-07-17.
 */
@Controller
public class InfoExtensionController extends SiteBaseController {

    @Autowired
    private InfoExtensionService infoExtensionService;

    /**
     * 进入资讯状态（普通会员：进入资讯推广 小伙伴：进入推广中心）
     *
     * @return
     */
    @RequestMapping("/getInfoExtension")
    public String getInfoExtension(@ModelAttribute("userId") Long userId) {
        int userType = infoExtensionService.getUserType(userId);
        if (userType == 0) {//普通会员
            return "infoextension/info_center";
        } else {//小伙伴
            return "infoextension/info_extension";
        }
    }
}
