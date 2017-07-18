package com.huotu.scrm.web.controller.mall;
import com.huotu.scrm.common.utils.InformationSearch;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.service.info.InfoService;
import com.huotu.scrm.web.service.StaticResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * 资讯管理控制器
 * Created by luohaibo on 2017/7/10.
 */
@Controller
public class InfoController extends MallBaseController {

    private Log logger = LogFactory.getLog(InfoController.class);
    @Autowired
    InfoService infoService;
    @Autowired
    StaticResourceService staticResourceService;

    /***
     * 展示资讯首页内容
     * @param model
     * @return
     */
    @RequestMapping(value = "/info/infoList")
    public String infoHomeLists(InformationSearch informationSearch, @ModelAttribute("customerId") Long customerId , Model model){
        logger.info(informationSearch);
        informationSearch.setCustomerId(customerId);
        logger.info(informationSearch);
        Page<Info> page = infoService.infoSList(informationSearch);
        model.addAttribute("infoListsPage",page);
        long account = infoService.infoListsCount(false);
        logger.info(account);
        model.addAttribute("totalAccount",account);
        return "info/info_list";
    }

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/info/edit")
    public String infoEditPage(Long id,  Model model,@ModelAttribute("customerId") Long customerId){
        Info info =  infoService.findOneById(id);
        try {
            URI imgUri = staticResourceService.getResource(StaticResourceService.huobanmallMode, info.getImageUrl());
            logger.info(imgUri.toString());
            info.setImageUrl(imgUri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        model.addAttribute("info",info);
        return "info/info_edit";
    }



    /**
     * 保存修改资讯
     * @param info
     * @return
     */
    @RequestMapping("/info/saveInfo")
    public String saveInfo(@ModelAttribute("customerId") Long customerId, Info info){
        logger.info(info);
        if (info.getCustomerId() == null || info.getCustomerId() == 0){
            info.setCustomerId(customerId);
        }
        infoService.infoSave(info);
        return "redirect:/mall/info/infoList";

    }


}
