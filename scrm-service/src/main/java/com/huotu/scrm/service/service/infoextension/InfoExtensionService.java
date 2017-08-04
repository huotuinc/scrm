package com.huotu.scrm.service.service.infoextension;

import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.model.*;

import java.util.List;

/**
 * Created by hxh on 2017-07-17.
 */
public interface InfoExtensionService {
    /**
     * 查询用户类型
     * 如果用户存在返回相应的用户类型；如果用户不存在返回为空
     *
     * @param userId 用户ID
     * @return
     */
    UserType getUserType(Long userId);

    /**
     * 查询用户所有相关资讯
     *
     * @param user   用户
     * @return
     */
    List<InfoModel> findInfo(User user);

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
     * @param userId 用户ID
     * @return
     */
    DayScoreRankingInfo getScoreRankingInfo(Long userId,Long customerId);

    /**
     * 统计用户积分信息
     *
     * @param userId 用户ID
     * @return
     */
    DayScoreInfo getScoreInfo(Long userId,Long customerId);

    /**
     * 统计访问量信息
     *
     * @param userId 用户ID
     * @return
     */
    DayVisitorNumInfo getVisitorNumInfo(Long userId);

    /**
     * 统计关注量信息
     *
     * @param userId 用户ID
     * @return
     */
    DayFollowNumInfo getFollowNumInfo(Long userId);

    /**
     * 判断是否为销售 (在小伙伴的前提下)
     *
     * @param userId 用户ID
     * @return
     */
    boolean checkIsSalesman(Long userId);
}
