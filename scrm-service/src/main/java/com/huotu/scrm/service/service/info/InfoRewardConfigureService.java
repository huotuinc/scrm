package com.huotu.scrm.service.service.info;

import com.huotu.scrm.service.entity.info.InfoConfigure;

/**
 * Created by luohaibo on 2017/7/19.
 */
public interface InfoRewardConfigureService {


    /**
     * 保存资讯积分配置
     * @param infoConfigure
     * @return
     */
    InfoConfigure saveRewardConfigure(InfoConfigure infoConfigure);


    /**
     * 读取积分配置信息
     * @param customerId
     * @return
     */
    InfoConfigure readRewardConfigure(Long customerId);
}
