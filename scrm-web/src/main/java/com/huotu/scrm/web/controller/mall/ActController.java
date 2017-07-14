package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 大转盘活动控制层
 * Created by montage on 2017/7/12.
 */

@Controller
@RequestMapping("/Act")
public class ActController {
    @Autowired
    private ActivityService activityService;

    /**
     * 分页查询活动
     * @param pageIndex 当前页数
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String actList(int pageIndex,Model model){
        Page<Activity> allActivity = activityService.findAllActivity(pageIndex, 20);
        model.addAttribute("activities",allActivity.getContent());
        model.addAttribute("totalPages",allActivity.getTotalPages());
        model.addAttribute("totalRecords",allActivity.getTotalElements());
        model.addAttribute("pageIndex",pageIndex);
        model.addAttribute("pageSize",20);
        return "act/list";
    }

    /**
     * 跳转到添加页面
     * @return
     */
    @RequestMapping("/list/add")
    public String actToAdd(){
        return "act/add";
    }

    public ApiResult addAct(int actType,String actTitle, String startTime,String endTime, int openStatus ,int gameCostlyScore,String ruleDesc,String rateDesc){
        Activity activity = new Activity();

        //TODO 添加活动
       return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
    }
}
