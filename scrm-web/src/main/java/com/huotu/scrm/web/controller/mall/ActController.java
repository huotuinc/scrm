package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.common.utils.StringUtil;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.service.ActPrizeService;
import com.huotu.scrm.service.service.ActWinDetailService;
import com.huotu.scrm.service.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 大转盘活动控制层
 * Created by montage on 2017/7/12.
 */

@Controller
@RequestMapping("/act")
public class ActController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActPrizeService actPrizeService;

    @Autowired
    private ActWinDetailService actWinDetailService;

    /**
     * 分页查询活动
     *
     * @param pageIndex 当前页数
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String actList(int pageIndex, Model model) {
        Page<Activity> allActivity = activityService.findAllActivity(pageIndex, 20);
        model.addAttribute("activities", allActivity.getContent());
        model.addAttribute("totalPages", allActivity.getTotalPages());
        model.addAttribute("totalRecords", allActivity.getTotalElements());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", 20);
        return "act/list";
    }

    /**
     * 保存活动
     *
     * @param activity
     * @return
     */
    @RequestMapping("/list/save")
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
    @RequestMapping("/list/delete")
    @ResponseBody
    public ApiResult deleteAct(Long actId) {
        if (actId != null && actId > 0){
            activityService.deleteActivity(actId,true);
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
    @RequestMapping("/Win/list")
    public String prizeDetailList(int pageIndex, Model model){
        Page<ActWinDetail> pageActWinDetail = actWinDetailService.getPageActWinDetail(pageIndex, 20);
        model.addAttribute("prizeDetailList",pageActWinDetail.getContent());
        model.addAttribute("totalPages",pageActWinDetail.getTotalPages());
        model.addAttribute("totalRecords",pageActWinDetail.getTotalElements());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", 20);
        return "win/ist";
    }

    /**
     * 参加抽奖活动
     *
     * @return
     */
    @RequestMapping("prize/list")
    @ResponseBody
    public ApiResult joinAct(
            HttpServletRequest request , String userId, int userScore,
            @RequestParam(required = false,defaultValue = "null" ) String userName,
            @RequestParam(required = false,defaultValue = "null") String userTel)
    {
        //回归算法得到奖品
       int proCount = 100;
       String minRate = "min";
       String maxRate = "max";
       Integer tempInt = 0;
        //待中奖奖品数组
        Map<Long,Map<String,Integer>> prizesMap = new HashMap<>();
        List<ActPrize> actPrizeList = actPrizeService.findAll();
        for (ActPrize actPrize: actPrizeList) {
            Map<String,Integer> oddMap = new HashMap<>();
            oddMap.put(minRate,tempInt);
            tempInt += actPrize.getWinRate();
            oddMap.put(maxRate,tempInt);
            prizesMap.put(actPrize.getPrizeId(),oddMap);
        }
        //得到随机数
        int random = (int) Math.random() * proCount;
        ActPrize prize = null;
        String format = null;
        Set<Long> prizeIds = prizesMap.keySet();
        for (Long prizeId : prizeIds) {
            Map<String, Integer> oddsMap = prizesMap.get(prizeId);
            Integer minNum = oddsMap.get(minRate);
            Integer maxNum = oddsMap.get(maxRate);
            //验证random 在哪件奖品中间
            if (minNum <= random && maxNum > random ) {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                format = dateFormat.format(now);
                prize = actPrizeService.findByPrizeId(prizeId);
                if (prize.getRemainCount() == 0){
                    prize =actPrizeService.findByPrizeType(false);
                }
                break;
            }
        }
        //TODO 用户积分没有得到 无法计算游戏消耗积分
        //得到用户的Ip地址
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        } else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        } else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        ActWinDetail actWinDetail = new ActWinDetail();
        actWinDetail.setPrize(prize);
        actWinDetail.setIpAddress(ip);
        actWinDetail.setUserId(Long.valueOf(userId));
        actWinDetail.setWin_Time(StringUtil.stringToDate(format));
        actWinDetail.setWinnerName(userName);
        actWinDetail.setWinnerTel(userTel);
        actWinDetail = actWinDetailService.saveActWinDetail(actWinDetail);

        if (actWinDetail != null ){
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
        return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR);
    }

}
