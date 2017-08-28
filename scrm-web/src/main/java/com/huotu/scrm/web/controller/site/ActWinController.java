/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.ActivityStatus;
import com.huotu.scrm.service.model.PrizeType;
import com.huotu.scrm.service.service.activity.ActPrizeService;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import com.huotu.scrm.service.service.activity.ActivityService;
import com.huotu.scrm.service.service.api.ApiService;
import com.huotu.scrm.service.service.mall.UserService;
import com.huotu.scrm.web.service.StaticResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Created by montage on 2017/7/17.
 */

@Controller
public class ActWinController extends SiteBaseController {


    private Log logger = LogFactory.getLog(ActWinController.class);
    @Autowired
    private StaticResourceService staticResourceService;
    @Autowired
    private ActWinDetailService actWinDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActPrizeService actPrizeService;
    @Autowired
    private ApiService apiService;

    @RequestMapping("/activity/index")
    public String marketingActivity(@ModelAttribute("userId") Long userId,
                                    @RequestParam Long customerId,
                                    @RequestParam Long actId, Model model) {

        //查询用户积分
        User user = userService.getByIdAndCustomerId(userId, customerId);
        //获取奖品
        Activity activity = activityService.findByActId(actId);
        activity.getActPrizes().sort(Comparator.comparingInt(ActPrize::getSort));

        activity.getActPrizes().stream().filter(p -> !StringUtils.isEmpty(p.getPrizeImageUrl())
        ).forEach(p -> {
            try {
                p.setPrizeImageUrl(staticResourceService.getResource(null, p.getPrizeImageUrl()).toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        //用户可参与次数
        int times = activityUseTimes(activity, user);
        model.addAttribute("times", times);
        model.addAttribute("active", activity);
        model.addAttribute("customerId", customerId);
        return "activity/game";

    }

    /**
     * 参加抽奖活动
     *
     * @param request
     * @param userId
     * @param actId
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/join/act")
    @ResponseBody
    public ApiResult joinAct(HttpServletRequest request,
                             @ModelAttribute("userId") Long userId,
                             @RequestParam Long actId,
                             @RequestParam Long customerId) {
        //查询用户积分
        User user = userService.getByIdAndCustomerId(userId, customerId);
        //获取活动
        Activity activity = activityService.findByActId(actId);
        //活动情况
        //1、活动还没开始
        if (!activity.activeBegin()) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, ActivityStatus.ACTIVITY_STAUS_TYPE_UNBEGIN.getValue());
        }
        //2、活动结束
        if (!activity.activeEnd()) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, ActivityStatus.ACTIVITY_STAUS_TYPE_END.getValue());
        }

        if (activityUseTimes(activity, user) > 0) { //判读用户是否有抽奖机会
            //调商城接口扣除积分
            ApiResult apiResult;
            try {
                apiResult = apiService.rechargePoint(customerId, userId, 0L - activity.getGameCostlyScore(), IntegralTypeEnum.ACTIVE_SCORE);
            } catch (UnsupportedEncodingException e) {
                logger.error("积分扣取失败", e);
                return ApiResult.resultWith(ResultCodeEnum.SEND_FAIL, "积分扣取失败,请稍后再试", null);
            }
            if (apiResult.getCode() == 200) {
                ActPrize actPrize;
                ActWinDetail actWinDetail;
                synchronized (actId) {
                    //抽取中奖奖品
                    actPrize = winArithmetic(activity.getActPrizes());
                    //记入中奖记录，奖品数量减1
                    actWinDetail = winPrizeRecord(request.getRemoteAddr(), userId, activity, actPrize);
                }
                if (actWinDetail != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("prizeId", actPrize.getPrizeId());
                    data.put("prizeName", actPrize.getPrizeName());
                    data.put("prizeType", actPrize.getPrizeType());
                    data.put("prizeDetailId", actWinDetail.getWinDetailId());
                    try {
                        String imageUrl = staticResourceService.getResource(null, actPrize.getPrizeImageUrl()).toString();
                        data.put("prizeImageUrl", imageUrl);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    return ApiResult.resultWith(ResultCodeEnum.SUCCESS, data);
                }
            }
            return ApiResult.resultWith(ResultCodeEnum.SEND_FAIL, "积分扣取失败,请稍后再试", null);
        }
        return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR, "您当前抽奖机会已用完", null);
    }

    /**
     * 更新中奖记录
     *
     * @param userId
     * @param actWinDetailId
     * @param name
     * @param mobile
     * @param authCode
     * @return
     */
    @RequestMapping(value = "/update/winRecord")
    @ResponseBody
    public ApiResult winPrizeRecordUpdate(@ModelAttribute("userId") Long userId,
                                          @RequestParam Long actWinDetailId,
                                          @RequestParam String name,
                                          @RequestParam String mobile,
                                          @RequestParam String authCode,
                                          @RequestParam Long customerId) {
        try {
            ApiResult apiResult = apiService.checkCode(customerId, mobile, authCode);
            if (apiResult.getCode() == 200) {
                ActWinDetail actWinDetail = actWinDetailService.updateActWinDetail(actWinDetailId, name, mobile);
                if (actWinDetail != null) {
                    return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
                }
            }
            return apiResult;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, null, "手机验证码错误");
        }
    }


    /**
     * 获取用户某个活动的中奖记录
     *
     * @param userId
     * @param actId
     * @return
     */
    @RequestMapping(value = "/act/prizeList")
    public String prizeList(@ModelAttribute("userId") Long userId,@ModelAttribute("customerId") Long customerId, @RequestParam(value = "actId") Long actId, Model model) {

        //查询到用户某个活动的中奖记录
        List<ActWinDetail> list = actWinDetailService.getActWinDetailRecordByActIdAndUserId(actId, userId);
        list.forEach(p -> {
            ActPrize actPrize = p.getPrize();
            try {
                actPrize.setMallPrizeImageUrl(staticResourceService.getResource(null, actPrize.getPrizeImageUrl()).toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        model.addAttribute("winRecords", list);
        model.addAttribute("customerId", customerId);
        return "activity/site_prize_list";
    }


    /**
     * 记入中奖记入
     *
     * @param ipAddress
     * @param userId
     * @param activity
     * @return
     */
    @Transactional
    private ActWinDetail winPrizeRecord(String ipAddress, Long userId,
                                        Activity activity, ActPrize actPrize) {
        if (!PrizeType.PRIZE_TYPE_THANKS.equals(actPrize.getPrizeType())) {
            actPrize.setRemainCount(actPrize.getRemainCount() - 1);
            actPrize.setPrizeCount(actPrize.getPrizeCount() - 1);
            actPrizeService.saveActPrice(actPrize);
        }
        ActWinDetail actWinDetail = new ActWinDetail();
        actWinDetail.setUserId(userId);
        actWinDetail.setWinTime(LocalDateTime.now());
        actWinDetail.setPrize(actPrize);
        actWinDetail.setIpAddress(ipAddress);
        actWinDetail.setActId(activity.getActId());
        return actWinDetailService.saveActWinDetail(actWinDetail);
    }

    /**
     * 判读用户参与活动次数
     *
     * @param activity
     * @param user
     * @return
     */
    private int activityUseTimes(Activity activity, User user) {
        double score = user.getUserIntegral() - user.getLockedIntegral();
        int costScore = activity.getGameCostlyScore();
        if (score > 0) {
            return (int) (score / costScore);
        }
        return 0;
    }

    /**
     * 抽奖算法
     *
     * @param actPrizes 奖品列表
     * @return
     */
    private ActPrize winArithmetic(List<ActPrize> actPrizes) {
        Integer[] prizeArray = new Integer[actPrizes.size() + 1];
        prizeArray[0] = 0;
        //抽奖奖项,默认为谢谢惠顾
        ActPrize winAward = actPrizes.stream().filter(p -> PrizeType.PRIZE_TYPE_THANKS.equals(p.getPrizeType()))
                .findFirst().orElse(null);
        for (int i = 0; i < actPrizes.size(); i++) {
            prizeArray[i + 1] = prizeArray[i] + actPrizes.get(i).getWinRate();
        }
        int ran = new Random().nextInt(prizeArray[prizeArray.length - 1]);
        for (int i = 0; i < prizeArray.length - 1; i++) {
            //ran 在[,)区间内，并且剩余奖品数量足够
            if (ran >= prizeArray[i] && ran < prizeArray[i + 1]) {
                if (actPrizes.get(i).getRemainCount() > 0) {
                    winAward = actPrizes.get(i);
                }
                break;
            }
        }
        return winAward;
    }
}
