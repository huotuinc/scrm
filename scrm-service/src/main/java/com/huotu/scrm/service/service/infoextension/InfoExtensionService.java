package com.huotu.scrm.service.service.infoextension;

import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.DayFollowNumInfo;
import com.huotu.scrm.service.model.DayScoreInfo;
import com.huotu.scrm.service.model.DayScoreRankingInfo;
import com.huotu.scrm.service.model.DayVisitorNumInfo;
import com.huotu.scrm.service.model.InfoModel;
import com.huotu.scrm.service.model.StatisticalInformation;

import java.util.List;

/**
 * Created by hxh on 2017-07-17.
 */
public interface InfoExtensionService {

    /**
     * 查询用户所有相关资讯
     *
     * @param user 用户
     * @return
     */
    List<InfoModel> findInfo(User user);

    /**
     * 查询用户转发过的资讯
     *
     * @param
     * @return
     */
    List<InfoModel> findForwardInfo(User user);

    /**
     * 统计用户信息 （积分排名等）
     *
     * @param user 用户
     * @return
     */
    StatisticalInformation getInformation(User user);

    /**
     * 统计用户积分排名信息
     *
     * @param user 用户
     * @return
     */
    DayScoreRankingInfo getScoreRankingInfo(User user);

    /**
     * 统计用户积分信息
     *
     * @param user 用户
     * @return
     */
    DayScoreInfo getScoreInfo(User user);

    /**
     * 统计访问量信息
     *
     * @param user 用户ID
     * @return
     */
    DayVisitorNumInfo getVisitorNumInfo(User user);

    /**
     * 统计关注量信息
     *
     * @param user 用户
     * @return
     */
    DayFollowNumInfo getFollowNumInfo(User user);

    /**
     * 判断是否为销售 (在小伙伴的前提下)
     *
     * @param user
     * @return
     */
    boolean checkIsSalesman(User user);
}
