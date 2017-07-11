package com.huotu.scrm.service.service;

import com.huotu.scrm.service.entity.info.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luohaibo on 2017/7/5.
 */
public interface InfoServer {

    /**
     * 获取资讯总数
     * @return
     */
    long infoListsCount();



    /**
     *  根据某一个模糊条件搜索标题查找相应的资讯列表
     */


    /**
     * 根据分页条件查找到某一页的资讯列表
     */
    Page<Info> infoSList(byte disable, Pageable pageable);


    /**
     * jpa 自带可以不用自己写  saveAndFlush
     * 创建资讯保存到数据库
     */
    void infoSave(Info info);



    /**
     * 根据对应到资讯ID逻辑删除相应的资讯
     */
    @Transactional
    void deleteInfoById(Long id);



    /**
     * 根据资讯ID修改资讯的推广状态
     */
    @Transactional
    void updateInfoExtendStatusById(Long id);

    /**
     * 根据资讯ID修改资讯的发布状态
     */
}
