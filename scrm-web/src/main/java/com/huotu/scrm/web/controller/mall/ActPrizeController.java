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
import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.service.activity.ActPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/prize")
public class ActPrizeController extends MallBaseController {

    @Autowired
    private ActPrizeService actPrizeService;

    /**
     * 分页查询所有奖品
     *
     * @param pageIndex 当前页数
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String prizeList(@RequestParam(required = false,defaultValue = "1") int pageIndex, Model model){
        Page<ActPrize> pageActPrize = actPrizeService.getPageActPrize(pageIndex, Constant.PAGE_SIZE);
        model.addAttribute("prizeList",pageActPrize.getContent());
        model.addAttribute("totalPages",pageActPrize.getTotalPages());
        model.addAttribute("totalRecords",pageActPrize.getTotalElements());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", Constant.PAGE_SIZE);
        return "activity/prize_list";
    }

    /**
     * 保存奖品
     *
     * @param actPrize 奖品实体
     * @return
     */
    @RequestMapping("/list/save")
    @ResponseBody
    public ApiResult savePrize(ActPrize actPrize){
        if (actPrize.getPrizeId() != null && actPrize.getPrizeId()>0){
            actPrize = actPrizeService.findByPrizeId(actPrize.getPrizeId());
        }
        actPrize = actPrizeService.saveActPrize(actPrize);
        if (actPrize != null){
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
        return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR);
    }

    /**
     * 删除奖品
     *
     * @param prizeId 奖品Id
     * @return
     */
    @RequestMapping("/list/delete")
    @ResponseBody
    public ApiResult deletePrize(Long prizeId){
        if (prizeId != null && prizeId > 0){
            actPrizeService.deleteActPrize(prizeId);
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
        return ApiResult.resultWith(ResultCodeEnum.DATA_BAD_PARSER);
    }

}
