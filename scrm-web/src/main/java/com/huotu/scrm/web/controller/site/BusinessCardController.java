package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.EnumHelper;
import com.huotu.scrm.common.ienum.UploadResourceEnum;
import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.service.businesscard.BusinessCardRecordService;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import com.huotu.scrm.service.service.mall.UserLevelService;
import com.huotu.scrm.service.service.mall.UserService;
import com.huotu.scrm.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;
import java.time.LocalDateTime;

/**
 * 名片控制器
 * Created by jinxiangdong on 2017/7/11.
 */
@Controller
@RequestMapping("/site/businessCard")
public class BusinessCardController extends SiteBaseController {
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private BusinessCardRecordService businessCardRecordService;
    @Autowired
    private StaticResourceService staticResourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserLevelService userLevelService;

    /***
     * 编辑销售员名片信息
     * @param userId
     * @param customerId
     * @param model
     * @return
     */
    @RequestMapping(value = "/editBusinessCard", method = RequestMethod.GET)
    public String editBusinessCard(@ModelAttribute("userId") Long userId,
                                   @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId,
                                   Model model) {
        BusinessCard businessCard = businessCardService.getBusinessCard(userId, customerId);
        if (businessCard == null) {
            businessCard = new BusinessCard();
            businessCard.setCustomerId(customerId);
            businessCard.setUserId(userId);
            businessCard.setAvatar("");
        }
        model.addAttribute("businessCard", businessCard);
        return "businesscard/edit_businesscard";
    }

    /***
     * 上传名片头像接口
     * @param customerId 商户Id
     * @param userId 用户Id
     * @param btnFile 图片
     * @return
     */
    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadAvatar(@ModelAttribute("userId") Long userId,
                                  @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId,
                                  MultipartFile btnFile) throws Exception {
        if (btnFile == null || btnFile.isEmpty() || btnFile.getSize() < 1) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST);
        }
        String type = btnFile.getContentType();
        if (type == null || !type.toLowerCase().startsWith("image/")) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, "不支持的文件类型，仅支持图片！");
        }

        User user = userService.getByIdAndCustomerId(userId, customerId);
        if (user == null) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, "不存在的用户", null);
        }
        UserLevel userLevel = userLevelService.findByLevelAndCustomerId(user.getLevelId(), user.getCustomerId());
        if (userLevel == null || !userLevel.isSalesman()) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, "该用户不是销售员，无权编辑名片信息", null);
        }

        UploadResourceEnum uploadResourceType = UploadResourceEnum.USER;
        String fileName = btnFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String path = StaticResourceService.IMG + customerId + "/" + uploadResourceType.getValue() + "/" + userId + "." + prefix;
        String mode = null;
        //先删除原来的图片
        staticResourceService.deleteResource(path);
        //再上传最新的图片
        URI uri = staticResourceService.uploadResource(mode, path, btnFile.getInputStream());
        String uriString = uri.toString();
        //然后保存图片的uri地址到数据库
        BusinessCard businessCard = businessCardService.updateBusinessCard(customerId, userId, BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR, uriString);
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS, businessCard);
    }

    /***
     * 更新名片信息
     * @param userId
     * @param type
     * @param value
     * @return
     */
    @RequestMapping(value = "/updateBusinessCardInfo", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult updateBusinessCardInfo(@ModelAttribute("userId") Long userId,
                                            @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId,
                                            @RequestParam(name = "type", required = false, defaultValue = "1") Integer type,
                                            @RequestParam(name = "value", required = false, defaultValue = "") String value) {
        User user = userService.getByIdAndCustomerId(userId, customerId);
        if (user == null) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, "不存在的用户", null);
        }
        UserLevel userLevel = userLevelService.findByLevelAndCustomerId(user.getLevelId(), user.getCustomerId());
        if (userLevel == null || !userLevel.isSalesman()) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, "该用户不是销售员，无权编辑名片信息", null);
        }

        BusinessCardUpdateTypeEnum typeEnum = EnumHelper.getEnumType(BusinessCardUpdateTypeEnum.class, type);
        BusinessCard businessCard = businessCardService.updateBusinessCard(customerId, userId, typeEnum, value);

        ApiResult<BusinessCard> result = new ApiResult<>(ResultCodeEnum.SUCCESS.getResultMsg(), ResultCodeEnum.SUCCESS.getResultCode());
        result.setData(businessCard);
        return result;
    }

    /***
     * 显示名片页面
     * @param userId
     * @param customerId
     * @param salesmanId
     * @param model
     * @return
     */
    @RequestMapping("/showBusinessCard")
    public String showBusinessCard(@ModelAttribute("userId") Long userId,
                                   @RequestParam(name = "customerId", required = false, defaultValue = "0") long customerId,
                                   @RequestParam(name = "salesmanId", required = false, defaultValue = "0") long salesmanId,
                                   Model model) {
        //检测是否关注了其他销售员
        boolean isFollowedOther = businessCardRecordService.existsByCustomerIdAndFollowerIdNotInSalesmanId(customerId, userId, salesmanId);
        if (!isFollowedOther) {
            //检测是否关注了指定的销售员
            boolean isFollowed = businessCardRecordService.existsByCustomerIdAndUserIdAndFollowId(customerId, salesmanId, userId);
            if (!isFollowed) {
                BusinessCardRecord follow = new BusinessCardRecord();
                follow.setFollowDate(LocalDateTime.now());
                follow.setFollowId(userId);
                follow.setUserId(salesmanId);
                follow.setCustomerId(customerId);
                businessCardRecordService.insert(follow);
            }
        }
        SalesmanBusinessCard salesmanBusinessCard = businessCardService.getSalesmanBusinessCard(customerId, salesmanId, userId);
        model.addAttribute("businessCard", salesmanBusinessCard);
        return "businesscard/show_businesscard";
    }

    /***
     * 取消关注
     * @param userId
     * @param customerId
     * @param salesmanId
     * @return
     */
    @RequestMapping(value = "/cancelFollow", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult cancelFollow(@ModelAttribute("userId") Long userId,
                                  @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId,
                                  @RequestParam(name = "salesmanId", required = false, defaultValue = "0") Long salesmanId) {
        try {
            businessCardRecordService.deleteByCustomerIdAndUserIdAndFollowId(customerId, salesmanId, userId);
            int numberOfFollower = businessCardRecordService.countNumberOfFollowerByCustomerIdAndUserId(customerId, salesmanId);
            Map<Object, Object> resultMap = new HashMap<>();
            resultMap.put("numberOfFollower", numberOfFollower);
            resultMap.put("isFollowed", false);
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS, resultMap);
        } catch (Exception ex) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST);
        }
    }

    /***
     * 我的名片夹
     * @param userId
     * @param customerId
     * @return
     */
    @RequestMapping("/myBusinessCard")
    public ModelAndView myBusinessCard(@ModelAttribute("userId") Long userId, @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId) {
        List<SalesmanBusinessCard> myBusinessCards = businessCardService.getMyBusinessCardList(customerId, userId);
        ModelAndView modelAndView = new ModelAndView("businesscard/my_businesscard");
        modelAndView.addObject("myBusinessCards", myBusinessCards);
        return modelAndView;
    }
}
