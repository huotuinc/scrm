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
import com.huotu.scrm.common.utils.ExcelUtil;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.service.activity.ActPrizeService;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import com.huotu.scrm.service.service.activity.ActivityService;
import com.huotu.scrm.service.service.mall.UserService;
import com.huotu.scrm.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        User user =  userService.getByIdAndCustomerId(userId,customerId);
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
            @ModelAttribute("userId") Long userId,
            Long actId) {

        //抽取
        Long priezeId =  WinArithmetic(actId);

        //获取奖品
        Activity  activity = activityService.findByActId(actId);

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

    /**
     * 中奖记录导出Excel表格
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadWinDetail")
    public void downloadAllWinDetail(HttpServletResponse response) throws IOException {
        //完善配置信息
        String fileName = "活动中奖记录";
        List<Map<String, Object>> excelRecord = actWinDetailService.createExcelRecord();
        List<String> columnNames = Arrays.asList("用户编号", "活动名称", "奖品名称", "姓名", "电话", "日期", "IP");
        List<String> keys = Arrays.asList("userId", "actName", "prizeName", "winnerName", "winnerTel", "winTime", "ipAddress");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ExcelUtil.createWorkBook(excelRecord, keys, columnNames).write(os);
        byte[] bytes = os.toByteArray();
        InputStream is = new ByteArrayInputStream(bytes);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms -excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        //开流数据导出
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048 * 10];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

}
