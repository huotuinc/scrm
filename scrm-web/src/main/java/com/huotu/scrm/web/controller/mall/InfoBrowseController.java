package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.service.info.InfoBrowseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 浏览量转发控制器
 * Created by luohaibo on 2017/7/12.
 */
@Controller
public class InfoBrowseController extends MallBaseController{

    @Autowired
    InfoBrowseServer infoBrowseServer;


    /**
     *
     * @param infoBrowse
     * @param customerId
     * @return
     */
    @RequestMapping("/info/turnIn")
    @ResponseBody
    public void infoTurnInRecord(InfoBrowse infoBrowse, @ModelAttribute("customerId") Long customerId){
        infoBrowseServer.infoTurnInSave(infoBrowse,customerId);
    }


//    @RequestMapping("/addBrowse")
//    @ResponseBody
//    public String insertBrowse(Long infoId){
//        InfoBrowse infoBrowse = new InfoBrowse();
//        infoBrowseServer.infoBroseSave(infoBrowse);
//        return "";
//    }


}
