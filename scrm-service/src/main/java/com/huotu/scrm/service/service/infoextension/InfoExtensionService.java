package com.huotu.scrm.service.service.infoextension;

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
     * 查询用户类型 0：普通会员 1：小伙伴
     *
     * @param userId 用户ID
     * @return
     */
    int getUserType(Long userId);

    /**
     * 查询用户所有相关资讯
     *
     * @param userId   用户ID
     * @param userType 0：普通会员 1：小伙伴
     * @return
     */
    List<InfoModel> findInfo(Long userId, int userType);

    /**
     * 统计用户信息 （积分排名等）
     *
     * @param userId 用户ID
     * @return
     */
    StatisticalInformation getInformation(Long userId);

    /**
     * 统计用户积分排名信息
     *
     * @param userId 用户ID
     * @return
     */
    DayScoreRankingInfo getScoreRankingInfo(Long userId);

    /**
     * 统计用户积分信息
     *
     * @param userId 用户ID
     * @return
     */
    DayScoreInfo getScoreInfo(Long userId);

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
}
