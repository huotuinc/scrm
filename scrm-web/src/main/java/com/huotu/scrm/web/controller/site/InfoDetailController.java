package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.exception.ApiResultException;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.repository.mall.UserRepository;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;


/**
 * Created by luohaibo on 2017/7/26.
 */
@Controller
public class InfoDetailController extends SiteBaseController {


    private Log logger = LogFactory.getLog(InfoDetailController.class);
    @Autowired
    private InfoService infoService;
    @Autowired
    StaticResourceService staticResourceService;
    @Autowired
    private InfoBrowseService infoBrowseService;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/info/infoDetail")
    public String infoDetail(@ModelAttribute("userId") Long userId,
                             @RequestParam(value = "infoId") Long infoId,
                             @RequestParam(value = "customerId") Long customerId,
                             @RequestParam(value = "sourceUserId", required = false) Long sourceUserId,
                             Model model) throws URISyntaxException, ApiResultException {
        Info info = infoService.findOneByIdAndCustomerId(infoId, customerId);
        //查询当前用户的类型
        UserType readUserType = userRepository.findUserTypeById(userId);
        //判断资讯对该类型用户是否启用
        if(!((readUserType == UserType.normal && info.isStatus()) || (readUserType == UserType.buddy && info.isExtend()))){
            throw new ApiResultException("资讯未开启");
        }
        //sourceUserId 不为空 时表示用户在浏览别人转发出来的资讯，这时候需要记录浏览记录。
        if(sourceUserId != null){
            infoTurnInRecord(infoId, userId, sourceUserId, customerId);
        }
        if (!StringUtils.isEmpty(info.getImageUrl())) {
            info.setImageUrl(staticResourceService.getResource(StaticResourceService.huobanmallMode, info.getImageUrl()).toString());
        }
        int turnNum = infoBrowseService.countByTurn(infoId);
        model.addAttribute("infoTurnNum", turnNum);
        int browse = infoBrowseService.countByBrowse(infoId);
        model.addAttribute("browseNum", browse);
        model.addAttribute("customerId", customerId);
        model.addAttribute("userId", userId);
        model.addAttribute("info", info);
        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        infoBrowseAndTurnSearch.setSourceType(0);
        infoBrowseAndTurnSearch.setInfoId(infoId);
        Page<InfoBrowse> page = infoBrowseService.infoSiteBrowseRecord(infoBrowseAndTurnSearch);
        model.addAttribute("headImages", page.getContent());
        return "info/information_detail";
    }

    @RequestMapping(value = "/info/infoDetailBrowse")
    @SuppressWarnings("Duplicates")
    public String infoBrowse(@ModelAttribute("userId") Long userId, Long infoId, Long customerId, Model model) throws URISyntaxException {

        //浏览记录
        int browse = infoBrowseService.countByBrowse(infoId);
        model.addAttribute("browseNum", browse);

        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        infoBrowseAndTurnSearch.setSourceType(1);
        infoBrowseAndTurnSearch.setInfoId(infoId);
        Page<InfoBrowse> page = infoBrowseService.infoSiteBrowseRecord(infoBrowseAndTurnSearch);
        model.addAttribute("headImages", page.getContent());
        return "info/browse_log";
    }

    /**
     * 添加浏览记录
     *
     * @param infoId
     * @param userId
     * @param sourceUserId
     * @param customerId
     */
    private void infoTurnInRecord(Long infoId, Long userId,
                                  Long sourceUserId
            , Long customerId) {
        InfoBrowse infoBrowse = new InfoBrowse();
        infoBrowse.setSourceUserId(sourceUserId);
        infoBrowse.setReadUserId(userId);
        infoBrowse.setInfoId(infoId);
        try {
            infoBrowseService.infoTurnInSave(infoBrowse, customerId);
        } catch (UnsupportedEncodingException e) {
            logger.error("添加积分失败", e);
        }
    }


}
