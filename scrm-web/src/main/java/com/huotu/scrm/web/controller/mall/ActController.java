package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.common.utils.ExceUtil;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import com.huotu.scrm.service.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public String actList(@RequestParam (required = false,defaultValue = "1") int pageIndex, Model model) {
        Page<Activity> allActivity = activityService.findAllActivity(pageIndex, Constant.PAGE_SIZE);
        model.addAttribute("activities", allActivity.getContent());
        model.addAttribute("totalPages", allActivity.getTotalPages());
        model.addAttribute("totalRecords", allActivity.getTotalElements());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", Constant.PAGE_SIZE);
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
    public String prizeDetailList(@RequestParam (required = false,defaultValue = "1") int pageIndex, Model model){
        Page<ActWinDetail> pageActWinDetail = actWinDetailService.getPageActWinDetail(pageIndex, Constant.PAGE_SIZE);
        model.addAttribute("prizeDetailList",pageActWinDetail.getContent());
        model.addAttribute("totalPages",pageActWinDetail.getTotalPages());
        model.addAttribute("totalRecords",pageActWinDetail.getTotalElements());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", Constant.PAGE_SIZE);
        return "activity/winPrize_list";
    }

    /**
     * 中奖记录导出Excel表格
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/downloadAllWinDetail")
    public void downloadAllWinDetail(HttpServletResponse response) throws IOException {
        //完善配置信息
        String fileName = LocalDate.now().toString()+"活动中奖记录文件";
        List<Map<String, Object>> excelRecord = actWinDetailService.createExcelRecord();
        List<String> keys = Arrays.asList("winDetailId","userId","actName","prizeName","winnerName","winnerTel","winTime","ipAddress");
        List<String> columnNames = Arrays.asList("序号","用户编号","活动名称","奖品名称","姓名","电话","日期","IP");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ExceUtil.createWorkBook(excelRecord, keys, columnNames).write(os);
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
            bos = new BufferedOutputStream(os);
            byte[] buff = new byte[2048*10];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        }catch (IOException e){
               e.printStackTrace();
        }finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

}
