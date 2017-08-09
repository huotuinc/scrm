package com.huotu.scrm.web.controller;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.businesscard.BusinessCardRecord;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRecordRepository;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRepository;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
import com.huotu.scrm.service.repository.info.InfoRepository;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
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





        /* ===================================获取商城商户号======================*/
        public Customer getCustomerMock()throws Exception{
            Customer customer=customerRepository.findOne(3447L);
            return customer;
        }


        public User getUserMock()throws Exception{
            User user=userRepository.findOne(131L);
            return user;
        }

    /* ===================================咨询测试数据======================*/

        /**
         * 生成咨询信息
         * @param title
         * @param number
         * @throws Exception
         */
        public void createInFoMock(String title,int number)throws Exception {
            for (int i=0;i<number;i++){
                Info info =new Info();
                info.setCustomerId(getCustomerMock().getId());
                info.setTitle(title+i);
                info.setIntroduce("cosy测试cosy测试cosy测试cosy测试cosy测试");
                info.setContent("cosy真棒真漂亮真可爱真真正正各种cosy真棒真漂亮真可爱真真正正各种cosy真棒真漂亮真可爱真真正正各种cosy真棒真漂亮真可爱真真正正各种");
                //  info.setCreateTime(new LocalDateTime());
                info.setStatus(true);//1、 已发布  0、未发布
                infoRepository.saveAndFlush(info);
            }
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
        public void createInfoConfigureMock(Boolean isReward,int rewardScore,boolean isReward_Limit,int rewardLimitNum,int rewardUserType,boolean isExchange,int exchangeRate,int exchangeUserType)throws Exception{
            InfoConfigure infoConfigure=new InfoConfigure();
            infoConfigure.setCustomerId(getCustomerMock().getId());
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
    public BusinessCard createBusinessCardMock()throws Exception{
        BusinessCard businessCard=new BusinessCard();
        businessCard.setUserId(getUserMock().getId());
        businessCard.setCustomerId(getCustomerMock().getId());
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
    public BusinessCardRecord createBusinessCardRecordMock()throws Exception{
        BusinessCardRecord businessCardRecord=new BusinessCardRecord();
        businessCardRecord.setUserId(getUserMock().getId());//被关注者
        businessCardRecord.setCustomerId(getCustomerMock().getId());
        businessCardRecord.setFollowId(getUserMock().getId());//关注者
        businessCardRecord.setFollowDate(LocalDateTime.now());
        businessCardRecord= businessCardRecordRepository.saveAndFlush(businessCardRecord);
        return businessCardRecord;
    }


    }
