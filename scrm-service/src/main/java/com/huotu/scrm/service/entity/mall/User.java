package com.huotu.scrm.service.entity.mall;

import com.huotu.scrm.common.ienum.UserType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Description;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by helloztt on 2017-07-10.
 */
@Entity
@Getter
@Setter
@Table(name = "Hot_UserBaseInfo")
@Description("用户")
@Cacheable(value = false)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UB_UserID")
    private Long id;

    /**
     * 商户ID
     */
    @Column(name = "UB_CustomerID")
    private Long customerId;

    /**
     * 用户等级
     */
    @Column(name = "UB_LevelID")
    private Long levelId;

    /**
     * 登录名
     */
    @Column(name = "UB_UserLoginName", nullable = false, length = 50)
    private String loginName;

    /**
     * 昵称
     */
    @Column(name = "UB_UserNickName", length = 100)
    private String nickName;

    /**
     * 用户类型
     * 0：普通会员
     * 1：小伙伴
     */
    @Column(name = "UB_UserType")
    private UserType userType;

    /**
     * 微信头像
     */
    @Column(name = "UB_WxHeadImg")
    private String weixinImageUrl;

    /**
     * 微信昵称
     */
    @Column(name = "UB_WxNickName")
    private String wxNickName;

    /**
     * 余额
     */
    @Column(name = "UB_UserBalance")
    private Double userBalance;


    /**
     * 积分
     */
    @Column(name = "UB_UserIntegral")
    private Integer userIntegral;


    /**
     * 临时积分
     */
    @Column(name = "UB_UserTempIntegral")
    private Integer userTempIntegral;

    /**
     * 冻结金额
     */
    @Column(name = "UB_LockedBalance")
    private Double lockedBalance;

    /**
     * 冻结积分
     */
    @Column(name = "UB_LockedIntegral")
    private Double lockedIntegral;

    /**
     * 注册时间
     */
    @Column(name = "UB_UserRegTime")
    private Date regTime;

    /***
     * 性别
     */
    @Column(name="UB_UserGender",length = 10)
    private String userGender;
    /***
     * 手机
     */
    @Column(name="UB_UserMobile",length = 50)
    private String userMobile;

    /***
     * 姓名
     */
    @Column(name="UB_UserRealName",length = 100)
    private String realName;
    /**
     * 标记是否已经绑定了手机，0：表示已经绑定，1：表示未绑定
     */
    @Column(name="UB_MobileToBeBind")
    private Integer mobileToBeBind;
}
