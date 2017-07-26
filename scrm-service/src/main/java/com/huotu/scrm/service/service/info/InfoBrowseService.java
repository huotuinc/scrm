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
public interface InfoBrowseService {


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
    @Transactional
    int updateInfoTurnRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch);

    /**
     * 查询资讯的转发记入
     * @param
     * @return
     */
    Page<InfoBrowse> infoBrowseRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch);

    /**
     * 删除浏览记录
     * @param infoBrowseAndTurnSearch
     * @return
     */
    @Transactional
    int updateInfoBrowse(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch);


    /**
     * 获取资讯转发量
     * @param infoId
     * @return
     */
    int countByTurn(Long infoId);


    /**
     * 获取资讯浏览量
     * @param infoId
     * @return
     */
    int countByBrowse(Long infoId);
}
