package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.service.info.InfoBrowseService;
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

import java.net.URISyntaxException;


/**
 * Created by luohaibo on 2017/7/26.
 */
@Controller
public class InfoDetailController extends SiteBaseController {


    private Log logger = LogFactory.getLog(InfoDetailController.class);
    @Autowired
    InfoService infoService;
    @Autowired
    StaticResourceService staticResourceService;
    @Autowired
    InfoBrowseService infoBrowseService;

    @RequestMapping(value = "/info/infoDetail")
    public String infoDetail(@ModelAttribute("userId") Long userId, Long infoId, Long customerId, Model model) throws URISyntaxException {
        //todo去记录浏览量
        Info info =  infoService.findOneByIdAndCustomerId(infoId,customerId);
        if(!StringUtils.isEmpty(info.getImageUrl())){
            info.setImageUrl(staticResourceService.getResource(StaticResourceService.huobanmallMode, info.getImageUrl()).toString());
        }
        int turnNum = infoBrowseService.countByTurn(infoId);
        model.addAttribute("infoTurnNum", new Integer(turnNum));

        int browse = infoBrowseService.countByBrowse(infoId);
        model.addAttribute("browseNum", new Integer(browse));

        model.addAttribute("info",info);


        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        infoBrowseAndTurnSearch.setSourceType(0);
        infoBrowseAndTurnSearch.setInfoId(infoId);
        Page<InfoBrowse> page = infoBrowseService.infoSiteBrowseRecord(infoBrowseAndTurnSearch);
        model.addAttribute("headImages",page.getContent());
        return "info/information_detail";
    }

    @RequestMapping(value = "/info/infoDetailBrowse")
    public String infoBrowse(@ModelAttribute("userId") Long userId, Long infoId, Long customerId, Model model) throws URISyntaxException {

        //浏览记录
        int browse = infoBrowseService.countByBrowse(infoId);
        model.addAttribute("browseNum", new Integer(browse));

        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        infoBrowseAndTurnSearch.setSourceType(1);
        infoBrowseAndTurnSearch.setInfoId(infoId);
        Page<InfoBrowse> page = infoBrowseService.infoSiteBrowseRecord(infoBrowseAndTurnSearch);
        model.addAttribute("headImages",page.getContent());
        return "info/browse_log";
    }

}
