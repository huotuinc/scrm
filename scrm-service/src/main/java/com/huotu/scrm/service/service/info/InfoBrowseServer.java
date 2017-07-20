package com.huotu.scrm.service.service.info;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by luohaibo on 2017/7/12.
 */
public interface InfoBrowseServer {


    /**
     * 根据分页条件查找到某一资讯的资讯列表转发记入
     */
    Page<InfoBrowse> infoBrowseLists(Long infoId, int page, int pageSize);



    /**
     * 查找莫一条资讯转发总数
     *
     * @return
     */
    long infoListsCount(Long infoId);



    /**
     * jpa 自带可以不用自己写  saveAndFlush
     * 创建资讯保存到数据库
     */
    @Transactional
    InfoBrowse infoBroseSave(InfoBrowse infoBrowse);


    /**
     * 通过资讯ID查找对应的浏览记录
     * @return
     */
    List <InfoBrowse> InfoBrowseByInfoId(Long infoId);


}
