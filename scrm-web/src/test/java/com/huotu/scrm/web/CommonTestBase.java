package com.huotu.scrm.web;

import com.huotu.scrm.common.SysConstant;
import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.common.utils.EncryptUtils;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.entity.report.MonthReport;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
import com.huotu.scrm.service.repository.info.InfoRepository;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.repository.report.DayReportRepository;
import com.huotu.scrm.service.repository.report.MonthReportRepository;
import com.huotu.scrm.web.config.MVCConfig;
import com.huotu.scrm.web.controller.page.AbstractPage;
import com.huotu.scrm.web.interceptor.UserInterceptor;
import org.junit.runner.RunWith;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 这里放一些公用的方法，参数之类的
 * Created by helloztt on 2017-06-28.
 */
@WebAppConfiguration
@ActiveProfiles({"test", "unit_test"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MVCConfig.class})
public abstract class CommonTestBase extends SpringWebTest {

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserLevelRepository userLevelRepository;
    @Autowired
    protected BusinessCardRecordRepository businessCardRecordRepository;
    @Autowired
    protected InfoRepository infoRepository;
    @Autowired
    protected InfoBrowseRepository infoBrowseRepository;
    @Autowired
    protected InfoConfigureRepository infoConfigureRepository;
    @Autowired
    protected MonthReportRepository monthReportRepository;
    @Autowired
    protected DayReportRepository dayReportRepository;
    protected Random random = new Random();

    /**
     * 初始化逻辑页面
     *
     * @param clazz 该页面相对应的逻辑页面的类
     * @param <T>   该页面相对应的逻辑页面
     * @return 页面实例
     */
    public <T extends AbstractPage> T initPage(Class<T> clazz) {
        T page = PageFactory.initElements(webDriver, clazz);
        return page;
    }

    /**
     * 模拟用户登录添加cookie
     *
     * @param userId
     * @param customerId
     * @throws Exception
     */
    public void mockUserLogin(Long userId, Long customerId) throws Exception {
        String cookieName = UserInterceptor.USER_ID_PREFIX + customerId;
        String cookieValue = EncryptUtils.aesEncrypt(String.valueOf(userId), UserInterceptor.USER_ID_SECRET_KEY);
        webDriver.get("http://localhost");
        webDriver.manage().deleteAllCookies();
        webDriver.manage().addCookie(new Cookie(cookieName, URLEncoder.encode(cookieValue,"utf-8") ));
    }

    public javax.servlet.http.Cookie mockCookie(Long userId , Long customerId) throws Exception{
        String cookieName = UserInterceptor.USER_ID_PREFIX + customerId;
        String cookieValue = URLEncoder.encode(EncryptUtils.aesEncrypt(String.valueOf(userId),UserInterceptor.USER_ID_SECRET_KEY),"utf-8");
        return new javax.servlet.http.Cookie(cookieName , cookieValue);
    }


    /**
     * 测试嵌入到伙伴商城的页面，不带参数 customerId 的情况
     *
     * @param controllerUrl 请求地址
     * @param method        请求方法
     *                      0：get
     *                      1：post
     */
    protected void testWithNoParam(String controllerUrl, Integer method) throws Exception {
        switch (method) {
            case 0:
            default:
                mockMvc.perform(get(controllerUrl))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl("http://login." + SysConstant.COOKIE_DOMAIN));
                break;
            case 1:
                mockMvc.perform(post(controllerUrl))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl("http://login." + SysConstant.COOKIE_DOMAIN));
                break;
        }
    }

    /**
     * 模拟平台商户
     *
     * @return
     */
    protected Customer mockCustomer() {
        Customer customer = new Customer();
        customer.setLoginName(UUID.randomUUID().toString());
        customer.setEnabled(true);
        customer.setLoginPassword(UUID.randomUUID().toString());
        customer.setNickName(UUID.randomUUID().toString());
        customer.setMobile(String.valueOf(random.nextInt()));
        customer.setNickName(UUID.randomUUID().toString());
        customer.setSubDomain(UUID.randomUUID().toString());
        customer.setId(Long.valueOf(String.valueOf(random.nextInt())));
        return customerRepository.saveAndFlush(customer);
    }

    /**
     * 模拟资讯信息
     *
     * @param customerId 商户ID
     * @return
     */
    protected Info mockInfo(Long customerId) {
        Info infoE = new Info();
        infoE.setCustomerId(customerId);
        infoE.setTitle(UUID.randomUUID().toString());
        infoE.setIntroduce(UUID.randomUUID().toString());
        infoE.setContent(UUID.randomUUID().toString());
        infoE.setCreateTime(LocalDateTime.now());
        infoE.setDisable(false);
        infoE.setStatus(true);
        infoE.setExtend(true);
        return infoRepository.saveAndFlush(infoE);
    }


    /**
     * 模拟活动
     * @param customerId
     * @return 商户ID
     */
    protected Activity mockActivity(Long customerId){


        return null;
    }

    protected ActPrize mockActPrize(){

        return null;
    }


    /**
     * 模拟用户数据
     *
     * @param customerId
     * @param userLevel
     * @return
     */
    protected User mockUser(Long customerId, UserLevel userLevel) {
        User user = new User();
        user.setCustomerId(customerId);
        user.setLoginName(UUID.randomUUID().toString());
        user.setUserGender("男");
        user.setNickName(UUID.randomUUID().toString());
        user.setUserType(userLevel.getType());
        user.setLevelId(userLevel.getId());
        user.setLockedBalance(random.nextDouble());
        user.setLockedIntegral(random.nextDouble());
        user.setUserMobile(String.valueOf(random.nextInt()));
        user.setUserBalance(random.nextDouble());
        user.setUserTempIntegral(random.nextInt());
        user.setWeixinImageUrl(UUID.randomUUID().toString());
        user.setWxNickName(UUID.randomUUID().toString());
        user.setRegTime(new Date());
        return userRepository.saveAndFlush(user);
    }

    @SuppressWarnings("Duplicates")
    protected UserLevel mockUserLevel(Long customerId, UserType userType, boolean isSalesman) {
        UserLevel userLevel = new UserLevel();
        userLevel.setCustomerId(customerId);
        userLevel.setLevel(random.nextInt());
        userLevel.setLevelName(UUID.randomUUID().toString());
        userLevel.setType(userType);
        userLevel.setSalesman(isSalesman);
        return userLevelRepository.saveAndFlush(userLevel);
    }

    protected Info mockInfo(Long customerId, boolean isStatus, boolean isExtend) {
        Info info = new Info();
        info.setCustomerId(customerId);
        info.setExtend(isExtend);
        info.setStatus(isStatus);
        info.setThumbnailImageUrl(UUID.randomUUID().toString());
        info.setContent(UUID.randomUUID().toString());
        info.setCreateTime(LocalDateTime.now());
        info.setDisable(false);
        info.setImageUrl(UUID.randomUUID().toString());
        info.setIntroduce(UUID.randomUUID().toString());
        info.setTitle(UUID.randomUUID().toString());
        return infoRepository.saveAndFlush(info);
    }

    protected InfoBrowse mockInfoBrowse(Long infoId, Long sourceUserId, Long customerId) {
        InfoBrowse infoBrowse = new InfoBrowse();
        infoBrowse.setInfoId(infoId);
        infoBrowse.setSourceUserId(sourceUserId);
        infoBrowse.setReadUserId(Long.valueOf(String.valueOf(random.nextInt())));
        infoBrowse.setCustomerId(customerId);
        infoBrowse.setBrowseTime(LocalDateTime.now());
        return infoBrowseRepository.saveAndFlush(infoBrowse);
    }

    protected InfoBrowse mockInfoBrowse2(Long infoId, Long readUserId, Long customerId) {
        InfoBrowse infoBrowse = new InfoBrowse();
        infoBrowse.setInfoId(infoId);
        infoBrowse.setSourceUserId(Long.valueOf(String.valueOf(random.nextInt())));
        infoBrowse.setReadUserId(readUserId);
        infoBrowse.setCustomerId(customerId);
        infoBrowse.setBrowseTime(LocalDateTime.now());
        return infoBrowseRepository.saveAndFlush(infoBrowse);
    }
    protected InfoConfigure mockInfoConfigure(Long customerId, boolean exchangeSwitch, int type,int rate, boolean exchangeLimitSwitch, int dayExchangeLimit) {
        InfoConfigure infoConfigure = new InfoConfigure();
        infoConfigure.setCustomerId(customerId);
        infoConfigure.setExchangeSwitch(exchangeSwitch);
        infoConfigure.setExchangeUserType(type);
        infoConfigure.setDayExchangeLimit(dayExchangeLimit);
        infoConfigure.setExchangeRate(rate);
        infoConfigure.setDayExchangeLimitSwitch(exchangeLimitSwitch);
        return infoConfigureRepository.saveAndFlush(infoConfigure);
    }

    protected InfoConfigure mockInfoConfigure(Long customerId) {
        InfoConfigure infoConfigure = new InfoConfigure();
        infoConfigure.setCustomerId(customerId);
        infoConfigure.setExchangeSwitch(false);
        return infoConfigureRepository.saveAndFlush(infoConfigure);
    }

    protected MonthReport mockMonthReport(Long userId, Long customerId, boolean isSales, int visitorNum, int extensionScore, int scoreRanking, int followRanking, LocalDate reportMonth) {
        MonthReport monthReport = new MonthReport();
        monthReport.setSalesman(isSales);
        monthReport.setFollowRanking(followRanking);
        monthReport.setUserId(userId);
        monthReport.setCustomerId(customerId);
        monthReport.setVisitorNum(visitorNum);
        monthReport.setExtensionScore(extensionScore);
        monthReport.setScoreRanking(scoreRanking);
        monthReport.setReportMonth(reportMonth);
        return monthReportRepository.saveAndFlush(monthReport);
    }

    protected DayReport mockDayReport(Long userId, Long customerId, int visitorNum, int extensionScore, LocalDate reportDay) {
        DayReport dayReport = new DayReport();
        dayReport.setUserId(userId);
        dayReport.setCustomerId(customerId);
        dayReport.setVisitorNum(visitorNum);
        dayReport.setExtensionScore(extensionScore);
        dayReport.setReportDay(reportDay);
        return dayReportRepository.saveAndFlush(dayReport);
    }

    protected BusinessCardRecord mockBusinessCardRecord(Long userId, Long customerId) {
        BusinessCardRecord businessCardRecord = new BusinessCardRecord();
        businessCardRecord.setUserId(userId);
        businessCardRecord.setCustomerId(customerId);
        businessCardRecord.setFollowId(Long.valueOf(String.valueOf(random.nextInt())));
        businessCardRecord.setFollowDate(LocalDateTime.now());
        return businessCardRecordRepository.saveAndFlush(businessCardRecord);
    }
}
