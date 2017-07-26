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
import org.springframework.util.StringUtils;
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
    @RequestMapping(value = "/extension/getInfoExtension")
    public String getInfoExtension(@ModelAttribute("userId") Long userId, Model model) throws URISyntaxException {
        //获取用户类型
        int userType = infoExtensionService.getUserType(userId);
        //获取统计信息（积分，排名等）
        List<InfoModel> infoModels = infoExtensionService.findInfo(userId, userType);
        //获取图片资源
        for (InfoModel infoModel : infoModels) {
            String thumbnailImageUrl = infoModel.getThumbnailImageUrl();
            if (!StringUtils.isEmpty(thumbnailImageUrl)) {
                URI uri = staticResourceService.getResource(StaticResourceService.huobanmallMode, thumbnailImageUrl);
                infoModel.setThumbnailImageUrl(uri.getPath());
            }
        }
        model.addAttribute("infoModes", infoModels);
        if (userType == 0) {//普通会员
            return "infoextension/info_extension";
        } else {//小伙伴
            StatisticalInformation statisticalInformation = infoExtensionService.getInformation(userId);
            //判断是否为销售员
            boolean status = infoExtensionService.checkIsSalesman(userId);
            model.addAttribute("statisticalInformation", statisticalInformation);
//            model.addAttribute("status", status);
            model.addAttribute("status", true);
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
    @RequestMapping("/extension/getScoreRanking")
    public String getScoreRanking(@ModelAttribute("userId") Long userId, Model model) {
        int userType = infoExtensionService.getUserType(userId);
        if (userType != 1) {
            return "redirect:/site/extension/getInfoExtension";
        } else {
            //获取统计性能（积分，排名等）
            DayScoreRankingInfo dayScoreRankingInfo = infoExtensionService.getScoreRankingInfo(userId);
            //判断是否为销售员
            boolean status = infoExtensionService.checkIsSalesman(userId);
            model.addAttribute("dayScoreRankingInfo", dayScoreRankingInfo);
            model.addAttribute("status", status);
            return "extensiondetail/personal_ranking";
        }
    }

    /**
     * 转到今日积分统计页面
     *
     * @param userId 用户ID
     * @return
     */
    @RequestMapping("/extension/getScoreInfo")
    public String getScoreInfo(@ModelAttribute("userId") Long userId, Model model) {
        int userType = infoExtensionService.getUserType(userId);
        if (userType != 1) {
            return "redirect:/site/extension/getInfoExtension";
        } else {
            //获取统计性能（积分，排名等）
            DayScoreInfo dayScoreInfo = infoExtensionService.getScoreInfo(userId);
            //判断是否为销售员
            boolean status = infoExtensionService.checkIsSalesman(userId);
            model.addAttribute("dayScoreInfo", dayScoreInfo);
            model.addAttribute("status", status);
            return "extensiondetail/personal_score";
        }
    }

    /**
     * 转到今日关注统计页面
     *
     * @param userId 用户ID
     * @param model
     * @return
     */
    @RequestMapping("/extension/getFollowInfo")
    public String getFollowInfo(@ModelAttribute("userId") Long userId, Model model) {
        int userType = infoExtensionService.getUserType(userId);
        if (userType != 1) {
            return "redirect:/site/extension/getInfoExtension";
        } else {
            //获取统计性能（积分，排名等）
            DayFollowNumInfo dayFollowNumInfo = infoExtensionService.getFollowNumInfo(userId);
            model.addAttribute("dayFollowNumInfo", dayFollowNumInfo);
            return "extensiondetail/personal_follow";
        }
    }

    /**
     * 转到今日访问量页面
     *
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping("/extension/getVisitorInfo")
    public String getVisitorInfo(@ModelAttribute("userId") Long userId, Model model) {
        int userType = infoExtensionService.getUserType(userId);
        if (userType != 1) {
            return "redirect:/site/extension/getInfoExtension";
        } else {
            //获取统计性能（积分，排名等）
            DayVisitorNumInfo dayVisitorNumInfo = infoExtensionService.getVisitorNumInfo(userId);
            //判断是否为销售员
            boolean status = infoExtensionService.checkIsSalesman(userId);
            model.addAttribute("dayVisitorNumInfo", dayVisitorNumInfo);
            model.addAttribute("status", status);
            return "extensiondetail/personal_uv";
        }
    }
}
