package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRepository;
import com.huotu.scrm.service.service.businesscard.BusinessCardService;
import com.huotu.scrm.service.service.mall.UserLevelService;
import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.controller.page.site.ShowBusinessCardPage;
import com.huotu.scrm.web.controller.page.site.TestEditBusinessCardPage;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Administrator on 2017/7/17.
 */
public class BusinessCardControllerTest extends CommonTestBase {
    private String editUrl = "http://localhost/site/businessCard/editBusinessCard";
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private BusinessCardRepository businessCardRepository;
    @Autowired
    private BusinessCardRecordRepository businessCardRecordRepository;

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
    public void updateBusinessCardInfo() throws Exception {

        Customer customer = mockCustomer();
        UserLevel userLevel = mockUserLevel(customer.getId(), UserType.buddy, true);
        User user = mockUser(customer.getId(), userLevel);

        String updateBusinessCardUrl = "http://localhost/site/businessCard/updateBusinessCardInfo";
        String temp = UUID.randomUUID().toString();

        //mockUserLogin( user.getId() , user.getCustomerId() );

        ResultActions resultActions = mockMvc.perform(post(updateBusinessCardUrl)
                .param("customerId", String.valueOf(customer.getId()))
                .param("type", String.valueOf(BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_JOB.getCode()))
                .param("typeValue", temp)
                .cookie(mockCookie(user.getId(), customer.getId()))
        );

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(200));

        temp = UUID.randomUUID().toString();
        mockMvc.perform(post(updateBusinessCardUrl)
                .param("customerId", String.valueOf(customer.getId()))
                .param("type", String.valueOf(BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_COMPANYADDRESS.getCode()))
                .param("typeValue", temp)
                .cookie(mockCookie(user.getId(), customer.getId()))
        ).andExpect(jsonPath("$.code").value(200));

        temp = UUID.randomUUID().toString();
        mockMvc.perform(post(updateBusinessCardUrl)
                .param("customerId", String.valueOf(customer.getId()))
                .param("type", String.valueOf(BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_QQ.getCode()))
                .param("typeValue", temp)
                .cookie(mockCookie(user.getId(), customer.getId()))
        ).andExpect(status().isOk());

        temp = UUID.randomUUID().toString();
        mockMvc.perform(post(updateBusinessCardUrl)
                .param("customerId", String.valueOf(customer.getId()))
                .param("type", String.valueOf(BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_COMPANYNAME.getCode()))
                .param("typeValue", temp)
                .cookie(mockCookie(user.getId(), customer.getId()))
        ).andExpect(jsonPath("$.code").value(200));

        temp = UUID.randomUUID().toString();
        mockMvc.perform(post(updateBusinessCardUrl)
                .param("customerId", String.valueOf(customer.getId()))
                .param("type", String.valueOf(BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_TEL.getCode()))
                .param("typeValue", temp)
                .cookie(mockCookie(user.getId(), customer.getId()))
        ).andExpect(jsonPath("$.code").value(200));

    }


    @Test
    public void uploadAvatar() throws Exception {
        String url = "http://localhost/site/businessCard/uploadAvatar";

        Cookie cookie = mockCookie(user.getId(), customerId);

        //1.检测get请求url
        String customerId = mockMvc.perform(MockMvcRequestBuilders.get(url).cookie(cookie).param("customerId", String.valueOf(this.customerId)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assert.assertThat(customerId, new Matcher<String>() {
            @Override
            public boolean matches(Object item) {
                if (customerId.contains("异常错误")) {
                    return true;
                }
                return false;
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {
            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
            }

            @Override
            public void describeTo(Description description) {

            }
        });


        //2.检测不是图片的 post请求
        String filename = UUID.randomUUID().toString();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.drawString("金向东", 20, 20);
        ImageIO.write(image, "png", outputStream);

        MockMultipartFile mockMultipartFile = new MockMultipartFile("btnFile", filename, "text/*", outputStream.toByteArray());
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload(url);
        builder.file(mockMultipartFile).param("customerId", String.valueOf(this.customerId)).cookie(cookie);

        mockMvc.perform(builder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.data").value("不支持的文件类型，仅支持图片！"));

        //3.检测大文件的post请求


        //4.检测未登录的post请求,怎么？？
        builder = MockMvcRequestBuilders.fileUpload(url);
        builder.file(mockMultipartFile).param("customerId", String.valueOf(this.customerId));
        ResultActions resultActions = mockMvc.perform(builder);
        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        OutputStream outputStream1 = mvcResult.getResponse().getOutputStream();
        int length = resultActions.andReturn().getResponse().getContentLength();
        byte[] buffer = new byte[length];
        outputStream1.write(buffer);
        //String c = new String(buffer);
        //.andExpect(status().is(302));
        //String mv = resultActions.andReturn().getModelAndView().getViewName();

        //5.检测非法用户的post请求


        //6.检测非销售员的post请求

        UserLevel userLevel = mockUserLevel(this.customerId, UserType.normal, false);
        User user = mockUser(this.customerId, userLevel);
        cookie = mockCookie(user.getId(), this.customerId);
        mockMultipartFile = new MockMultipartFile("btnFile", filename, "image/*", outputStream.toByteArray());
        builder = MockMvcRequestBuilders.fileUpload(url);
        builder.file(mockMultipartFile).param("customerId", String.valueOf(this.customerId)).cookie(cookie);
        mockMvc.perform(builder.cookie(cookie))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("该用户不是销售员，无权编辑名片信息"));

        //7.检测正确的post请求

        userLevel = mockUserLevel(this.customerId, UserType.buddy, true);
        user = mockUser(this.customerId, userLevel);
        cookie = mockCookie(user.getId(), this.customerId);
        builder = MockMvcRequestBuilders.fileUpload(url);
        builder.file(mockMultipartFile).param("customerId", String.valueOf(this.customerId)).cookie(cookie);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

    }

    @Test
    public void editBusinessCard() throws Exception {

        user = mockUser(customerId, userLevel);

        Long userId = user.getId();
        // TODO: 2017-08-01 修改图片后验证？其他字段为什么不验证？？单元测试需要包含所有case
        //设置名片图片
        BusinessCard businessCard = businessCardService.updateBusinessCard(customerId, userId, BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR, "a.jpg");
        //BusinessCard businessCard = businessCardService.getBusinessCard( customerId , userId );

        editUrl += "?customerId=" + customerId + "&userId=" + userId;
        System.out.println("customerId:" + customerId + ";userId=" + userId);
        //以 userId 登录
        mockUserLogin(userId, customerId);

        webDriver.get(editUrl);
        TestEditBusinessCardPage editBusinessCardPage = this.initPage(TestEditBusinessCardPage.class);
        editBusinessCardPage.setBusinessCard(businessCard);
        editBusinessCardPage.setUserRepository(userRepository);
        //WebElement ele = webDriver.findElement(By.id("div_job"));
        editBusinessCardPage.validate();

    }

    @Test
    public void showBusinessCard() throws Exception {

        UserLevel userLevel = mockUserLevel(customerId, UserType.buddy, true);
        User salesman = mockUser(customerId, userLevel);
        User user = mockUser(customerId, userLevel);
        Cookie cookie = mockCookie(user.getId(), customerId);

        BusinessCard businessCard = new BusinessCard();
        businessCard.setJob(UUID.randomUUID().toString());
        businessCard.setUserId(salesman.getId());
        businessCard.setCompanyAddress(UUID.randomUUID().toString());
        businessCard.setAvatar(UUID.randomUUID().toString());
        businessCard.setCustomerId(customerId);
        businessCard.setCompanyName(UUID.randomUUID().toString());
        businessCard.setEmail(UUID.randomUUID().toString().substring(0, 10));
        businessCard.setQq(UUID.randomUUID().toString().substring(0, 10));
        businessCard.setTel(UUID.randomUUID().toString().substring(0, 10));
        businessCard = businessCardRepository.saveAndFlush(businessCard);

        SalesmanBusinessCard salesmanBusinessCard = new SalesmanBusinessCard();
        salesmanBusinessCard.setBusinessCard(businessCard);
        salesmanBusinessCard.setFollowerId(user.getId());
        salesmanBusinessCard.setIsFollowed(true);
        salesmanBusinessCard.setSalesman(salesman);
        salesmanBusinessCard.setNumberOfFollowers(1);

        mockUserLogin(user.getId(),customerId);
        String url = "http://localhost/site/businessCard/showBusinessCard?customerId=" + customerId + "&salesmanId=" + salesman.getId();
        webDriver.get(url);
        ShowBusinessCardPage page = this.initPage(ShowBusinessCardPage.class);
        page.setSalesmanBusinessCard(salesmanBusinessCard);
        page.validate();

    }

    @Test
    public void cancelFollow() throws Exception {

    }
}
