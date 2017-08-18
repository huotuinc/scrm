package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.common.utils.ExcelUtil;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        Page<Activity> allActivity = activityService.findAllActivity(customerId, pageIndex, Constant.PAGE_SIZE);
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
    public ApiResult checkEnable(@RequestParam Long actId) {
        Activity activity = activityService.findByActId(actId);
        List<ActPrize> prizeList = activity.getActPrizes();
        if(prizeList.size() < 6 || prizeList.size() > 8){
            return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR,"奖品个数必须为6-8个",null);
        }
        int rate = prizeList.stream().mapToInt(ActPrize::getWinRate).sum();
        if (rate != 100) {
            return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR,"奖品概率合计值必须为100，当前合计值为" + rate,null);
        }
        activity.setOpenStatus(true);
        activityService.saveActivity(activity);
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
    }

    /**
     * 获取参与记录（中奖记录）
     *
     * @param model
     * @return
     */
    @RequestMapping("/join/record")
    public String getJoinRecord(@RequestParam(required = false, defaultValue = "1") int pageIndex,Long actId,@RequestParam(required = false, defaultValue = "0")int type, Model model) {
        Page<ActWinDetail> pageActWinDetail = actWinDetailService.getPageActWinDetail(actId,type,pageIndex, Constant.PAGE_SIZE);
        model.addAttribute("joinRecord", pageActWinDetail);
        model.addAttribute("totalPages", pageActWinDetail.getTotalPages());
        model.addAttribute("totalRecords", pageActWinDetail.getTotalElements());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", Constant.PAGE_SIZE);
        model.addAttribute("actId",actId);
        model.addAttribute("type",type);
        return "activity/winPrize_list";
    }

    /**
     * 中奖记录导出Excel表格
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadWinDetail")
    public void downloadAllWinDetail(HttpServletResponse response,Long actId,@RequestParam(required = false, defaultValue = "0")int type, int startPage, int endPage) throws IOException {
        //完善配置信息
        String fileName = "活动中奖记录";
        List<Map<String, Object>> excelRecord = actWinDetailService.createExcelRecord(actId, type,startPage, endPage);
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
