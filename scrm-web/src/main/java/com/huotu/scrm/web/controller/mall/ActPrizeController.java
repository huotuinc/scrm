/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.service.activity.ActPrizeService;
import com.huotu.scrm.service.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 奖品控制层
 * Created by montage on 2017/7/13.
 */

@Controller
public class ActPrizeController extends MallBaseController {

    @Autowired
    private ActPrizeService actPrizeService;
    @Autowired
    private ActivityService activityService;

    /**
     * 显示某个活动对应的奖品页面
     *
     * @param actId
     * @param model
     * @return
     */
    @RequestMapping("/prize/list")
    public String getPrizeDetail(Long actId, Model model) {
        Activity activity = activityService.findByActId(actId);
        model.addAttribute("prizeList", activity.getActPrizes());
        model.addAttribute("actId", actId);
        return "activity/prize_list";
    }

    /**
     * 转到奖品编辑和保存页面
     *
     * @param prizeId
     * @param model
     * @return
     */
    @RequestMapping("/prize/detail")
    public String getPrizeEdit(Long actId, @RequestParam(required = false, defaultValue = "0") Long prizeId, Model model) {
        ActPrize actPrize = new ActPrize();
        if (prizeId != 0) {
            actPrize = actPrizeService.findByPrizeId(prizeId);
        } else {
            Activity activity = activityService.findByActId(actId);
            actPrize.setActivity(activity);
        }
        model.addAttribute("actPrize", actPrize);
        return "activity/prize_detail";
    }

    /**
     * 保存奖品
     *
     * @param actPrize 奖品实体
     * @return
     */
    @RequestMapping("/prize/save")
    public String savePrize(ActPrize actPrize, Long actId) {
        Activity activity = activityService.findByActId(actId);
        if ((actPrize.getPrizeId() == null)) {
            actPrize.setPrizeCount(actPrize.getRemainCount());
        }
        actPrize.setActivity(activity);
        activity.getActPrizes().add(actPrize);
        actPrizeService.saveActPrize(activity);
        return "redirect:/mall/prize/list?actId=" + actId;
    }

    /**
     * 删除奖品
     *
     * @param prizeId 奖品Id
     * @return
     */
    @RequestMapping("/prize/delete")
    @ResponseBody
    public ApiResult deletePrize(Long prizeId) {
        if (prizeId != null && prizeId > 0) {
            actPrizeService.deleteActPrize(prizeId);
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
        return ApiResult.resultWith(ResultCodeEnum.DATA_BAD_PARSER);
    }
}
