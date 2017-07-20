package com.huotu.scrm.service.model;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.mall.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 销售员名片信息业务实体类
 * Created by jinxiangdong on 2017/7/11.
 */
@Getter
@Setter
public class SalesmanBusinessCard implements Serializable {
    /***
     * 销售员基本信息
     */
    private User salesman;
    /***
     * 销售员名片信息
     */
    private BusinessCard businessCard;
    /***
     * 关注人数
     */
    private Integer numberOfFollowers;
    /***
     * 是否被关注
     */
    private Boolean isFollowed;
    /***
     * 关注者Id
     */
    private Long followerId;
}
