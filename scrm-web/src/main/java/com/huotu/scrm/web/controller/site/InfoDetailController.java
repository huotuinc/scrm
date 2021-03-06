package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserBanner;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.exception.ApiResultException;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.info.InfoBrowseService;
import com.huotu.scrm.service.service.info.InfoService;
import com.huotu.scrm.service.service.mall.UserBannerService;
import com.huotu.scrm.service.service.mall.UserLevelService;
import com.huotu.scrm.service.service.mall.UserService;
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
    @Autowired
    private UserBannerService userBannerService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserLevelService userLevelService;

    @RequestMapping(value = "/info/infoDetail")
    public String infoDetail(@ModelAttribute("userId") Long userId,
                             @RequestParam(value = "infoId") Long infoId,
                             @RequestParam(value = "customerId") Long customerId,
                             @RequestParam(value = "sourceUserId", required = false) Long sourceUserId,
                             @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                             Model model) throws URISyntaxException, ApiResultException {
        //获取用户类型
        UserType userType = userRepository.findUserTypeById(userId);

        Info info = infoService.findOneByIdAndCustomerId(infoId, customerId);
        if (info == null || info.isDisable()){
            return "info/info_delete";
        }

        if (sourceUserId != null && sourceUserId != 0) {
            infoTurnInRecord(infoId, userId, sourceUserId, customerId);
        }
        if (!StringUtils.isEmpty(info.getImageUrl())) {
            info.setImageUrl(staticResourceService.getResource(StaticResourceService.huobanmallMode, info.getImageUrl()).toString());
        }
        int turnNum = infoBrowseService.countByTurn(infoId);
        model.addAttribute("infoTurnNum", turnNum);
        //0 表示所以的 1 表示按某个人的
        int browse = (type == 0) ? (infoBrowseService.countByBrowse(infoId)) :
                (infoBrowseService.countBrowseByInfoIdAndSourceUserId(infoId, userId));
        model.addAttribute("browseNum", browse);
        model.addAttribute("customerId", customerId);
        model.addAttribute("sourceUserId", sourceUserId);
        model.addAttribute("userId", userId);
        model.addAttribute("info", info);
        //给前端传递用户类型
        model.addAttribute("userType", userType);
        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        infoBrowseAndTurnSearch.setSourceType(0);
        infoBrowseAndTurnSearch.setInfoId(infoId);
        if (type == 0) {
            infoBrowseAndTurnSearch.setSourceUserId(sourceUserId);
        } else {
            infoBrowseAndTurnSearch.setSourceUserId(userId);
        }
        Page<InfoBrowse> page;
        if (type == 0) {
            page = infoBrowseService.infoSiteBrowseRecord(infoBrowseAndTurnSearch);
        } else {
            page = infoBrowseService.infoSiteBrowseRecordBySourceUserId(infoBrowseAndTurnSearch);
        }
        model.addAttribute("type", type);
        model.addAttribute("headImages", page.getContent());
        return "info/information_detail";
    }

    @RequestMapping(value = "/info/infoDetailBrowse")
    @SuppressWarnings("Duplicates")
    public String infoBrowse(@ModelAttribute("userId") Long userId,
                             Long infoId,
                             Long customerId,
                             @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                             Model model) throws URISyntaxException {

        //浏览记录
        int browse = (type == 0) ? (infoBrowseService.countByBrowse(infoId)) :
                (infoBrowseService.countBrowseByInfoIdAndSourceUserId(infoId, userId));
        model.addAttribute("browseNum", browse);


        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        infoBrowseAndTurnSearch.setSourceType(1);
        infoBrowseAndTurnSearch.setInfoId(infoId);
        infoBrowseAndTurnSearch.setSourceUserId(userId);
        Page<InfoBrowse> page;
        if (type == 0) {
            page = infoBrowseService.infoSiteBrowseRecord(infoBrowseAndTurnSearch);

        } else {
            page = infoBrowseService.infoSiteBrowseRecordBySourceUserId(infoBrowseAndTurnSearch);
        }
        UserBanner userBanner = userBannerService.findUserBanner(customerId);
        if (userBanner != null && !StringUtils.isEmpty(userBanner.getImage())) {
            userBanner.setImage(staticResourceService.getResource(StaticResourceService.huobanmallMode, userBanner.getImage()).toString());
            model.addAttribute("banner", userBanner);
        }
        model.addAttribute("headImages", page.getContent());
        if (type == 1) {
            return "info/browse_log";

        } else {
            return "info/browse_log_name";
        }

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


        User user =  userService.getByIdAndCustomerId(userId,customerId);
        UserLevel userLevel = userLevelService.findByLevelAndCustomerId(user.getLevelId(),customerId);
        //获取用户类型
        if (!userLevel.isSalesman()) {
            InfoBrowse infoBrowse = new InfoBrowse();
            infoBrowse.setSourceUserId(sourceUserId);
            infoBrowse.setReadUserId(userId);
            infoBrowse.setInfoId(infoId);
            infoBrowse.setCustomerId(customerId);
            try {
                infoBrowseService.infoTurnInSave(infoBrowse, customerId);
            } catch (UnsupportedEncodingException e) {
                logger.error("添加积分失败", e);
            }
        }

    }


}
