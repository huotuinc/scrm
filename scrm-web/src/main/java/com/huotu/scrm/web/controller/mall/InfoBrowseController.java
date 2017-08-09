package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.service.info.InfoBrowseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 浏览量转发控制器
 * Created by luohaibo on 2017/7/12.
 */
@Controller
public class InfoBrowseController extends MallBaseController{


    private Log logger = LogFactory.getLog(InfoBrowseController.class);
    @Autowired
    InfoBrowseService infoBrowseService;


    /**
     * 查询转发记录
     * @param infoBrowseAndTurnSearch
     * @param customerId
     * @param model
     * @return
     */
    @RequestMapping("/info/turnRecord")
    public String infoTurnRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch, @ModelAttribute("customerId") Long customerId, Model model){
      infoBrowseAndTurnSearch.setCustomerId(customerId);
      Page<InfoBrowse> page =  infoBrowseService.infoTurnRecord(infoBrowseAndTurnSearch);
      model.addAttribute("infoTurnListPage",page);
      return "info/info_turn";
    }

    /***
     * 删除转发记录
     * @param infoBrowseAndTurnSearch
     * @return
     */
    @RequestMapping("/info/deleteTurn")
    @ResponseBody
    public ApiResult deleteTurn(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch){
        int count = infoBrowseService.updateInfoTurnRecord(infoBrowseAndTurnSearch);
        logger.info(count);
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
    }

    /**
     * 获取某条资讯的浏览记录
     * @param infoBrowseAndTurnSearch
     * @param customerId
     * @param model
     * @return
     */
    @RequestMapping("/info/browseRecord")
    public String infoBrowseRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch, @ModelAttribute("customerId") Long customerId, Model model){
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        Page<InfoBrowse> page =  infoBrowseService.infoBrowseRecord(infoBrowseAndTurnSearch);
        model.addAttribute("infoBrowseListPage",page);
        return "info/info_browse";
    }

    /***
     * 删除转发记录
     * @param infoBrowseAndTurnSearch
     * @return
     */
    @RequestMapping("/info/deleteBrowse")
    @ResponseBody
    public ApiResult deleteBrowse(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch){
        int count = infoBrowseService.updateInfoBrowse(infoBrowseAndTurnSearch);
        if(count > 0){
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
        return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR);

    }



}
