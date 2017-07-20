package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.service.info.InfoRewardConfigureService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 积分控制配置后台控制器
 * Created by luohaibo on 2017/7/20.
 */
@Controller
public class InfoRewardController extends MallBaseController{


    private Log logger = LogFactory.getLog(InfoRewardController.class);
    @Autowired
    InfoRewardConfigureService infoRewardConfigureService;

    /**
     * 读取积分奖励配置
     * @param customerId
     * @return
     */
    @RequestMapping("/reward/read")
    public String readRewardConfigureFromDb(@ModelAttribute("customerId") Long customerId, Model model){
        InfoConfigure infoConfigure = infoRewardConfigureService.readRewardConfigure(customerId);
        model.addAttribute("infoConfigure",infoConfigure);
        return "info/info_reward_configure";
    }

    /**
     * 读取积分奖励配置
     * @param customerId
     * @return
     */
    @RequestMapping("/reward/Save")
    @ResponseBody
    public ApiResult saveRewardConfigureFromDb(@ModelAttribute("customerId") Long customerId, InfoConfigure infoConfigure){
        infoConfigure.setCustomerId(customerId);
        InfoConfigure infoConfigure1 = infoRewardConfigureService.saveRewardConfigure(infoConfigure);
        if (infoConfigure1 == null){
            return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR);
        }
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
    }
}
