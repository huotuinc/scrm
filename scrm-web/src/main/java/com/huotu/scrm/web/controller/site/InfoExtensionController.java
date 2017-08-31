package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.exception.ApiResultException;
import com.huotu.scrm.service.model.statisticinfo.DayFollowNumInfo;
import com.huotu.scrm.service.model.statisticinfo.DayScoreInfo;
import com.huotu.scrm.service.model.statisticinfo.DayScoreRankingInfo;
import com.huotu.scrm.service.model.statisticinfo.DayVisitorNumInfo;
import com.huotu.scrm.service.model.info.InfoModel;
import com.huotu.scrm.service.model.statisticinfo.StatisticalInformation;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
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
    @Autowired
    private InfoConfigureRepository infoConfigureRepository;

    /**
     * 进入资讯状态（普通会员：进入资讯推广 小伙伴：进入推广中心）
     *
     * @param userId 用户ID
     * @param model  model
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping("/extension/getInfoExtension")
    public String getInfoExtension(@ModelAttribute("userId") Long userId, Model model) throws Exception {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new ApiResultException("用户不存在");
        }
        //获取资讯信息
        List<InfoModel> infoModels = infoExtensionService.findInfo(user);
        setInfoImg(infoModels);
        model.addAttribute("infoModes", infoModels);
        if (user.getUserType() == UserType.normal) {//普通会员
            InfoConfigure infoConfigure = infoConfigureRepository.findOne(user.getCustomerId());
            model.addAttribute("infoConfigure", infoConfigure);
            return "infoextension/info_extension";
        } else {//小伙伴
            StatisticalInformation statisticalInformation = infoExtensionService.getInformation(user);
            List<InfoModel> forwardInfoList = infoExtensionService.findForwardInfo(user);
            setInfoImg(forwardInfoList);
            //判断是否为销售员
            boolean status = infoExtensionService.checkIsSalesman(user);
            model.addAttribute("statisticalInformation", statisticalInformation);
            model.addAttribute("forwardInfoList", forwardInfoList);
            model.addAttribute("status", status);
            model.addAttribute("customerId", user.getCustomerId());
            return "infoextension/info_center";
        }
    }

    private void setInfoImg(List<InfoModel> infoModelList) {
        if (infoModelList != null && infoModelList.size() > 0) {
            infoModelList.stream().filter(p -> !StringUtils.isEmpty(p.getThumbnailImageUrl()))
                    .forEach(p -> {
                        //这里不应该抛出异常，某个资讯获取图片失败，不应该影响到其他资讯的显示
                        try {
                            URI uri = staticResourceService.getResource(StaticResourceService.huobanmallMode, p.getThumbnailImageUrl());
                            p.setThumbnailImageUrl(uri.toString());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    /**
     * 获取推广明细（统计明细）
     *
     * @param userId 用户id
     * @param model  model
     * @return
     */
    @RequestMapping("extension/getInfoDetail")
    public String getInfoDetail(@ModelAttribute("userId") Long userId, Model model) throws Exception {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new ApiResultException("用户不存在");
        }
        //普通会员不进行排名信息
        if (user.getUserType() != UserType.buddy) {
            return "redirect:/site/extension/getInfoExtension";
        }
        //获取积分排名信息
        DayScoreRankingInfo dayScoreRankingInfo = infoExtensionService.getScoreRankingInfo(user);
        //获取积分信息
        DayScoreInfo dayScoreInfo = infoExtensionService.getScoreInfo(user);
        //获取uv信息
        DayVisitorNumInfo dayVisitorNumInfo = infoExtensionService.getVisitorNumInfo(user);
        //判断是否为销售员
        boolean status = infoExtensionService.checkIsSalesman(user);
        if (status) {
            //获取关注信息
            DayFollowNumInfo dayFollowNumInfo = infoExtensionService.getFollowNumInfo(user);
            model.addAttribute("dayFollowNumInfo", dayFollowNumInfo);
        }
        model.addAttribute("dayScoreRankingInfo", dayScoreRankingInfo);
        model.addAttribute("dayScoreInfo", dayScoreInfo);
        model.addAttribute("dayVisitorNumInfo", dayVisitorNumInfo);
        model.addAttribute("status", status);
        return "extensiondetail/personal_detail";
    }
}
