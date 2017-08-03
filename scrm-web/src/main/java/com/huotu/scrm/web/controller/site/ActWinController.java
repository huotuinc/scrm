/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.controller.site;
import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.IpUtil;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.service.activity.ActPrizeService;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import com.huotu.scrm.service.service.activity.ActivityService;
import com.huotu.scrm.service.service.mall.UserService;
import com.huotu.scrm.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by montage on 2017/7/17.
 */

@Controller
public class ActWinController extends SiteBaseController {


    @Autowired
    StaticResourceService staticResourceService;
    @Autowired
    private ActPrizeService actPrizeService;
    @Autowired
    private ActWinDetailService actWinDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    ActivityService activityService;

    @RequestMapping("/activity/index")
    public String marketingActivity(@ModelAttribute("userId") Long userId, Long customerId, Long actId,Model model){

        //查询用户积分
        User user =  userService.getByIdAndCustomerId(1058823L,customerId);
        double score = user.getUserIntegral() - user.getLockedIntegral();
        //获取奖品
        Activity  activity = activityService.findByActId(actId);
        activity.getActPrizes().sort(Comparator.comparingInt(ActPrize::getSort));
        activity.getActPrizes().forEach(p -> {
            try {
                p.setPrizeImageUrl(staticResourceService.getResource(null, p.getPrizeImageUrl()).toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        //计算抽奖次数
        boolean actStatus =  activity.actItSelfStatus();
        int costScore = activity.getGameCostlyScore();
        if (actStatus && score > costScore){
            model.addAttribute("times",(int)score/costScore);
        }else {
            model.addAttribute("times",0);
        }
        model.addAttribute("active",activity);
        return "activity/game";

    }


    /**
     * 参加抽奖活动
     *
     * @return
     */
    @RequestMapping(value = "/join/act")
    @ResponseBody
    public ApiResult joinAct(
            HttpServletRequest request, String userId,
            @RequestParam(required = false, defaultValue = "null") String userName,
            @RequestParam(required = false, defaultValue = "null") String userTel,
            Long actId) {

        //抽取
        Long priezeId =  WinArithmetic(actId);

        return null;

//        //TODO 用户积分没有得到 无法计算游戏消耗积分
//        ActWinDetail actWinDetail = new ActWinDetail();
//        actWinDetail.setPrize(prize);
//        actWinDetail.setIpAddress(IpUtil.IpAddress(request));
//        actWinDetail.setUserId(Long.valueOf(userId));
//        actWinDetail.setWin_Time(new Date());
//        actWinDetail.setWinnerName(userName);
//        actWinDetail.setWinnerTel(userTel);
//        actWinDetail = actWinDetailService.saveActWinDetail(actWinDetail);
//        if (actWinDetail != null) {
//            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
//        }
//        return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR);
    }

    /**
     * 抽奖算法
     * @param actId
     * @return
     */
    private Long WinArithmetic(Long actId) {
        //获取奖品
        Activity  activity = activityService.findByActId(actId);
        double  totalProbability = 0;
        Map<Long,Map<Double,Double>> prizesMap = new HashMap<>();
        for (ActPrize p:activity.getActPrizes()
                ) {
            double temp = (totalProbability + p.getWinRate());
            Map<Double,Double> tpRange = new HashMap<>();
            tpRange.put(new Double(totalProbability/100),new Double(temp/100));
            prizesMap.put(p.getPrizeId(),tpRange);
            totalProbability = temp;
        }
        //抽奖奖项
        Long winAwardId = -1L;
        double ran = Math.random();
        Set<Long> prizeIds = prizesMap.keySet();
        for (Long prizeId: prizeIds
                ) {
            Map<Double,Double> tpRange = prizesMap.get(prizeId);
            Double key = (Double)tpRange.keySet().toArray()[0];
            Double value = tpRange.get(key);
            if (ran >= key.doubleValue() && ran < value.doubleValue()){
                winAwardId = prizeId;
                break;
            }
        }
        return winAwardId;
    }
}
