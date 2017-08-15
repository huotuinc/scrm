package com.huotu.scrm.web.controller;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserFormalIntegral;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRepository;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
import com.huotu.scrm.service.repository.info.InfoRepository;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.repository.mall.UserFormalIntegralRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.web.CommonTestBase;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Created by cosy on 2017/7/21.
 */
public class WebTestOfMock extends CommonTestBase {

        @Autowired
        @SuppressWarnings("SpringJavaAutowiringInspection")
        public CustomerRepository customerRepository;

        @Autowired
        @SuppressWarnings("SpringJavaAutowiringInspection")
        public UserRepository userRepository;

        @Autowired
        @SuppressWarnings("SpringJavaAutowiringInspection")
        public InfoRepository infoRepository;

        @Autowired
        @SuppressWarnings("SpringJavaAutowiringInspection")
        public InfoConfigureRepository infoConfigureRepository;

        @Autowired
        @SuppressWarnings("SpringJavaAutowiringInspection")
        public BusinessCardRepository businessCardRepository;

        @Autowired
        @SuppressWarnings("SpringJavaAutowiringInspection")
        public BusinessCardRecordRepository businessCardRecordRepository;

        @Autowired
        @SuppressWarnings("SpringJavaAutowiringInspection")
        public InfoBrowseRepository infoBrowseRepository;
        @Autowired
        @SuppressWarnings("SpringJavaAutowiringInspection")
        public UserFormalIntegralRepository userFormalIntegralRepository;


    /**
     * 模拟平台上登入
     * @param customerId
     * @param userId
     * @throws Exception
     */
      public void customerLogin(Long customerId,Long userId)throws Exception{


      }






        /**
         * 生成咨询信息
         * @param status 资讯状态     1、 已发布  0、未发布 (判断普通会员，1)
         * @param extend 资讯推广状态  1、 已推广  0、未推广（判断小伙伴,1）
         *
         * @throws Exception
         */
        public Info createInFoMock(Customer customer,boolean status,boolean extend,String title)throws Exception {
                Info info =new Info();
                info.setCustomerId(customer.getId());
                info.setTitle(title);
                info.setIntroduce("cosy测试cosy测试cosy测试cosy测试cosy测试");
                info.setContent("cosy真棒真漂亮真可爱真真正正各种cosy真棒真漂亮真可爱真真正正各种cosy真棒真漂亮真可爱真真正正各种cosy真棒真漂亮真可爱真真正正各种");
                info.setStatus(status);
                info.setExtend(extend);
                info.setCreateTime(LocalDateTime.now());
                info=infoRepository.saveAndFlush(info);
                return info;
        }


        /**
         *  创建咨询转发配置
         * @param isReward  是否开启转发奖励
         * @param rewardScore 转发咨询奖励积分
         * @param isReward_Limit 是否开启转发奖励限制
         * @param rewardLimitNum 每日奖励限制次数
         * @param rewardUserType 转发奖励获取对象
         * @param isExchange 是否开启UV转换积分
         * @param exchangeRate UV转换积分的比例
         * @param exchangeUserType UV转换积分获取对象
         * @throws Exception
         */
        public void createInfoConfigureMock(Boolean isReward,int rewardScore,boolean isReward_Limit,int rewardLimitNum,int rewardUserType,boolean isExchange,int exchangeRate,int exchangeUserType,Customer customer)throws Exception{
            InfoConfigure infoConfigure=new InfoConfigure();
            infoConfigure.setCustomerId(customer.getId());
            infoConfigure.setRewardSwitch(isReward);// 是否开启转发奖励
            infoConfigure.setRewardScore(rewardScore);//转发咨询奖励积分
            infoConfigure.setRewardLimitSwitch(isReward_Limit);//是否开启转发奖励限制
            infoConfigure.setRewardLimitNum(rewardLimitNum);//每日奖励限制次数
            infoConfigure.setRewardUserType(rewardUserType);//转发奖励获取对象
            infoConfigure.setExchangeSwitch(isExchange);//是否开启UV转换积分
            infoConfigure.setExchangeRate(exchangeRate);//UV转换积分的比例
            infoConfigure.setExchangeUserType(exchangeUserType);//UV转换积分获取对象
            infoConfigureRepository.saveAndFlush(infoConfigure);
        }




    /**
     * 创建名片
     * @throws Exception
     */
    public BusinessCard createBusinessCardMock(Customer customer,User user)throws Exception{

        BusinessCard businessCard=new BusinessCard();
        businessCard.setUserId(user.getId());
        businessCard.setCustomerId(customer.getId());
        businessCard.setJob("测试狗");
        businessCard.setCompanyName("杭州火图科技有限公司");
        businessCard.setTel("13600541783");
        businessCard.setQq("826449783");
        businessCard.setCompanyAddress("浙江省阡陌路智慧E谷");
        businessCard.setEmail("826449783@qq.com");
        businessCard=businessCardRepository.saveAndFlush(businessCard);
        return businessCard;
    }

    /**
     * 名片关注记录
     * @return
     * @throws Exception
     */
    public BusinessCardRecord createBusinessCardRecordMock(User user,Customer customer)throws Exception{
        BusinessCardRecord businessCardRecord=new BusinessCardRecord();
        businessCardRecord.setUserId(user.getId());//被关注者
        businessCardRecord.setCustomerId(customer.getId());
        businessCardRecord.setFollowId(user.getId());//关注者
        businessCardRecord.setFollowDate(LocalDateTime.now());
        businessCardRecord= businessCardRecordRepository.saveAndFlush(businessCardRecord);
        return businessCardRecord;
    }


    /**
     * @param sourceUserId   资讯转发来源用户
     * @param readUserId     资讯查看用户
     * @param customerId     商户ID
     * @throws Exception
     */
     public InfoBrowse infoBrowse(Long inFoId,Long sourceUserId,Long readUserId,Long customerId) throws Exception{
         InfoBrowse infoBrowse=new InfoBrowse();
         infoBrowse.setInfoId(inFoId);
         infoBrowse.setSourceUserId(sourceUserId);
         infoBrowse.setReadUserId(readUserId);
         infoBrowse.setCustomerId(customerId);
         infoBrowse.setBrowseTime(LocalDateTime.now());
         infoBrowse.setTurnTime(LocalDateTime.now());
         infoBrowse=infoBrowseRepository.saveAndFlush(infoBrowse);
         return infoBrowse;
    }

    /**
     *
     * @param customer
     * @param user
     * @param score   用户当前积分
     * @param userLevelId  用户等级
     * @param userType  用户类型
     * @return
     * @throws Exception
     */
    public  UserFormalIntegral createUserFormalIntegral(Customer customer,User user,Integer score,Integer userLevelId,Integer userType)throws Exception{
        UserFormalIntegral userFormalIntegral=new UserFormalIntegral();
        userFormalIntegral.setMerchantId(customer.getId());
        userFormalIntegral.setUserId(user.getId());
        userFormalIntegral.setScore(score);
        userFormalIntegral.setStatus(IntegralTypeEnum.TURN_INFO);
        userFormalIntegral.setTime(LocalDateTime.now());
        userFormalIntegral.setUserLevelId(userLevelId);
        userFormalIntegral.setUserType(userType);
        userFormalIntegral=userFormalIntegralRepository.saveAndFlush(userFormalIntegral);
        return userFormalIntegral;
    }

}
