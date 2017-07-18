package com.huotu.scrm.service.service.infoextension;

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
}
