package com.huotu.scrm.service.service.info;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by luohaibo on 2017/7/12.
 */
public interface InfoBrowseServer {


    @Transactional
    void infoTurnInSave(InfoBrowse infoBrowse,Long customerId);

    /**
     * 查询资讯的转发记入
     * @param
     * @return
     */
    Page<InfoBrowse> infoTurnRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch);


    /**
     * 删除转发记录
     * @param infoBrowseAndTurnSearch
     * @return
     */
    int deleteInfoTurnRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch);




    Page<InfoBrowse> infoBrowseRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch);
//    /**
//     * 查询资讯的浏览记入
//     * @param infoBrowseAndTurnSearch
//     * @return
//     */
//    Page<InfoBrowse> infoBrowseRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch);

//
//
//
//    /**
//     * 查找莫一条资讯转发总数
//     *
//     * @return
//     */
//    long infoListsCount(Long infoId);
//
//
//
//    /**
//     * jpa 自带可以不用自己写  saveAndFlush
//     * 创建资讯保存到数据库
//     */
//    @Transactional
//    InfoBrowse infoBroseSave(InfoBrowse infoBrowse);
//
//
//    /**
//     * 通过资讯ID查找对应的浏览记录
//     * @return
//     */
//    List <InfoBrowse> InfoBrowseByInfoId(Long infoId);


}
