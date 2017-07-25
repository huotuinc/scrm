package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.service.info.InfoBrowseServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static sun.misc.Version.println;

/**
 * 浏览量转发控制器
 * Created by luohaibo on 2017/7/12.
 */
@Controller
public class InfoBrowseController extends MallBaseController{


    private Log logger = LogFactory.getLog(InfoBrowseController.class);
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
      Page<InfoBrowse> page =  infoBrowseServer.infoTurnRecord(infoBrowseAndTurnSearch);

      page.getContent().stream()
              .forEach(System.out::println);
      model.addAttribute("infoTurnListPage",page);
      return "info/info_turn";
    }

    @RequestMapping("/info/deleteTurn")
    @ResponseBody
    public ApiResult deleteTurn(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch){
        int count = infoBrowseServer.deleteInfoTurnRecord(infoBrowseAndTurnSearch);
        logger.info(count);
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
    }


}
