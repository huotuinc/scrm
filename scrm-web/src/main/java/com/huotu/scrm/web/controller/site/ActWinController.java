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
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import com.huotu.scrm.service.service.activity.ActivityService;
import com.huotu.scrm.service.service.api.ApiService;
import com.huotu.scrm.service.service.mall.UserService;
import com.huotu.scrm.web.service.StaticResourceService;
import com.huotu.scrm.web.service.VerifyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private ApiService apiService;
    @Autowired
    private VerifyService verifyService;

    @RequestMapping("/activity/index")
    public String marketingActivity(@ModelAttribute("userId") Long userId, Long customerId, Long actId, Model model) {

        //查询用户积分
        User user = userService.getByIdAndCustomerId(userId, customerId);
        //获取奖品
        Activity activity = activityService.findByActId(actId);
        activity.getActPrizes().sort(Comparator.comparingInt(ActPrize::getSort));
        activity.getActPrizes().forEach(p -> {
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
        return "activity/game";

    }

    /**
     * 参加抽奖活动
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
                             Long actId, Long customerId) {
        //查询用户积分
        User user = userService.getByIdAndCustomerId(userId, customerId);
        //获取活动
        Activity activity = activityService.findByActId(actId);
        //活动情况
        //1、活动还没开始
        if (!activity.activeBegin()) {
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS, ActivityStatus.ACTIVITY_STAUS_TYPE_UNBEGIN);
        }
        //2、活动结束
        if (!activity.activeEnd()) {
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS, ActivityStatus.ACTIVITY_STAUS_TYPE_END);
        }

        if (activityUseTimes(activity, user) > 0) { //判读用户是否有抽奖机会
            //抽取中奖奖品
            Long prizeId = WinArithmetic(actId);
            //调商城接口扣除积分
            ApiResult apiResult;
            try {
                apiResult = apiService.rechargePoint(customerId,userId,0L-activity.getGameCostlyScore(), IntegralTypeEnum.ACTIVE_SCRORE);
            } catch (UnsupportedEncodingException e) {
                logger.error("积分扣取失败",e);
                return ApiResult.resultWith(ResultCodeEnum.SEND_FAIL,"积分扣取失败,请稍后再试",null);
            }
            if(apiResult.getCode() == 200) {
                //记入中奖激励
                ActWinDetail actWinDetail = winPrizeRecord(request.getRemoteAddr(), prizeId, userId, activity);
                if (actWinDetail != null) {
                    ActPrize actPrize = getPrizeByPrizeId(activity, prizeId);
                    if (actPrize.getPrizeCount() <= 0) {
                        return ApiResult.resultWith(ResultCodeEnum.SUCCESS, ActivityStatus.ACTIVITY_STAUS_TYPE_PRIZEOVER);
                    }
                    Map<String, Object> data = new HashMap<>();
                    data.put("prizeId", prizeId);
                    data.put("prizeName", actPrize.getPrizeName());
                    data.put("prizeType",actPrize.getPrizeType());
                    data.put("prizeDetailId",actWinDetail.getWinDetailId());
                    try {
                        String imageUrl = staticResourceService.getResource(null, actPrize.getPrizeImageUrl()).toString();
                        data.put("prizeImageUrl", imageUrl);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    return ApiResult.resultWith(ResultCodeEnum.SUCCESS, data);
                }
            }
            return ApiResult.resultWith(ResultCodeEnum.SEND_FAIL,"积分扣取失败,请稍后再试",null);
        }
        return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR, "您当前抽奖机会已用完",null);
    }

    /**
     * 更新中奖记录
     * @param userId
     * @param ActWinDetailId
     * @param name
     * @param mobile
     * @param authCode
     * @return
     */
    @RequestMapping(value = "/update/winRecord")
    @ResponseBody
    public ApiResult winPrizeRecordUpdate(@ModelAttribute("userId") Long userId, Long ActWinDetailId, String name, String mobile,
                                        String authCode){
        if (verifyService.verifyVerificationCode(mobile,authCode)){
            ActWinDetail actWinDetail = actWinDetailService.updateActWinDetail(ActWinDetailId,name,mobile);
            if (actWinDetail!=null){
                return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
            }
            return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR);
        }
         return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR,null,"手机验证码错误");
    }


    /**
     * 获取用户某个活动的中奖记录
     * @param userId
     * @param actId
     * @return
     */
    @RequestMapping(value = "/act/prizeList")
    public String prizeList(@ModelAttribute("userId") Long userId, @RequestParam(value = "actId") Long actId,Model model){

        List<ActWinDetail> list = actWinDetailService.getActWinDetailRecordByActIdAndUserId(actId,userId);
        List<ActPrize> actPrizes = new ArrayList<>();

        list.stream().forEach(p->{
            ActPrize actPrize = p.getPrize();
            try {
                actPrize.setPrizeImageUrl(staticResourceService.getResource(null, actPrize.getPrizeImageUrl()).toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            actPrizes.add(actPrize);
        });
        model.addAttribute("winRecords",actPrizes);
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
    private ActWinDetail winPrizeRecord(String ipAddress, Long prizeId, Long userId,
                                        Activity activity) {

        ActWinDetail actWinDetail = new ActWinDetail();
        actWinDetail.setUserId(userId);
        actWinDetail.setWinTime(LocalDateTime.now());
        actWinDetail.setPrize(getPrizeByPrizeId(activity, prizeId));
        actWinDetail.setIpAddress(ipAddress);
        actWinDetail.setActId(activity.getActId());
        return actWinDetailService.saveActWinDetail(actWinDetail);
    }

    /**
     * 通过id查找对应奖品
     *
     * @param activity
     * @param prizeId
     * @return
     */
    private ActPrize getPrizeByPrizeId(Activity activity, Long prizeId) {
        for (ActPrize actPrize : activity.getActPrizes()
                ) {
            if (actPrize.getPrizeId().longValue() == prizeId.longValue()) {
                return actPrize;
            }
        }
        return null;
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
        if (score > costScore)
            return (int) (score / costScore);
        return 0;
    }

    /**
     * 抽奖算法
     *
     * @param actId
     * @return
     */
    private Long WinArithmetic(Long actId) {
        //获取奖品
        Activity activity = activityService.findByActId(actId);
        double totalProbability = 0;
        Map<Long, Map<Double, Double>> prizesMap = new HashMap<>();
        for (ActPrize p : activity.getActPrizes()
                ) {
            double temp = (totalProbability + p.getWinRate());
            Map<Double, Double> tpRange = new HashMap<>();
            tpRange.put(new Double(totalProbability / 100), new Double(temp / 100));
            prizesMap.put(p.getPrizeId(), tpRange);
            totalProbability = temp;
        }
        //抽奖奖项
        Long winAwardId = -1L;
        double ran = Math.random();
        Set<Long> prizeIds = prizesMap.keySet();
        for (Long prizeId : prizeIds
                ) {
            Map<Double, Double> tpRange = prizesMap.get(prizeId);
            Double key = (Double) tpRange.keySet().toArray()[0];
            Double value = tpRange.get(key);
            if (ran >= key.doubleValue() && ran < value.doubleValue()) {
                winAwardId = prizeId;
                break;
            }
        }
        return winAwardId;
    }
}
