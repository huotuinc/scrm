package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.EnumHelper;
import com.huotu.scrm.common.ienum.UploadResourceEnum;
import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.BusinessCardRecordService;
import com.huotu.scrm.service.service.BusinessCardService;
import com.huotu.scrm.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


/**
 * 名片控制器
 * Created by jinxiangdong on 2017/7/11.
 */
//@RequestMapping("/businessCard")
@Controller
public class BusinessCardController extends SiteBaseController {
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private BusinessCardRecordService businessCardRecordService;
    @Autowired
    private StaticResourceService staticResourceService;
    @Autowired
    private UserRepository userRepository;

    /***
     * 编辑销售员名片信息
     * @param request
     * @param customerId
     * @param model
     * @return
     */
    @RequestMapping("/editBusinessCard")
    public String editBusinessCard(HttpServletRequest request,
                                   @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId, Model model) {
        long userId = this.getUserId(request);
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
     * @param btnFile 图片
     * @return
     */
    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadAvatar(HttpServletRequest request,
                                  @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId,
                                  MultipartFile btnFile) throws Exception {

        if (btnFile == null || btnFile.isEmpty() || btnFile.getSize() < 1) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST);
        }
        String type = btnFile.getContentType();
        if (type == null || !type.toLowerCase().startsWith("image/")) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, "不支持的文件类型，仅支持图片！");
        }

        Long userId = this.getUserId(request);
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
     * @param request
     * @param type
     * @param value
     * @return
     */
    @RequestMapping(value = "/updateBusinessCardInfo", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult updateBusinessCardInfo(HttpServletRequest request,
                                            @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId,
                                            @RequestParam(name = "type", required = false, defaultValue = "1") Integer type,
                                            @RequestParam(name = "value", required = false, defaultValue = "") String value) {
        long userId = this.getUserId(request);
        User user = userRepository.getOne(userId);
        if (user == null) {
            return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST);
        }
        BusinessCardUpdateTypeEnum typeEnum = EnumHelper.getEnumType(BusinessCardUpdateTypeEnum.class, type);
        BusinessCard businessCard = businessCardService.updateBusinessCard(customerId, userId, typeEnum, value);

        ApiResult<BusinessCard> result = new ApiResult<BusinessCard>(ResultCodeEnum.SUCCESS.getResultMsg(), ResultCodeEnum.SUCCESS.getResultCode());
        result.setData(businessCard);
        return result;
    }

    /***
     * 显示名片页面
     * @param customerId
     * @param salesmanId
     * @param model
     * @return
     */
    @RequestMapping("/showBusinessCard")
    public String showBusinessCard(HttpServletRequest request,
                                   @RequestParam(name = "customerId", required = false, defaultValue = "0") long customerId,
                                   @RequestParam(name = "salesmanId", required = false, defaultValue = "0") long salesmanId,
                                   Model model) {
        Long followerId = this.getUserId(request);
        //检测是否关注了其他销售员
        boolean isFollowedOther = businessCardRecordService.existsByCustomerIdAndFollowerIdNotInSalesmanId(customerId, followerId, salesmanId);
        if (!isFollowedOther) {
            //检测是否关注了指定的销售员
            boolean isFollowed = businessCardRecordService.existsByCustomerIdAndUserIdAndFollowId(customerId, salesmanId, followerId);
            if (!isFollowed) {
                BusinessCardRecord follow = new BusinessCardRecord();
                follow.setFollowDate(new Date());
                follow.setFollowId(followerId);
                follow.setUserId(salesmanId);
                follow.setCustomerId(customerId);
                businessCardRecordService.insert(follow);
            }
        }
        SalesmanBusinessCard salesmanBusinessCard = businessCardService.getSalesmanBusinessCard(customerId, salesmanId, followerId);
        model.addAttribute("businessCard", salesmanBusinessCard);
        return "businesscard/show_businesscard";
    }


    /***
     * 取消关注
     * @param customerId
     * @param salesmanId
     * @return
     */
    @RequestMapping(value = "/cancelFollow", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult cancelFollow(HttpServletRequest request,
                                  @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId,
                                  @RequestParam(name = "salesmanId", required = false, defaultValue = "0") Long salesmanId) {
        try {
            Long followerId = this.getUserId(request);
            businessCardRecordService.deleteByCustomerIdAndUserIdAndFollowId(customerId, salesmanId, followerId);
            int numberOfFollower = businessCardRecordService.getFollowCountByCustomerIdAndUserId(customerId, salesmanId);
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
     * @param customerId
     * @return
     */
    @RequestMapping("/myBusinessCard")
    public ModelAndView myBusinessCard(HttpServletRequest request, @RequestParam(name = "customerId", required = false, defaultValue = "0") Long customerId) {
        Long userId = this.getUserId(request);
        List<SalesmanBusinessCard> myBusinessCards = businessCardService.getMyBusinessCardList(customerId, userId);
        ModelAndView modelAndView = new ModelAndView("businesscard/my_businesscard");
        modelAndView.addObject("myBusinessCards", myBusinessCards);
        return modelAndView;
    }
}
