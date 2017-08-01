package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import com.huotu.scrm.service.service.mall.UserLevelService;
import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.controller.page.site.ShowBusinessCardPage;
import com.huotu.scrm.web.controller.page.site.TestEditBusinessCardPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */
public class BusinessCardControllerTest extends CommonTestBase {
    private String editUrl = "http://localhost/site/businessCard/editBusinessCard";
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private UserLevelService userLevelService;
    private Long customerId = 0L;
    private UserLevel userLevel;
    private User user, followerUser;

    @Before
    public void init() {
        Customer customer = mockCustomer();
        customerId = customer.getId();
        //名片只有销售员才有
        userLevel = mockUserLevel(customerId, UserType.buddy, true);
        List<UserLevel> userLevelList = userLevelService.findByCustomerIdAndIsSalesman(customerId, true);
        Assert.assertEquals(1, userLevelList.size());
        user = mockUser(customerId, userLevel);
        followerUser = mockUser(customerId, userLevel);
    }

    @Test
    public void editBusinessCard() throws Exception {
        Long userId = user.getId();
        // TODO: 2017-08-01 修改图片后验证？其他字段为什么不验证？？单元测试需要包含所有case
        //设置名片图片
        BusinessCard businessCard = businessCardService.updateBusinessCard(customerId, userId, BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR, "a.jpg");
        //BusinessCard businessCard = businessCardService.getBusinessCard( customerId , userId );

        editUrl += "?customerId=" + customerId + "&userId=" + userId;
        System.out.println("customerId:" + customerId + ";userId=" + userId);
        //以 userId 登录
        mockUserLogin(userId,customerId);
        webDriver.get(editUrl);
        TestEditBusinessCardPage editBusinessCardPage = this.initPage(TestEditBusinessCardPage.class);
        editBusinessCardPage.setBusinessCard(businessCard);
        editBusinessCardPage.setUserRepository(userRepository);
        //WebElement ele = webDriver.findElement(By.id("div_job"));
        editBusinessCardPage.validate();

    }

    @Test
    public void showBusinessCard() {

        // TODO: 2017-08-01 这个 update 放到 editBusinessCard 方法中？？
        businessCardService.updateBusinessCard(customerId, user.getId(), BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_JOB, "job");

        SalesmanBusinessCard salesmanBusinessCard = businessCardService.getSalesmanBusinessCard(customerId, user.getId(), followerUser.getId());

        String url = "http://localhost/site/businessCard/seeBusinessCard?customerId=" + customerId + "&salesmanId=" + user.getId() + "&userId=" + followerUser.getId();
        webDriver.get(url);
        ShowBusinessCardPage page = this.initPage(ShowBusinessCardPage.class);
        page.setSalesmanBusinessCard(salesmanBusinessCard);
        page.validate();

    }
}
