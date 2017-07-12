package com.huotu.scrm.service.model;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/11.
 */
@Getter
@Setter
public class UserBusinessCard implements Serializable{

    private User user;

    private BusinessCard businessCard;
}
