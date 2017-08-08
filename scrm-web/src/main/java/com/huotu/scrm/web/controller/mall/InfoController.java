package com.huotu.scrm.web.controller.mall;


import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.model.info.InformationSearch;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * 资讯管理控制器
 * Created by luohaibo on 2017/7/10.
 */
@Controller
public class InfoController extends MallBaseController {

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
        informationSearch.setCustomerId(customerId);
        Page<Info> page = infoService.infoList(informationSearch);
        model.addAttribute("infoListsPage",page);
        long account = infoService.infoListsCount(false);
        model.addAttribute("totalAccount",account);
        return "info/info_list";
    }

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/info/edit")
    public String infoEditPage(@RequestParam(required = false,defaultValue = "0") Long id, Model model, @ModelAttribute("customerId") Long customerId){
        Info info =  infoService.findOneByIdAndCustomerId(id,customerId);
        if (info.getId() != null && info.getId() != 0) {
            if (info.getImageUrl() != null && !StringUtils.isEmpty(info.getImageUrl())) {
                URI imgUri = null;
                try {
                    imgUri = staticResourceService.getResource(StaticResourceService.huobanmallMode, info.getImageUrl());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                info.setMallImageUrl(imgUri.toString());
            }
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
        if (info.getCustomerId() == null || info.getCustomerId() == 0){
            info.setCustomerId(customerId);
        }
        infoService.infoSave(info);
        return "redirect:/mall/info/infoList";
    }

    /**
     * 删除资讯
     * @param customerId
     * @param id
     * @return
     */
    @RequestMapping("/info/deleteInfo")
    @ResponseBody
    public ApiResult deleteInfo(@ModelAttribute("customerId") Long customerId, Long id){
        if (infoService.deleteInfo(customerId,id)){
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS,"删除成功");
        }
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS,"删除失败");
    }

}
