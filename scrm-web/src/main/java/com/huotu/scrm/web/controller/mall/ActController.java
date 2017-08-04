package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import com.huotu.scrm.service.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 大转盘活动控制层
 * Created by montage on 2017/7/12.
 */

@Controller
public class ActController extends MallBaseController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActWinDetailService actWinDetailService;

    /**
     * 分页查询活动
     *
     * @param pageIndex 当前页数
     * @param model
     * @return
     */
    @RequestMapping("/act/list")
    public String actList(@RequestParam(required = false, defaultValue = "1") int pageIndex, @ModelAttribute("customerId") Long customerId, Model model) {
        Page<Activity> allActivity = activityService.findAllActivity(pageIndex, Constant.PAGE_SIZE);
        model.addAttribute("activities", allActivity.getContent());
        model.addAttribute("totalPages", allActivity.getTotalPages());
        model.addAttribute("totalRecords", allActivity.getTotalElements());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", Constant.PAGE_SIZE);
        return "activity/activity_list";
    }

    /**
     * 跳转到活动编辑和保存页面
     *
     * @param actId 活动Id
     * @param model
     * @return
     */
    @RequestMapping("/act/detail")
    public String getActDetail(@RequestParam(required = false, defaultValue = "0") Long actId, Model model) {
        Activity activity = new Activity();
        if (actId != 0) {
            activity = activityService.findByActId(actId);
        }
        model.addAttribute("activity", activity);
        return "activity/activity_detail";
    }

    /**
     * 保存活动
     *
     * @param activity
     * @return
     */
    @RequestMapping("/act/save")
    public String saveAct(Activity activity, @ModelAttribute("customerId") Long customerId) {
        activity.setCustomerId(customerId);
        activityService.saveActivity(activity);
        return "redirect:/mall/act/list";
    }

    /**
     * 逻辑删除活动
     *
     * @param actId 活动Id
     * @return
     */
    @RequestMapping("/act/delete")
    @ResponseBody
    public ApiResult deleteAct(Long actId) {
        if (actId != null) {
            activityService.updateActivity(actId);
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
        return ApiResult.resultWith(ResultCodeEnum.DATA_BAD_PARSER);
    }

    /**
     * 禁用活动
     *
     * @param actId 活动编号
     * @return
     */
    @RequestMapping("/act/disable")
    @ResponseBody
    public ApiResult checkDisable(Long actId) {
        Activity activity = activityService.findByActId(actId);
        if (activity == null) {
            return ApiResult.resultWith(ResultCodeEnum.DATA_BAD_PARSER);
        }
        activity.setOpenStatus(false);
        activityService.saveActivity(activity);
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
    }

    /**
     * 启用活动
     *
     * @param actId 活动编号
     * @return
     */
    @RequestMapping("/act/enable")
    @ResponseBody
    public ApiResult checkEnable(Long actId) {
        Activity activity = activityService.findByActId(actId);
        List<ActPrize> prizeList = activity.getActPrizes();
        int rate = 0;
        for (ActPrize actPrize : prizeList
                ) {
            rate += actPrize.getWinRate();
        }
        if (rate != 100) {
            return ApiResult.resultWith(ResultCodeEnum.DATA_BAD_PARSER);
        }
        activity.setOpenStatus(true);
        activityService.saveActivity(activity);
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
    }
}
