package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.model.DayFollowNumInfo;
import com.huotu.scrm.service.model.DayScoreInfo;
import com.huotu.scrm.service.model.DayScoreRankingInfo;
import com.huotu.scrm.service.model.DayVisitorNumInfo;
import com.huotu.scrm.service.model.InfoModel;
import com.huotu.scrm.service.model.StatisticalInformation;
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
     * @param userId 用户ID
     * @param model
     * @return
     * @throws URISyntaxException
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
            return "infoextension/info_extension";
        } else {//小伙伴
            StatisticalInformation statisticalInformation = infoExtensionService.getInformation(userId);
            model.addAttribute("statisticalInformation", statisticalInformation);
            return "infoextension/info_center";
        }
    }

    /**
     * 转到今日积分排名页面
     *
     * @param userId 用户ID
     * @param model
     * @return
     */
    @RequestMapping("/getScoreRanking")
    public String getScoreRanking(@ModelAttribute("userId") Long userId, Model model) {
        DayScoreRankingInfo dayScoreRankingInfo = infoExtensionService.getScoreRankingInfo(userId);
        model.addAttribute("dayScoreRankingInfo", dayScoreRankingInfo);
        return "extensiondetail/personal_ranking";
    }

    /**
     * 转到今日积分统计页面
     *
     * @param userId 用户ID
     * @return
     */
    @RequestMapping("/getScoreInfo")
    public String getScoreInfo(@ModelAttribute("userId") Long userId, Model model) {
        DayScoreInfo dayScoreInfo = infoExtensionService.getScoreInfo(userId);
        model.addAttribute("dayScoreInfo", dayScoreInfo);
        return "extensiondetail/personal_score";
    }

    /**
     * 转到今日关注统计页面
     *
     * @param userId 用户ID
     * @param model
     * @return
     */
    @RequestMapping("/getFollowInfo")
    public String getFollowInfo(@ModelAttribute("userId") Long userId, Model model) {
        DayFollowNumInfo dayFollowNumInfo = infoExtensionService.getFollowNumInfo(userId);
        model.addAttribute("dayFollowNumInfo", dayFollowNumInfo);
        return "extensiondetail/personal_follow";
    }

    /**
     * 转到今日访问量页面
     *
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping("/getVisitorInfo")
    public String getVisitorInfo(@ModelAttribute("userId") Long userId, Model model) {
        DayVisitorNumInfo dayVisitorNumInfo = infoExtensionService.getVisitorNumInfo(userId);
        model.addAttribute("dayVisitorNumInfo", dayVisitorNumInfo);
        return "extensiondetail/personal_uv";
    }
}
