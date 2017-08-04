package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.exception.ApiResultException;
import com.huotu.scrm.service.model.*;
import com.huotu.scrm.service.repository.mall.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    /**
     * 进入资讯状态（普通会员：进入资讯推广 小伙伴：进入推广中心）
     *
     * @param userId 用户ID
     * @param model  model
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/extension/getInfoExtension")
    public String getInfoExtension(@ModelAttribute("userId") Long userId, Model model) throws Exception {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new ApiResultException("用户不存在");
        }
        //获取资讯信息
        List<InfoModel> infoModels = infoExtensionService.findInfo(user);
        if (infoModels != null) {
            infoModels.stream().filter(p -> !StringUtils.isEmpty(p.getThumbnailImageUrl()))
                    .forEach(p -> {
                        //这里不应该抛出异常，某个资讯获取图片失败，不应该影响到其他资讯的显示
                        try {
                            URI uri = staticResourceService.getResource(StaticResourceService.huobanmallMode, p.getThumbnailImageUrl());
                            p.setThumbnailImageUrl(uri.toString());
                        } catch (URISyntaxException ignored) {
                        }
                    });
        }
        model.addAttribute("infoModes", infoModels);
        if (user.getUserType() == UserType.normal) {//普通会员
            return "infoextension/info_extension";
        } else {//小伙伴
            StatisticalInformation statisticalInformation = infoExtensionService.getInformation(user);
            //判断是否为销售员
            boolean status = infoExtensionService.checkIsSalesman(userId);
            model.addAttribute("statisticalInformation", statisticalInformation);
            model.addAttribute("status", status);
            return "infoextension/info_center";
        }
    }

    /**
     * 转到今日积分排名页面
     *
     * @param userId 用户ID
     * @param model  model
     * @return
     */
    @RequestMapping("/extension/getScoreRanking")
    public String getScoreRanking(@ModelAttribute("userId") Long userId, Model model) throws Exception {
        User user = userRepository.findOne(userId);
        if(user == null){
            throw new ApiResultException("用户不存在");
        }
        //普通用户不限制排名信息
        if (user.getUserType() != UserType.buddy) {
            return "redirect:/site/extension/getInfoExtension";
        } else {
            //获取统计性能（积分，排名等）
            DayScoreRankingInfo dayScoreRankingInfo = infoExtensionService.getScoreRankingInfo(userId,user.getCustomerId());
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
    public String getScoreInfo(@ModelAttribute("userId") Long userId, Model model) throws Exception {
        User user = userRepository.findOne(userId);
        if(user == null){
            throw new ApiResultException("用户不存在");
        }
        if (user.getUserType() != UserType.buddy) {
            return "redirect:/site/extension/getInfoExtension";
        } else {
            //获取统计性能（积分，排名等）
            DayScoreInfo dayScoreInfo = infoExtensionService.getScoreInfo(userId,user.getCustomerId());
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
     * @param model  model
     * @return
     */
    @RequestMapping("/extension/getFollowInfo")
    public String getFollowInfo(@ModelAttribute("userId") Long userId, Model model) throws Exception{
        UserType userType = userRepository.findUserTypeById(userId);
        if(userType == null){
            throw new ApiResultException("用户不存在");
        }
        if (userType != UserType.buddy) {
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
     * @param userId 用户ID
     * @param model  model
     * @return
     */
    @RequestMapping("/extension/getVisitorInfo")
    public String getVisitorInfo(@ModelAttribute("userId") Long userId, Model model) throws Exception {
        UserType userType = userRepository.findUserTypeById(userId);
        if(userType == null){
            throw new ApiResultException("用户不存在");
        }
        if (userType != UserType.buddy) {
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
