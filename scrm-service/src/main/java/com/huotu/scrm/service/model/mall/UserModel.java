package com.huotu.scrm.service.model.mall;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by xyr on 2017/10/25.
 */
@Data
public class UserModel {

    /**
     * 微信昵称
     */
    private String wxNickName;

    /**
     * 资讯查看时间
     */
    private LocalDateTime browseTime;

    /**
     * 所属上线昵称
     */
    private String belongOneNickName;

    /**
     * 所属上线等级
     */
    private int belongOneLevel;

    public UserModel(String wxNickName, LocalDateTime browseTime, String belongOneNickName, int belongOneLevel) {
        this.wxNickName = wxNickName;
        this.browseTime = browseTime;
        this.belongOneNickName = belongOneNickName;
        this.belongOneLevel = belongOneLevel;
    }
}
