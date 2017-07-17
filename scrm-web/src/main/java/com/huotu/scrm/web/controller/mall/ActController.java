package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.service.ActWinDetailService;
import com.huotu.scrm.service.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 大转盘活动控制层
 * Created by montage on 2017/7/12.
 */

@Controller
@RequestMapping("/act")
public class ActController extends MallBaseController{
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
    @RequestMapping(value = "/list")
    public String actList(int pageIndex, Model model) {
        Page<Activity> allActivity = activityService.findAllActivity(pageIndex, Constant.PAGE_SIZE);
        model.addAttribute("activities", allActivity.getContent());
        model.addAttribute("totalPages", allActivity.getTotalPages());
        model.addAttribute("totalRecords", allActivity.getTotalElements());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", 20);
        return "activity/activity_list";
    }

    /**
     * 保存活动
     *
     * @param activity
     * @return
     */
    @RequestMapping(value = "/list/save")
    @ResponseBody
    public ApiResult saveAct(Activity activity) {
        if (activity.getActId() != null && activity.getActId() > 0){
            Activity newActivity = activityService.findByActId(activity.getActId());
            activity.setDelete(newActivity.isDelete());
        }
        activity = activityService.saveActivity(activity);
        if (activity != null){
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
        return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR);
    }

    /**
     * 逻辑删除活动
     *
     * @param actId 活动Id
     * @return
     */
    @RequestMapping(value = "/list/delete")
    @ResponseBody
    public ApiResult deleteAct(Long actId) {
        if (actId != null && actId > 0){
            activityService.updateActivity(actId,true);
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
            return ApiResult.resultWith(ResultCodeEnum.DATA_BAD_PARSER);
    }

    /**
     * 分页查询中奖记录
     *
     * @param pageIndex 当前页数
     * @param model
     * @return
     */
    @RequestMapping(value = "/win/list")
    public String prizeDetailList(int pageIndex, Model model){
        Page<ActWinDetail> pageActWinDetail = actWinDetailService.getPageActWinDetail(pageIndex, Constant.PAGE_SIZE);
        model.addAttribute("prizeDetailList",pageActWinDetail.getContent());
        model.addAttribute("totalPages",pageActWinDetail.getTotalPages());
        model.addAttribute("totalRecords",pageActWinDetail.getTotalElements());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", 20);
        return "activity/winPrize_list";
    }

}
