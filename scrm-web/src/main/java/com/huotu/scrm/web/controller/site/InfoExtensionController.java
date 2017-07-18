package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.model.InfoModel;
import com.huotu.scrm.service.service.infoextension.InfoExtensionService;
import com.huotu.scrm.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by hxh on 2017-07-17.
 */
@Controller
public class InfoExtensionController extends SiteBaseController {

    @Autowired
    private InfoExtensionService infoExtensionService;
    @Autowired
    private StaticResourceService staticResourceService;

    /**
     * 进入资讯状态（普通会员：进入资讯推广 小伙伴：进入推广中心）
     *
     * @return
     */
    @RequestMapping("/getInfoExtension")
    public String getInfoExtension(@ModelAttribute("userId") Long userId, Model model) throws URISyntaxException {
        int userType = infoExtensionService.getUserType(userId);
        List<InfoModel> infoModels = infoExtensionService.findInfo(userId, userType);
        for (InfoModel infoModel : infoModels) {
            URI uri = staticResourceService.getResource(StaticResourceService.huobanmallMode, infoModel.getThumbnailImageUrl());
            infoModel.setThumbnailImageUrl(uri.getPath());
        }
        model.addAttribute("infoModes", infoModels);
        if (userType == 0) {//普通会员
            return "infoextension/info_center";
        } else {//小伙伴

            return "infoextension/info_extension";
        }
    }
}
