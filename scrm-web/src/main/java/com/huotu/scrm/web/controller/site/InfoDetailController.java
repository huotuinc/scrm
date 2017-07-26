package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.service.info.InfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by luohaibo on 2017/7/26.
 */
@Controller
public class InfoDetailController extends SiteBaseController {


    private Log logger = LogFactory.getLog(InfoDetailController.class);
    @Autowired
    InfoService infoService;

    @RequestMapping(value = "/info/infoDetail")
    public String infoDetail(@ModelAttribute("userId") Long userId, Long infoId, Long customerId, Model model){
        //todo去记录浏览量
        Info info =  infoService.findOneByIdAndCustomerId(infoId,customerId);
        model.addAttribute("info",info);
        return "info/information_detail";
    }
}
